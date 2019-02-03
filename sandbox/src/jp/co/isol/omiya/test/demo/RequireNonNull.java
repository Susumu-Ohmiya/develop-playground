package jp.co.isol.omiya.test.demo;

public class RequireNonNull {

    public static void main(String[] args) {
        
        // NullPointerExceptionを出力する
        try {
            requireNonNull(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 通過する
        try {
            requireNonNull("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void requireNonNull(Object target) {
        
    }
}
