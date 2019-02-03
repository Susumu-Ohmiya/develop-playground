package jp.co.isol.omiya.fileconvert.common.component;

import java.text.ParseException;

/**
 * 文字列より対象データ型に変換するParserのインタフェース
 * @param <T> 変換先のデータ型
 * 
 * @author susumu.omiya
 *
 */
public interface Parser<T> {

    /**
     * ソース文字列を対象データ型に変換する<br/>
     * 
     * @param source ソース文字列
     * @return 変換されたオブジェクト
     * @throws ParseException 変換に失敗した場合に投げられる
     */
    public T parse(String source) throws ParseException;
    
    /**
     * ソース文字列を対象データ型に変換する。<br/>
     * 変換に必要なパラメータは第二引数で指定する。<br/>
     * パラメータの解釈は実装に依存する。<br/>
     * 
     * @param source ソース文字列
     * @param parameter 変換時に必要なオプションの文字列
     * @return 変換されたオブジェクト
     * @throws ParseException 変換に失敗した場合に投げられる
     */
    public T parse(String s, String[] parameter) throws ParseException;
    
    /**
     * TODO:コメント書く
     */
    public String format(T data);
    
    /**
     * TODO:コメント書く
     */
    public String format(T data,  String[] parameter);

}
