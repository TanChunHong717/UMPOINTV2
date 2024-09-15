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
 * Accommodation Booking
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-09-08
 */
@Data
@TableName("acc_booking")
public class AccBookingEntity {
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
	* Accommodation ID
	*/
	private Long accommodationId;
	/**
	* Admin that approve/reject, user will contact this admin rather manager if umpoint.booking.accommodation.find-approve-admin-first=true
	*/
	private Long adminId;
	/**
	* User ID
	*/
	private Long userId;
	/**
	* Worker responsible if booking is not in working day
	*/
	private Long workerId;
	/**
	* Amount need to be pay
	*/
	private BigDecimal paymentAmount;
	/**
	 * Description of the event or purpose for the booking
	 */
	private String event;
	/**
	* Start day of booking
	*/
	private Date startDay;
	/**
	* End day of booking
	*/
	private Date endDay;
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
	 * Name of accommodation that being booked
	 */
	@TableField(exist = false)
	private String accommodation;
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
	 * Name of worker that being assigned to this booking
	 */
	@TableField(exist = false)
	private String worker;
	/**
	 * Payment entity list
	 */
	@TableField(exist = false)
	private List<AccPaymentEntity> accPaymentEntityList;
}