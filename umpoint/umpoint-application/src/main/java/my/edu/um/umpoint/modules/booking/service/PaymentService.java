package my.edu.um.umpoint.modules.booking.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.booking.dto.PaymentDTO;
import my.edu.um.umpoint.modules.booking.entity.PaymentEntity;

/**
 * Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
public interface PaymentService extends CrudService<PaymentEntity, PaymentDTO> {
    void refund(Long id);
}