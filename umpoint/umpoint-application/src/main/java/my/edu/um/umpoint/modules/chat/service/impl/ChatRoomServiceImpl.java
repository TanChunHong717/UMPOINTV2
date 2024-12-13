package my.edu.um.umpoint.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.annotation.DataFilter;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
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
import java.util.HashMap;
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
    public PageData<ChatRoomDTO> listUserRoomPage(Map<String, Object> params){
        IPage<ChatRoomEntity> page = getPage(params, "created_at", false);

        List<ChatRoomEntity> list = baseDao.getClientRooms(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @DataFilter(tableAlias = "r", deptId = "facility_department_id")
    public PageData<ChatRoomDTO> listAdminDepartmentRoomPage(Map<String, Object> params){
        IPage<ChatRoomEntity> page = getPage(params, "created_at", false);

        List<ChatRoomEntity> list = baseDao.getClientRooms(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public Long getRoomByFacilityId(Long userId, ChatConstant.FacilityType facilityType, Long facilityId){
        UserDetail currentUser = SecurityUser.getUser();

        QueryWrapper<ChatRoomEntity> wrapper = getRoomByFacilityIdWrapper(facilityType, facilityId);
        wrapper.eq("initiate_user_id", userId);

        ChatRoomEntity chatRoom = baseDao.selectOne(wrapper);
        Long chatRoomId;
        if (chatRoom == null) {
            ChatRoomDTO chatRoomDTO = createChatRoom(userId, facilityType, facilityId);
            chatRoomDTO.setCreator(currentUser.getId());
            super.save(chatRoomDTO);

            chatRoomId = chatRoomDTO.getId();
        } else {
            chatRoomId = chatRoom.getId();
        }

        return chatRoomId;
    }

    @Override
    public Long getRoomByFacilityId(Long userId, Long adminId, ChatConstant.FacilityType facilityType, Long facilityId){
        UserDetail currentUser = SecurityUser.getUser();

        QueryWrapper<ChatRoomEntity> wrapper = getRoomByFacilityIdWrapper(facilityType, facilityId);
        wrapper.eq("initiate_user_id", userId);
        wrapper.eq("assigned_admin_id", adminId);

        ChatRoomEntity chatRoom = baseDao.selectOne(wrapper);
        Long chatRoomId;
        if (chatRoom == null) {
            ChatRoomDTO chatRoomDTO = createChatRoom(userId, adminId, facilityType, facilityId);
            chatRoomDTO.setCreator(currentUser.getId());
            super.save(chatRoomDTO);

            chatRoomId = chatRoomDTO.getId();
        } else {
            chatRoomId = chatRoom.getId();
        }

        return chatRoomId;
    }

    private QueryWrapper<ChatRoomEntity> getRoomByFacilityIdWrapper(
        ChatConstant.FacilityType facilityType,
        Long facilityId
    ){
        QueryWrapper<ChatRoomEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("facility_type", facilityType.getValue());
        wrapper.eq("facility_id", facilityId);
        wrapper.notIn("status", List.of(
            ChatConstant.RoomStatus.CLOSED.getValue(),
            ChatConstant.RoomStatus.RESOLVED.getValue(),
            ChatConstant.RoomStatus.REPORTED.getValue()
        ));
        wrapper.orderByDesc("created_at");
        wrapper.last("LIMIT 1");

        return wrapper;
    }

    private ChatRoomDTO createChatRoom(Long clientUserId, ChatConstant.FacilityType facilityType, Long facilityId){
        return createChatRoom(clientUserId, null, facilityType, facilityId);
    }

    private ChatRoomDTO createChatRoom(
        Long clientUserId, Long adminUserId, ChatConstant.FacilityType facilityType,
        Long facilityId
    ){
        String roomName = "";
        Long departmentId = null;
        switch (facilityType) {
            case SPACE -> {
                SpcSpaceDTO space = spcSpaceService.get(facilityId);
                roomName = space.getName() + " (" + space.getDeptName() + ")";
                departmentId = space.getDeptId();
            }
            case SERVICE -> {
                SvcServiceDTO service = svcServiceService.get(facilityId);
                roomName = service.getName() + " (" + service.getDeptName() + ")";
                departmentId = service.getDeptId();
            }
            case ACCOMMODATION -> {
                AccAccommodationDTO acco = accAccommodationService.get(facilityId);
                roomName = acco.getName() + " (" + acco.getDeptName() + ")";
                departmentId = acco.getDeptId();
            }
        }

        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setFacilityType(facilityType.getValue());
        chatRoomDTO.setFacilityId(facilityId);
        chatRoomDTO.setFacilityDepartmentId(departmentId);
        chatRoomDTO.setInitiateUserId(clientUserId);
        chatRoomDTO.setAssignedAdminId(adminUserId);
        chatRoomDTO.setStatus(ChatConstant.RoomStatus.CREATED.getValue());
        chatRoomDTO.setAutoChatbotReply(ChatConstant.AutoReply.ENABLED.getValue());
        chatRoomDTO.setName(roomName);
        chatRoomDTO.setCreatedAt(DateUtils.convertLocalDateTimeToDate(LocalDateTime.now()));
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

    public void saveAndSendSystemMessage(Long roomId, String message){
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
    public boolean canChatInRoom(ChatRoomDTO chatRoomDTO){
        return (
            chatRoomDTO.getStatus() == ChatConstant.RoomStatus.CREATED.getValue() ||
            chatRoomDTO.getStatus() == ChatConstant.RoomStatus.OPEN.getValue()
        );
    }

    @Override
    public boolean canChatInRoom(Long roomId){
        return canChatInRoom(ConvertUtils.sourceToTarget(
            baseDao.selectById(roomId),
            ChatRoomDTO.class
        ));
    }
}