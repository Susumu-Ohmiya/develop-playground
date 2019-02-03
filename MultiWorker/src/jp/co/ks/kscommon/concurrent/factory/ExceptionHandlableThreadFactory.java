package jp.co.ks.kscommon.concurrent.factory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 例外ハンドラを登録するThreadのファクトリ。
 */
public class ExceptionHandlableThreadFactory implements ThreadFactory {

    private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
    private final Thread.UncaughtExceptionHandler handler;

    /**
     * コンストラクタ
     * @param handler 例外ハンドラ
     */
    public ExceptionHandlableThreadFactory(Thread.UncaughtExceptionHandler handler) {
        this.handler = handler;
    }

    @Override
    public Thread newThread(Runnable run) {
        Thread thread = defaultFactory.newThread(run);
        thread.setUncaughtExceptionHandler(this.handler);
        return thread;
    }
}
