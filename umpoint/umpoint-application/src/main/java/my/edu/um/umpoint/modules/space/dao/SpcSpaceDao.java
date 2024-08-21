package my.edu.um.umpoint.modules.space.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.space.entity.SpcSpaceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Mapper
public interface SpcSpaceDao extends BaseDao<SpcSpaceEntity> {
    List<SpcSpaceEntity> getList(Map<String, Object> params);

    @Override
    SpcSpaceEntity selectById(Serializable id);
}