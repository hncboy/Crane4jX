package com.hncboy.crane4jx.toolwindow.tree;

import com.hncboy.crane4jx.entity.Crane4jXTreeChildEntity;
import com.hncboy.crane4jx.entity.Crane4jXTreeContainerChildEntity;
import com.hncboy.crane4jx.entity.Crane4jXTreeParentEntity;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Objects;

/**
 * @author hncboy
 * Crane4jX 树
 */
public class Crane4jXTree extends JTree {

    private final Project project;

    public Crane4jXTree(@NotNull Project project) {
        this.project = project;
        // 设置树形控件的单元格渲染器，用于自定义树节点的显示样式
        this.setCellRenderer(new Crane4jXTreeCellRenderer());
        // 隐藏根节点
        this.setRootVisible(false);
        // 初始化隐藏折叠箭头
        this.setShowsRootHandles(false);

        // 初始化鼠标监听器
        initMouseListener();
    }

    /**
     * 初始化鼠标监听器
     */
    private void initMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 鼠标左键双击
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    // 获取最后选中的节点
                    DefaultMutableTreeNode lastSelectedMutableTreeNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
                    if (Objects.isNull(lastSelectedMutableTreeNode)) {
                        return;
                    }
                    Object userObject = lastSelectedMutableTreeNode.getUserObject();
                    // 如果选中的是子节点则跳转
                    if (userObject instanceof Crane4jXTreeContainerChildEntity) {
                        // 跳转到对应的元素
                        ((Crane4jXTreeContainerChildEntity) userObject).navigate(true);
                    }
                }
            }
        });
    }

    /**
     * 渲染树
     *
     * @param treeData 树数据
     */
    public void renderTree(List<Crane4jXTreeParentEntity> treeData) {
        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode("Hello Crane4jX");

        for (Crane4jXTreeParentEntity parentEntity : treeData) {
            ParentNode parentTreeNode = new ParentNode(parentEntity);
            // 添加父节点
            rootTreeNode.add(parentTreeNode);
            // 添加子节点
            parentEntity.getChildEntities().forEach(childEntity -> parentTreeNode.add(new ChildNode(childEntity)));
        }

        // 展示折叠箭头
        this.setShowsRootHandles(true);
        ((DefaultTreeModel) this.getModel()).setRoot(rootTreeNode);
    }

    /**
     * 父节点
     */
    public static class ParentNode extends DefaultMutableTreeNode {

        public ParentNode(@NotNull Crane4jXTreeParentEntity data) {
            super(data);
        }
    }

    /**
     * 子节点
     */
    public static class ChildNode extends DefaultMutableTreeNode {

        public ChildNode(@NotNull Crane4jXTreeChildEntity data) {
            super(data);
        }
    }
}
