package my.edu.um.umpoint.modules.job.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.job.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ScheduleJobDao extends BaseDao<ScheduleJobEntity> {

	int updateBatch(Map<String, Object> map);
}
