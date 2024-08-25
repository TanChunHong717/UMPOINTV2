package my.edu.um.umpoint.modules.accommodation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import my.edu.um.umpoint.common.validator.group.AddGroup;
import my.edu.um.umpoint.common.validator.group.DefaultGroup;
import my.edu.um.umpoint.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;
import java.util.List;

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
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@SchemaProperty(name = "Name")
	@NotEmpty(groups = {DefaultGroup.class})
	private String name;

	@SchemaProperty(name = "Category ID")
	@NotNull(groups = {DefaultGroup.class})
	private Long catId;

	@SchemaProperty(name = "Department ID")
	@NotNull(groups = {DefaultGroup.class})
	private Long deptId;

	@SchemaProperty(name = "Address")
	@NotEmpty(groups = {DefaultGroup.class})
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
	@Range(min=0, max=1, groups = DefaultGroup.class)
	private Integer status;

	@SchemaProperty(name = "Creator")
	private Long creator;

	@SchemaProperty(name = "Create date")
	private Date createDate;

	@SchemaProperty(name = "Updater")
	private Long updater;

	@SchemaProperty(name = "Update date")
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
	private AccBookingRuleDTO accBookingRuleDTO;

	@SchemaProperty(name = "Image DTO List")
	private List<AccImageDTO> accImageDTOList;

	@SchemaProperty(name = "Tag DTO List")
	private List<AccTagDTO> accTagDTOList;
}