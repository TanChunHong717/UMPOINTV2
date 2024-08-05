package my.edu.um.umpoint.modules.job.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.job.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduleJobLogDao extends BaseDao<ScheduleJobLogEntity> {
	
}
