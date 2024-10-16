package my.edu.um.umpoint.modules.payment.dao;

import my.edu.um.umpoint.common.dao.BaseDao;
import my.edu.um.umpoint.modules.payment.entity.SvcPaymentItemEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * Service Payment Itemized
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-16
 */
@Mapper
public interface SvcPaymentItemDao extends BaseDao<SvcPaymentItemEntity> {
	
}