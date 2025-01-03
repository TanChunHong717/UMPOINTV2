package my.edu.um.umpoint.modules.accommodation.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.accommodation.entity.AccBookingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Mapper
public interface AccBookingDao extends BaseDao<AccBookingEntity> {
    List<AccBookingEntity> getList(Map<String, Object> params);

    @Override
    AccBookingEntity selectById(Serializable id);
}