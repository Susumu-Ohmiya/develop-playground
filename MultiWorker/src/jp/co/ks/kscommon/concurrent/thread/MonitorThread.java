package jp.co.ks.kscommon.concurrent.thread;

import static jp.co.ks.kscommon.concurrent.util.ThreadUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import jp.co.ks.kscommon.concurrent.execption.ThreadProcessExceptionHandler;
import jp.co.ks.kscommon.concurrent.task.Task;

/**
 * 監視Thread。全Threadの完了を確認するまで常駐します。
 * 例外ハンドラからの通知を受け取って全スレッドの停止を行います。
 * 
 * @param <T> 処理を行うTask
 *
 */
public class MonitorThread<T extends Task> implements Runnable {

    /** ThreadPool */
    private ExecutorService pool;
    /** Workerの処理内で発生した例外のハンドラ */
    private ThreadProcessExceptionHandler handler;
    /** 実行中のWokerのList */
    private List<Worker<T>> threads = new ArrayList<Worker<T>>();
    /** 全停止命令を送ったかのフラグ */
    private boolean haltSignaled = false;
    /** タイムアウト確認用のタイマー */
    private Timecounter timeoutCheck = new Timecounter(10000);

    /**
     * 監視Threadのコンストラクタ
     * @param pool ThreadPool
     * @param handler Workerの処理内で発生した例外のハンドラ
     * @param threads ThreadPoolでexecuteされているWokerのList
     */
    public MonitorThread(ExecutorService pool, ThreadProcessExceptionHandler handler, List<Worker<T>> threads) {
        this.pool = pool;
        this.handler = handler;
        this.threads = threads;
    }

    /**
     * 監視処理を行う。</br>
     * 
     * 無限ループを行ってThreadPoolが完了した場合に終了する。</br>
     * 例外ハンドラにThreadPool停止要求を行なうエラー発生が通知されていた場合は全Wokerのstop()を呼び出す。</br>
     * 全Workerに停止要求を行った後に、規定時間以上経過した場合、タイムアウトとして当スレッドを終了します。
     */
    @Override
    public void run() {
        
        log("Monitor begin");

        while (true) {
            // Workerが全部終了している場合は監視を終了する
            if (this.pool.isTerminated()) {
                break;
            }
            
            if (this.handler.happenedError()) {
                // エラー発生時の処理
                if (this.handler.isNeedHaltThreadPool() && !this.haltSignaled) {
                    // 全停止要求が必要なエラーの場合
                    for (Worker<?> t : this.threads) {
                        log("halt![" + t.getName() + "]");
                        t.stop();
                    }
                    this.haltSignaled = true;
                    timeoutCheck.start();
                }
            }
            // タイムアウトチェック
            if (this.haltSignaled && timeoutCheck.check()) {
                pool.shutdownNow();
                break;
            }
        }

        log("Monitor end");
    }
    
    private static class Timecounter {
        private long timer;
        private long start;
        private boolean started = false;
        
        public Timecounter(long timer) {
            this.timer = timer;
        }
        
        public void start() {
            this.start = System.currentTimeMillis();
            this.started = true;
        }
        
        public boolean check() {
            return this.started && (System.currentTimeMillis() - this.start) >= this.timer;
        }
    }
}
