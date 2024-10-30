package my.edu.um.umpoint.modules.chat.service;

import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatRoomEntity;

import java.util.List;

/**
 * Chat room
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
public interface ChatRoomService extends CrudService<ChatRoomEntity, ChatRoomDTO>{
    List<ChatMessageDTO> getRoomMessages(Long roomId);

    Long getRoomByFacilityId(ChatConstant.FacilityType facilityType, Long facilityId);
}