package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.space.dao.SpcCategoryDao;
import my.edu.um.umpoint.modules.space.dto.SpcCategoryDTO;
import my.edu.um.umpoint.modules.space.entity.SpcCategoryEntity;
import my.edu.um.umpoint.modules.space.service.SpcCategoryService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Space Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class SpcCategoryServiceImpl extends CrudServiceImpl<SpcCategoryDao, SpcCategoryEntity, SpcCategoryDTO> implements SpcCategoryService {

    @Override
    public QueryWrapper<SpcCategoryEntity> getWrapper(Map<String, Object> params){
        String name = (String)params.get("name");

        QueryWrapper<SpcCategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    public PageData<SpcCategoryDTO> page(Map<String, Object> params) {
        paramsToLike(params, "name");

        IPage<SpcCategoryEntity> page = getPage(params, "name", true);

        List<SpcCategoryEntity> list = baseDao.listWithCount(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public List<SpcCategoryDTO> list(Map<String, Object> params) {
        if (params.get("filter") != null && (boolean)params.get("filter")) {
            List<SpcCategoryEntity> entityList = baseDao.filterList();
            return ConvertUtils.sourceToTarget(entityList, currentDtoClass());
        }

        return super.list(params);
    }
}