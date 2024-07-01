package com.hncboy.crane4jx.toolwindow.toolbar;

import com.hncboy.crane4jx.toolwindow.Crane4jXToolWindowContent;
import com.hncboy.crane4jx.toolwindow.Crane4jXToolWindowFactory;
import com.hncboy.crane4jx.toolwindow.tree.Crane4jXTree;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.Objects;

/**
 * @author hncboy
 * Crane4jX 扩展按钮
 */
public class Crane4jXToolbarExpandAction extends DumbAwareAction {

    public Crane4jXToolbarExpandAction() {
        getTemplatePresentation().setText("Expand All");
        getTemplatePresentation().setIcon(AllIcons.Actions.Expandall);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Crane4jXToolWindowContent crane4jXToolWindowContent = Crane4jXToolWindowFactory.getToolWindowContent(Objects.requireNonNull(e.getProject()));
        Crane4jXTree crane4jXTree = Objects.requireNonNull(crane4jXToolWindowContent).getCrane4jXTree();
        SwingUtilities.invokeLater(() -> expandAllNode(crane4jXTree));
    }

    /**
     * 扩展所有节点
     *
     * @param tree 树
     */
    private void expandAllNode(JTree tree) {
        // 从树的根节点开始
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        expandNode(root, tree);
        model.setRoot(root);
    }

    /**
     * 扩展节点
     *
     * @param node 节点
     * @param tree 树
     */
    private void expandNode(DefaultMutableTreeNode node, JTree tree) {
        // 扩展当前节点
        tree.expandPath(new TreePath(node.getPath()));

        // 递归扩展所有子节点
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            expandNode(child, tree);
        }
    }
}
