package my.edu.um.umpoint.modules.job.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.job.dto.ScheduleJobLogDTO;
import my.edu.um.umpoint.modules.job.entity.ScheduleJobLogEntity;

import java.util.Map;

public interface ScheduleJobLogService extends BaseService<ScheduleJobLogEntity> {

	PageData<ScheduleJobLogDTO> page(Map<String, Object> params);

	ScheduleJobLogDTO get(Long id);
}
