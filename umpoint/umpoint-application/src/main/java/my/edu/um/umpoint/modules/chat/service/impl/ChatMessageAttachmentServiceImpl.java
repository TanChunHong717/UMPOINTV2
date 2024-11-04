package my.edu.um.umpoint.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.chat.dao.ChatMessageAttachmentDao;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageAttachmentDTO;
import my.edu.um.umpoint.modules.chat.dto.ChatMessageDTO;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageAttachmentEntity;
import my.edu.um.umpoint.modules.chat.service.ChatMessageAttachmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    public QueryWrapper<ChatMessageAttachmentEntity> getWrapper(
        Map<String,
            Object> params
    ){
        String id = (String) params.get("id");

        QueryWrapper<ChatMessageAttachmentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public void save(List attachments, ChatMessageDTO chatMessageDTO){
        List<ChatMessageAttachmentEntity> attachmentList = new ArrayList<>();
        for (Object attachment : attachments) {
            if (attachment instanceof String attachmentURL) {
                // treat default string item as url to image
                ChatMessageAttachmentEntity attachmentEntity = new ChatMessageAttachmentEntity();
                attachmentEntity.setType("image");
                attachmentEntity.setUrl(attachmentURL);
                attachmentEntity.setMessageId(chatMessageDTO.getId());
                attachmentList.add(attachmentEntity);
            } else if (attachment instanceof Map<?, ?> attachmentMap) {
                if (
                    attachmentMap.containsKey("type") &&
                    attachmentMap.containsKey("url") &&
                    !attachmentMap.get("type").toString().isEmpty() &&
                    !attachmentMap.get("url").toString().isEmpty()
                ) {
                    ChatMessageAttachmentEntity attachmentEntity = new ChatMessageAttachmentEntity();
                    attachmentEntity.setType(attachmentMap.get("type").toString());
                    attachmentEntity.setUrl(attachmentMap.get("url").toString());
                    attachmentEntity.setMessageId(chatMessageDTO.getId());
                    attachmentList.add(attachmentEntity);
                }
            }
        }

        super.insertBatch(attachmentList);
    }
}