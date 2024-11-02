package my.edu.um.umpoint.modules.space.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.space.entity.SpcClosureEntity;
import my.edu.um.umpoint.modules.space.entity.SpcDeptBookingRuleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Space Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Mapper
public interface SpcClosureDao extends BaseDao<SpcClosureEntity> {
    List<SpcClosureEntity> list(Map<String, Object> params);
}