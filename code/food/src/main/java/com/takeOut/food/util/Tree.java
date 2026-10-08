package com.takeOut.food.util;

import com.takeOut.food.api.entity.Class;

import java.util.ArrayList;
import java.util.List;

public class Tree {

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public static List<Class> buildByRecursive(List<Class> treeNodes) {
        List<Class> trees = new ArrayList<Class>();
        for (Class treeNode : treeNodes) {
            if (treeNode.getParentId()==0) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static Class findChildren(Class treeNode, List<Class> treeNodes) {
        for (Class it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<Class>());
                }
                treeNode.getChildren().add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
}
