package my.edu.um.umpoint.modules.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import my.edu.um.umpoint.common.annotation.LogOperation;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.BadHttpRequestException;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.utils.Result;
import my.edu.um.umpoint.common.validator.ValidatorUtils;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageAttachmentEntity;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageEntity;
import my.edu.um.umpoint.modules.chat.service.ChatMessageAttachmentService;
import my.edu.um.umpoint.modules.chat.service.ChatMessageService;
import my.edu.um.umpoint.modules.chat.service.ChatRoomService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Chat message
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@RestController
@RequestMapping("chat/message")
@Tag(name = "Chat message")
public class ChatMessageController{
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ChatMessageAttachmentService chatMessageAttachmentService;

    @GetMapping("room/{roomId}/page")
    @Operation(summary = "Get list of messages")
    @RequiresPermissions("chat:message:view")
    public Result<List<ChatMessageEntity>> getMessages(@PathVariable("roomId") Long roomId){
        List<ChatMessageEntity> data = chatMessageService.getRoomMessages(roomId);
        return new Result<List<ChatMessageEntity>>().ok(data);
    }

    @GetMapping("room/{roomId}")
    @Operation(summary = "Pagination")
    @Parameters(
        {
            @Parameter(
                name = Constant.PAGE, description = "Current page number, starting from 1", in = ParameterIn.QUERY,
                required = true, ref = "int"
            ),
            @Parameter(
                name = Constant.LIMIT, description = "Number of records per page", in = ParameterIn.QUERY,
                required = true, ref = "int"
            ),
            @Parameter(name = Constant.ORDER_FIELD, description = "Sort field", in = ParameterIn.QUERY, ref = "String"),
            @Parameter(
                name = Constant.ORDER, description = "Sort order, optional values (asc, desc)", in = ParameterIn.QUERY,
                ref = "String"
            )
        }
    )
    @RequiresPermissions("chat:message:view")
    public Result<PageData<ChatMessageDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<ChatMessageDTO> page = chatMessageService.page(params);

        return new Result<PageData<ChatMessageDTO>>().ok(page);
    }


    @PostMapping("room/{roomId}")
    @Operation(summary = "Save")
    @LogOperation("Save")
    @RequiresPermissions("chat:message:save")
    @Parameters(
        {
            @Parameter(
                name = "message", description = "Message text content", in = ParameterIn.QUERY,
                required = true, ref = "String"
            ),
            @Parameter(name = "replyMessageId", description = "Reply message ID", in = ParameterIn.QUERY, ref = "Long"),
            @Parameter(name = "attachments", description = "Reply message ID", in = ParameterIn.QUERY, ref = "Long")
        }
    )
    public Result sendMessage(
        @PathVariable("roomId") Long roomId, @RequestBody Map<String, Object> dto
    ) throws NotFoundException{
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        // validate if chat room exist
        ChatRoomDTO chatRoomDTO = chatRoomService.get(roomId);
        if (chatRoomDTO == null) {
            throw new BadHttpRequestException(400, "Invalid room ID");
        }

        // validate if user is in chat room
        UserDetail user = SecurityUser.getUser();
        if (!validateChatIsUser(chatRoomDTO, user)) {
            throw new BadHttpRequestException(400, "Invalid room ID");
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
            dto.containsKey("message") &&
            dto.get("message") != null &&
            !dto.get("message").toString().isEmpty()
        ) {
            contentIsEmpty = false;
            chatMessageDTO.setMessage(dto.get("message").toString());
        }

        boolean hasAttachment = false;
        if (
            dto.containsKey("attachments") &&
            dto.get("attachments") != null &&
            dto.get("attachments") instanceof List<?> &&
            !((List<?>) dto.get("attachments")).isEmpty()
        ) {
            contentIsEmpty = false;
            hasAttachment = true;
        }
        if (contentIsEmpty) {
            throw new BadHttpRequestException(400, "Message content can not be empty");
        }

        chatMessageDTO.setChatRoomId(chatRoomDTO.getId());
        if (dto.containsKey("replyMessageId")) {
            if (!validateChatMessageId(dto.get("replyMessageId").toString())) {
                throw new BadHttpRequestException(400, "Invalid reply message ID");
            }
            chatMessageDTO.setReplyMessageId((Long) dto.get("replyMessageId"));
        }

        chatMessageService.save(chatMessageDTO);

        if (hasAttachment) {
            chatMessageAttachmentService.save((List<?>) dto.get("attachments"), chatMessageDTO);
        }

        return new Result<Long>().ok(chatMessageDTO.getId());
    }

    private static boolean validateChatIsUser(ChatRoomDTO chatRoomDTO, UserDetail user){
        return user.getId() != null;
    }

    private boolean validateChatMessageId(String messageId){
        return chatMessageService.get(Long.valueOf(messageId)) != null;
    }
}
