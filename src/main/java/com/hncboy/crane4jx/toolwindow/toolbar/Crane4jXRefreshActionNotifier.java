package com.hncboy.crane4jx.toolwindow.toolbar;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;

/**
 * @author hncboy
 * Crane4jX 刷新动作通知器
 */
public class Crane4jXRefreshActionNotifier {

    public static void notifyError(Project project, String content) {
        NotificationGroupManager groupManager = NotificationGroupManager.getInstance();
        Notification notification = groupManager.getNotificationGroup("Custom Notification Group")
                .createNotification("测试通知", MessageType.INFO);
        Notifications.Bus.notify(notification);
    }
}
