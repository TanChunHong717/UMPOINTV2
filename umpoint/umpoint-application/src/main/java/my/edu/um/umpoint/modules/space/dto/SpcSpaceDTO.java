package my.edu.um.umpoint.modules.space.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Space
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-03
 */
@Data
@Schema(title = "Space")
public class SpcSpaceDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 4955180313433960914L;

	@Schema(title = "ID")
	@Null(groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	private Long id;

	@Schema(title = "Status 0:suspend 1:normal")
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

	@Schema(title = "Capacity")
	@NotNull(groups = {DefaultGroup.class})
	private Integer capacity;

	@Schema(title = "Manager ID")
	private Long manager;

	@Schema(title = "Price for book an hour")
	private BigDecimal hourPrice;

	@Schema(title = "Price for book four hours")
	private BigDecimal fourHoursPrice;

	@Schema(title = "Price for book a day")
	private BigDecimal dayPrice;

	@Schema(title = "Booking Rule ID")
	private Long bookingRuleId;

	@Schema(title = "Creator ID")
	private Long creator;

	@Schema(title = "Create Date")
	private Date createDate;

	@Schema(title = "Last Updater ID")
	private Long updater;

	@Schema(title = "Last Update Date")
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
	private SpcBookingRuleDTO spcBookingRuleDTO;

	@Schema(title = "Image DTO List")
	private List<SpcImageDTO> spcImageDTOList;

	@Schema(title = "Tag DTO List")
	private List<SpcTagDTO> spcTagDTOList;
}
