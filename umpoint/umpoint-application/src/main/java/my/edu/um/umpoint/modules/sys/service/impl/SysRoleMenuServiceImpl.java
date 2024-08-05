package my.edu.um.umpoint.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import my.edu.um.umpoint.common.service.impl.BaseServiceImpl;
import my.edu.um.umpoint.modules.sys.dao.SysRoleMenuDao;
import my.edu.um.umpoint.modules.sys.entity.SysRoleMenuEntity;
import my.edu.um.umpoint.modules.sys.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
		deleteByRoleIds(new Long[]{roleId});
		if(CollUtil.isEmpty(menuIdList)){
			return ;
		}

		for(Long menuId : menuIdList){
			SysRoleMenuEntity sysRoleMenuEntity = new SysRoleMenuEntity();
			sysRoleMenuEntity.setMenuId(menuId);
			sysRoleMenuEntity.setRoleId(roleId);

			insert(sysRoleMenuEntity);
		}
	}

	@Override
	public List<Long> getMenuIdList(Long roleId){
		return baseDao.getMenuIdList(roleId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByRoleIds(Long[] roleIds) {
		baseDao.deleteByRoleIds(roleIds);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByMenuId(Long menuId) {
		baseDao.deleteByMenuId(menuId);
	}

}