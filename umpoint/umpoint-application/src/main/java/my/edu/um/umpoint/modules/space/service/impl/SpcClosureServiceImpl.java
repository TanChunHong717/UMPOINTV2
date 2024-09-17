package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.SpcClosureDao;
import my.edu.um.umpoint.modules.space.dao.SpcEventDao;
import my.edu.um.umpoint.modules.space.dto.SpcClosureDTO;
import my.edu.um.umpoint.modules.space.entity.SpcClosureEntity;
import my.edu.um.umpoint.modules.space.service.SpcBookingService;
import my.edu.um.umpoint.modules.space.service.SpcClosureService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.space.service.SpcEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Space Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Service
public class SpcClosureServiceImpl extends CrudServiceImpl<SpcClosureDao, SpcClosureEntity, SpcClosureDTO> implements SpcClosureService {

    @Autowired
    private SpcEventService spcEventService;

    @Autowired
    private SpcBookingService spcBookingService;

    @Autowired
    private SpcEventDao spcEventDao;

    @Override
    public QueryWrapper<SpcClosureEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpcClosureEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SpcClosureDTO dto) {
        super.save(dto);

        spcEventService.addEvent(dto);

        rejectOverlapBooking(dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SpcClosureDTO dto) {
        super.update(dto);

        spcEventService.deleteByClosureId(dto.getId());
        spcEventService.addEvent(dto);

        rejectOverlapBooking(dto.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        spcEventService.deleteByClosureId(id);

        Long[] ids = {id};
        super.delete(ids);
    }

    private void rejectOverlapBooking(Long closureId) {
        spcEventDao.getOverlapEvent(closureId).forEach(spcBookingService::reject);
    }
}