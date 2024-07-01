package com.hncboy.crane4jx.util;

import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.concurrency.CancellablePromise;

import javax.annotation.PreDestroy;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author hncboy
 * 异步工具类
 */
public class AsyncUtil {

    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            1,
            5,
            5L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );

    /**
     * 在后台线程中异步执行读取操作，并在UI线程上处理结果。
     *
     * @param project 当前项目，用于确定智能模式和 UI 线程的模态状态。
     * @param background 一个在后台执行的 Callable 任务，返回操作结果。
     * @param consumer 一个 Consumer，用于在 UI 线程上消费处理结果。
     * @return 返回一个CancellablePromise 对象，可以用于取消操作或添加回调。
     */
    public static <T> CancellablePromise<T> runRead(Project project, Callable<T> background, Consumer<T> consumer) {
        // 使用 ReadAction.nonBlocking 将长时间运行的读取操作提交到后台线程
        return ReadAction.nonBlocking(background)
                // 指定操作在项目处于智能模式时执行
                .inSmartMode(project)
                // 指定在 UI 线程上使用给定的模态状态执行结果处理
                .finishOnUiThread(ModalityState.defaultModalityState(), consumer)
                // 提交操作到定义的线程池执行
                .submit(EXECUTOR);
    }

    @PreDestroy
    public void dispose() {
        EXECUTOR.shutdown();
    }
}
