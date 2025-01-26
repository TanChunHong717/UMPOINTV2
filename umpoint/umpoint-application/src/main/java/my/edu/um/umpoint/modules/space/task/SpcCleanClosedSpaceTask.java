package my.edu.um.umpoint.modules.space.task;

import my.edu.um.umpoint.modules.space.service.SpcClosedSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SpcCleanClosedSpaceTask {
    @Autowired
    private SpcClosedSpaceService spcClosedSpaceService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearRedis() {
        spcClosedSpaceService.clean();
    }
}
