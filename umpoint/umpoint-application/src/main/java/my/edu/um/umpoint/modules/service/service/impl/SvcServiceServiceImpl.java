package my.edu.um.umpoint.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.annotation.DataFilter;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.service.dao.SvcServiceDao;
import my.edu.um.umpoint.modules.service.dto.SvcServiceDTO;
import my.edu.um.umpoint.modules.service.entity.SvcImageEntity;
import my.edu.um.umpoint.modules.service.entity.SvcServiceEntity;
import my.edu.um.umpoint.modules.service.entity.SvcServiceTagEntity;
import my.edu.um.umpoint.modules.service.service.SvcImageService;
import my.edu.um.umpoint.modules.service.service.SvcServiceService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.service.service.SvcServiceTagService;
import my.edu.um.umpoint.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Service
public class SvcServiceServiceImpl extends CrudServiceImpl<SvcServiceDao, SvcServiceEntity, SvcServiceDTO> implements SvcServiceService {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SvcServiceTagService svcServiceTagService;

    @Autowired
    private SvcImageService svcImageService;

    @Override
    public QueryWrapper<SvcServiceEntity> getWrapper(Map<String, Object> params){
        String name = (String)params.get("name");

        QueryWrapper<SvcServiceEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    @DataFilter(tableAlias = "s")
    public PageData<SvcServiceDTO> page(Map<String, Object> params) {
        if (params.get("deptId") != null) {
            params.put("deptIdList", sysDeptService.getSubDeptIdList(Long.getLong((String) params.get("deptId"))));
        }

        paramsToLike(params, "name");

        IPage<SvcServiceEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<SvcServiceEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SvcServiceDTO dto) {
        super.save(dto);
        updateServiceTag(dto);
        updateServiceImage(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SvcServiceDTO dto) {
        super.update(dto);
        updateServiceTag(dto);
        updateServiceImage(dto);
    }

    private void updateServiceImage(SvcServiceDTO dto) {
        svcImageService.deleteByServiceId(dto.getId());
        svcImageService.insertBatch(ConvertUtils.sourceToTarget(dto.getSvcImageDTOList(), SvcImageEntity.class));
    }

    private void updateServiceTag(SvcServiceDTO dto) {
        svcServiceTagService.deleteByServiceId(dto.getId());
        svcServiceTagService.insertBatch(ConvertUtils.sourceToTarget(dto.getSvcTagDTOList(), SvcServiceTagEntity.class));
    }
}