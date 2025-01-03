package my.edu.um.umpoint.modules.service.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.service.entity.SvcBookingRuleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Service Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Mapper
public interface SvcBookingRuleDao extends BaseDao<SvcBookingRuleEntity> {
	
}