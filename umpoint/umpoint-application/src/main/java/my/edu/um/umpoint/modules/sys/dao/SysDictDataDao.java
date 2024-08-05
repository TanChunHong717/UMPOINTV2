package my.edu.um.umpoint.modules.sys.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.sys.entity.DictData;
import my.edu.um.umpoint.modules.sys.entity.SysDictDataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDictDataDao extends BaseDao<SysDictDataEntity> {

    List<DictData> getDictDataList();
}
