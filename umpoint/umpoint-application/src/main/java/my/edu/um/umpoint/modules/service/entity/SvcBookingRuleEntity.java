package my.edu.um.umpoint.modules.service.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Service Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@TableName("svc_booking_rule")
public class SvcBookingRuleEntity {

    /**
     * ID
     */
	@TableId
	private Long id;
    /**
     * Price
     */
	private BigDecimal price;
    /**
     * Is booking require approve by manager
     */
	private Integer approvalRequired;
}