package my.edu.um.umpoint.modules.payment.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.payment.entity.SvcPaymentEntity;
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
public interface SvcPaymentDao extends BaseDao<SvcPaymentEntity> {
    List<SvcPaymentEntity> getList(Map<String, Object> params);

    @Override
    SvcPaymentEntity selectById(Serializable id);
}