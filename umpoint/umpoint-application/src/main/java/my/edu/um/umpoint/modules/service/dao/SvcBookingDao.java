package my.edu.um.umpoint.modules.service.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.service.entity.SvcBookingEntity;
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
public interface SvcBookingDao extends BaseDao<SvcBookingEntity> {
    List<SvcBookingEntity> getList(Map<String, Object> params);

    @Override
    SvcBookingEntity selectById(Serializable id);
}