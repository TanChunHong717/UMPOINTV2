package my.edu.um.umpoint.modules.job.init;

import my.edu.um.umpoint.modules.job.dao.ScheduleJobDao;
import my.edu.um.umpoint.modules.job.entity.ScheduleJobEntity;
import my.edu.um.umpoint.modules.job.utils.ScheduleUtils;
import lombok.AllArgsConstructor;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class JobCommandLineRunner implements CommandLineRunner {
    private final Scheduler scheduler;
    private final ScheduleJobDao scheduleJobDao;

    @Override
    public void run(String... args) {
        List<ScheduleJobEntity> scheduleJobList = scheduleJobDao.selectList(null);
        for (ScheduleJobEntity scheduleJob : scheduleJobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getId());
            //create if not exist
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }
}