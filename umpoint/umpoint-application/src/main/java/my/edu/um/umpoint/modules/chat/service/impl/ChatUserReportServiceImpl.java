package my.edu.um.umpoint.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.modules.chat.dao.ChatUserReportDao;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatRoomDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatUserReportDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatRoomEntity;
import my.edu.um.umpoint.modules.chat.entity.ChatUserReportEntity;
import my.edu.um.umpoint.modules.chat.service.ChatMessageService;
import my.edu.um.umpoint.modules.chat.service.ChatRoomService;
import my.edu.um.umpoint.modules.chat.service.ChatUserReportService;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.space.entity.SpcBookingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Report user in chat
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-11-25
 */
@Service
public class ChatUserReportServiceImpl extends CrudServiceImpl<ChatUserReportDao, ChatUserReportEntity, ChatUserReportDTO> implements ChatUserReportService{

    @Autowired
    ChatRoomService chatRoomService;

    @Override
    public QueryWrapper<ChatUserReportEntity> getWrapper(Map<String, Object> params){
        String id = (String) params.get("id");

        QueryWrapper<ChatUserReportEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<ChatUserReportDTO> page(Map<String, Object> params) {
        IPage<ChatUserReportEntity> page = getPage(params, "created_at", false);
        List<ChatUserReportEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public void save(ChatUserReportDTO dto){
        dto.setStatus(ChatConstant.ReportStatus.UNRESOLVED.getValue());

        ChatRoomDTO chatRoom = chatRoomService.get(dto.getChatRoomId());
        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() != null) {
            dto.setReportedBy(user.getId());
            dto.setReportedByType(ChatConstant.UserType.ADMIN.getValue());

            if (dto.getReportedUser() == null) {
                dto.setReportedUser(chatRoom.getInitiateUserId());
                dto.setReportedUserType(ChatConstant.UserType.USER.getValue());
            }
        } else {
            dto.setReportedBy(user.getId());
            dto.setReportedByType(ChatConstant.UserType.USER.getValue());

            if (dto.getReportedUser() == null) {
                if (chatRoom.getAssignedAdminId() != null)
                    dto.setReportedUser(chatRoom.getAssignedAdminId());
                else
                    dto.setReportedUser(0L);
                dto.setReportedUserType(ChatConstant.UserType.ADMIN.getValue());
            }
        }

        super.save(dto);

        // set room to reported
        chatRoomService.reportRoom(dto.getChatRoomId());
    }

    @Override
    public void resolve(Long id) {
        ChatUserReportEntity entity = new ChatUserReportEntity();
        entity.setStatus(ChatConstant.ReportStatus.RESOLVED.getValue());

        baseDao.update(entity, new QueryWrapper<ChatUserReportEntity>().eq("id", id));
    }
}