package my.edu.um.umpoint.modules.payment.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.payment.entity.AccPaymentEntity;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Mapper
public interface AccPaymentDao extends BaseDao<AccPaymentEntity> {
    List<AccPaymentEntity> getList(Map<String, Object> params);

    @Override
    AccPaymentEntity selectById(Serializable id);
}