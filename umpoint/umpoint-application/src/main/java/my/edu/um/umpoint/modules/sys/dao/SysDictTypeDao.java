package my.edu.um.umpoint.modules.sys.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.sys.entity.DictType;
import my.edu.um.umpoint.modules.sys.entity.SysDictTypeEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDictTypeDao extends BaseDao<SysDictTypeEntity> {

    List<DictType> getDictTypeList();

}
