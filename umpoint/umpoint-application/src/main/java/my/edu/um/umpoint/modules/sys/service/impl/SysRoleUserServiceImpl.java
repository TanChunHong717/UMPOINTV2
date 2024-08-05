package my.edu.um.umpoint.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import my.edu.um.umpoint.common.service.impl.BaseServiceImpl;
import my.edu.um.umpoint.modules.sys.dao.SysRoleUserDao;
import my.edu.um.umpoint.modules.sys.entity.SysRoleUserEntity;
import my.edu.um.umpoint.modules.sys.service.SysRoleUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleUserServiceImpl extends BaseServiceImpl<SysRoleUserDao, SysRoleUserEntity> implements SysRoleUserService {

    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        deleteByUserIds(new Long[]{userId});

        if(CollUtil.isEmpty(roleIdList)){
            return ;
        }

        for(Long roleId : roleIdList){
            SysRoleUserEntity sysRoleUserEntity = new SysRoleUserEntity();
            sysRoleUserEntity.setUserId(userId);
            sysRoleUserEntity.setRoleId(roleId);

            insert(sysRoleUserEntity);
        }
    }

    @Override
    public void deleteByRoleIds(Long[] roleIds) {
        baseDao.deleteByRoleIds(roleIds);
    }

    @Override
    public void deleteByUserIds(Long[] userIds) {
        baseDao.deleteByUserIds(userIds);
    }

    @Override
    public List<Long> getRoleIdList(Long userId) {

        return baseDao.getRoleIdList(userId);
    }
}