package my.edu.um.umpoint.modules.chat.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageEntity;

import java.util.List;

/**
 * Chat message
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
public interface ChatMessageService extends CrudService<ChatMessageEntity, ChatMessageDTO>{
    List<ChatMessageEntity> getRoomMessages(Long roomId);
    String generateAiChatResponse(ChatRoomDTO chatRoomDTO, ChatMessageDTO chatMessageDTO);
    List<String> generateAiChatResponseOptions(ChatRoomDTO chatRoomDTO, ChatMessageDTO chatMessageDTO, ChatMessageDTO botMessageDTO);
}