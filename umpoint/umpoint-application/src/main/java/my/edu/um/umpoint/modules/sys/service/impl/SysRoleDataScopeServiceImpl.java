package my.edu.um.umpoint.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import my.edu.um.umpoint.common.service.impl.BaseServiceImpl;
import my.edu.um.umpoint.modules.sys.dao.SysRoleDataScopeDao;
import my.edu.um.umpoint.modules.sys.entity.SysRoleDataScopeEntity;
import my.edu.um.umpoint.modules.sys.service.SysRoleDataScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleDataScopeServiceImpl extends BaseServiceImpl<SysRoleDataScopeDao, SysRoleDataScopeEntity>
        implements SysRoleDataScopeService {

    @Override
    public List<Long> getDeptIdList(Long roleId) {
        return baseDao.getDeptIdList(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
        deleteByRoleIds(new Long[]{roleId});

        if(CollUtil.isEmpty(deptIdList)){
            return ;
        }

        for(Long deptId : deptIdList){
            SysRoleDataScopeEntity sysRoleDataScopeEntity = new SysRoleDataScopeEntity();
            sysRoleDataScopeEntity.setDeptId(deptId);
            sysRoleDataScopeEntity.setRoleId(roleId);

            insert(sysRoleDataScopeEntity);
        }
    }

    @Override
    public void deleteByRoleIds(Long[] roleIds) {
        baseDao.deleteByRoleIds(roleIds);
    }
}