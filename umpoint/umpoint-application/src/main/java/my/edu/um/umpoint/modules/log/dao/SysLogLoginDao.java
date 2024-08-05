package my.edu.um.umpoint.modules.log.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.log.entity.SysLogLoginEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysLogLoginDao extends BaseDao<SysLogLoginEntity> {
	
}
