package my.edu.um.umpoint.modules.payment.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@TableName("acc_payment")
public class AccPaymentEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Status: 0:Pending, 1:Success, 2:Failed, 3:Refund
	*/
	private Integer status;
	/**
	 * Space/Service/Accommodation Booking ID
	 */
	private Long bookingId;
	/**
	* User ID
	*/
	private Long userId;
	/**
	 * Payment Method ID
	 */
	private Long methodId;
	/**
	* Payment Amount
	*/
	private BigDecimal amount;
	/**
	* Payment Date
	*/
	@TableField(fill = FieldFill.INSERT)
	private Date date;
	/**
	 * Payment Method Name
	 */
	@TableField(exist = false)
	private String method;
	/**
	 * Payment item entity list
	 */
	@TableField(exist = false)
	private List<AccPaymentItemEntity> accPaymentItemEntityList;
}