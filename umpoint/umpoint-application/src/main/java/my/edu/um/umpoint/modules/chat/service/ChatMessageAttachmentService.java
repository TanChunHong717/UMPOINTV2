package my.edu.um.umpoint.modules.chat.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.chat.controller.ChatMessageAttachment;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageAttachmentDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageAttachmentEntity;

import java.util.List;

/**
 * Chat message attachment
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
public interface ChatMessageAttachmentService extends CrudService<ChatMessageAttachmentEntity,
    ChatMessageAttachmentDTO>{

    void save(List<ChatMessageAttachment> attachments, ChatMessageDTO chatMessageDTO);
}