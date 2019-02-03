package jp.co.ks.kscommon.concurrent.util;

/**
 * マルチスレッド並列処理で使用されるユーティリティ
 */
public final class ThreadUtils {

    private ThreadUtils() {
    }

    /**
     * ログ出力します。
     * @param o
     */
    public static void log(Object o) {
        log("%s", o);
    }

    /**
     * ログ出力実装
     * @param fmt
     * @param args
     */
    public static void log(String fmt, Object... args) {
        // TODO : ログ出力
        Thread currentThread = Thread.currentThread();
        String datetime = String.format("%1$tT.%1$tL", System.currentTimeMillis());
        datetime = "--:--:" + datetime.substring(6);
        String threadName = String.format("%s", currentThread.getName());
        System.out.printf("%s [%s] %s%n", datetime, threadName, String.format(fmt, args));
    }

    /**
     * 指定時間スリープします。割り込み例外は報告しません。
     * @param millis スリープする時間(ミリ秒)
     */
    public static void sleep(long millis) {
        sleep(millis, false);
    }
    
    /**
     * スリープします。
     * @param millis スリープする時間(ミリ秒)
     * @param reportInterrupted 割り込み例外を報告するか
     */
    public static void sleep(long millis, boolean reportInterrupted) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            if (reportInterrupted) {
                throw new RuntimeException(ex);
            }
        }
    }
}
