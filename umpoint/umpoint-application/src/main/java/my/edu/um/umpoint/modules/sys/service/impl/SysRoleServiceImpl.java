package my.edu.um.umpoint.modules.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.BaseServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.dao.SysRoleDao;
import my.edu.um.umpoint.modules.sys.dto.SysRoleDTO;
import my.edu.um.umpoint.modules.sys.entity.SysRoleEntity;
import my.edu.um.umpoint.modules.sys.enums.SuperAdminEnum;
import my.edu.um.umpoint.modules.sys.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
    private final SysRoleMenuService sysRoleMenuService;
    private final SysRoleDataScopeService sysRoleDataScopeService;
    private final SysRoleUserService sysRoleUserService;
    private final SysDeptService sysDeptService;

    @Override
    public PageData<SysRoleDTO> page(Map<String, Object> params) {
        IPage<SysRoleEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SysRoleDTO.class);
    }

    @Override
    public List<SysRoleDTO> list(Map<String, Object> params) {
        List<SysRoleEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysRoleDTO.class);
    }

    private QueryWrapper<SysRoleEntity> getWrapper(Map<String, Object> params) {
        String name = (String) params.get("name");

        QueryWrapper<SysRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(name), "name", name);

        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            List<Long> deptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            wrapper.in(deptIdList != null, "dept_id", deptIdList);
        }

        return wrapper;
    }

    @Override
    public SysRoleDTO get(Long id) {
        SysRoleEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysRoleDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysRoleDTO dto) {
        SysRoleEntity entity = ConvertUtils.sourceToTarget(dto, SysRoleEntity.class);

        insert(entity);

        sysRoleMenuService.saveOrUpdate(entity.getId(), dto.getMenuIdList());

        sysRoleDataScopeService.saveOrUpdate(entity.getId(), dto.getDeptIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleDTO dto) {
        SysRoleEntity entity = ConvertUtils.sourceToTarget(dto, SysRoleEntity.class);

        updateById(entity);

        sysRoleMenuService.saveOrUpdate(entity.getId(), dto.getMenuIdList());

        sysRoleDataScopeService.saveOrUpdate(entity.getId(), dto.getDeptIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        baseDao.deleteBatchIds(Arrays.asList(ids));

        sysRoleUserService.deleteByRoleIds(ids);

        sysRoleMenuService.deleteByRoleIds(ids);

        sysRoleDataScopeService.deleteByRoleIds(ids);
    }

}