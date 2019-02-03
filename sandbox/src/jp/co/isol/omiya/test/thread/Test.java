package jp.co.isol.omiya.test.thread;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    
    public static void main (String[] args) {
        
        Console.log("START single");
        single();
        Console.log(" END  single");
        System.out.println("---------------------------------------------");
        Console.log("START multi1");
        multi1();
        Console.log(" END  multi1");
        System.out.println("---------------------------------------------");
        Console.log("START multi by kimura");
        multiByKimura();
        Console.log(" END  multi by kimura");

    }

    /**
     * シングルスレッド実装
     */
    private static void single() {
        List<Model> l = new ArrayList<Model>();
        for (int i = 0; i < 100; ++i) {
            l.add(new Model());
        }
        
        for (Model model : l) {
            Console.log("Process : " + model.process());
        }
    }
    
    /**
     * マルチスレッド実装
     */
    private static void multi1() {
        Ch c = new Ch();
        for (int i = 0; i < 100; ++i) {
            c.put(new Model());
        }
        
        c.start();
        c.stop();
    }
    
    
    /** Model格納のためのリスト */
    private static List<Model> l = new ArrayList<Model>();
    /** カウント用変数 */
    private static int count = 0;
    
    /**
     * マルチスレッド実装(木村さん)
     */
    private static void multiByKimura() {
        
        
        for (int i = 0; i < 100; ++i) {
            l.add(new Model());
        }
        
        ExecutorService pool = Executors.newFixedThreadPool(4);
        
        for(Model model : l) {
            pool.submit(new MultiTreadByKimura());                 
        }
        
        pool.shutdown();
        
        while(!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        
    }
    
    
    
    /**
     * スレッドの取りまとめクラス
     */
    public static class Ch {
        
        /** 起動するスレッド数 */
        private static final int THREAD_COUNT = 4;
        /** スレッドプール */
        private Th[] threadPool = new Th[THREAD_COUNT];
        /** スレッドで処理するモデルのキュー */
        private ConcurrentLinkedQueue<Model> que = new ConcurrentLinkedQueue<Model>();
        /** スレッド実行状態制御 */
        private boolean isRunning = false;

        /** キューにモデル */
        public synchronized void put(Model m) {
            que.add(m);
        }
        
        public synchronized Model get() {
            Model m = que.poll();
            notifyAll();
            return m;
        }
        public void start() {
            isRunning = true;
            for (int i = 0; i < THREAD_COUNT; i++) {
                threadPool[i] = new Th(this);
                threadPool[i].start();
            }
        }
        
        public void stop() {
            while(!que.isEmpty()) {
                continue;
            }
            isRunning = false;
            for (int i = 0; i < THREAD_COUNT; i++) {
                Console.log("JOIN-" + i);
                try {
                    threadPool[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Console.log("STOP" + i);
            }
        }
        
        public boolean isRunning() {
            return isRunning;
        }

    }
    
    public static class Th extends Thread {
 
        private Ch c;
        
        public Th(Ch c) {
            this.c = c;
        }
        
        @Override
        public void run() {
            while (c.isRunning()) {
                Model m;
                if ((m = c.get()) != null) {
                    Console.log("Process : " + m.process());
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public static class Model {
        
        private static int processCount = 0;
        
        private byte[] data = new byte[800];
        
        public Model() {
            try {
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                sr.nextBytes(data);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        
        public int process() {
            
            long ctm = System.currentTimeMillis();
            long digit = 0;
            for (byte b : data) {
                digit += ctm % ((Math.abs(b) == 0) ? 1 : Math.abs(b));
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            Console.log(BigInteger.valueOf(System.currentTimeMillis()).multiply(BigInteger.valueOf(digit)).toString());
            return ++processCount;
        }
    }
    
    public static class Console {
        public static void log(CharSequence msg) {
            System.out.println(String.format("%s %3s %s", SimpleDateFormat.getDateTimeInstance().format(new Date()), Thread.currentThread().getId(), msg));
        }
    }
    
    /**
     * スレッド処理の内容が書かれたクラス
     */
    public static class MultiTreadByKimura extends Thread {
        @Override
        public void run() {
            Console.log("Process : " + l.get(count).process());
            count++;
        }
    }

}
