package my.edu.um.umpoint.modules.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import my.edu.um.umpoint.common.constant.Constant;
import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.impl.BaseServiceImpl;
import my.edu.um.umpoint.common.utils.ConvertUtils;
import my.edu.um.umpoint.modules.security.password.PasswordUtils;
import my.edu.um.umpoint.modules.security.user.SecurityUser;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.dao.SysUserDao;
import my.edu.um.umpoint.modules.sys.dto.SysUserDTO;
import my.edu.um.umpoint.modules.sys.entity.SysUserEntity;
import my.edu.um.umpoint.modules.sys.enums.SuperAdminEnum;
import my.edu.um.umpoint.modules.sys.service.SysDeptService;
import my.edu.um.umpoint.modules.sys.service.SysRoleUserService;
import my.edu.um.umpoint.modules.sys.service.SysUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    private final SysRoleUserService sysRoleUserService;
    private final SysDeptService sysDeptService;

    @Override
    public PageData<SysUserDTO> page(Map<String, Object> params) {
        paramsToLike(params, "username");

        IPage<SysUserEntity> page = getPage(params, Constant.CREATE_DATE, false);

        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            params.put("deptIdList", sysDeptService.getSubDeptIdList(user.getDeptId()));
        }

        List<SysUserEntity> list = baseDao.getList(params);

        return getPageData(list, page.getTotal(), SysUserDTO.class);
    }

    @Override
    public List<SysUserDTO> list(Map<String, Object> params) {
        UserDetail user = SecurityUser.getUser();
        if (user.getSuperAdmin() == SuperAdminEnum.NO.value()) {
            params.put("deptIdList", sysDeptService.getSubDeptIdList(user.getDeptId()));
        }

        List<SysUserEntity> entityList = baseDao.getList(params);

        return ConvertUtils.sourceToTarget(entityList, SysUserDTO.class);
    }

    @Override
    public SysUserDTO get(Long id) {
        SysUserEntity entity = baseDao.getById(id);

        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    @Override
    public SysUserDTO getByUsername(String username) {
        SysUserEntity entity = baseDao.getByUsername(username);
        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysUserDTO dto) {
        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);

        String password = PasswordUtils.encode(entity.getPassword());
        entity.setPassword(password);

        entity.setSuperAdmin(SuperAdminEnum.NO.value());
        insert(entity);

        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserDTO dto) {
        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);

        if (StrUtil.isBlank(dto.getPassword())) {
            entity.setPassword(null);
        } else {
            String password = PasswordUtils.encode(entity.getPassword());
            entity.setPassword(password);
        }

        updateById(entity);

        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
    }

    @Override
    public void delete(Long[] ids) {
        baseDao.deleteBatchIds(Arrays.asList(ids));

        sysRoleUserService.deleteByUserIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long id, String newPassword) {
        newPassword = PasswordUtils.encode(newPassword);

        baseDao.updatePassword(id, newPassword);
    }

    @Override
    public int getCountByDeptId(Long deptId) {
        return baseDao.getCountByDeptId(deptId);
    }

    @Override
    public List<Long> getUserIdListByDeptId(List<Long> deptIdList) {
        return baseDao.getUserIdListByDeptId(deptIdList);
    }

}
