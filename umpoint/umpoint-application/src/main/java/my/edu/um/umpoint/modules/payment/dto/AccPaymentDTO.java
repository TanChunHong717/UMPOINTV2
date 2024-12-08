package my.edu.um.umpoint.modules.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Accommodation Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@Schema(title = "Payment")
public class AccPaymentDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = -1511545322184053328L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Status: 0:Pending, 1:Success, 2:Failed, 3:Refund")
	private Integer status;

	@Schema(title = "Accommodation Booking ID")
	private Long bookingId;

	@Schema(title = "Payment Method ID")
	private String methodId;

	@Schema(title = "Payment Amount")
	private BigDecimal amount;

	@Schema(title = "Payment Date")
	private Date date;

	@Schema(title = "Creation Date")
	private Date createdAt;

	@Schema(title = "Payment Method Name")
	private String method;

	@Schema(title = "Payment item dto list")
	private List<AccPaymentItemDTO> accPaymentItemDTOList;
}
