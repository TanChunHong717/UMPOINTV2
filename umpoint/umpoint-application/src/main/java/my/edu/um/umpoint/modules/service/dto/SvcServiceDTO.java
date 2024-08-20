package my.edu.um.umpoint.modules.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@Schema(name = "Service")
public class SvcServiceDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 8480542002587547062L;

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
	private Integer capacity;

	@SchemaProperty(name = "Manager ID")
	private Long manager;

	@SchemaProperty(name = "Booking Rule ID")
	private Long bookingRuleId;

	@SchemaProperty(name = "Creator")
	private Long creator;

	@SchemaProperty(name = "Create date")
	private Date createDate;

	@SchemaProperty(name = "Updater")
	private Long updater;

	@SchemaProperty(name = "Update date")
	private Date updateDate;


}
