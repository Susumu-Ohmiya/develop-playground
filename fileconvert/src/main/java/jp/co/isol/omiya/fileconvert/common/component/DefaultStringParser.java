package jp.co.isol.omiya.fileconvert.common.component;

import java.text.ParseException;

/**
 * デフォルト変換実装クラス（文字列を文字列に移送）
 * 
 * @author susumu.omiya
 *
 */
public class DefaultStringParser implements Parser<String> {

    @Override
    public String parse(String s) throws ParseException {
        return parse(s,true);
    }

    @Override
    public String parse(String s, String[] options) throws ParseException {
        boolean trim = false;
        if (options != null && options.length != 0) {
            if (options[0].toLowerCase().equals("true")) {
                trim = true;
            }
        }
        return parse(s,trim);
    }
    
    private String parse(String s, boolean doTrim) {
        if (s == null) {
            throw new NullPointerException();
        }
        if (doTrim) {
            return s.trim();
        } else {
            return s;
        }
    }

    @Override
    public String format(String data) {
        return data;
    }

    @Override
    public String format(String data, String[] parameter) {
        return data;
    }

}
