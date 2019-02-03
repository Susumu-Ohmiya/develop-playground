package jp.co.ks.concurrent.test.request;

import static jp.co.ks.kscommon.concurrent.util.ThreadUtils.*;

import java.io.IOException;

import jp.co.ks.concurrent.test.entity.Counter;
import jp.co.ks.concurrent.test.entity.Data;
import jp.co.ks.kscommon.concurrent.task.Task;

/**
 * @author susumu.omiya
 *
 */
public class DataTask implements Task {

    public static final int NO_ERROR = 0;
    public static final int CHECKED_ERROR = 1;
    public static final int UNCHECK_ERROR = 2;
    public static final int TIME_OUT = 3;
    
    
	private Data data;
	private int errorType;

	/**
	 * @param data
	 * @param isError 
	 */
	public DataTask(Data data, int errorType) {
		this.data = data;
		this.errorType = errorType;
	}

	@Override
	public void process() {
		sleep(10);
		
		try {
            if (errorType == CHECKED_ERROR) {
                log("CHECKED ERROR!!");
                Counter.getInstance().countError();
                throw new IOException(Thread.currentThread().getName());
            } else if (errorType == UNCHECK_ERROR) {
                log("UNCHECKED ERROR!!");
                Counter.getInstance().countError();
                throw new RuntimeException(Thread.currentThread().getName());
            } else if (errorType == TIME_OUT) {
                log("TIME OUT!!");
                sleep(100000,true);
            }
            log(String.format("%8d", Counter.getInstance().count()) + " : " + this.data.getStr());
        } catch (IOException e) {
            log(e);
        }
	}

}
