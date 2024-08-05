package my.edu.um.umpoint.modules.security.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.security.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserTokenDao extends BaseDao<SysUserTokenEntity> {

    SysUserTokenEntity getByToken(String token);

    SysUserTokenEntity getByUserId(Long userId);

    void updateToken(@Param("userId") Long userId, @Param("token") String token);
}
