package my.edu.um.umpoint.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.DateUtils;
import my.edu.um.umpoint.modules.accommodation.dto.AccAccommodationDTO;
import my.edu.um.umpoint.modules.accommodation.service.AccAccommodationService;
import my.edu.um.umpoint.modules.chat.dao.ChatMessageDao;
import my.edu.um.umpoint.modules.chat.dao.ChatRoomDao;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageEntity;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private ChatMessageDao chatMessageDao;
    @Autowired
    private SpcSpaceService spcSpaceService;
    @Autowired
    private SvcServiceService svcServiceService;
    @Autowired
    private AccAccommodationService accAccommodationService;

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
        wrapper.notIn("initiate_user_id", List.of(
            ChatConstant.RoomStatus.CLOSED.getValue(),
            ChatConstant.RoomStatus.RESOLVED.getValue(),
            ChatConstant.RoomStatus.REPORTED.getValue()
        ));
        wrapper.orderByDesc("created_at");
        wrapper.last("LIMIT 1");

        ChatRoomEntity chatRoom = baseDao.selectOne(wrapper);
        Long chatRoomId;
        if (chatRoom == null ) {
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
            chatRoomDTO.setName(roomName);
            chatRoomDTO.setCreatedAt(DateUtils.convertLocalDateTimeToDate(LocalDateTime.now()));
            super.save(chatRoomDTO);

            chatRoomId = chatRoomDTO.getId();
        } else {
            chatRoomId = chatRoom.getId();
        }

        return chatRoomId;
    }
}