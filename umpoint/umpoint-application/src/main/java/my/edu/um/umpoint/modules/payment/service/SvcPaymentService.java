package my.edu.um.umpoint.modules.payment.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.payment.dto.SvcPaymentDTO;
import my.edu.um.umpoint.modules.payment.entity.SvcPaymentEntity;

/**
 * Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
public interface SvcPaymentService extends CrudService<SvcPaymentEntity, SvcPaymentDTO> {
    void refund(Long id);
}