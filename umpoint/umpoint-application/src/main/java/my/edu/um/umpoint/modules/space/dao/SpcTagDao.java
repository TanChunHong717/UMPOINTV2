package my.edu.um.umpoint.modules.space.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.space.entity.SpcTagEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Space Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Mapper
public interface SpcTagDao extends BaseDao<SpcTagEntity> {
    List<SpcTagEntity> listWithCount(Map<String, Object> params);
    List<SpcTagEntity> filterList();
}