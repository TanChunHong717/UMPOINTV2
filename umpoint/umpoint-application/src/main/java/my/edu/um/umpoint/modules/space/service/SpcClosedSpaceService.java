package my.edu.um.umpoint.modules.space.service;

import java.util.List;

public interface SpcClosedSpaceService {
    void addSpace(Long startTime, Long endTime, Long spaceId);
    void removeSpace(Long startTime, Long endTime, Long spaceId);
    List<Long> getClosedSpace(Long startTime, Long endTime);
    void clean();
}
