package jp.co.ks.kscommon.concurrent.execption;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * スレッド処理で発生しキャッチされなかった例外のハンドラ
 */
public class ThreadProcessExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Queue<Throwable> exceptions = new ConcurrentLinkedQueue<Throwable>();
    
    private boolean needHaltThreadPool = false;

    /**
     * コンストラクタ
     */
    public ThreadProcessExceptionHandler() {
    }

    /**
     * @return true:エラーが発生している場合 / false:エラーが発生していない場合
     */
    public boolean happenedError() {
        return !this.exceptions.isEmpty();
    }

    /**
     * @return 発生した例外オブジェクトを格納したキュー
     */
    public Queue<Throwable> getExceptions() {
        return this.exceptions;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.exceptions.add(e);
        this.needHaltThreadPool = true;
        
        // TODO:エラー処理を記述する
        e.printStackTrace();

    }

    /**
     * @return ThreadPoolの全停止が必要か否かを返す
     */
    public boolean isNeedHaltThreadPool() {
        return this.needHaltThreadPool;
    }
}
