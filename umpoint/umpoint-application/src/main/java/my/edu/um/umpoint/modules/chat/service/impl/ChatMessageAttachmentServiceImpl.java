package my.edu.um.umpoint.modules.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.chat.dao.ChatMessageAttachmentDao;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageAttachmentDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageAttachmentEntity;
import my.edu.um.umpoint.modules.chat.service.ChatMessageAttachmentService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Chat message attachment
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Service
public class ChatMessageAttachmentServiceImpl extends CrudServiceImpl<ChatMessageAttachmentDao,
    ChatMessageAttachmentEntity, ChatMessageAttachmentDTO> implements ChatMessageAttachmentService{

    @Override
    public QueryWrapper<ChatMessageAttachmentEntity> getWrapper(Map<String, Object> params){
        String id = (String) params.get("id");

        QueryWrapper<ChatMessageAttachmentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}