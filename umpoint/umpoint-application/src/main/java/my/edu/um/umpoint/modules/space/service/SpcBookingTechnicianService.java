package my.edu.um.umpoint.modules.space.service;

import my.edu.um.umpoint.common.service.CrudService;
import my.edu.um.umpoint.modules.space.dto.SpcBookingTechnicianDTO;
import my.edu.um.umpoint.modules.space.entity.SpcBookingTechnicianEntity;

public interface SpcBookingTechnicianService extends CrudService<SpcBookingTechnicianEntity, SpcBookingTechnicianDTO> {
    void deleteByBookingId(Long bookingId);
}
