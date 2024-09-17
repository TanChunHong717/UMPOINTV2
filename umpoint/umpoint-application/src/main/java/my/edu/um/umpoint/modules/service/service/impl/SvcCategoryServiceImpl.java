package my.edu.um.umpoint.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.service.dao.SvcCategoryDao;
import my.edu.um.umpoint.modules.service.dto.SvcCategoryDTO;
import my.edu.um.umpoint.modules.service.entity.SvcCategoryEntity;
import my.edu.um.umpoint.modules.service.service.SvcCategoryService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Service
public class SvcCategoryServiceImpl extends CrudServiceImpl<SvcCategoryDao, SvcCategoryEntity, SvcCategoryDTO> implements SvcCategoryService {

    @Override
    public QueryWrapper<SvcCategoryEntity> getWrapper(Map<String, Object> params){
        paramsToLike(params, "name");
        String name = (String)params.get("name");

        QueryWrapper<SvcCategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    public PageData<SvcCategoryDTO> page(Map<String, Object> params) {
        paramsToLike(params, "name");

        IPage<SvcCategoryEntity> page = getPage(params, "name", true);

        List<SvcCategoryEntity> list = baseDao.listWithCount(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public List<SvcCategoryDTO> list(Map<String, Object> params) {
        if (params.get("filter") != null && (boolean)params.get("filter")) {
            List<SvcCategoryEntity> entityList = baseDao.filterList();
            return ConvertUtils.sourceToTarget(entityList, currentDtoClass());
        }

        return super.list(params);
    }
}