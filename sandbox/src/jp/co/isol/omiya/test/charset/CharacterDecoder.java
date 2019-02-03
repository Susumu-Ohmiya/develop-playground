package jp.co.isol.omiya.test.charset;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 *
 */
public class CharacterDecoder {

    private CharasetEncoding encodeing;
    private CharasetEncoding encodeingAlt;
    private Charset cs;
    
    /** 定数：ライン・フォワード */
    private static String LF = "\n";
    /** 定数：キャリッジ・リターン */
    private static String CR = "\r";
    /** 定数：CRLF（Windows改行） */
    private static String CRLF = "\r\n";
    
    /** 指定エンコーディングにおけるキャリッジ・リターンのバイト表現 */
    private byte[] cr;
    /** 指定エンコーディングにおけるライン・フォワードのバイト表現 */
    private byte[] lf;
    /** 指定エンコーディングにおけるWindows改行のバイト表現 */
    private byte[] crlf;

    /**
     * 文字デコーダを構築します。
     * 
     * @param encodeName エンコード名
     */
    public CharacterDecoder(String encodeName) {
        
        cs = Charset.forName(encodeName);
        
        this.lf = LF.getBytes(cs);
        this.cr = CR.getBytes(cs);
        this.crlf = CRLF.getBytes(cs);
        
        this.encodeing = new CharasetEncoding(encodeName, true);
        this.encodeingAlt = new CharasetEncoding(encodeName, "?", "?".getBytes(cs));

    }
    
    /**
     * バイト配列を指定の文字エンコーディングに従って文字列にデコードします。
     * 
     * @param target
     * @return
     */
    public String decode(byte[] target) {
        String result;
        try {
            result = this.encodeing.decode(target);
        } catch (CharacterCodingException e) {
            
            // TODO:エラーログを出力
            
            try {
                result = this.encodeingAlt.decode(target);
            } catch (@SuppressWarnings("unused") CharacterCodingException ex) {
                // 代替指定エンコーダでは発生し得ない。
                result = new String(target, cs);
            }
        }
        return result;
    }
    
    
    
    /**
     * 文字列をバイト表現にエンコードします。
     * 
     * @param target エンコード対象文字列
     * @return エンコードされたバイト表現
     */
    public byte[] encode(String target) {
        byte[] result;
        try {
            result = this.encodeing.encode(target);
        } catch (CharacterCodingException e) {
            
            // TODO:エラーログを出力
            
            try {
                result = this.encodeingAlt.encode(target);
            } catch (@SuppressWarnings("unused") CharacterCodingException ex) {
                // 代替指定エンコーダでは発生し得ない。
                result = target.getBytes(cs);
            }
        }
        return result;
    }

    /**
     * この文字エンコーディングにおけるキャリッジリターン(CR)のバイト表現を返します。
     * 
     * @return CR のバイト表現
     */
    public byte[] cr(){
        return cr;
    }
    
    /**
     * この文字エンコーディングにおけるラインフォワード(LF)のバイト表現を返します。
     * 
     * @return LF のバイト表現
     */
    public byte[] lf(){
        return lf;
    }
    
    /**
     * この文字エンコーディングにおけるWindows改行(CRLF)のバイト表現を返します。
     * 
     * @return CRLF のバイト表現
     */
    public byte[] crlf(){
        return crlf;
    }
}
