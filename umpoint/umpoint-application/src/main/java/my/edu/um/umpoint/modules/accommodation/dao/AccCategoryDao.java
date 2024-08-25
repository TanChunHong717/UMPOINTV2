package my.edu.um.umpoint.modules.accommodation.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.accommodation.entity.AccCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Accommodation Category
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Mapper
public interface AccCategoryDao extends BaseDao<AccCategoryEntity> {
    List<AccCategoryEntity> listWithCount(Map<String, Object> params);
    List<AccCategoryEntity> filterList();
}