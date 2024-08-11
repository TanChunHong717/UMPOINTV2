package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@Schema(name = "Space")
public class SpaceDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 4955180313433960914L;

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

	@SchemaProperty(name = "Capacity")
	private Integer capacity;

	@SchemaProperty(name = "Manager ID")
	private Long manager;

	@SchemaProperty(name = "Booking Rule ID")
	private BookingRuleDTO bookingRuleId;

	@SchemaProperty(name = "Creator ID")
	private Long creator;

	@SchemaProperty(name = "Create Date")
	private Date createDate;

	@SchemaProperty(name = "Last Updater ID")
	private Long updater;

	@SchemaProperty(name = "Last Update Date")
	private Date updateDate;

	@SchemaProperty(name = "Category Name")
	private String category;

	@SchemaProperty(name = "Department Name")
	private String deptName;

	@SchemaProperty(name = "Manager Name")
	private String managerName;

	@SchemaProperty(name = "Creator Name")
	private String creatorName;

	@SchemaProperty(name = "Last Updater Name")
	private String updaterName;

	@SchemaProperty(name = "Booking Rule DTO")
	private BookingRuleDTO bookingRuleDTO;

	@SchemaProperty(name = "Image DTO List")
	private List<ImageDTO> imageDTOList;

	@SchemaProperty(name = "Tag DTO List")
	private List<TagDTO> tagDTOList;
}
