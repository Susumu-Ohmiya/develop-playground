package jp.co.ks.kscommon.concurrent.task;

/**
 * マルチスレッド化を行なう処理単位のインタフェース
 */
public interface Task {
    
    /**
     * Threadで呼び出される処理
     * ここで発生した実行時例外は例外ハンドラに処理が移譲されます。
     */
    public void process();
}
