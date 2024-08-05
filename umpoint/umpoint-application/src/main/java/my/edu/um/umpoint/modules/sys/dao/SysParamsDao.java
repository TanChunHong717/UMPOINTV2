package my.edu.um.umpoint.modules.sys.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.sys.entity.SysParamsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysParamsDao extends BaseDao<SysParamsEntity> {

    String getValueByCode(String paramCode);

    List<String> getParamCodeList(Long[] ids);

    int updateValueByCode(@Param("paramCode") String paramCode, @Param("paramValue") String paramValue);
}
