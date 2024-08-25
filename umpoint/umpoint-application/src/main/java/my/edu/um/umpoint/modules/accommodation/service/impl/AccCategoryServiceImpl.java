package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.accommodation.dao.AccCategoryDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccCategoryDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccCategoryEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccCategoryService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.space.dto.SpcCategoryDTO;
import my.edu.um.umpoint.modules.space.entity.SpcCategoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Accommodation Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccCategoryServiceImpl extends CrudServiceImpl<AccCategoryDao, AccCategoryEntity, AccCategoryDTO> implements AccCategoryService {

    @Override
    public QueryWrapper<AccCategoryEntity> getWrapper(Map<String, Object> params){
        String name = (String)params.get("name");

        QueryWrapper<AccCategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    public PageData<AccCategoryDTO> page(Map<String, Object> params) {
        paramsToLike(params, "name");

        IPage<AccCategoryEntity> page = getPage(params, "name", true);

        List<AccCategoryEntity> list = baseDao.listWithCount(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public List<AccCategoryDTO> list(Map<String, Object> params) {
        if (params.get("filter") != null && (boolean)params.get("filter")) {
            List<AccCategoryEntity> entityList = baseDao.filterList();
            return ConvertUtils.sourceToTarget(entityList, currentDtoClass());
        }

        return super.list(params);
    }
}