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
    :rooms-loaded="true"
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
        <el-button size="small" v-for="option in message.options" @click="sendButtonMessage(message.id, option)">{{ option }}</el-button>
      </div>
    </template>

    <!-- hide avatars -->
    <div v-for="message in messages" :slot="'message-avatar_' + message._id"></div>
  </vue-advanced-chat>
</template>

<script setup>
import { ref, computed, watch, onBeforeUnmount } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";

import * as chatApi from "@/service/api-chat";
import { uploadFile } from "@/service/api-upload.js";
import { chatRoomTypes, chatUserTypes, chatFacilityTypes } from "@/constants/chat";

// chat component
import { register } from "vue-advanced-chat";
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
  let targetMessage = messages.value.find((m) => m._id === "new-message" && parseInt(m.indexId) === parseInt(response.returnMessage));

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
    targetMessage.files = chatApi.parseFilesFromApi(response.chatMessageAttachmentDTOList);
  } else {
    // new message from channel
    messages.value.push(chatApi.parseMessageFromApi(response, props.userId));
  }
}
onBeforeUnmount(() => {
  if (wsCurrentRoom) {
    wsCurrentRoom.unsubscribe();
  }
  wsClient.deactivate();
});

// full list of rooms
const rooms = ref();
const loadFirstRoom = ref(false); // don't load first room by default

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
  return room.status === chatRoomTypes.CREATED || room.status === chatRoomTypes.OPEN;
});

// first time check for query params to load room based on facility ID
const currentRoute = useRoute();
async function getFacilityChatRoom(facilityType, facilityId) {
  let roomId = await chatApi.getChatRoomIdByFacility(facilityType, facilityId);
  return roomId;
}
watch(
  () => currentRoute.query,
  async (query) => {
    if (query.facilityType && query.facilityId) {
      let roomId = await getFacilityChatRoom(query.facilityType, query.facilityId);
      changeWsClientRoom(wsClient, roomId);
      loadFirstRoom.value = true;
    }
  },
  { immediate: true }
);

// load all rooms
async function fetchRooms() {
  try {
    let apiRooms = await chatApi.getChatRooms();
    // reverse chronological order
    rooms.value = apiRooms.toReversed();
  } catch (error) {
    ElMessage.error("Error fetching chat rooms");
  }
}
fetchRooms();

const messages = ref([]);
const messagesFullyLoaded = ref(false);

/* Load messages */
async function fetchMessages(event) {
  let {
    detail: [{ room, options }]
  } = event;
  messagesFullyLoaded.value = false;

  if (options && options.reset) {
    // room is opened for first time
    // change websocket subscribe channel
    changeWsClientRoom(wsClient, room.roomId);
    // fetch messages
    try {
      let messagesRes = await chatApi.getMessages(room.roomId, props.userId);
      messages.value = messagesRes;
    } catch (error) {
      ElMessage.error("Error fetching messages");
    }
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
        options: JSON.parse(message.content)
      };
    });

  // <div slot="`message_${message._id}`">
  //     <el-button>I need more help</el-button>
  //     <el-button>Talk to human</el-button>
  // </div>
});
function sendButtonMessage(messageId, userResponse) {
  // only send message if the last message is the bot options
  // if (messageId == messages.value.slice(-1)[0]._id) {
  //     sendMessage({
  //         detail: [
  //             {
  //                 roomId: currentRoomId.value,
  //                 content: userResponse,
  //             },
  //         ],
  //     });
  // }
}

// send message
async function sendMessage(event) {
  let {
    detail: [{ roomId, content, files, replyMessage }]
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
      month: "long"
    }),
    timestamp: new Date().toLocaleTimeString("en-MY", {
      hour: "numeric",
      minute: "2-digit"
    }),
    system: false,
    saved: false,
    distributed: false,
    seen: false,
    deleted: false,
    failure: false
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
          type: file.type
        })
      )
    );
  }
  // set return message to identify this message
  wsMessage.returnMessage = indexId;

  // call api to send message
  chatApi.sendMessage(wsClient, roomId, { token: props.userToken }, wsMessage);
}

/* Action events */
const router = useRouter();
const viewFacilityInfo = (roomId) => {
  console.log(roomId);
  let room = getRoom(roomId);
  console.log(room);
  router.push({
    name: "facility-information",
    params: {
      type: chatFacilityTypes[room.facilityType],
      id: room.facilityId
    }
  });
};
const assignToSelf = (roomId) => {
  chatApi.assignToSelf(roomId, props.userId).then(() => {
    ElMessage.success("Assigned to you");
    fetchRooms();
  });
};
/* User actions for chat room */
const roomActions = ref([
  { name: "viewLocation", title: "View Facility Information" },
  { name: "assignToSelf", title: "Assign to You" },
  { name: "markAsResolved", title: "Mark as Resolved" },
  { name: "reportChat", title: "Report Chat" }
]);
function roomActionHandler(event) {
  let {
    detail: [
      {
        roomId,
        action: { name: actionName }
      }
    ]
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
    case "assignToSelf":
      chatApi.assignToSelf(roomId);
      break;
  }
}

/* User actions for chat message */
const messageActions = ref([
  { name: "replyMessage", title: "Reply" },
  { name: "reportMessage", title: "Report" }
]);
function messageActionHandler(event) {
  let {
    detail: [
      {
        roomId,
        message,
        action: { name: actionName }
      }
    ]
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
        chatApi.reportMessage(roomId, message._id, reason, props.userId);
        fetchRooms();
      });
      break;
  }
}

/* Report chat, prompt reason from user */
function showReportChatPopup(callback) {
  ElMessageBox.prompt("Please provide a reason for reporting user. The room will be closed after reporting.", "Report User", {
    confirmButtonText: "OK",
    cancelButtonText: "Cancel",
    inputPattern: /\S+/,
    inputErrorMessage: "Please provide a reason"
  }).then(({ value }) => {
    callback(value);
  });
}
</script>

<style>
.vac-avatar-current-offset {
  margin: 0 !important;
}
</style>
