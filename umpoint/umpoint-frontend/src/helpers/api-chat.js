import api from "@/utils/api";

async function getChatRooms() {
    let response = await api.get(`/chat/room/page`);
    let rooms = [];
    for (let room of response.data.data.list) {
        rooms.push({
            roomId: room.id,
            roomName: room.name,
            unreadCount: 0,
            lastMessage: room.lastMessage,
            users: [],
            facilityType: room.facility_type,
            facilityId: room.facility_id,
        });
    }
    return rooms;
}

async function getChatRoomIdByFacility(facilityType, facilityId) {
    let response = await api.post(`/chat/room`, {
        facilityType,
        facilityId,
    });
    return response.data.data;
}

async function getMessages(roomId, page = 1) {
    let response = await api.get(`/chat/message/room/${roomId}/page`, {
        params: {
            page,
            limit: 100,
        },
    });
    let messages = [];
    for (let messageDto of response.data.data) {
        let message = {
            _id: messageDto.id,
            indexId: messageDto.id,
            content: messageDto.message ?? "",
            senderId:
                messageDto.senderType === 2
                    ? messageDto.userId
                    : messageDto.senderType === 3
                    ? messageDto.adminId
                    : null,
            date: new Date(messageDto.createdAt).toLocaleDateString("en-MY", {
                day: "numeric",
                month: "long",
                // only merge year option if not current year
                ...(messageDto.createdAt.year == new Date().year
                    ? null
                    : { year: "numeric" }),
            }),
            timestamp: new Date(messageDto.createdAt).toLocaleTimeString(
                "en-MY",
                { hour: "numeric", minute: "2-digit" }
            ),
            system: messageDto.senderType === 0,
            saved: true,
            distributed: true,
            seen: true,
            deleted: false,
            failure: false,
            disableActions: true,
            disableReactions: true,
        };
        if (messageDto.chatMessageAttachmentEntityList) {
            message.files = [];
            for (let file of messageDto.chatMessageAttachmentEntityList) {
                message.files.push({
                    name: file.name,
                    type: file.type,
                    url: file.url,
                });
            }
        }
        if (messageDto.replyMessageId) {
            message.replyMessage = {
                content: messageDto.replyMessage.message,
                senderId:
                    messageDto.replyMessage.senderType === 2
                        ? messageDto.replyMessage.userId
                        : messageDto.replyMessage.senderType === 3
                        ? messageDto.replyMessage.adminId
                        : null,
            };
            if (messageDto.replyMessage.chatMessageAttachmentEntityList) {
                message.replyMessage.files = [];
                for (let file of messageDto.replyMessage
                    .chatMessageAttachmentEntityList) {
                    message.replyMessage.files.push({
                        name: file.name,
                        type: file.type,
                        url: file.url,
                    });
                }
            }
        }
        messages.push(message);

        // chat target
        //{
        //     _id: '7890',
        //     indexId: 12092,
        //     content: 'Message 1',
        //     senderId: '1234',
        //     username: 'John Doe',
        //     avatar: 'assets/imgs/doe.png',
        //     date: '13 November',
        //     timestamp: '10:20',
        //     system: false,
        //     saved: true,
        //     distributed: true,
        //     seen: true,
        //     deleted: false,
        //     failure: true,
        //     disableActions: false,
        //     disableReactions: false,
        //     files: [
        //       {
        //         name: 'My File',
        //         size: 67351,
        //         type: 'png',
        //         audio: true,
        //         duration: 14.4,
        //         url: 'https://firebasestorage.googleapis.com/...',
        //         preview: 'data:image/png;base64,iVBORw0KGgoAA...',
        //         progress: 88
        //       }
        //     ],
        //     replyMessage: {
        //       content: 'Reply Message',
        //       senderId: '4321',
        //       files: [
        //         {
        //           name: 'My Replied File',
        //           size: 67351,
        //           type: 'png',
        //           audio: true,
        //           duration: 14.4,
        //           url: 'https://firebasestorage.googleapis.com/...',
        //           preview: 'data:image/png;base64,iVBORw0KGgoAA...'
        //         }
        //       ]
        //     },
        //   }
        // response
        //{
        //     "id": "1852747975760048129",
        //     "chatRoomId": "1851522712135282689",
        //     "senderType": 2,
        //     "userId": "1",
        //     "adminId": null,
        //     "message": null,
        //     "replyMessageId": null,
        //     "createdAt": "2024-11-03 00:21:54",
        //     "chatMessageAttachmentEntityList": [
        //         {
        //             "id": null,
        //             "messageId": null,
        //             "type": "image",
        //             "url": "http"
        //         },
        //         {
        //             "id": null,
        //             "messageId": null,
        //             "type": "image",
        //             "url": "img2"
        //         }
        //     ]
        // }
    }
    return messages;
}

function sendMessage(roomId, message) {
    return api.post(`/chat/message/room/${roomId}`, message);
}

function reportChatRoom(roomId, reason) {
    if (reason.strip() === "") {
        return Promise.reject("Reason is required");
    }
    return api.post(`/chat/report`, {
        chat_room_id: roomId,
        reason: reason,
    });
}

function reportMessage(roomId, messageId, reason) {
    if (reason.strip() === "") {
        return Promise.reject("Reason is required");
    }
    return api.post(`/chat/report`, {
        chat_room_id: roomId,
        message_id: messageId,
        reason: reason,
    });
}

export default {
    getChatRooms,
    getChatRoomIdByFacility,
    getMessages,
    sendMessage,
    reportChatRoom,
    reportMessage,
};
