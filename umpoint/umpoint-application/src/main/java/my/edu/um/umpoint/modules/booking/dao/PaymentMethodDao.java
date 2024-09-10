package my.edu.um.umpoint.modules.booking.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.booking.entity.PaymentMethodEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Payment Method
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-10
 */
@Mapper
public interface PaymentMethodDao extends BaseDao<PaymentMethodEntity> {
	
}