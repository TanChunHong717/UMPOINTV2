package my.edu.um.umpoint.common.utils;

import my.edu.um.umpoint.common.validator.AssertUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TreeUtils {

    public static <T extends TreeNode> List<T> build(List<T> treeNodes, Long pid) {

        AssertUtils.isNull(pid, "pid");

        List<T> treeList = new ArrayList<>();
        for(T treeNode : treeNodes) {
            if (pid.equals(treeNode.getPid())) {
                treeList.add(findChildren(treeNodes, treeNode));
            }
        }

        return treeList;
    }

    private static <T extends TreeNode> T findChildren(List<T> treeNodes, T rootNode) {
        for(T treeNode : treeNodes) {
            if(rootNode.getId().equals(treeNode.getPid())) {
                rootNode.getChildren().add(findChildren(treeNodes, treeNode));
            }
        }
        return rootNode;
    }

    public static <T extends TreeNode> List<T> build(List<T> treeNodes) {
        List<T> result = new ArrayList<>();

        //list转map
        Map<Long, T> nodeMap = new LinkedHashMap<>(treeNodes.size());
        for(T treeNode : treeNodes){
            nodeMap.put(treeNode.getId(), treeNode);
        }

        for(T node : nodeMap.values()) {
            T parent = nodeMap.get(node.getPid());
            if(parent != null && !(node.getId().equals(parent.getId()))){
                parent.getChildren().add(node);
                continue;
            }

            result.add(node);
        }

        return result;
    }

}