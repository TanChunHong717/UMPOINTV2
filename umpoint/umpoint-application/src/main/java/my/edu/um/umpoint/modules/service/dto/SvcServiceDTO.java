package my.edu.um.umpoint.modules.service.dto;

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
 * Service
 *
 * @author Tan Chun Hong tanchunhong717@gmail.com
 * @since 1.0.0 2024-08-20
 */
@Data
@Schema(title = "Service")
public class SvcServiceDTO implements Serializable {
	@Serial
    private static final long serialVersionUID = 8480542002587547062L;

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

	@Schema(title = "Description")
	private String description;

	@Schema(title = "Manager ID")
	private Long manager;

	@Schema(title = "Price")
	private BigDecimal price;

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

	// for user ui
	@Schema(title = "Manager Mobile")
	private String managerMobile;
	@Schema(title = "Manager Email")
	private String managerEmail;

	@Schema(title = "Booking Rule DTO")
	private SvcBookingRuleDTO svcBookingRuleDTO;

	@Schema(title = "Image DTO List")
	private List<SvcImageDTO> svcImageDTOList;

	@Schema(title = "Tag DTO List")
	private List<SvcTagDTO> svcTagDTOList;
}
