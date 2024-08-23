package my.edu.um.umpoint.modules.accommodation.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Accommodation
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@TableName("acc_accommodation")
public class AccAccommodationEntity {
	/**
	* ID
	*/
	@TableId
	private Long id;
	/**
	* Name
	*/
	private String name;
	/**
	* Category ID
	*/
	private Long catId;
	/**
	* Department ID
	*/
	private Long deptId;
	/**
	* Address
	*/
	private String address;
	/**
	* Description
	*/
	private String description;
	/**
	* Facilities
	*/
	private String facilities;
	/**
	* Max capacity
	*/
	private Integer capacity;
	/**
	* Manager ID
	*/
	private Long manager;
	/**
	* Booking Rule ID
	*/
	private Long bookingRuleId;
	/**
	* Status 0:Suspend 1:Normal
	*/
	private Integer status;
	/**
	* Creator
	*/
	private Long creator;
	/**
	* Create date
	*/
	private Date createDate;
	/**
	* Updater
	*/
	private Long updater;
	/**
	* Update date
	*/
	private Date updateDate;
}