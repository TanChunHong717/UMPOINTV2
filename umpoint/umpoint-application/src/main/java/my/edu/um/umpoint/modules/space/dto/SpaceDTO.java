package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;

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
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@SchemaProperty(name = "Name")
	@NotEmpty
	private String name;

	@SchemaProperty(name = "Category ID")
	@NotNull
	private Long catId;

	@SchemaProperty(name = "Department ID")
	@NotNull
	private Long deptId;

	@SchemaProperty(name = "Address")
	@NotNull
	private String address;

	@SchemaProperty(name = "Description")
	private String description;

	@SchemaProperty(name = "Facilities")
	private String facilities;

	@SchemaProperty(name = "Capacity")
	@NotNull
	private Integer capacity;

	@SchemaProperty(name = "Manager ID")
	private Long manager;

	@SchemaProperty(name = "Booking Rule ID")
	private BookingRuleDTO bookingRuleId;

	@SchemaProperty(name = "Creator ID")
	@Null
	private Long creator;

	@SchemaProperty(name = "Create Date")
	@Null
	private Date createDate;

	@SchemaProperty(name = "Last Updater ID")
	@Null
	private Long updater;

	@SchemaProperty(name = "Last Update Date")
	@Null
	private Date updateDate;

	@SchemaProperty(name = "Category Name")
	@Null
	private String category;

	@SchemaProperty(name = "Department Name")
	@Null
	private String deptName;

	@SchemaProperty(name = "Manager Name")
	@Null
	private String managerName;

	@SchemaProperty(name = "Creator Name")
	@Null
	private String creatorName;

	@SchemaProperty(name = "Last Updater Name")
	@Null
	private String updaterName;

	@SchemaProperty(name = "Booking Rule DTO")
	private BookingRuleDTO bookingRuleDTO;

	@SchemaProperty(name = "Image DTO List")
	private List<ImageDTO> imageDTOList;

	@SchemaProperty(name = "Tag DTO List")
	private List<TagDTO> tagDTOList;
}
