package my.edu.um.umpoint.modules.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.exception.ErrorCode;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.common.utils.MessageUtils;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatMessage;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageEntity;
import my.edu.um.umpoint.modules.chat.service.ChatMessageAttachmentService;
import my.edu.um.umpoint.modules.chat.service.ChatMessageService;
import my.edu.um.umpoint.modules.chat.service.ChatRoomService;
import my.edu.um.umpoint.modules.client.entity.CliTokenEntity;
import my.edu.um.umpoint.modules.client.entity.CliUserEntity;
import my.edu.um.umpoint.modules.security.entity.SysUserTokenEntity;
import my.edu.um.umpoint.modules.security.service.ShiroService;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Chat message
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@RestController
@Tag(name = "Chat message")
public class ChatMessageController{

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ChatMessageAttachmentService chatMessageAttachmentService;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private ShiroService shiroService;

    @GetMapping("chat/messages/{roomId}")
    @Operation(summary = "Get list of messages")
    @RequiresPermissions("chat:message:view")
    public Result<List<ChatMessageEntity>> getMessages(@PathVariable("roomId") Long roomId){
        List<ChatMessageEntity> data = chatMessageService.getRoomMessages(roomId);
        return new Result<List<ChatMessageEntity>>().ok(data);
    }


    @MessageMapping("chat/messages/{roomId}/sendMessage")
    @Operation(summary = "Save")
    public void sendMessage(
        @DestinationVariable("roomId") Long roomId,
        SimpMessageHeaderAccessor headerAccessor,
        @Payload ChatMessage request
    ){
        String destination = "/queue/room/" + roomId;

        // validate if chat room exist and accept messages
        ChatRoomDTO chatRoomDTO = chatRoomService.get(roomId);
        if (chatRoomDTO == null) {
            throw new BadWebSocketRequestException(destination, "Invalid room ID", request.returnMessage);
        }
        if (!chatRoomService.canChatInRoom(chatRoomDTO)) {
            throw new BadWebSocketRequestException(destination, "Invalid room ID", request.returnMessage);
        }

        // validate if user is in chat room
        UserDetail user = doGetAuthenticationInfo(headerAccessor.getNativeHeader("token").get(0));
        if (!ChatRoomController.validateUserInChat(chatRoomDTO, user)) {
            throw new BadWebSocketRequestException(destination, "Invalid room ID", request.returnMessage);
        }

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        if (user.getSuperAdmin() == null) {
            chatMessageDTO.setUserId(user.getId());
            chatMessageDTO.setSenderType(
                ChatConstant.UserType.USER.getValue()
            );
        } else {
            chatMessageDTO.setAdminId(user.getId());
            chatMessageDTO.setSenderType(
                ChatConstant.UserType.ADMIN.getValue()
            );
        }

        // validate content not empty
        boolean contentIsEmpty = true;
        if (
            request.message != null &&
            !request.message.isEmpty()
        ) {
            contentIsEmpty = false;
            chatMessageDTO.setMessage(request.message);
        }

        boolean hasAttachment = false;

        if (
            request.attachments != null &&
            !request.attachments.isEmpty()
        ) {
            contentIsEmpty = false;
            hasAttachment = true;
        }
        if (contentIsEmpty) {
            throw new BadWebSocketRequestException(destination, "Message content can not be empty",
                request.returnMessage
            );
        }

        chatMessageDTO.setChatRoomId(chatRoomDTO.getId());
        if (request.replyMessageId != null) {
            if (!validateChatMessageId(roomId, request.replyMessageId)) {
                throw new BadWebSocketRequestException(destination, "Invalid reply message ID", request.returnMessage);
            }
            chatMessageDTO.setReplyMessageId(request.replyMessageId);
        }

        chatMessageService.save(chatMessageDTO);

        if (hasAttachment) {
            chatMessageAttachmentService.save(request.attachments, chatMessageDTO);
        }

        // finished saving, refetch to broadcast to ws channel
        // set return message
        chatMessageDTO.setReturnMessage(Optional.ofNullable(request.returnMessage));
        messagingTemplate.convertAndSend(destination, JsonUtils.toJsonStringWithStringId(chatMessageDTO));

        // check automatic response
        if (chatRoomDTO.getAutoChatbotReply() == ChatConstant.AutoReply.ENABLED.getValue()) {
            // check if require response from human
            if (chatMessageDTO.getMessage().toLowerCase().contains("talk to human")) {
                // convert chat to not auto reply
                chatRoomService.stopRoomAutoReply(chatRoomDTO.getId());
                // insert system message that hand over to human
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    ChatMessageDTO systemMessage = buildSystemMessage(
                        roomId,
                        "This chat is handed over to human agent."
                    );
                    chatMessageService.save(systemMessage);
                    messagingTemplate.convertAndSend(
                        destination,
                        JsonUtils.toJsonStringWithStringId(systemMessage)
                    );
                });

                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

                return;
            } else {
                CompletableFuture<Void> future = CompletableFuture
                    .supplyAsync(() -> {
                        // generate ai chat response
                        return chatMessageService
                            .generateAiChatResponse(
                                chatRoomDTO,
                                chatMessageDTO
                            );
                    })
                    .thenApply((String response) -> buildBotMessage(roomId, response))
                    .thenApply((ChatMessageDTO botMessage) -> {
                        // store to database
                        chatMessageService.save(botMessage);
                        // broadcast to user
                        messagingTemplate.convertAndSend(
                            destination,
                            JsonUtils.toJsonStringWithStringId(botMessage)
                        );

                        return botMessage;
                    })
                    .thenApply((ChatMessageDTO botMessage) -> {
                        return chatMessageService.generateAiChatResponseOptions(
                            chatRoomDTO,
                            chatMessageDTO,
                            botMessage
                        );
                    })
                    .thenApply(
                        (List<String> options) -> {
                            if (options.isEmpty()) {
                                // default must have one option
                                return buildBotOptionsMessage(
                                    roomId,
                                    Collections.singletonList("Talk to human")
                                );
                            }
                            return buildBotOptionsMessage(
                                roomId,
                                options
                            );
                        }
                    )
                    .thenAccept((ChatMessageDTO optionsMessage) -> {
                        // store to database
                        chatMessageService.save(optionsMessage);
                        // broadcast to user
                        messagingTemplate.convertAndSend(
                            destination,
                            JsonUtils.toJsonStringWithStringId(optionsMessage)
                        );
                    });

                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public boolean validateChatMessageId(Long roomId, Long messageId){
        ChatMessageDTO message = chatMessageService.get(messageId);
        if (message == null) return false;
        return message.getChatRoomId().equals(roomId);
    }

    private ChatMessageDTO buildBotMessage(Long chatRoomId, String response){
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setChatRoomId(chatRoomId);
        chatMessageDTO.setSenderType(ChatConstant.UserType.BOT.getValue());
        chatMessageDTO.setMessage(response);
        chatMessageDTO.setCreatedAt(new Date());
        return chatMessageDTO;
    }

    private ChatMessageDTO buildSystemMessage(Long chatRoomId, String message){
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setChatRoomId(chatRoomId);
        chatMessageDTO.setSenderType(ChatConstant.UserType.SYSTEN.getValue());
        chatMessageDTO.setMessage(message);
        chatMessageDTO.setCreatedAt(new Date());
        return chatMessageDTO;
    }

    private ChatMessageDTO buildBotOptionsMessage(Long chatRoomId, List<String> options){
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setChatRoomId(chatRoomId);
        chatMessageDTO.setSenderType(ChatConstant.UserType.BOT_REPLY_OPTIONS.getValue());
        chatMessageDTO.setMessage(JsonUtils.toJsonString(options));
        chatMessageDTO.setCreatedAt(new Date());
        return chatMessageDTO;
    }

    protected UserDetail doGetAuthenticationInfo(String accessToken) throws AuthenticationException{
        SysUserTokenEntity tokenEntity = shiroService.getByToken(accessToken);
        CliTokenEntity cliTokenEntity = shiroService.getCliByToken(accessToken);
        if (
            (tokenEntity == null && cliTokenEntity == null) ||
            (tokenEntity != null && tokenEntity.getExpireDate().getTime() < System.currentTimeMillis()) ||
            (cliTokenEntity != null && cliTokenEntity.getExpireDate().getTime() < System.currentTimeMillis())
        ) {
            throw new IncorrectCredentialsException(MessageUtils.getMessage(ErrorCode.TOKEN_INVALID));
        }

        UserDetail userDetail;
        if (tokenEntity != null) {
            SysUserEntity userEntity = shiroService.getUser(tokenEntity.getUserId());
            userDetail = ConvertUtils.sourceToTarget(userEntity, UserDetail.class);
            List<Long> deptIdList = shiroService.getDataScopeList(userDetail.getId());
            userDetail.setDeptIdList(deptIdList);

            if (userDetail.getStatus() == 0) {
                throw new LockedAccountException(MessageUtils.getMessage(ErrorCode.ACCOUNT_LOCK));
            }
        } else {
            CliUserEntity userEntity = shiroService.getCliUser(cliTokenEntity.getUserId());
            userDetail = ConvertUtils.sourceToTarget(userEntity, UserDetail.class);
        }

        return userDetail;
    }

    // Exception handler for websocket
    @MessageExceptionHandler(BadWebSocketRequestException.class)
    public void handleWebSocketException(BadWebSocketRequestException exp){
//        System.out.println("Handling exception " + exp.message);
        messagingTemplate.convertAndSend(exp.destination, JsonUtils.toJsonString(
            Map.of(
                "error", 1,
                "message", exp.message,
                "returnMessage", exp.returnMessage
            )
        ));
    }

    static class BadWebSocketRequestException extends RuntimeException{
        private final String message;
        private final String returnMessage;
        private final String destination;

        public BadWebSocketRequestException(String destination, String message, String returnMessage){
            super(message);
            this.destination = destination;
            this.message = message;
            this.returnMessage = returnMessage;
        }
    }
}

