package my.edu.um.umpoint.modules.sys.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserDao extends BaseDao<SysUserEntity> {

	List<SysUserEntity> getList(Map<String, Object> params);

	SysUserEntity getById(Long id);

	SysUserEntity getByUsername(String username);

	int updatePassword(@Param("id") Long id, @Param("newPassword") String newPassword);

	int getCountByDeptId(Long deptId);

	List<Long> getUserIdListByDeptId(List<Long> deptIdList);
}