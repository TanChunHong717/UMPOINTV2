package my.edu.um.umpoint.modules.space.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.space.entity.SpcDeptBookingRuleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Space Department Level Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-11-02
 */
@Mapper
public interface SpcDeptBookingRuleDao extends BaseDao<SpcDeptBookingRuleEntity> {
    List<SpcDeptBookingRuleEntity> list(Map<String, Object> params);
}