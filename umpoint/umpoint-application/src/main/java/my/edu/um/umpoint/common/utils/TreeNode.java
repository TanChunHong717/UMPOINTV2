package my.edu.um.umpoint.common.utils;

import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

 @Data
public class TreeNode<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long pid;

    private List<T> children = new ArrayList<>();
}