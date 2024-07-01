package com.hncboy.crane4jx.toolwindow.toolbar;

import com.hncboy.crane4jx.toolwindow.Crane4jXToolWindowFactory;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author hncboy
 * Crane4jX 刷新按钮
 */
public class Crane4jXToolbarRefreshAction extends DumbAwareAction {

    public Crane4jXToolbarRefreshAction() {
        getTemplatePresentation().setText("Refresh");
        getTemplatePresentation().setIcon(AllIcons.Actions.Refresh);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Objects.requireNonNull(Crane4jXToolWindowFactory.getToolWindowContent(Objects.requireNonNull(e.getProject()))).refreshTree();
    }
}
