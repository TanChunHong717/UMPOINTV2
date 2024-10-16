package my.edu.um.umpoint.modules.payment.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.payment.entity.AccPaymentItemEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Accommodation Payment Itemized
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-16
 */
@Mapper
public interface AccPaymentItemDao extends BaseDao<AccPaymentItemEntity> {
	
}