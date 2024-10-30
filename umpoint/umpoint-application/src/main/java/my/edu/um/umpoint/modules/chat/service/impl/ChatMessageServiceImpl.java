package my.edu.um.umpoint.modules.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.chat.dao.ChatMessageDao;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageEntity;
import my.edu.um.umpoint.modules.chat.service.ChatMessageService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Chat message
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Service
public class ChatMessageServiceImpl extends CrudServiceImpl<ChatMessageDao, ChatMessageEntity, ChatMessageDTO> implements ChatMessageService{

    @Override
    public QueryWrapper<ChatMessageEntity> getWrapper(Map<String, Object> params){
        String id = (String) params.get("id");

        QueryWrapper<ChatMessageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}