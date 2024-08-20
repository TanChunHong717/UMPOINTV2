package my.edu.um.umpoint.modules.service.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@TableName("svc_service")
public class SvcServiceEntity {

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