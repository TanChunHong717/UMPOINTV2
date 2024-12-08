package my.edu.um.umpoint.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.chat.controller.ChatMessageController;
import my.edu.um.umpoint.modules.chat.dao.ChatMessageDao;
import my.edu.um.umpoint.modules.chat.dao.ChatRoomDao;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatRoomEntity;
import my.edu.um.umpoint.modules.chat.service.ChatMessageService;
import my.edu.um.umpoint.modules.chat.service.ChatRoomService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.service.dto.SvcServiceDTO;
import my.edu.um.umpoint.modules.service.service.SvcServiceService;
import my.edu.um.umpoint.modules.space.dto.SpcSpaceDTO;
import my.edu.um.umpoint.modules.space.service.SpcSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Chat room
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Service
public class ChatRoomServiceImpl extends CrudServiceImpl<ChatRoomDao, ChatRoomEntity, ChatRoomDTO> implements ChatRoomService{

    @Autowired
    private SpcSpaceService spcSpaceService;
    @Autowired
    private SvcServiceService svcServiceService;
    @Autowired
    private AccAccommodationService accAccommodationService;

    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Override
    public QueryWrapper<ChatRoomEntity> getWrapper(Map<String, Object> params){
        String id = (String) params.get("id");

        QueryWrapper<ChatRoomEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public Long getRoomByFacilityId(ChatConstant.FacilityType facilityType, Long facilityId){
        UserDetail user = SecurityUser.getUser();

        QueryWrapper<ChatRoomEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("facility_type", facilityType.getValue());
        wrapper.eq("facility_id", facilityId);
        wrapper.eq("initiate_user_id", user.getId());
        wrapper.notIn("status", List.of(
            ChatConstant.RoomStatus.CLOSED.getValue(),
            ChatConstant.RoomStatus.RESOLVED.getValue(),
            ChatConstant.RoomStatus.REPORTED.getValue()
        ));
        wrapper.orderByDesc("created_at");
        wrapper.last("LIMIT 1");

        ChatRoomEntity chatRoom = baseDao.selectOne(wrapper);
        Long chatRoomId;
        if (chatRoom == null) {
            ChatRoomDTO chatRoomDTO = createChatRoom(user, facilityType, facilityId);

            chatRoomId = chatRoomDTO.getId();
        } else {
            chatRoomId = chatRoom.getId();
        }

        return chatRoomId;
    }

    private ChatRoomDTO createChatRoom(UserDetail user, ChatConstant.FacilityType facilityType, Long facilityId){
        // create new room if no room or room was closed/resolved/reported
        String roomName = "";
        switch (facilityType) {
            case SPACE -> {
                SpcSpaceDTO space = spcSpaceService.get(facilityId);
                roomName = space.getName() + " (" + space.getDeptName() + ")";
            }
            case SERVICE -> {
                SvcServiceDTO service = svcServiceService.get(facilityId);
                roomName = service.getName() + " (" + service.getDeptName() + ")";
            }
            case ACCOMMODATION -> {
                AccAccommodationDTO acco = accAccommodationService.get(facilityId);
                roomName = acco.getName() + " (" + acco.getDeptName() + ")";
            }
        }

        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setFacilityType(facilityType.getValue());
        chatRoomDTO.setFacilityId(facilityId);
        chatRoomDTO.setInitiateUserId(user.getId());
        chatRoomDTO.setStatus(ChatConstant.RoomStatus.CREATED.getValue());
        chatRoomDTO.setAutoChatbotReply(ChatConstant.AutoReply.ENABLED.getValue());
        chatRoomDTO.setName(roomName);
        chatRoomDTO.setCreatedAt(DateUtils.convertLocalDateTimeToDate(LocalDateTime.now()));
        super.save(chatRoomDTO);
        return chatRoomDTO;
    }

    @Override
    public void assignAdminId(Long roomId){
        UserDetail user = SecurityUser.getUser();

        ChatRoomEntity chatRoom = baseDao.selectById(roomId);
        chatRoom.setAssignedAdminId(user.getId());

        super.updateById(chatRoom);
    }

    @Override
    public boolean stopRoomAutoReply(Long roomId){
        ChatRoomEntity chatRoom = baseDao.selectById(roomId);
        chatRoom.setAutoChatbotReply(ChatConstant.AutoReply.DISABLE.getValue());

        return super.updateById(chatRoom);
    }

    @Override
    public boolean closeRoom(Long roomId){
        saveAndSendSystemMessage(roomId, "This room is closed");
        return updateRoomStatus(roomId, ChatConstant.RoomStatus.CLOSED.getValue());
    }

    @Override
    public boolean resolveRoom(Long roomId){
        saveAndSendSystemMessage(roomId, "This room is resolved");
        return updateRoomStatus(roomId, ChatConstant.RoomStatus.RESOLVED.getValue());
    }

    @Override
    public boolean reportRoom(Long roomId){
        saveAndSendSystemMessage(roomId, "This room has been reported");
        return updateRoomStatus(roomId, ChatConstant.RoomStatus.REPORTED.getValue());
    }

    public boolean updateRoomStatus(Long roomId, int status){
        ChatRoomEntity chatRoom = baseDao.selectById(roomId);
        chatRoom.setStatus(status);

        return super.updateById(chatRoom);
    }

    public void saveAndSendSystemMessage(Long roomId, String message) {
        // send message
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setChatRoomId(roomId);
        chatMessageDTO.setSenderType(ChatConstant.UserType.SYSTEM.getValue());
        chatMessageDTO.setMessage(message);
        chatMessageDTO.setCreatedAt(new Date());
        chatMessageService.save(chatMessageDTO);

        String destination = "/queue/room/" + roomId;
        messagingTemplate.convertAndSend(
            destination,
            JsonUtils.toJsonStringWithStringId(chatMessageDTO)
        );
    }

    @Override
    public boolean canChatInRoom(ChatRoomDTO chatRoomDTO) {
        return (
            chatRoomDTO.getStatus() == ChatConstant.RoomStatus.CREATED.getValue() ||
            chatRoomDTO.getStatus() == ChatConstant.RoomStatus.OPEN.getValue()
        );
    }

    @Override
    public boolean canChatInRoom(Long roomId) {
        return canChatInRoom(ConvertUtils.sourceToTarget(
            baseDao.selectById(roomId),
            ChatRoomDTO.class
        ));
    }
}