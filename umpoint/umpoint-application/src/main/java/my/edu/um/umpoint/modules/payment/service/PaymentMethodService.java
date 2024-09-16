package my.edu.um.umpoint.modules.payment.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.payment.dto.PaymentMethodDTO;
import my.edu.um.umpoint.modules.payment.entity.PaymentMethodEntity;

/**
 * Payment Method
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-10
 */
public interface PaymentMethodService extends CrudService<PaymentMethodEntity, PaymentMethodDTO> {

}