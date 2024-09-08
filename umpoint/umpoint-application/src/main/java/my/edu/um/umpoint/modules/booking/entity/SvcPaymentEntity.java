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
@TableName("svc_payment")
public class SvcPaymentEntity {
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
	* User ID
	*/
	private Long userId;
	/**
	* Payment Amount
	*/
	private BigDecimal amount;
	/**
	* Payment Method
	*/
	private String method;
	/**
	* Payment date
	*/
	@TableField(fill = FieldFill.INSERT)
	private Date date;
}