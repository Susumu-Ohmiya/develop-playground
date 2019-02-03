package jp.co.isol.omiya.test.counter;


public class ProgressCounter {

    public static void main(String[] args) {

        // 総件数
        long total = 1300002;
        
        // カウンタをインスタンス化
        ProgressCounter counter = new ProgressCounter(total);
        
        // 経過時間計測用
        long start = System.currentTimeMillis();
        
        // ループしてカウント
        for (int i = 0; i < total; i++) {
            counter.progressCount(i);
        }
        
        System.out.println("Cost:" + (System.currentTimeMillis() - start) + "[ms]" );
    }

    /** 直近のカウンタ出力値 */
    private long counted = -1;
    /** 出力する単位 */
    private int scale;
    /** 総件数 */
    private long total;
    
    /**
     * コンストラクタ</br>
     * 5%刻みで出力を行います。
     * 
     * @param total 対象の総件数
     */
    public ProgressCounter(long total) {
        this(total, 5);
    }
    
    /**
     * コンストラクタ
     * @param total 対象の総件数
     * @param scale ログ出力を行う単位[%]
     */
    public ProgressCounter(long total, int scale) {
        this.total = total;
        this.scale = scale;
    }
    
    
    /**
     * プログレスカウントを行います。指定された単位でログに出力を行います。
     * @param current 現在の件数
     */
    public void progressCount(long current) {
        
        if (total == 0) {
            log(100);
            counted = 100;
            return;
        }
        
        int progressRate = (int) Math.ceil((double) current / total * 100);
        if (progressRate > counted && (progressRate % scale == 0)) {
            log(progressRate);
            counted = progressRate;
        }
    }
    
    /**
     * カウンタをリセットします。
     */
    public void reset() {
        counted = -1;
    }
    
    /**
     * ログ出力用メソッド。いい塩梅に書き直してください。
     * @param rate 出力する値
     */
    private void log(int rate) {
        String dispRate = String.format("%3d", Integer.valueOf(rate));
        System.out.println(dispRate);
    }

}
