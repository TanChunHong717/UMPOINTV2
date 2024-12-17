package my.edu.um.umpoint.modules.service.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import my.edu.um.umpoint.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("svc_service")
public class SvcServiceEntity extends BaseEntity {
	/**
	 * Status 0:Suspend 1:Normal
	 */
	@TableLogic(value = "1", delval = "0")
	private Integer status;
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
     * Description
     */
	private String description;
    /**
     * Manager ID
     */
	private Long manager;
	/**
	 * Price
	 */
	private BigDecimal price;
	/**
	 * Booking Rule Id
	 */
	private Long bookingRuleId;
	/**
	 * Last updater
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	 * Last update date
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
	/**
	 * Category name
	 */
	@TableField(exist = false)
	private String category;
	/**
	 * Department name
	 */
	@TableField(exist = false)
	private String deptName;
	/**
	 * Manager name
	 */
	@TableField(exist = false)
	private String managerName;
	/**
	 * Creator name
	 */
	@TableField(exist = false)
	private String creatorName;
	/**
	 * Last updater name
	 */
	@TableField(exist = false)
	private String updaterName;
	/**
	 * Manager mobile
	 */
	@TableField(exist = false)
	private String managerMobile;
	/**
	 * Manager email
	 */
	@TableField(exist = false)
	private String managerEmail;
	/**
	 * Booking rule entity
	 */
	@TableField(exist = false)
	private SvcBookingRuleEntity svcBookingRuleEntity;
	/**
	 * Image url list
	 */
	@TableField(exist = false)
	private List<SvcImageEntity> svcImageEntityList;
	/**
	 * Space tag list
	 */
	@TableField(exist = false)
	private List<SvcTagEntity> svcTagEntityList;
}