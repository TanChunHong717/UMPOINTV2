package my.edu.um.umpoint.modules.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.chat.controller.ChatMessageAttachment;
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
    public void save(List<ChatMessageAttachment> attachments, ChatMessageDTO chatMessageDTO){
        List<ChatMessageAttachmentEntity> attachmentEntityList = new ArrayList<>();

        for (ChatMessageAttachment attachment : attachments) {
            if (
                attachment.name != null &&
                attachment.type != null &&
                attachment.url != null
            ) {
                ChatMessageAttachmentEntity attachmentEntity = new ChatMessageAttachmentEntity();
                attachmentEntity.setName(attachment.name);
                attachmentEntity.setType(attachment.type);
                attachmentEntity.setUrl(attachment.url);
                attachmentEntity.setMessageId(chatMessageDTO.getId());
                attachmentEntityList.add(attachmentEntity);
            }
        }

        super.insertBatch(attachmentEntityList);

        // for return message
        chatMessageDTO.setChatMessageAttachmentDTOList(
            ConvertUtils.sourceToTarget(attachmentEntityList, ChatMessageAttachmentDTO.class)
        );
    }
}