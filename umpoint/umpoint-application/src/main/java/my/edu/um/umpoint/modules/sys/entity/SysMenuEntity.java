package my.edu.um.umpoint.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_menu")
public class SysMenuEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private Long pid;

	private String name;

	private String url;

	private String permissions;

	private Integer menuType;

	private String icon;

	private Integer sort;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;

	@TableField(exist = false)
	private String parentName;

}