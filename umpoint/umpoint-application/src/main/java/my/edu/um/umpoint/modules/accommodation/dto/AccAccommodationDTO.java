package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * Accommodation
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-23
 */
@Data
@Schema(name = "Accommodation")
public class AccAccommodationDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 3422489955367337478L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Name")
	private String name;

	@SchemaProperty(name = "Category ID")
	private Long catId;

	@SchemaProperty(name = "Department ID")
	private Long deptId;

	@SchemaProperty(name = "Address")
	private String address;

	@SchemaProperty(name = "Description")
	private String description;

	@SchemaProperty(name = "Facilities")
	private String facilities;

	@SchemaProperty(name = "Max capacity")
	private BigDecimal capacity;

	@SchemaProperty(name = "Manager ID")
	private Long manager;

	@SchemaProperty(name = "Booking Rule ID")
	private Long bookingRuleId;

	@SchemaProperty(name = "Status 0:Suspend 1:Normal")
	private Integer status;

	@SchemaProperty(name = "Creator")
	private Long creator;

	@SchemaProperty(name = "Create date")
	private Date createDate;

	@SchemaProperty(name = "Updater")
	private Long updater;

	@SchemaProperty(name = "Update date")
	private Date updateDate;
}
