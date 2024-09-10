package my.edu.um.umpoint.modules.booking.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Payment
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@TableName("payment")
public class PaymentEntity {
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
	 * Resource Type: 0:Space, 1:Service, 2:Accommodation
	 */
	private Integer resourceType;
	/**
	 * Space/Service/Accommodation ID
	 */
	private Long resourceId;
	/**
	* User ID
	*/
	private Long userId;
	/**
	 * Payment Method ID
	 */
	private Long method_id;
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
	 * Name of user who make payment
	 */
	@TableField(exist = false)
	private String username;
}