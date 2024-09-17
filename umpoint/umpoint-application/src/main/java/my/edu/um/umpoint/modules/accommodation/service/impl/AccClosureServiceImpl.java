package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccClosureDao;
import my.edu.um.umpoint.modules.accommodation.dao.AccEventDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccClosureDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccClosureEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingService;
import my.edu.um.umpoint.modules.accommodation.service.AccClosureService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.accommodation.service.AccEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Accommodation Closure Period
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-16
 */
@Service
public class AccClosureServiceImpl extends CrudServiceImpl<AccClosureDao, AccClosureEntity, AccClosureDTO> implements AccClosureService {

    @Autowired
    private AccEventService accEventService;

    @Autowired
    private AccBookingService accBookingService;

    @Autowired
    private AccEventDao accEventDao;

    @Override
    public QueryWrapper<AccClosureEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<AccClosureEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AccClosureDTO dto) {
        super.save(dto);

        accEventService.addEvent(dto);

        rejectOverlapBooking(dto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AccClosureDTO dto) {
        super.update(dto);

        accEventService.deleteByClosureId(dto.getId());
        accEventService.addEvent(dto);

        rejectOverlapBooking(dto.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Long[] ids = {id};
        super.delete(ids);

        accEventService.deleteByClosureId(id);
    }

    private void rejectOverlapBooking(Long closureId) {
        accEventDao.getOverlapEvent(closureId).forEach(accBookingService::reject);
    }
}