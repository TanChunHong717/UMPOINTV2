package my.edu.um.umpoint.modules.space.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.space.entity.SpcEventEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Space Occupied Event
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Mapper
public interface SpcEventDao extends BaseDao<SpcEventEntity> {
	List<Long> getOverlapEvent(Long closureId);
}