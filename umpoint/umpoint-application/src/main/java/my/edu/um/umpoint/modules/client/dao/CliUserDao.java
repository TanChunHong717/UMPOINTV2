package my.edu.um.umpoint.modules.client.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.client.entity.CliUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * User
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-10-01
 */
@Mapper
public interface CliUserDao extends BaseDao<CliUserEntity> {
	
}