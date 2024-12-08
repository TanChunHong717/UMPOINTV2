package my.edu.um.umpoint.modules.payment.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.payment.dto.SpcPaymentDTO;
import my.edu.um.umpoint.modules.payment.entity.SpcPaymentEntity;

/**
 * Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
public interface SpcPaymentService extends CrudService<SpcPaymentEntity, SpcPaymentDTO> {
    SpcPaymentEntity getLatestPayment(Long bookingId);
    void pay(Long id);
    void refund(Long id);
}