package my.edu.um.umpoint.modules.booking.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Space Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@TableName("svc_booking")
public class SvcBookingEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Status: 0:Pending, 1:Reject, 2:Approve(Pending Payment), 3:Approve(Payment Complete), 4:Cancel
	*/
	private Integer status;
	/**
	* Space ID
	*/
	private Long serviceId;
	/**
	* Admin that approve/reject, user will contact this admin rather manager if umpoint.service.booking.find-approve-admin-first=true
	*/
	private Long adminId;
	/**
	* User ID
	*/
	private Long userId;
	/**
	* Amount need to be pay
	*/
	private BigDecimal paymentAmount;
	/**
	* Create date
	*/
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;
	/**
	* Update date
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
	/**
	 * Name of service that being booked
	 */
	@TableField(exist = false)
	private String service;
	/**
	 * Name of admin who approve/reject booking
	 */
	@TableField(exist = false)
	private String admin;
	/**
	 * Name of user who make booking
	 */
	@TableField(exist = false)
	private String username;
	/**
	 * Payment entity list
	 */
	@TableField(exist = false)
	private List<SvcPaymentEntity> svcPaymentEntityList;
}