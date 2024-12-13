package my.edu.um.umpoint.modules.chat.service;

import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatRoomEntity;

import java.util.Map;

/**
 * Chat room
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
public interface ChatRoomService extends CrudService<ChatRoomEntity, ChatRoomDTO> {
    Long getRoomByFacilityId(Long userId, ChatConstant.FacilityType facilityType, Long facilityId);
    Long getRoomByFacilityId(Long userId, Long adminId, ChatConstant.FacilityType facilityType, Long facilityId);

    PageData<ChatRoomDTO> listUserRoomPage(Map<String, Object> params);
    PageData<ChatRoomDTO> listAdminDepartmentRoomPage(Map<String, Object> params);

    void assignAdminId(Long roomId);
    boolean stopRoomAutoReply(Long roomId);

    boolean closeRoom(Long roomId);
    boolean resolveRoom(Long roomId);
    boolean reportRoom(Long roomId);

    boolean canChatInRoom(ChatRoomDTO chatRoomDTO);
    boolean canChatInRoom(Long roomId);
}