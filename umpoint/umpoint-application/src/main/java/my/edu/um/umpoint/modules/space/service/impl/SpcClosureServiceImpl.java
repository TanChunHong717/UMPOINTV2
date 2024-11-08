package my.edu.um.umpoint.modules.space.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.modules.space.dao.SpcClosureDao;
import my.edu.um.umpoint.modules.space.dao.SpcEventDao;
import my.edu.um.umpoint.modules.space.dto.SpcClosureDTO;
import my.edu.um.umpoint.modules.space.entity.SpcClosureEntity;
import my.edu.um.umpoint.modules.space.service.SpcBookingService;
import my.edu.um.umpoint.modules.space.service.SpcClosureService;
import my.edu.um.umpoint.modules.space.service.SpcEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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
    public QueryWrapper<SpcClosureEntity> getWrapper(Map<String, Object> params) {
        String id = (String)params.get("id");

        QueryWrapper<SpcClosureEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public PageData<SpcClosureDTO> page(Map<String, Object> params) {
        paramsToLike(params, "deptName");

        IPage<SpcClosureEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<SpcClosureEntity> list = baseDao.list(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        spcEventService.deleteByClosureId(Arrays.asList(ids));
        super.delete(ids);
    }

    private void rejectOverlapBooking(Long closureId) {
        List<Long> overlapEventList = spcEventDao.getOverlapEvent(closureId);
        if (!overlapEventList.isEmpty())
            spcBookingService.reject(overlapEventList);
    }
}