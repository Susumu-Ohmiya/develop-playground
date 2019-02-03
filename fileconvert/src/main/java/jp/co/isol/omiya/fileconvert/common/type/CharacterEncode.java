package jp.co.isol.omiya.fileconvert.common.type;

import java.nio.charset.Charset;

/**
 * 文字コードの列挙型
 * 
 * @author susumu.omiya
 *
 */
public enum CharacterEncode {

    UTF_8("0", Charset.forName("UTF-8")),
    Shift_JIS("1", Charset.forName("Shift_JIS")),
    EUC_JP("2", Charset.forName("EUC-JP"));
    
    private Charset charset;
    private String code;
    
    CharacterEncode(String code,Charset charset) {
        this.code = code;
        this.charset = charset;
    }
    
    public Charset charset() {
        return charset;
    }
    
    public static CharacterEncode of(String code) {
        if (code == null) {
            return null;
        }
        for (CharacterEncode r : CharacterEncode.values()) {
            if (r.code.equals(code)) {
                return r;
            }
        }
        return null;
    }

}
