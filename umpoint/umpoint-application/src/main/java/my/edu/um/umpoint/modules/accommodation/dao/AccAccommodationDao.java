package my.edu.um.umpoint.modules.accommodation.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.accommodation.entity.AccAccommodationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Accommodation
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Mapper
public interface AccAccommodationDao extends BaseDao<AccAccommodationEntity> {
    List<AccAccommodationEntity> getList(Map<String, Object> params);

    @Override
    AccAccommodationEntity selectById(Serializable id);
}