package my.edu.um.umpoint.modules.space.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Data;
import my.edu.um.umpoint.modules.sys.dto.SysDeptDTO;

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
    private static final long serialVersionUID = 1L;

	@SchemaProperty(name = "ID")
	private Long id;

	@SchemaProperty(name = "Name")
	private String name;

	@SchemaProperty(name = "Category ID")
	private Long catId;

	@SchemaProperty(name = "Department ID")
	private Long deptId;

	@SchemaProperty(name = "Description")
	private String description;

	@SchemaProperty(name = "Facilities")
	private String facilities;

	@SchemaProperty(name = "Creator")
	private Long creator;

	@SchemaProperty(name = "Create Date")
	private Date createDate;

	@SchemaProperty(name = "Updater")
	private Long updater;

	@SchemaProperty(name = "Update Date")
	private Date updateDate;

	@SchemaProperty(name = "Category Name")
	private String category;

	@SchemaProperty(name = "Department Name")
	private String deptName;

	@SchemaProperty(name = "Image Url List")
	private List<String> imageUrls;

	@SchemaProperty(name = "Tags")
	private List<String> tags;
}
