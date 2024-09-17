package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.space.dao.SpcTagDao;
import my.edu.um.umpoint.modules.space.dto.SpcTagDTO;
import my.edu.um.umpoint.modules.space.entity.SpcTagEntity;
import my.edu.um.umpoint.modules.space.service.SpcTagService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Space Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class SpcTagServiceImpl extends CrudServiceImpl<SpcTagDao, SpcTagEntity, SpcTagDTO> implements SpcTagService {

    @Override
    public QueryWrapper<SpcTagEntity> getWrapper(Map<String, Object> params){
        paramsToLike(params, "name");
        String name = (String)params.get("name");

        QueryWrapper<SpcTagEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    public PageData<SpcTagDTO> page(Map<String, Object> params) {
        paramsToLike(params, "tagName");

        IPage<SpcTagEntity> page = getPage(params, "tag_name", true);

        List<SpcTagEntity> list = baseDao.listWithCount(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public List<SpcTagDTO> list(Map<String, Object> params) {
        if (params.get("filter") != null && (boolean)params.get("filter")) {
            List<SpcTagEntity> entityList = baseDao.filterList();
            return ConvertUtils.sourceToTarget(entityList, currentDtoClass());
        }

        return super.list(params);
    }
}