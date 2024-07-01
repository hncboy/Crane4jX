package com.hncboy.crane4jx.toolwindow;

import com.hncboy.crane4jx.constant.Crane4jXToolWindowConstant;
import com.hncboy.crane4jx.service.Crane4jXService;
import com.hncboy.crane4jx.toolwindow.tree.Crane4jXTree;
import com.hncboy.crane4jx.util.AsyncUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBScrollPane;
import lombok.Getter;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;

/**
 * @author hncboy
 * Crane4jX 工具窗口内容
 */
public class Crane4jXToolWindowContent extends JPanel {

    private final Project project;

    @Getter
    private Crane4jXTree crane4jXTree;

    public Crane4jXToolWindowContent(Project project) {
        this.project = project;

        // 设置布局
        setLayout(new BorderLayout());

        // 添加动作工具栏
        addActionToolbar();

        // 添加滚动面板
        addScrollPane();

        // 延迟执行首次加载，等待构建完毕
        DumbService.getInstance(project).smartInvokeLater(this::refreshTree);
    }

    /**
     * 添加滚动面板
     */
    private void addScrollPane() {
        JBScrollPane scrollPane = new JBScrollPane();
        // 放到中间
        add(scrollPane, BorderLayout.CENTER);
        // 设置滚动面板的水平滚动条策略为[按需显示]
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // 添加树组件，将树形控件设置为滚动面板的视图组件
        this.crane4jXTree = new Crane4jXTree(project);
        scrollPane.setViewportView(crane4jXTree);
    }

    /**
     * 刷新树
     */
    public void refreshTree() {
        // 当项目处于智能模式时
        DumbService.getInstance(project).runWhenSmart(() -> {

            AsyncUtil.runRead(project,
                    // 加载树数据
                    Crane4jXService.getInstance(project)::loadTreeData,
                    treeData -> {
                        // 渲染树
                        crane4jXTree.renderTree(treeData);

                        // 显示刷新通知
                        showRefreshNotification();
                    });
        });
    }

    /**
     * 显示刷新通知
     */
    private void showRefreshNotification() {
        BalloonBuilder balloonBuilder = JBPopupFactory.getInstance().createHtmlTextBalloonBuilder("Refresh success",
                null, MessageType.INFO.getPopupBackground(), null);

        // 创建气球通知
        Balloon balloon = balloonBuilder
                // 设置气球消失时间
                .setFadeoutTime(2000)
                // 设置按键盘时隐藏气球
                .setHideOnKeyOutside(true)
                // 设置点击链接时隐藏气球
                .setHideOnLinkClick(true)
                .createBalloon();

        // 获取 ToolWindow 组件
        JComponent toolWindowComponent = Crane4jXToolWindowFactory.getToolWindow(project).getComponent();
        // 显示气球通知在 ToolWindow 的西北角上方
        balloon.show(RelativePoint.getNorthWestOf(toolWindowComponent), Balloon.Position.above);
    }

    /**
     * 添加动作工具栏
     */
    private void addActionToolbar() {
        // 获取 ActionGroup
        ActionGroup actionGroup = (ActionGroup) ActionManager.getInstance().getAction(Crane4jXToolWindowConstant.ACTION_TOOLBAR_ID);
        // 创建一个水平的 ActionToolbar
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, actionGroup, true);
        // 设置目标组件并添加到布局中
        actionToolbar.setTargetComponent(this);
        // 放到北部
        add(actionToolbar.getComponent(), BorderLayout.NORTH);
    }
}
