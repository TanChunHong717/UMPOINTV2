<template>
    <vue-advanced-chat
        :current-user-id="$props.userId"
        show-add-room="false"
        show-audio="true"
        :show-footer="canTalkInChat"
        show-reaction-emojis="false"
        capture-files="environment"
        accepted-files="image/png,image/jpeg,image/jpg,application/pdf"
        username-options='{"minUsers": 0, "currentUser": false}'
        :room-id="currentRoomId"
        :load-first-room="loadFirstRoom"
        @fetch-more-rooms="fetchRooms"
        :rooms="JSON.stringify(rooms)"
        :rooms-loaded="roomsFullyLoaded"
        room-info-enabled="true"
        @room-info="viewFacilityInfo(currentRoomId)"
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
    <ChatReportPopup
        v-model:visible="isDialogVisible"
        @close="closeDialog"
        @submit="submitReport"
    ></ChatReportPopup>
</template>

<script setup>
import { mdiMenuOpen } from "@mdi/js";
import { ref, computed, watch, onBeforeUnmount, onBeforeMount } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";

import * as chatApi from "@/helpers/api-chat";
import { uploadFile } from "@/helpers/api-upload.js";
import {
    chatRoomTypes,
    chatUserTypes,
    chatFacilityTypes,
} from "@/constants/chat";

import ChatReportPopup from "./ChatReportPopup.vue";

// chat component
import { register } from "vue-advanced-chat";
register();

const props = defineProps(["userId", "userToken"]);
const router = useRouter();

// websocket client
var wsClient = chatApi.createWebSocketClient();
var wsCurrentRoom = null;
function onChangeRoom(roomId) {
    if (wsCurrentRoom) {
        wsCurrentRoom.unsubscribe();
    }
    currentRoomId.value = roomId;
    // change route
    router.replace({
        query: { roomId },
    });
    wsCurrentRoom = chatApi.subscribeToRoom(wsClient, roomId, updateNewMessage);
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
onBeforeUnmount(() => {
    // when destroyed, stop websocket
    if (wsCurrentRoom) {
        wsCurrentRoom.unsubscribe();
    }
    wsClient.deactivate();
});
onBeforeMount(() => {
    // activate websocket
    if (!wsClient) wsClient = chatApi.createWebSocketClient();
});

// full list of rooms
const rooms = ref();
const roomsFullyLoaded = ref(false);
const loadFirstRoom = ref(false); // don't load first room by default
const roomCurrentPage = ref(1);

// load all rooms
async function fetchRooms(firstTime = false) {
    if (firstTime) {
        roomCurrentPage.value = 1;
    } else {
        roomCurrentPage.value++;
    }
    roomsFullyLoaded.value = false;
    try {
        let { rooms: apiRooms, total } = await chatApi.getChatRooms(
            roomCurrentPage.value
        );
        rooms.value = apiRooms;

        if (apiRooms.length == 0 || apiRooms.length == total) {
            roomsFullyLoaded.value = true;
        }
    } catch (error) {
        ElMessage.error("Error fetching chat rooms");
    }
}
fetchRooms(true);

// helper function
const getRoom = (roomId) => {
    if (!rooms.value) {
        return null;
    }
    return rooms.value.find((r) => r.roomId === roomId);
};

// current chat setup
const currentRoomId = ref();
const canTalkInChat = computed(() => {
    let room = getRoom(currentRoomId.value);
    if (!room) {
        return false;
    }
    return (
        room.status === chatRoomTypes.CREATED ||
        room.status === chatRoomTypes.OPEN
    );
});

// first time check for query params to load room based on facility ID
const currentRoute = useRoute();
async function getFacilityChatRoom(facilityType, facilityId) {
    let roomId = await chatApi.getChatRoomIdByFacility(
        facilityType,
        facilityId
    );
    return roomId;
}
watch(
    () => currentRoute.query,
    async (query) => {
        if (query.facilityType && query.facilityId) {
            let roomId = await getFacilityChatRoom(
                query.facilityType,
                query.facilityId
            );
            loadFirstRoom.value = true;
            onChangeRoom(roomId);
        } else if (query.roomId) {
            loadFirstRoom.value = true;
            onChangeRoom(query.roomId);
        }
    },
    { immediate: true }
);

const messages = ref([]);
const messagesFullyLoaded = ref(false);
const messageCurrentPage = ref(1);

/* Load messages */
async function fetchMessages(event) {
    let {
        detail: [{ room, options }],
    } = event;
    messagesFullyLoaded.value = false;

    if (options && options.reset) {
        // room is opened for first time
        // change websocket subscribe channel
        onChangeRoom(room.roomId);
        // reset messages
        messages.value = [];
        // reset pages
        messageCurrentPage.value = 1;
        // fetch messages
        try {
            let { messages: messagesRes, total } = await chatApi.getMessages(
                room.roomId,
                props.userId
            );
            messages.value = messagesRes.toReversed();
            if (messagesRes.length == 0 || messagesRes.length == total) {
                messagesFullyLoaded.value = true;
            }
        } catch (error) {
            ElMessage.error("Error fetching messages");
        }
    } else {
        // prepare to load next page
        messageCurrentPage.value++;
        // fetch more messages
        try {
            let { messages: messagesRes } = await chatApi.getMessages(
                room.roomId,
                props.userId,
                messageCurrentPage.value
            );
            if (messagesRes.length == 0) {
                messagesFullyLoaded.value = true;
                return;
            }
            let newMessages = messagesRes.toReversed().concat(messages.value);
            messages.value = newMessages; // performance issue, build then replace
        } catch (error) {
            ElMessage.error("Error fetching messages");
        }
    }
}

// Message with buttons for bot reply
const messageBotReplyButtons = computed(() => {
    if (!messages.value || messages.value.length == 0) {
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
    let room = getRoom(roomId);
    router.push({
        name: "facility-information",
        params: {
            type: chatFacilityTypes[room.facilityType],
            id: room.facilityId,
        },
    });
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
                chatApi.reportChatRoom(roomId, reason);
                fetchRooms(true);
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
                chatApi.reportMessage(roomId, message._id, reason);
                fetchRooms(true);
            });
            break;
    }
}

/* Report chat, prompt reason from user */
const isDialogVisible = ref(false);
let submitReport = null;
const closeDialog = () => {
    isDialogVisible.value = false;
};
function showReportChatPopup(callback) {
    submitReport = (reason) => {
        console.log("Reported Chat: ", reason);
        isDialogVisible.value = false;
        callback(reason);
    };
    isDialogVisible.value = true;
}
</script>

<style>
.vac-avatar-current-offset {
    margin: 0 !important;
}
</style>
