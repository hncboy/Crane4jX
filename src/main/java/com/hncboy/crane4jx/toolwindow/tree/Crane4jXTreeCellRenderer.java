package com.hncboy.crane4jx.toolwindow.tree;

import com.hncboy.crane4jx.entity.Crane4jXTreeContainerChildEntity;
import com.hncboy.crane4jx.entity.Crane4jXTreeContainerParentEntity;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.JTree;

/**
 * @author hncboy
 * Crane4jX 树单元格渲染器
 */
public class Crane4jXTreeCellRenderer extends ColoredTreeCellRenderer {

    @Override
    public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof Crane4jXTree.ParentNode node) {
            Object userObject = node.getUserObject();
            if (userObject instanceof Crane4jXTreeContainerParentEntity containerParentEntity) {
                // 图标
                setIcon(containerParentEntity.getIcon());
                // 名称
                append(String.format("%s(%d)", containerParentEntity.getName(), containerParentEntity.getChildCount()));
            }
        }

        if (value instanceof Crane4jXTree.ChildNode node) {
            Object userObject = node.getUserObject();
            if (userObject instanceof Crane4jXTreeContainerChildEntity containerChildEntity) {
                // 图标
                setIcon(containerChildEntity.getIcon());
                // 命名空间
                append(containerChildEntity.getNamespace());
                // 灰色全限定名
                append(" - " + containerChildEntity.getQualifiedName(), SimpleTextAttributes.GRAYED_ATTRIBUTES);
            }
        }
    }
}
