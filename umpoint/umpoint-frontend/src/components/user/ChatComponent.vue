<template>
    <vue-advanced-chat
        :current-user-id="$props.userId"
        show-add-room="false"
        show-audio="false"
        show-reaction-emojis="false"
        capture-files="environment"
        accepted-files="image/png,image/jpeg,image/jpg,application/pdf"
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
        <div slot="message_bot_reply_1">
            <el-button>I need more help</el-button>
            <el-button>Talk to human</el-button>
        </div>

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
import { ref, nextTick, watch } from "vue";
import { useRoute } from "vue-router";
import { default as chatApi } from "@/helpers/api-chat.js";
import { uploadFile } from "@/helpers/api-upload.js";

// chat component
import { register } from "vue-advanced-chat";
register();

const props = defineProps(["userId"]);

// current route
const currentRoute = useRoute();

// full list of rooms
const rooms = ref();

async function getFacilityChatRoom(facilityType, facilityId) {
    let roomId = await chatApi.getChatRoomIdByFacility(
        facilityType,
        facilityId
    );
    console.log(roomId);
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
            currentRoomId.value = roomId;
            loadFirstRoom.value = true;
        }
    },
    { immediate: true }
);

async function fetchRooms() {
    let apiRooms = await chatApi.getChatRooms();
    console.log(apiRooms);
    rooms.value = apiRooms;
}
fetchRooms();

// current chat setup
const currentRoomId = ref();
const loadFirstRoom = ref(false); // don't load first room by default
const messages = ref();
const messagesFullyLoaded = ref(false);

/* Load rooms and messages */
async function fetchMessages(event) {
    let {
        detail: [{ room, options }],
    } = event;
    messagesFullyLoaded.value = false;

    if (options && options.reset) {
        let messagesRes = await chatApi.getMessages(room.roomId);
        console.log(messagesRes);
        messages.value = messagesRes;
    } else {
    }
    // messagesLoaded.value  = true;
    //{ room, options }
}

// send message
async function sendMessage(event) {
    let {
        detail: [{ roomId, content, files, replyMessage }],
    } = event;
    // files: [{ blob, localUrl, name, size, type, extension }]
    console.log("send", roomId, content, files, replyMessage);
    let message = { message: content };
    if (replyMessage) {
        message.replyTo = replyMessage._id;
    }
    if (files && files.length) {
        message.attachments = await Promise.all(
            files.map((file) => uploadFile(file))
        );
    }
    let indexId = messages.value.length + 1;
    messages.value.push({
        content: content ?? "",
        replyMessage,
        files,
        // default values
        indexId,
        _id: "new-message",
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

    // call api to send message
    chatApi
        .sendMessage(roomId, message)
        .then((res) => {
            // update message with new id
            let newMessage = messages.value.find(
                (m) => m._id === "new-message" && m.indexId === indexId
            );
            newMessage._id = res.data.data;
            newMessage.saved = true;
        })
        .catch((err) => {
            console.log(err);
            let newMessage = messages.value.find(
                (m) => m._id === "new-message" && m.indexId === indexId
            );
            newMessage.failure = true;
        });
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
    console.log(event.detail[0]);
    // console.log("Clicked on room action", roomId, action);
}

/* User actions for chat message */
const messageActions = ref([
    { name: "replyMessage", title: "Reply" },
    { name: "reportMessage", title: "Report" },
]);
function messageActionHandler(event) {
    console.log(event.detail[0]);
    // console.log("Clicked on message action", roomId, action);
}
</script>

<style>
.vac-avatar-current-offset {
    margin: 0 !important;
}
</style>
