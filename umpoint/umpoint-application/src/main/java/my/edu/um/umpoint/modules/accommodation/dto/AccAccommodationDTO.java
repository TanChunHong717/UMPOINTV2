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
@Schema(title = "Accommodation")
public class AccAccommodationDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 3422489955367337478L;

	@Schema(title = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@Schema(title = "Status 0:Suspend 1:Normal")
	@Range(min=0, max=1, groups = DefaultGroup.class)
	private Integer status;

	@Schema(title = "Name")
	@NotEmpty(groups = {DefaultGroup.class})
	private String name;

	@Schema(title = "Category ID")
	@NotNull(groups = {DefaultGroup.class})
	private Long catId;

	@Schema(title = "Department ID")
	@NotNull(groups = {DefaultGroup.class})
	private Long deptId;

	@Schema(title = "Address")
	@NotEmpty(groups = {DefaultGroup.class})
	private String address;

	@Schema(title = "Description")
	private String description;

	@Schema(title = "Facilities")
	private String facilities;

	@Schema(title = "Max capacity")
	private Integer capacity;

	@Schema(title = "Manager ID")
	private Long manager;

	@Schema(title = "Price for book a day")
	private BigDecimal dayPrice;

	@Schema(title = "Price for book a week")
	private BigDecimal weekPrice;

	@Schema(title = "Booking Rule ID")
	private Long bookingRuleId;

	@Schema(title = "Creator")
	private Long creator;

	@Schema(title = "Create date")
	private Date createDate;

	@Schema(title = "Updater")
	private Long updater;

	@Schema(title = "Update date")
	private Date updateDate;

	@Schema(title = "Category Name")
	private String category;

	@Schema(title = "Department Name")
	private String deptName;

	@Schema(title = "Manager Name")
	private String managerName;

	@Schema(title = "Creator Name")
	private String creatorName;

	@Schema(title = "Last Updater Name")
	private String updaterName;

	@Schema(title = "Booking Rule DTO")
	private AccBookingRuleDTO accBookingRuleDTO;

	@Schema(title = "Image DTO List")
	private List<AccImageDTO> accImageDTOList;

	@Schema(title = "Tag DTO List")
	private List<AccTagDTO> accTagDTOList;
}
