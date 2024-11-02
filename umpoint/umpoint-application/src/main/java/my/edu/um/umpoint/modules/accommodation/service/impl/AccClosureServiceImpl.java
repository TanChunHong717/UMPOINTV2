package my.edu.um.umpoint.modules.accommodation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.accommodation.dao.AccClosureDao;
import my.edu.um.umpoint.modules.accommodation.dao.AccEventDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccClosureDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccClosureEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccBookingService;
import my.edu.um.umpoint.modules.accommodation.service.AccClosureService;
import my.edu.um.umpoint.modules.accommodation.service.AccEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public QueryWrapper<AccClosureEntity> getWrapper(Map<String, Object> params) {
        String id = (String)params.get("id");

        QueryWrapper<AccClosureEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<AccClosureDTO> page(Map<String, Object> params) {
        paramsToLike(params, "deptName");

        IPage<AccClosureEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<AccClosureEntity> list = baseDao.list(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        accEventService.deleteByClosureId(ids);
        super.delete(ids);
    }

    private void rejectOverlapBooking(Long closureId) {
        accEventDao.getOverlapEvent(closureId).forEach(accBookingService::reject);
    }
}