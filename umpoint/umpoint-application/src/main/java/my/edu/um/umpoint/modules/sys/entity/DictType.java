package my.edu.um.umpoint.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DictType {
    @JsonIgnore
    private Long id;
    private String dictType;
    private List<DictData> dataList = new ArrayList<>();
}
