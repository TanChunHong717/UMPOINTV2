package my.edu.um.umpoint.modules.booking.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Payment Method
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-10
 */
@Data
@TableName("payment_method")
public class PaymentMethodEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Method
	*/
	private String method;
}