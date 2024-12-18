package my.edu.um.umpoint.modules.chat.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageAttachmentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Chat message attachment
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Mapper
public interface ChatMessageAttachmentDao extends BaseDao<ChatMessageAttachmentEntity> {
	
}