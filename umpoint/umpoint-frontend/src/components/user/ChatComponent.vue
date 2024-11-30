<template>
    <vue-advanced-chat
        :current-user-id="$props.userId"
        show-add-room="false"
        show-audio="false"
        :show-footer="canTalkInChat"
        show-reaction-emojis="false"
        capture-files="environment"
        accepted-files="image/png,image/jpeg,image/jpg,application/pdf"
        username-options='{"minUsers": 0, "currentUser": false}'
        :room-id="currentRoomId"
        :load-first-room="loadFirstRoom"
        @fetch-more-rooms="fetchRooms"
        :rooms="JSON.stringify(rooms)"
        :rooms-loaded="true"
        room-info-enabled="true"
        @room-info="viewFacilityInfo"
        :room-actions="JSON.stringify(roomActions)"
        :menu-actions="JSON.stringify(roomActions)"
        @room-action-handler="roomActionHandler"
        @menu-action-handler="roomActionHandler"
        :messages="JSON.stringify(messages)"
        :messages-loaded="messagesFullyLoaded"
        @fetch-messages="fetchMessages"
        @send-message="sendMessage"
        :message-actions="JSON.stringify(messageActions)"
        @message-action-handler="messageActionHandler"
    >
        <template v-for="message in messageBotReplyButtons">
            <div :slot="`message_${message.id}`">
                <el-button
                    size="small"
                    v-for="option in message.options"
                    @click="sendButtonMessage(message.id, option)"
                    >{{ option }}</el-button
                >
            </div>
        </template>

        <svg-icon slot="toggle-icon" type="mdi" :path="mdiMenuOpen" />

        <!-- hide avatars -->
        <div
            v-for="message in messages"
            :slot="'message-avatar_' + message._id"
        ></div>
    </vue-advanced-chat>
</template>

<script setup>
import { mdiMenuOpen } from "@mdi/js";
import { ref, computed, watch } from "vue";
import { useRoute } from "vue-router";
import * as chatApi from "@/helpers/api-chat";
import { uploadFile } from "@/helpers/api-upload.js";

// chat component
import { register } from "vue-advanced-chat";
import { ElMessage, ElMessageBox } from "element-plus";
import { chatRoomTypes, chatUserTypes } from "@/constants/chat";
register();

const props = defineProps(["userId", "userToken"]);

// websocket client
const wsClient = chatApi.createWebSocketClient();
var wsCurrentRoom = null;
function changeWsClientRoom(client, roomId) {
    if (wsCurrentRoom) {
        wsCurrentRoom.unsubscribe();
    }
    currentRoomId.value = roomId;
    wsCurrentRoom = chatApi.subscribeToRoom(client, roomId, updateNewMessage);
}
function updateNewMessage(message) {
    let response = JSON.parse(message.body);
    let targetMessage = messages.value.find(
        (m) =>
            m._id === "new-message" &&
            parseInt(m.indexId) === parseInt(response.returnMessage)
    );

    if (response.error) {
        // error on server side, set message to error
        ElMessage.error(response.message);
        targetMessage.failure = true;
        return;
    }

    if (targetMessage) {
        // update message with new id
        targetMessage._id = response.id;
        // set as seen
        targetMessage.saved = true;
        targetMessage.distributed = true;
        // targetMessage.seen = true;
        targetMessage.files = chatApi.parseFilesFromApi(
            response.chatMessageAttachmentDTOList
        );
    } else {
        // new message from channel
        messages.value.push(
            chatApi.parseMessageFromApi(response, props.userId)
        );
    }
}

// full list of rooms
const rooms = ref();
const loadFirstRoom = ref(false); // don't load first room by default

// current chat setup
const currentRoomId = ref();
const canTalkInChat = computed(() => {
    if (!rooms.value) {
        return false;
    }
    let room = rooms.value.find((r) => r.roomId === currentRoomId.value);
    if (!room) {
        return false;
    }
    return (
        room.status === chatRoomTypes.CREATED ||
        room.status === chatRoomTypes.OPEN
    );
});

// current route
const currentRoute = useRoute();
async function getFacilityChatRoom(facilityType, facilityId) {
    let roomId = await chatApi.getChatRoomIdByFacility(
        facilityType,
        facilityId
    );
    console.log(roomId);
    return roomId;
}
// first time check for query params to load room
watch(
    () => currentRoute.query,
    async (query) => {
        if (query.facilityType && query.facilityId) {
            let roomId = await getFacilityChatRoom(
                query.facilityType,
                query.facilityId
            );
            changeWsClientRoom(wsClient, roomId);
            loadFirstRoom.value = true;
        }
    },
    { immediate: true }
);

async function fetchRooms() {
    let apiRooms = await chatApi.getChatRooms();
    rooms.value = apiRooms;
}
fetchRooms();

const messages = ref([]);
const messagesFullyLoaded = ref(false);

