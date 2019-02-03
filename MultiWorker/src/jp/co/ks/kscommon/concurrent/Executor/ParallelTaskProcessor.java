package jp.co.ks.kscommon.concurrent.Executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import static jp.co.ks.kscommon.concurrent.util.ThreadUtils.*;

import jp.co.ks.kscommon.concurrent.execption.ThreadProcessExceptionHandler;
import jp.co.ks.kscommon.concurrent.factory.ExceptionHandlableThreadFactory;
import jp.co.ks.kscommon.concurrent.task.Task;
import jp.co.ks.kscommon.concurrent.thread.MonitorThread;
import jp.co.ks.kscommon.concurrent.thread.Worker;

/**
 * 指定されたスレッド数で処理を行う並列処理器
 * @param <T> 並列化する処理
 */
public class ParallelTaskProcessor<T extends Task> {

    /** Workerの処理内で発生した例外のハンドラ */
    private ThreadProcessExceptionHandler handler;
    /** 例外ハンドラを設定しているThreadFactory */
    private ThreadFactory factory;
    /** ThreadPool */
    private ExecutorService pool;
    /** 投入されるタスクを格納するキュー */
    private ConcurrentLinkedQueue<T> queue;
    /** 監視Thread用のThreadPool(SingleThread) */
    private ExecutorService monitorPool;
    /** 監視Threadの実行結果 */
    private Future<?> monitorResult;
    /** 並列Thread実行数 */
    private int nThreads;
    /** WokerのList */
    private List<Worker<T>> workers = new ArrayList<Worker<T>>();
    /** 稼働状態 */
    private boolean initialized = false;

    /**
     * 並列処理のインスタンス化
     * @param nThreads 並行実行数(Threadの最大起動数)
     */
    public ParallelTaskProcessor(int nThreads) {

        // WokerThread関係初期化
        this.nThreads = nThreads;
        this.handler = new ThreadProcessExceptionHandler();
        this.factory = new ExceptionHandlableThreadFactory(this.handler);
        this.pool = Executors.newFixedThreadPool(nThreads, this.factory);
        this.queue = new ConcurrentLinkedQueue<T>();
        // 監視Thread関係初期化
        this.monitorPool = Executors.newSingleThreadExecutor();

    }

    /**
     * 並列処理を開始状態にする。
     * 
     */
    public void start() {

        this.monitorResult = this.monitorPool.submit(new MonitorThread<T>(this.pool, this.handler, this.workers));

        for (int i = 0; i < this.nThreads; i++) {
            Worker<T> t = new Worker<T>(this.queue , "Worker" + (i + 1));
            this.workers.add(t);
            this.pool.execute(t);
        }
        
        this.initialized = true;

    }

    /**
     * スレッド処理待ちのキューにタスクを投入します。</br>
     * </br>
     * タスク投入が失敗する場合は以下の通りです。</br>
     * ・初期化されていない。</br>
     * ・ThreadPoolがshutdownされている。</br>
     * ・スレッド処理内でエラーが発生している。</br>
     * 
     * @param task スレッド処理待ちQueueに投入するタスク
     * @return タスクの投入に成功した場合:ture / タスク投入に失敗した場合:false
     */
    public boolean add(T task) {
        if (!checkStatus()) {
            return false;
        }

        while (this.queue.size() > 5) {
            sleep(10);
            if (!checkStatus()) {
                return false;
            }
            continue;
        }

        this.queue.add(task);

        return true;
    }

    /**
     * 並列処理器の状態をチェックします。
     * 併せて、エラー発生時は受付キューをクリアします。
     */
    private boolean checkStatus() {
        if (!this.initialized) {
            return false;
        }
        
        if (this.pool.isShutdown()) {
            return false;
        }
        
        if (this.handler.happenedError()) {
            this.queue.clear();
            return false;
        }
        return true;
    }

    /**
     * ThreadPoolをシャットダウンしキューが空になったらすべてのthreadに停止命令を行ないます。
     * その後、監視Threadの終了を待って処理終了します。
     */
    public void shutdown() {
        
        this.pool.shutdown();
        
        while (!this.queue.isEmpty()) {
            sleep(10);
        }
        
        for (Worker<T> t : this.workers) {
            t.stop();
        }

        this.monitorPool.shutdown();
        
        try {
            this.monitorResult.get();
        } catch (InterruptedException e) {
            // TODO:エラーハンドリング
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO:エラーハンドリング
            e.printStackTrace();
        }
        

    }


}
