package my.edu.um.umpoint.modules.service.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.service.entity.SvcTagEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Service Tag
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Mapper
public interface SvcTagDao extends BaseDao<SvcTagEntity> {
    List<SvcTagEntity> listWithCount(Map<String, Object> params);
    List<SvcTagEntity> filterList();
}