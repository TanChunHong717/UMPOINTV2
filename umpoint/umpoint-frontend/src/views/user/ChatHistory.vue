<template>
    <BaseLayout>
        <template #title>Chat History</template>

        <vue-advanced-chat
            :current-user-id="currentUserId"
            :rooms="JSON.stringify(rooms)"
            :room-id="currentRoomId"
            :rooms-loaded="true"
            :messages="JSON.stringify(messages)"
            :messages-loaded="messagesLoaded"
            :message-actions="JSON.stringify(messageActions)"
            show-add-room="false"
            show-audio="false"
            show-reaction-emojis="false"
            accepted-files="image/png,image/jpeg,image/jpg,application/pdf"
            capture-files="environment"
            room-info-enabled="true"
            @room-info="viewFacilityInfo"
            :room-actions="JSON.stringify(roomActions)"
            :menu-actions="JSON.stringify(roomActions)"
            @room-action-handler="roomActionHandler"
            @menu-action-handler="roomActionHandler"
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
    </BaseLayout>
</template>

<script setup>
import { mdiMenuOpen } from "@mdi/js";
import { register } from "vue-advanced-chat";
import { ref, nextTick } from "vue";
register();

const messagesLoaded = ref(false);
nextTick(() => {
    messagesLoaded.value = true;
    messages.value[3].content = "Message 1 updated";
});

const currentRoomId = ref("1");
const currentUserId = ref("1234");
const rooms = ref([
    {
        roomId: "1",
        roomName: "Tennis Court 1 - UM Arena",
        avatar: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAeAXCYHdm1-SXe-evCVF1VlhelqfXEG8TGw&s",
        unreadCount: 0,
        index: 1,
        lastMessage: {
            _id: "7892",
            content: "Sorry for the inconvenience, we will look",
            senderId: "1234",
            username: "John Doe",
            timestamp: "10:20",
            saved: true,
            distributed: false,
            seen: false,
            new: true,
        },
        users: [
            {
                _id: "bot",
                username: "Bot",
            },
            {
                _id: "1234",
                username: "John Doe",
            },
            {
                _id: "4321",
                username: "John Snow",
            },
        ],
        facilityID: "1234",
    },
    {
        roomId: "2",
        roomName: "Auditorium Utama - FOP",
        avatar: "https://umpoint.um.edu.my/search/imagethumb.php?spdid=426",
        unreadCount: 0,
        index: 4,
        lastMessage: {
            _id: "7892",
            content: "Last message received",
            senderId: "1234",
            username: "John Doe",
            timestamp: "10:20",
            saved: true,
            distributed: false,
            seen: false,
            new: true,
        },
        users: [
            {
                _id: "bot",
                username: "Bot",
            },
            {
                _id: "1234",
                username: "John Doe",
            },
            {
                _id: "4321",
                username: "John Snow",
            },
        ],
        facilityID: "5678",
    },
]);
const messages = ref([
    {
        _id: "7890",
        indexId: 12092,
        content: "See ni rosak",
        senderId: "1234",
        username: "John Doe",
        date: "13 November",
        timestamp: "10:20",
        system: false,
        saved: true,
        distributed: true,
        seen: true,
        deleted: false,
        failure: false,
        disableActions: false,
        disableReactions: false,
        files: [
            {
                name: "My File",
                type: "png",
                url: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEZLVDGej5kEJlCPtMR9zYQxl85DDXnPdcEA&s",
                progress: 88,
            },
        ],
        replyMessage: {
            content: "I have faced issue with court",
            senderId: "4321",
        },
    },
    {
        _id: "1234",
        indexId: 12091,
        senderId: "0",
        content: "This facility is currently closed",
        date: "13 November",
        timestamp: "11:20",
        system: true,
        seen: true,
    },
    {
        _id: "1235",
        indexId: 12091,
        senderId: "4321",
        content: "How may I help you?",
        date: "13 November",
        timestamp: "10:20",
        system: false,
        seen: true,
    },
    {
        _id: "bot_reply_1",
        indexId: 12092,
        content: "Bot reply button",
        senderId: "1234",
        username: "John Doe",
        date: "13 November",
        timestamp: "10:20",
        system: false,
        saved: true,
        distributed: true,
        seen: true,
    },
    {
        _id: "7892",
        indexId: 12093,
        content: "Talk to human",
        senderId: "1234",
        username: "John Doe",
        date: "13 November",
        timestamp: "10:20",
        system: false,
        saved: true,
        distributed: true,
        seen: true,
    },
    {
        _id: "1235",
        indexId: 12091,
        content: "This chat is transferred to human agent",
        date: "13 November",
        timestamp: "11:20",
        system: true,
        seen: true,
    },
    {
        _id: "7893",
        indexId: 12094,
        senderId: "4321",
        content: "Sorry for the inconvenience, we will look into it",
        date: "13 November",
        timestamp: "11:21",
        system: false,
        seen: true,
    },
]);

/* User actions for chat */
const roomActions = ref([
    { name: "viewLocation", title: "View Facility Information" },
    { name: "markAsResolved", title: "Mark as Resolved" },
    { name: "reportChat", title: "Report Chat" },
]);
const messageActions = ref([
    { name: "replyMessage", title: "Reply" },
    { name: "reportMessage", title: "Report" },
]);

/* vue events */
const viewFacilityInfo = (roomId) => {
    console.log("Room Info", roomId);
};
function roomActionHandler (event) {
    console.log(event.detail[0])
    // console.log("Clicked on room action", roomId, action);
};
</script>

<style>
.vac-avatar-current-offset {
    margin:0 !important;
}
</style>