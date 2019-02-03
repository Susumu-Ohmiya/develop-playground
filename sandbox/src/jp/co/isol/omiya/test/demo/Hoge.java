package jp.co.isol.omiya.test.demo;

public class Hoge {

	public static void main(String[] args) {
	    try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	
	private static void process() throws HogeException {
        try {
            throw new Exception("main");
        } catch (Exception e) {
            throw new HogeException("catch");
        } finally {
            try {
                if (true) {
                    throw new Exception("fin");
                }
            } catch (Exception e) {
                throw new HogeException("finfin");
            }
        }
	}
	
	private static class HogeException extends Exception {

	    private static final long serialVersionUID = 1L;

        public HogeException(String msg) {
	        super(msg);
	    }
	}
}
