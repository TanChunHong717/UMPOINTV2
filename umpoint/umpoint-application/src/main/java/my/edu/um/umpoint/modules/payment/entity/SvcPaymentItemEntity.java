package my.edu.um.umpoint.modules.payment.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Service Payment Itemized
 *
 * @author NKSL2001 s2116593@siswa.um.edu.my
 * @since 1.0.0 2024-10-16
 */
@Data
@TableName("svc_payment_item")
public class SvcPaymentItemEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Payment ID
	*/
	private Long paymentId;
	/**
	* Payment Item Name
	*/
	private String itemName;
	/**
	* Payment Item Count
	*/
	private Integer itemAmount;
	/**
	* Price per item (total = amount * price)
	*/
	private BigDecimal itemPrice;
}