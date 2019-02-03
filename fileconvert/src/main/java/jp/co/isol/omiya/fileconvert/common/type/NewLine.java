package jp.co.isol.omiya.fileconvert.common.type;

/**
 * 改行コードの列挙型
 * 
 */
public enum NewLine {

    CR("CR", "\n"),
    LF("LF", "\r"),
    CRLF("CRLF", "\n\r"),
    UNDEFINED("","");
    
    private String charCode;
    private String defineCode;
    
    NewLine(String code,String charCode) {
        this.defineCode = code;
        this.charCode = charCode;
    }
    
    public String charCode() {
        return charCode;
    }
    
    public static NewLine of(String code) {
        if (code == null || code.length() == 0) {
            return UNDEFINED;
        }
        for (NewLine r : NewLine.values()) {
            if (r.defineCode.equals(code)) {
                return r;
            }
        }
        return null;
    }

}
