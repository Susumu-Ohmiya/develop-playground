package jp.co.ks.concurrent.test.entity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 単純なカウンタ
 */
public class Counter {
    private AtomicInteger count = new AtomicInteger(0);
    private AtomicInteger eCount = new AtomicInteger(0);
    private static Counter singleton = new Counter();
    
    /**
     * @return カウンタのインスタンス
     */
    public static Counter getInstance() {
        return singleton;
    }
    
    /**
     * 現在のカウンタ値を返します。
     * @return カウンタ値
     */
    public int getCount() {
        return this.count.get();
    }
    
    /**
     * カウントアップします。
     * @return カウンタ値
     */
    public int count() {
        return this.count.incrementAndGet();
    }
    
    /**
     * エラー数をカウントアップします。
     * @return エラーカウンタ値
     */
    public int countError() {
        return this.eCount.incrementAndGet();
    }
    
    @Override
    public String toString() {
        return "Success:" + this.count.get() + "/ Error:" + this.eCount.get();
    }
}
