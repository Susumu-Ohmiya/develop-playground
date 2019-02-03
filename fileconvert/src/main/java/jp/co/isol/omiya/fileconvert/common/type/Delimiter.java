package jp.co.isol.omiya.fileconvert.common.type;

/**
 * デリミタの列挙型
 * 
 * @author susumu.omiya
 *
 */
public enum Delimiter {

    COMMA("1",","),
    TAB("2","\t");
    
    String code;
    String delimiter;
    
    Delimiter(String code, String delimiter) {
        this.code = code;
        this.delimiter = delimiter;
    }
    
    public String value() {
        return delimiter;
    }
    
    public static Delimiter of(String code) {
        if (code == null) {
            return null;
        }
        for (Delimiter d : Delimiter.values()) {
            if (d.code.equals(code)) {
                return d;
            }
        }
        return null;
    }

}
