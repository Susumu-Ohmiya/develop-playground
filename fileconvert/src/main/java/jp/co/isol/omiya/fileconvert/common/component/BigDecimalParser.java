package jp.co.isol.omiya.fileconvert.common.component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;

/**
 * BigDecimal型への変換実装クラス
 * 
 * @author susumu.omiya
 *
 */
public class BigDecimalParser implements Parser<BigDecimal> {

    /**
     * TODO:コメント書く
     * 
     * @see Parser#parse(String)
     */
    @Override
    public BigDecimal parse(String s) throws ParseException {
        return parse(s, (MathContext) null);
    }

    /**
     * TODO:コメント書く
     * 
     * @see Parser#parse(String, String[])
     */
    @Override
    public BigDecimal parse(String s, String[] parameter) throws ParseException {
        if (parameter != null && parameter.length != 0) {
            return parse(s, new MathContext(parameter[0]));
        } else {
            return parse(s, (MathContext) null);
        }
    }
    
    /**
     * TODO:コメント書く
     */
    public BigDecimal parse(String s, MathContext mc) throws ParseException {
        try {
            if (mc == null) {
                return new BigDecimal(s);
            } else {
                return new BigDecimal(s, mc);
            }
        } catch (RuntimeException e) {
            throw new ParseException(e.getMessage(),0);
        }
    }

    /**
     * TODO:コメント書く
     * 
     * @see Parser#format(BigDecimal)
     */
    @Override
    public String format(BigDecimal data) {
        return data.toPlainString();
    }

    /**
     * TODO:コメント書く
     * 
     * @see Parser#format(BigDecimal, String[])
     */
    @Override
    public String format(BigDecimal data, String[] parameter) {
        return data.toPlainString();
    }

}