/* Load messages */
async function fetchMessages(event) {
    let {
        detail: [{ room, options }],
    } = event;
    messagesFullyLoaded.value = false;

    if (options && options.reset) {
        // room is opened for first time
        changeWsClientRoom(wsClient, room.roomId);
        let messagesRes = await chatApi.getMessages(room.roomId, props.userId);
        messages.value = messagesRes;
    }
    messagesFullyLoaded.value = true;
}

// Message with buttons for bot reply
const messageBotReplyButtons = computed(() => {
    if (messages.value.length == 0) {
        return [];
    }

    return messages.value
        .filter((message) => {
            return message.senderType === chatUserTypes.BOT_REPLY_OPTIONS;
        })
        .map((message) => {
            return {
                id: message._id,
                options: JSON.parse(message.content),
            };
        });

    // <div slot="`message_${message._id}`">
    //     <el-button>I need more help</el-button>
    //     <el-button>Talk to human</el-button>
    // </div>
});
function sendButtonMessage(messageId, userResponse) {
    // only send message if the last message is the bot options
    if (messageId == messages.value.slice(-1)[0]._id) {
        sendMessage({
            detail: [
                {
                    roomId: currentRoomId.value,
                    content: userResponse,
                },
            ],
        });
    }
}

// send message
async function sendMessage(event) {
    let {
        detail: [{ roomId, content, files, replyMessage }],
    } = event;
    // files: [{ blob, localUrl, name, size, type, extension }]

    // insert new message to ui
    let indexId = messages.value.length + 1;
    messages.value.push({
        content: content ?? "",
        replyMessage,
        files: files?.map((item) => {
            item.progress = 0;
            return item;
        }),
        // default values
        indexId, // placeholder
        _id: "new-message", // placeholder
        senderId: props.userId,
        date: new Date().toLocaleDateString("en-MY", {
            day: "numeric",
            month: "long",
        }),
        timestamp: new Date().toLocaleTimeString("en-MY", {
            hour: "numeric",
            minute: "2-digit",
        }),
        system: false,
        saved: false,
        distributed: false,
        seen: false,
        deleted: false,
        failure: false,
    });

    // prepare sending message
    let wsMessage = { message: content };
    if (replyMessage) {
        wsMessage.replyMessageId = replyMessage._id;
    }
    if (files && files.length) {
        wsMessage.attachments = await Promise.all(
            files.map((file) =>
                uploadFile({
                    item: file.blob,
                    name: `${file.name}.${file.extension}`,
                    type: file.type,
                })
            )
        );
    }
    // set return message to identify this message
    wsMessage.returnMessage = indexId;

    // call api to send message
    chatApi.sendMessage(
        wsClient,
        roomId,
        { token: props.userToken },
        wsMessage
    );
}

/* User actions for chat room */
const roomActions = ref([
    { name: "viewLocation", title: "View Facility Information" },
    { name: "markAsResolved", title: "Mark as Resolved" },
    { name: "reportChat", title: "Report Chat" },
]);
/* Action events */
const viewFacilityInfo = (roomId) => {
    console.log("Room Info", roomId);
};
function roomActionHandler(event) {
    let {
        detail: [
            {
                roomId,
                action: { name: actionName },
            },
        ],
    } = event;

    switch (actionName) {
        case "viewLocation":
            viewFacilityInfo(roomId);
            break;
        case "markAsResolved":
            chatApi.markAsResolved(roomId).then(() => {
                ElMessage.success("Chat is resolved");
            });
            break;
        case "reportChat":
            showReportChatPopup((reason) => {
                chatApi.reportChatRoom(roomId, reason, props.userId);
                fetchRooms();
            });
            break;
    }
}

/* User actions for chat message */
const messageActions = ref([
    { name: "replyMessage", title: "Reply" },
    { name: "reportMessage", title: "Report" },
]);
function messageActionHandler(event) {
    let {
        detail: [
            {
                roomId,
                message,
                action: { name: actionName },
            },
        ],
    } = event;

    switch (actionName) {
        case "viewLocation":
            viewFacilityInfo(roomId);
            break;
        case "markAsResolved":
            chatApi.markAsResolved(roomId).then(() => {
                ElMessage.success("Chat is resolved");
            });
            break;
        case "reportMessage":
            showReportChatPopup((reason) => {
                chatApi.reportMessage(
                    roomId,
                    message._id,
                    reason,
                    props.userId
                );
                fetchRooms();
            });
            break;
    }
}

/* Report chat, prompt reason from user */
function showReportChatPopup(callback) {
    ElMessageBox.prompt(
        "Please provide a reason for reporting user. The room will be closed after reporting.",
        "Report User",
        {
            confirmButtonText: "OK",
            cancelButtonText: "Cancel",
            inputPattern: /\S+/,
            inputErrorMessage: "Please provide a reason",
        }
    ).then(({ value }) => {
        callback(value);
    });
}
</script>

<style>
.vac-avatar-current-offset {
    margin: 0 !important;
}
</style>
