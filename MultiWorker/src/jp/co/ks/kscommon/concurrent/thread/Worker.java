package jp.co.ks.kscommon.concurrent.thread;

import static jp.co.ks.kscommon.concurrent.util.ThreadUtils.*;

import java.util.Queue;

import jp.co.ks.kscommon.concurrent.task.Task;

/**
 * タスクを並列処理させるためのスレッド。
 * @param <T> タスク（Threadの処理実体）
 */
public class Worker<T extends Task> implements Runnable {

    /** 投入されるタスクを格納するキュー */
    private Queue<T> queue;
    
    /** Threadの走行状態制御（falseにするとThread処理を停止する） */
    private boolean running;
    
    /** Worker名 */
    private String name;

    /**
     * WorkerThreadのコンストラクタ
     * @param queue 処理する対象を取得するTaskのQueue
     * @param name Workerの名称
     */
    public Worker(Queue<T> queue, String name) {
        this.queue = queue;
        this.name = name;
        this.running = true;
    }
    
    /**
     * このThreadの処理を停止させる。
     */
    public void stop() {
        this.running = false;
    }
    
    /**
     * このWorkerの名称を返す
     * @return Worker名
     */
    public String getName() {
        return this.name;
    }

    @Override
    public void run() {
        while (this.running) {
            if (this.queue.isEmpty()) {
                sleep(10);
                continue;
            }
            T task = this.queue.poll();
            if (task != null) {
                task.process();
            }
        }
    }
}
