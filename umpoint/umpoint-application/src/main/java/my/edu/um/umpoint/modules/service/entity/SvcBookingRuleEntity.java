package my.edu.um.umpoint.modules.service.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Service Booking Rule
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-11
 */
@Data
@TableName("spc_booking_rule")
public class SvcBookingRuleEntity {
    /**
     * ID
     */
	@TableId
	private Long id;
	/**
	 * 0:Automatic approve 1: Require admin approve
	 */
	private Integer approvalRequired;
	/**
     * 0:Staff not allow to book 1:Staff allow to book
	 */
	private Integer openForStaff;
	/**
	 * 0:Student not allow to book 1:Student allow to book
	 */
	private Integer openForStudent;
	/**
	 * 0:Public not allow to book 1:Public allow to book
	 */
	private Integer openForPublic;
}