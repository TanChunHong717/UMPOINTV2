package my.edu.um.umpoint.modules.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.service.dao.SvcTagDao;
import my.edu.um.umpoint.modules.service.dto.SvcTagDTO;
import my.edu.um.umpoint.modules.service.entity.SvcTagEntity;
import my.edu.um.umpoint.modules.service.service.SvcTagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Service
public class SvcTagServiceImpl extends CrudServiceImpl<SvcTagDao, SvcTagEntity, SvcTagDTO> implements SvcTagService {

    @Override
    public QueryWrapper<SvcTagEntity> getWrapper(Map<String, Object> params){
        String name = (String)params.get("name");

        QueryWrapper<SvcTagEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    public PageData<SvcTagDTO> page(Map<String, Object> params) {
        paramsToLike(params, "tagName");

        IPage<SvcTagEntity> page = getPage(params, "tag_name", true);

        List<SvcTagEntity> list = baseDao.listWithCount(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public List<SvcTagDTO> list(Map<String, Object> params) {
        if (params.get("filter") != null && (boolean)params.get("filter")) {
            List<SvcTagEntity> entityList = baseDao.filterList();
            return ConvertUtils.sourceToTarget(entityList, currentDtoClass());
        }

        return super.list(params);
    }

}