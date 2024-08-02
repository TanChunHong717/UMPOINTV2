package my.edu.um.umpoint.common.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(title = "Page Data")
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "total")
    private int total;

    @Schema(title = "list")
    private List<T> list;

    public PageData(List<T> list, long total) {
        this.list = list;
        this.total = (int)total;
    }
}
