package my.edu.um.umpoint.modules.job.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.job.dto.ScheduleJobDTO;
import my.edu.um.umpoint.modules.job.entity.ScheduleJobEntity;

import java.util.Map;

public interface ScheduleJobService extends BaseService<ScheduleJobEntity> {

	PageData<ScheduleJobDTO> page(Map<String, Object> params);

	ScheduleJobDTO get(Long id);

	void save(ScheduleJobDTO dto);

	void update(ScheduleJobDTO dto);

	void deleteBatch(Long[] ids);

	int updateBatch(Long[] ids, int status);

	void run(Long[] ids);

	void pause(Long[] ids);

	void resume(Long[] ids);
}
