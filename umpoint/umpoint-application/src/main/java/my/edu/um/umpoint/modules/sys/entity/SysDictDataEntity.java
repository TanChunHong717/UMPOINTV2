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
@TableName("sys_dict_data")
public class SysDictDataEntity extends BaseEntity {

	@Serial
	private static final long serialVersionUID = -7470312191691126557L;

	private Long dictTypeId;

	private String dictLabel;

	private String dictValue;

	private String remark;

	private Integer sort;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}