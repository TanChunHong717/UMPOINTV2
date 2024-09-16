package my.edu.um.umpoint.modules.accommodation.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.accommodation.entity.AccClosureEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Accommodation Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Mapper
public interface AccClosureDao extends BaseDao<AccClosureEntity> {
	
}