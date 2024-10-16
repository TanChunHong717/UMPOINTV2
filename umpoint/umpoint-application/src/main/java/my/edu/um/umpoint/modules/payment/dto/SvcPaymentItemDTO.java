package my.edu.um.umpoint.modules.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * Service Payment Itemized
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-16
 */
@Data
@Schema(name = "Service Payment Itemized")
public class SvcPaymentItemDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 220973959925029187L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Payment ID")
	private Long paymentId;

	@SchemaProperty(name = "Payment Item Name")
	private String itemName;

	@SchemaProperty(name = "Payment Item Count")
	private Integer itemAmount;

	@SchemaProperty(name = "Price per item (total = amount * price)")
	private BigDecimal itemPrice;
}
