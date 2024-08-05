package my.edu.um.umpoint.modules.sys.service;

import my.edu.um.umpoint.common.page.PageData;
import my.edu.um.umpoint.common.service.BaseService;
import my.edu.um.umpoint.modules.sys.dto.SysUserDTO;
import my.edu.um.umpoint.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

public interface SysUserService extends BaseService<SysUserEntity> {

	PageData<SysUserDTO> page(Map<String, Object> params);

	List<SysUserDTO> list(Map<String, Object> params);

	SysUserDTO get(Long id);

	SysUserDTO getByUsername(String username);

	void save(SysUserDTO dto);

	void update(SysUserDTO dto);

	void delete(Long[] ids);

	void updatePassword(Long id, String newPassword);

	int getCountByDeptId(Long deptId);

	List<Long> getUserIdListByDeptId(List<Long> deptIdList);

}
