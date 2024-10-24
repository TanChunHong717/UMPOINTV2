package my.edu.um.umpoint.modules.security.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import my.edu.um.umpoint.modules.client.dao.CliTokenDao;
import my.edu.um.umpoint.modules.client.dao.CliUserDao;
import my.edu.um.umpoint.modules.client.entity.CliTokenEntity;
import my.edu.um.umpoint.modules.client.entity.CliUserEntity;
import my.edu.um.umpoint.modules.security.config.CliPermissionConfig;
import my.edu.um.umpoint.modules.security.dao.SysUserTokenDao;
import my.edu.um.umpoint.modules.security.entity.SysUserTokenEntity;
import my.edu.um.umpoint.modules.security.service.ShiroService;
import my.edu.um.umpoint.modules.security.user.UserDetail;
import my.edu.um.umpoint.modules.sys.dao.SysMenuDao;
import my.edu.um.umpoint.modules.sys.dao.SysRoleDataScopeDao;
import my.edu.um.umpoint.modules.sys.dao.SysUserDao;
import my.edu.um.umpoint.modules.sys.entity.SysUserEntity;
import my.edu.um.umpoint.modules.sys.enums.SuperAdminEnum;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ShiroServiceImpl implements ShiroService {
    private final SysMenuDao sysMenuDao;
    private final SysUserDao sysUserDao;
    private final SysUserTokenDao sysUserTokenDao;
    private final SysRoleDataScopeDao sysRoleDataScopeDao;
    private final CliUserDao cliUserDao;
    private final CliTokenDao cliTokenDao;
    private final CliPermissionConfig cliPermissionConfig;

    @Override
    public Set<String> getUserPermissions(UserDetail user) {
        List<String> permissionsList;
        if (user.getSuperAdmin() != null) {
            if (user.getSuperAdmin() == SuperAdminEnum.YES.value())
                permissionsList = sysMenuDao.getPermissionsList();
            else
                permissionsList = sysMenuDao.getUserPermissionsList(user.getId());
        } else {
            permissionsList = new ArrayList<>(cliPermissionConfig.getCommon());
            if (user.getSpacePermission() == 1)
                permissionsList.addAll(cliPermissionConfig.getSpace());
            if (user.getServicePermission() == 1)
                permissionsList.addAll(cliPermissionConfig.getService());
            if (user.getAccommodationPermission() == 1)
                permissionsList.addAll(cliPermissionConfig.getAccommodation());
        }

        Set<String> permsSet = new HashSet<>();
        for (String permissions : permissionsList) {
            if (StrUtil.isBlank(permissions)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(permissions.trim().split(",")));
        }

        return permsSet;
    }

    @Override
    public SysUserTokenEntity getByToken(String token) {
        return sysUserTokenDao.getByToken(token);
    }

    @Override
    public SysUserEntity getUser(Long userId) {
        return sysUserDao.selectById(userId);
    }

    @Override
    public List<Long> getDataScopeList(Long userId) {
        return sysRoleDataScopeDao.getDataScopeList(userId);
    }

    @Override
    public CliTokenEntity getCliByToken(String accessToken) {
        QueryWrapper<CliTokenEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("token", accessToken);
        return cliTokenDao.selectOne(queryWrapper);
    }

    @Override
    public CliUserEntity getCliUser(Long userId) {
        QueryWrapper<CliUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return cliUserDao.selectOne(queryWrapper);
    }
}