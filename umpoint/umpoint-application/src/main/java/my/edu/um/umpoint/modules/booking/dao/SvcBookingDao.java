package my.edu.um.umpoint.modules.booking.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.booking.entity.SvcBookingEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Mapper
public interface SvcBookingDao extends BaseDao<SvcBookingEntity> {
	
}