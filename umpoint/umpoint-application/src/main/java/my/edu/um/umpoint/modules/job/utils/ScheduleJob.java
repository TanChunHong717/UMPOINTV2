package my.edu.um.umpoint.modules.job.utils;

import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.exception.ExceptionUtils;
import my.edu.um.umpoint.common.utils.SpringContextUtils;
import my.edu.um.umpoint.modules.job.entity.ScheduleJobEntity;
import my.edu.um.umpoint.modules.job.entity.ScheduleJobLogEntity;
import my.edu.um.umpoint.modules.job.service.ScheduleJobLogService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Date;

public class ScheduleJob extends QuartzJobBean {
	private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) {
        ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap().
				get(ScheduleUtils.JOB_PARAM_KEY);

        ScheduleJobLogEntity log = new ScheduleJobLogEntity();
        log.setJobId(scheduleJob.getId());
        log.setBeanName(scheduleJob.getBeanName());
        log.setParams(scheduleJob.getParams());
		log.setCreateDate(new Date());

        long startTime = System.currentTimeMillis();
        
        try {
			logger.info("Job run，Job ID：{}", scheduleJob.getId());
			Object target = SpringContextUtils.getBean(scheduleJob.getBeanName());
			Method method = target.getClass().getDeclaredMethod("run", String.class);
			method.invoke(target, scheduleJob.getParams());

			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int)times);
			log.setStatus(Constant.SUCCESS);
			
			logger.info("Job run completed，Job ID：{}  Total spend：{}ms", scheduleJob.getId(), times);
		} catch (Exception e) {
			logger.error("Job run failed，Job ID：{}", scheduleJob.getId(), e);

			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int)times);

			log.setStatus(Constant.FAIL);
			log.setError(ExceptionUtils.getErrorStackTrace(e));
		}finally {
			ScheduleJobLogService scheduleJobLogService = SpringContextUtils.getBean(ScheduleJobLogService.class);
			scheduleJobLogService.insert(log);
		}
    }
}