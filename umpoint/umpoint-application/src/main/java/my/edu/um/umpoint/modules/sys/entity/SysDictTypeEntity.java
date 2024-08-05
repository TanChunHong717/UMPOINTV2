package my.edu.um.umpoint.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import my.edu.um.umpoint.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_dict_type")
public class SysDictTypeEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -2092554014782806159L;

	private String dictType;

	private String dictName;

	private String remark;

	private Integer sort;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}