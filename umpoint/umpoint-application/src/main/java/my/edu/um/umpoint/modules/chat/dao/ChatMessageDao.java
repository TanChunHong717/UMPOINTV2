package my.edu.um.umpoint.modules.chat.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.chat.entity.ChatMessageEntity;
import my.edu.um.umpoint.modules.space.entity.SpcBookingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Chat message
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-28
 */
@Mapper
public interface ChatMessageDao extends BaseDao<ChatMessageEntity> {
    List<ChatMessageEntity> getRoomMessages(Map<String, Object> params);
}