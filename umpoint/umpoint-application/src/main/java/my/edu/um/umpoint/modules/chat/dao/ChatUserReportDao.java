package my.edu.um.umpoint.modules.chat.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.chat.entity.ChatUserReportEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Report user in chat
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-11-25
 */
@Mapper
public interface ChatUserReportDao extends BaseDao<ChatUserReportEntity> {
    List<ChatUserReportEntity> getList(Map<String, Object> params);
}