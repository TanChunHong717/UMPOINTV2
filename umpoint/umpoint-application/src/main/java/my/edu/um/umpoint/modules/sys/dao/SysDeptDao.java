package my.edu.um.umpoint.modules.sys.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.sys.entity.SysDeptEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysDeptDao extends BaseDao<SysDeptEntity> {

    List<SysDeptEntity> getList(Map<String, Object> params);

    SysDeptEntity getById(Long id);

    List<SysDeptEntity> getIdAndPidList();

    List<Long> getSubDeptIdList(String id);

}