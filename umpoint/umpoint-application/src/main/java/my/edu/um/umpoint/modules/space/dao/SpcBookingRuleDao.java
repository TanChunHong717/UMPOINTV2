package my.edu.um.umpoint.modules.space.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.space.entity.SpcBookingRuleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Space Availability
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Mapper
public interface SpcBookingRuleDao extends BaseDao<SpcBookingRuleEntity> {
	
}