package my.edu.um.umpoint.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.constant.ChatConstant;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.JsonUtils;
import my.edu.um.umpoint.modules.chat.dao.ChatUserReportDao;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatUserReportDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatUserReportEntity;
import my.edu.um.umpoint.modules.chat.service.ChatMessageService;
import my.edu.um.umpoint.modules.chat.service.ChatRoomService;
import my.edu.um.umpoint.modules.chat.service.ChatUserReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public void save(ChatUserReportDTO dto){
        super.save(dto);

        // set room to reported
        chatRoomService.reportRoom(dto.getChatRoomId());
    }

}