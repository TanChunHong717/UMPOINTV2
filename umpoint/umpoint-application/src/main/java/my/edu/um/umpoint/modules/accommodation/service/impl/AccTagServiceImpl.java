package my.edu.um.umpoint.modules.accommodation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.accommodation.dao.AccTagDao;
import my.edu.um.umpoint.modules.accommodation.dto.AccTagDTO;
import my.edu.um.umpoint.modules.accommodation.entity.AccTagEntity;
import my.edu.um.umpoint.modules.accommodation.service.AccTagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Accommodation Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Service
public class AccTagServiceImpl extends CrudServiceImpl<AccTagDao, AccTagEntity, AccTagDTO> implements AccTagService {

    @Override
    public QueryWrapper<AccTagEntity> getWrapper(Map<String, Object> params){
        paramsToLike(params, "name");
        String name = (String)params.get("name");

        QueryWrapper<AccTagEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    public PageData<AccTagDTO> page(Map<String, Object> params) {
        paramsToLike(params, "tagName");

        IPage<AccTagEntity> page = getPage(params, "tag_name", true);

        List<AccTagEntity> list = baseDao.listWithCount(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public List<AccTagDTO> list(Map<String, Object> params) {
        if (params.get("filter") != null && (boolean)params.get("filter")) {
            List<AccTagEntity> entityList = baseDao.filterList();
            return ConvertUtils.sourceToTarget(entityList, currentDtoClass());
        }

        return super.list(params);
    }
}