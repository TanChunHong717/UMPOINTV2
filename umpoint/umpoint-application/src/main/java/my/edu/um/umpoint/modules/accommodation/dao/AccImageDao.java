package my.edu.um.umpoint.modules.accommodation.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.accommodation.entity.AccImageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Accommodation Image
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Mapper
public interface AccImageDao extends BaseDao<AccImageEntity> {
	
}