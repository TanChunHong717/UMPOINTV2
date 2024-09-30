<template>
    <BaseLayout>
        <template #title>Chat History</template>

        <vue-advanced-chat
            :current-user-id="currentUserId"
            :rooms="JSON.stringify(rooms)"
            :rooms-loaded="true"
            :messages="JSON.stringify(messages)"
            :messages-loaded="messagesLoaded"
            :room-actions="JSON.stringify(roomActions)"
            :menu-actions="JSON.stringify(roomActions)"
            :message-actions="JSON.stringify(messageActions)"
            show-add-room="false"
            show-audio="false"
            show-reaction-emojis="false"
            accepted-files="image/png,image/jpeg,image/jpg,application/pdf"
            capture-files="environment"
            room-info-enabled="true"
            @room-info="roomInfo"
        >
            <div slot="message_7891">
                <el-button>I need more help</el-button>
                <el-button>Talk to human</el-button>
            </div>
            
            <svg-icon slot="toggle-icon" type="mdi" :path="mdiMenuOpen" />

            <!-- hide avatars -->
            <div
                v-for="message in messages"
                :slot="'message-avatar_' + message._id"
            />
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

const currentUserId = ref("1234");
const rooms = ref([
    {
        roomId: "1",
        roomName: "Room 1",
        avatar: "assets/imgs/people.png",
        unreadCount: 4,
        index: 3,
        lastMessage: {
            _id: "xyz",
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
                status: {
                    state: "online",
                },
            },
            {
                _id: "1234",
                username: "John Doe",
                status: {
                    state: "online",
                    lastChanged: "today, 14:30",
                },
            },
            {
                _id: "4321",
                username: "John Snow",
                status: {
                    state: "offline",
                    lastChanged: "14 July, 20:00",
                },
            },
        ],
        facilityID: "1234",
    },
]);
const messages = ref([
    {
        _id: "7890",
        indexId: 12092,
        content: "Message 1",
        senderId: "1234",
        username: "John Doe",
        avatar: "assets/imgs/doe.png",
        date: "13 November",
        timestamp: "10:20",
        system: false,
        saved: true,
        distributed: true,
        seen: true,
        deleted: false,
        failure: true,
        disableActions: false,
        disableReactions: false,
        files: [
            {
                name: "My File",
                size: 67351,
                type: "png",
                audio: true,
                duration: 14.4,
                url: "https://firebasestorage.googleapis.com/...",
                preview: "data:image/png;base64,iVBORw0KGgoAA...",
                progress: 88,
            },
        ],
        replyMessage: {
            content: "Reply Message",
            senderId: "4321",
            files: [
                {
                    name: "My Replied File",
                    size: 67351,
                    type: "png",
                    audio: true,
                    duration: 14.4,
                    url: "https://firebasestorage.googleapis.com/...",
                    preview: "data:image/png;base64,iVBORw0KGgoAA...",
                },
            ],
        },
    },
    {
        _id: "1234",
        indexId: 12091,
        senderId: "0",
        content: "System message",
        date: "13 November",
        system: true,
    },
    {
        _id: "7891",
        indexId: 12092,
        content: "Reply Message",
        senderId: "1234",
        username: "John Doe",
        avatar: "assets/imgs/doe.png",
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
    },
    {
        _id: "7892",
        indexId: 12093,
        content: "Random",
        senderId: "1234",
        username: "John Doe",
        avatar: "assets/imgs/doe.png",
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
    },
]);

/* User actions for chat */
const roomActions = ref([
    { name: "markAsResolved", title: "Mark as Resolved" },
    { name: "reportChat", title: "Report Chat" },
]);
const messageActions = ref([
    { name: "replyMessage", title: "Reply" },
    { name: "reportMessage", title: "Report" },
]);

/* vue events */
const roomInfo = (roomId) => {
    console.log("Room Info", roomId);
};
const sendMessage = (message) => {
    console.log("Send Message", message);
};
</script>
