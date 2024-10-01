package my.edu.um.umpoint.modules.client.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.client.entity.CliTokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserToken
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@Mapper
public interface CliTokenDao extends BaseDao<CliTokenEntity> {
	
}