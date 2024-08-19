package my.edu.um.umpoint.modules.space.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceTagEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Space tag relationship
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Mapper
public interface SpcSpaceTagDao extends BaseDao<SpcSpaceTagEntity> {
	
}