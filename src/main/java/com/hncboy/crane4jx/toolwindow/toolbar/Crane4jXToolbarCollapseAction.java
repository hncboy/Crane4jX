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
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Objects;

/**
 * @author hncboy
 * Crane4jX 折叠按钮
 */
public class Crane4jXToolbarCollapseAction extends DumbAwareAction {

    public Crane4jXToolbarCollapseAction() {
        getTemplatePresentation().setText("Collapse All");
        getTemplatePresentation().setIcon(AllIcons.Actions.Collapseall);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Crane4jXToolWindowContent crane4jXToolWindowContent = Crane4jXToolWindowFactory.getToolWindowContent(Objects.requireNonNull(e.getProject()));
        Crane4jXTree crane4jXTree = Objects.requireNonNull(crane4jXToolWindowContent).getCrane4jXTree();
        SwingUtilities.invokeLater(() -> collapseAllNode(crane4jXTree));
    }

    /**
     * 折叠所有节点
     *
     * @param tree 树
     */
    private void collapseAllNode(JTree tree) {
        // 从树的根节点开始
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        collapseNode(root, tree);
        model.setRoot(root);
    }

    /**
     * 折叠节点
     *
     * @param node 节点
     * @param tree 树
     */
    private void collapseNode(DefaultMutableTreeNode node, JTree tree) {
        // 折叠当前节点
        tree.collapsePath(new TreePath(node.getPath()));

        // 递归折叠所有子节点
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            collapseNode(child, tree);
        }
    }
}
