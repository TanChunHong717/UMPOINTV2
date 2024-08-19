package my.edu.um.umpoint.modules.space.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.space.entity.SpcCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Space Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Mapper
public interface SpcCategoryDao extends BaseDao<SpcCategoryEntity> {
    List<SpcCategoryEntity> listWithCount(Map<String, Object> params);
    List<SpcCategoryEntity> filterList();
}