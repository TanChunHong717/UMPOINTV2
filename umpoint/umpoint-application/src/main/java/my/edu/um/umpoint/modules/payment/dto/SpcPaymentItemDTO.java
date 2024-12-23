package my.edu.um.umpoint.modules.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Space Payment Itemized
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-15
 */
@Data
@Schema(title = "Space Payment Itemized")
public class SpcPaymentItemDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 2354434705777956413L;

	@Schema(title = "ID")
	private Long id;

	@Schema(title = "Payment ID")
	private Long paymentId;

	@Schema(title = "Payment Item Name")
	private String itemName;

	@Schema(title = "Payment Item Count")
	private Integer itemAmount;

	@Schema(title = "Price per item (total = amount * price)")
	private BigDecimal itemPrice;
}
