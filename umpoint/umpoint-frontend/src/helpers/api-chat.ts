import api from "@/utils/api";
import app, { facilityTypes } from "@/constants/app";
import { chatUserTypes } from "@/constants/chat";
import sockjs from "sockjs-client/dist/sockjs";
import { Client as StompClient } from "@stomp/stompjs";
import { Message, MessageFile } from "vue-advanced-chat";
import { JavaId } from "@/types/interface";

const OPTION_KEY = "options_chat_name";
const BOT_KEY = "bot_chat_name";

export function createWebSocketClient() {
    const socketUrl = app.apiUrl + "/ws";

    const socket = new sockjs(socketUrl);
    const client = new StompClient({
        webSocketFactory: () => socket,
        debug: function (str) {
            console.log("debugging in client", str);
        },
    });

    client.activate();
    return client;
}

export function sendMessage(
    client: StompClient,
    roomId: JavaId,
    headers = {},
    message: any
) {
    return client.publish({
        destination: `/ws/chat/messages/${roomId}/sendMessage`,
        body: JSON.stringify(message),
        headers,
    });
}

export function subscribeToRoom(
    client: StompClient,
    roomId: JavaId,
    callback: (message: any) => void
) {
    return client.subscribe(`/queue/room/${roomId}`, callback);
}
export function unsubscribeFromRoom(client: StompClient, roomId: JavaId) {
    return client.unsubscribe(`/queue/room/${roomId}`);
}
export function stopWebSocketClient(client: StompClient) {
    return client.deactivate();
}

export async function getChatRooms() {
    let response = await api.get(`/chat/room/page`);
    let rooms = [];
    for (let room of response.data.data.list) {
        let users = [
            {
                // default bot
                _id: BOT_KEY,
                username: "Friendly Bot",
            },
            {
                // default system
                _id: "system",
                username: "System",
            },
            {
                // initiate user
                _id: room.initiateUserId,
                username: "User",
            },
        ];
        if (room.assignedAdminId) {
            users.push({
                _id: room.assignedAdminId,
                username: "Admin",
            });
        }

        rooms.push({
            roomId: room.id,
            roomName: room.name,
            unreadCount: 0,
            lastMessage: room.lastMessage,
            users,
            facilityType: room.facilityType,
            facilityId: room.facilityId,
            status: room.status,
        });
    }
    return rooms;
}

export async function getMessages(
    roomId: JavaId,
    currentUserId: JavaId,
    page: number = 1,
) {
    let response = await api.get(`/chat/messages/${roomId}`, {
        params: {
            page,
            limit: 100,
        },
    });
    let messages = response.data.data.map(
        (message: any) => { return parseMessageFromApi(message, currentUserId) }
    );
    return messages;
}

export function reportChatRoom(
    chatRoomId: JavaId,
    reason: string,
) {
    if (reason.trim() === "") {
        return Promise.reject("Reason is required");
    }
    return api.post(`/chat/report`, {
        chatRoomId,
        reason,
    });
}

export function reportMessage(
    chatRoomId: JavaId,
    messageId: JavaId,
    reason: string,
) {
    if (reason.trim() === "") {
        return Promise.reject("Reason is required");
    }
    return api.post(`/chat/report`, {
        chatRoomId,
        messageId,
        reason,
    });
}

export function markAsResolved(roomId: JavaId) {
    return api.post(`/chat/room/${roomId}/resolve`);
}

/*
 *  Convert message from API to chat component format
 */
export function parseMessageFromApi(
    messageDto: any,
    currentUserId: JavaId = null,
): Message {
    let message: Message & Record<string, unknown> = {
        _id: messageDto.id,
        indexId: messageDto.id,
        content: messageDto.message ?? "",
        senderId: convertSenderId(messageDto, currentUserId),
        date: new Date(messageDto.createdAt).toLocaleDateString("en-MY", {
            day: "numeric",
            month: "long",
            // only merge year option if not current year
            ...(new Date(messageDto.createdAt).getFullYear() ==
                new Date().getFullYear()
                ? null
                : { year: "numeric" }),
        }),
        timestamp: new Date(messageDto.createdAt).toLocaleTimeString("en-MY", {
            hour: "numeric",
            minute: "2-digit",
        }),
        system: messageDto.senderType === chatUserTypes.SYSTEM,
        saved: true,
        distributed: true,
        seen: true,
        deleted: false,
        failure: false,
        disableActions: false,
        disableReactions: true,
        senderType: messageDto.senderType,
    };
    messageDto.attachments = messageDto.chatMessageAttachmentEntityList ?? messageDto.chatMessageAttachmentDTOList;
    if (messageDto.attachments) {
        message.files = parseFilesFromApi(
            messageDto.attachments
        );
    }
    if (messageDto.replyMessageId) {
        message.replyMessage = {
            _id: messageDto.replyMessageId,
            content: messageDto.replyMessage.message,
            senderId: parseUsername(messageDto.replyMessage) ?? "",
        };
        messageDto.replyMessage.attachments = messageDto.replyMessage.chatMessageAttachmentEntityList ?? messageDto.replyMessage.chatMessageAttachmentDTOList;
        if (messageDto.replyMessage.attachments) {
            message.replyMessage.files = parseFilesFromApi(
                messageDto.replyMessage.attachments
            );
        }
    }
    return message;

    // chat component target
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

    // response from api
    //{
    //     "id": "1852747975760048129",
    //     "chatRoomId": "1851522712135282689",
    //     "senderType": 2,
    //     "userId": "1",
    //     "adminId": null,
    //     "message": null,
    //     "replyMessageId": null,
    //     "createdAt": "2024-11-03 00:21:54",
    //     "chatMessageAttachmentEntityList": [ // or chatMessageAttachmentDTOList
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
export function parseFilesFromApi(messageAttachmentDto: Array<any>) {
    let files: Array<MessageFile> = [];
    if (!messageAttachmentDto || messageAttachmentDto.length === 0) {
        return files;
    }
    for (let file of messageAttachmentDto) {
        files.push({
            name: file.name,
            type: file.type,
            extension: "",
            url: file.url,
        });
    }
    return files;
}
export function parseUsername(messageDto: {
    senderType: number;
    userId: string | null;
    adminId: string | null;
}): string | null {
    switch (messageDto.senderType) {
        case chatUserTypes.SYSTEM:
            return "system";
        case chatUserTypes.BOT:
            return BOT_KEY;
        case chatUserTypes.USER:
            return messageDto.userId;
        case chatUserTypes.ADMIN:
            return messageDto.adminId;
        case chatUserTypes.BOT_REPLY_OPTIONS:
            return OPTION_KEY;
        default:
            return null;
    }
}

// different per user type
export function convertSenderId(messageDto: any, currentUserId: JavaId): string {
    let senderId = parseUsername(messageDto);
    if (senderId == null) {
        return "";
    }
    // convert option message to own message
    if (senderId === OPTION_KEY && currentUserId) {
        return currentUserId.toString();
    }
    return senderId;
}

// client only
export async function getChatRoomIdByFacility(
    facilityType: keyof typeof facilityTypes,
    facilityId: JavaId
) {
    let response = await api.post(`/chat/room`, {
        facilityType,
        facilityId,
    });
    return response.data.data;
}