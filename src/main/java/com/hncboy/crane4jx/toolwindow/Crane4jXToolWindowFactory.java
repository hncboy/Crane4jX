package com.hncboy.crane4jx.toolwindow;

import com.hncboy.crane4jx.constant.Crane4jXToolWindowConstant;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author hncboy
 * Crane4jX ToolWindow 工厂
 */
public class Crane4jXToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        Crane4jXToolWindowContent crane4jXToolWindowContent = new Crane4jXToolWindowContent(toolWindow.getProject());
        Content content = ContentFactory.getInstance().createContent(crane4jXToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * 获取 ToolWindow
     *
     * @param project project
     * @return ToolWindow
     */
    public static ToolWindow getToolWindow(@NotNull Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(Crane4jXToolWindowConstant.TOOL_WINDOW_ID);
    }

    /**
     * 获取 Crane4jXToolWindowContent
     *
     * @param project project
     * @return Crane4jXToolWindowContent
     */
    public static Crane4jXToolWindowContent getToolWindowContent(@NotNull Project project) {
        // 获取工具窗口
        ToolWindow toolWindow = getToolWindow(project);
        if (Objects.isNull(toolWindow)) {
            return null;
        }

        // 获取工具窗口的内容管理器
        ContentManager contentManager = toolWindow.getContentManager();
        // 遍历所有内容查找 Crane4jXToolWindowContent
        for (Content content : contentManager.getContents()) {
            if (content.getComponent() instanceof Crane4jXToolWindowContent) {
                // 返回 Crane4jXToolWindowContent 的实例
                return (Crane4jXToolWindowContent) content.getComponent();
            }
        }

        return null;
    }
}
