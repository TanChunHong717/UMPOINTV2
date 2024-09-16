package my.edu.um.umpoint.modules.space.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.space.entity.SpcBookingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Mapper
public interface SpcBookingDao extends BaseDao<SpcBookingEntity> {
    List<SpcBookingEntity> getList(Map<String, Object> params);

    @Override
    SpcBookingEntity selectById(Serializable id);
}