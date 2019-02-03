package jp.co.ks.concurrent.test.tester;

import static jp.co.ks.kscommon.concurrent.util.ThreadUtils.*;

import jp.co.ks.concurrent.test.entity.Counter;
import jp.co.ks.concurrent.test.entity.Data;
import jp.co.ks.concurrent.test.request.DataTask;
import jp.co.ks.kscommon.concurrent.Executor.ParallelTaskProcessor;

/**
 * @author susumu.omiya
 *
 */
public class Tester {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        int dataAmount = 1000;
        int errorTiming = 250;
        int haltErrorTiming = 501;
        int timeoutTiming = 499;

        log("start");
        
        // 並列スレッド数は5件
        ParallelTaskProcessor<DataTask> processer = new ParallelTaskProcessor<DataTask>(5);
        
        // 並列処理を開始する
        processer.start();
        
        // 並列処理の受付キューにタスクを投入する
        for (int i = 1; i <= dataAmount; i++) {
            if ((i % errorTiming) == 0) {
                // 一般例外（タスク内で処理されて正常に処理続行する）
                processer.add(new DataTask(new Data(),DataTask.CHECKED_ERROR));
            } else if (i == haltErrorTiming) {
                // 実行事例外発生タスク投入。（スレッドをすべて止めるべきエラー）
                processer.add(new DataTask(new Data(),DataTask.UNCHECK_ERROR));
            } else if (i == timeoutTiming) {
                // タイムアウト発生タスク投入。（スレッド停止要求に対して応答しない）
                processer.add(new DataTask(new Data(),DataTask.TIME_OUT));
            } else {
                processer.add(new DataTask(new Data(),DataTask.NO_ERROR));
            }
        }
        
        // タスクの投入が終わったらプロセスをシャットダウンする
        processer.shutdown();
        
        log(Counter.getInstance());

        log("end");

    }


}
