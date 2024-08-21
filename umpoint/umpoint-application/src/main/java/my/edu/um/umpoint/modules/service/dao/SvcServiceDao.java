package my.edu.um.umpoint.modules.service.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.service.entity.SvcServiceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Mapper
public interface SvcServiceDao extends BaseDao<SvcServiceEntity> {
    List<SvcServiceEntity> getList(Map<String, Object> params);

    @Override
    SvcServiceEntity selectById(Serializable id);
}