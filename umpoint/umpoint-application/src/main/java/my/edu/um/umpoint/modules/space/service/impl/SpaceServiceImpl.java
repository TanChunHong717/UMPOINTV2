package my.edu.um.umpoint.modules.space.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.CrudServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.space.dao.SpaceDao;
import my.edu.um.umpoint.modules.space.dto.SpaceDTO;
import my.edu.um.umpoint.modules.space.entity.SpaceEntity;
import my.edu.um.umpoint.modules.space.service.SpaceService;
import cn.hutool.core.util.StrUtil;
import my.edu.um.umpoint.modules.sys.dto.SysUserDTO;
import my.edu.um.umpoint.modules.sys.entity.SysUserEntity;
import my.edu.um.umpoint.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Service
public class SpaceServiceImpl extends CrudServiceImpl<SpaceDao, SpaceEntity, SpaceDTO> implements SpaceService {

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public QueryWrapper<SpaceEntity> getWrapper(Map<String, Object> params){
        String name = (String)params.get("name");

        QueryWrapper<SpaceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override
    public PageData<SpaceDTO> page(Map<String, Object> params) {
        if (params.get("deptId") != null) {
            params.put("deptIdList", sysDeptService.getSubDeptIdList(Long.getLong((String) params.get("deptId"))));
        }

        paramsToLike(params, "name");

        IPage<SpaceEntity> page = getPage(params, Constant.CREATE_DATE, false);

        List<SpaceEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), currentDtoClass());
    }

    @Override
    public Long saveWithID(SpaceDTO dto) {
        SpaceEntity spaceEntity = ConvertUtils.sourceToTarget(dto, SpaceEntity.class);
        baseDao.insert(spaceEntity);
        return spaceEntity.getId();
    }
}