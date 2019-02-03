package jp.co.isol.omiya.fileconvert.common.type;

import java.util.regex.Pattern;

/**
 * 対象の書式の列挙型
 * 
 * @author susumu.omiya
 *
 */
public enum FormatType {

    /** 半角数字 */
    HALF_NUMERIC("^\\d*$"),
    
    /** 全角 */
    FULL("^[^ -~｡-ﾟ]+$"),

    /** 日付形式 YYYY/MM/DD */
    SLASHED_YYYYMMDD("^\\d{4}/[012]\\d/[0123]\\d$");
    
    
    String pattern;
    
    FormatType(String pattern) {
        this.pattern = pattern;
    }
    
    public Pattern getPattern() {
        return Pattern.compile(pattern);
    }
}
