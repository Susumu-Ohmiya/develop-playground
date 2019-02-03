package jp.co.isol.omiya.test.charset;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

/**
 * 文字エンコーディングを保持するクラス。</br>
 * </br>
 * 文字エンコーディング名を指定して、Stringとbyte配列の変換を行なう。</br>
 * また、指定したエンコーディングにおける改行コードの値を取得できる。</br>
 * 
 * @see java.nio.charset.Charset
 *
 */
public class CharasetEncoding {

    /** インスタンス化時に使用されたエンコード名 */
    private String encodeName;
    /** 処理時に使用するCharsetオブジェクト */
    private Charset charset;
    /** 指定エンコーディングにおけるデコーダ */
    private CharsetDecoder decoder;
    /** 指定エンコーディングにおけるエンコーダ */
    private CharsetEncoder encoder;
    
    
    /**
     * 文字エンコーディングオブジェクトを構築します。
     * このコンストラクタを使用した場合、EncodeおよびDecodeのエラーは無視されます。
     * 
     * @param encodeName エンコード名
     * @throws IllegalArgumentException エンコード名が不正・null・サポート外の場合
     */
    public CharasetEncoding(String encodeName) throws IllegalArgumentException {
        this(encodeName,false);
    }
    
    /**
     * 文字エンコーディングオブジェクトを構築します。
     * 
     * @param encodeName エンコード名
     * @param isReport EncodeおよびDecodeのエラー時に報告を行なうか否か。
     * @throws IllegalArgumentException エンコード名が不正・null・サポート外の場合
     */
    public CharasetEncoding(String encodeName, boolean isReport) throws IllegalArgumentException {
        
        this.charset = Charset.forName(encodeName);
        
        CodingErrorAction action = (isReport) ? CodingErrorAction.REPORT : CodingErrorAction.IGNORE;
        
        // 定数を初期化
        this.encodeName = encodeName;
        
        // デコーダを初期化
        decoder = charset.newDecoder();
        decoder.onMalformedInput(action);
        decoder.onUnmappableCharacter(action);
        
        // エンコーダを初期化
        encoder = charset.newEncoder();
        encoder.onMalformedInput(action);
        encoder.onUnmappableCharacter(action);
        
        
    }
    
    /**
     * 文字エンコーディングオブジェクトを構築します。
     * 
     * @param encodeName エンコード名
     * @param alternateOnDecoderUnmappable デコード時にマッピングできない値が来た際の代替文字。nullの場合、デコード時のエラーは無視する。
     * @param alternateOnEncoderUnmappable エンコード時にマッピングできない値が来た場合の代替値。nullの場合、エンコード時のエラーは無視する。
     * @throws IllegalArgumentException エンコード名が不正・null・サポート外の場合、代替文字、代替値が不正の場合
     */
    public CharasetEncoding(String encodeName, String alternateOnDecoderUnmappable, byte[] alternateOnEncoderUnmappable) throws IllegalArgumentException {
        this.charset = Charset.forName(encodeName);
        
        // 定数を初期化
        this.encodeName = encodeName;
        
        // デコーダを初期化
        decoder = charset.newDecoder();
        if (alternateOnDecoderUnmappable != null) {
            decoder.onMalformedInput(CodingErrorAction.REPLACE);
            decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            decoder.replaceWith(alternateOnDecoderUnmappable);
        } else {
            decoder.onMalformedInput(CodingErrorAction.IGNORE);
            decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
        }
        
        
        // エンコーダを初期化
        encoder = charset.newEncoder();
        if (alternateOnEncoderUnmappable != null && alternateOnEncoderUnmappable.length > 0) {
            encoder.onMalformedInput(CodingErrorAction.REPLACE);
            encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            encoder.replaceWith(alternateOnEncoderUnmappable);
        } else {
            encoder.onMalformedInput(CodingErrorAction.IGNORE);
            encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
        }
        
        
    }
    
    /**
     * 指定エンコードのバイト表現を文字列にデコードします。
     * 
     * @param target デコードを行うバイト配列
     * @return デコード結果文字列
     * @throws CharacterCodingException デコード時に例外が発生した場合
     */
    public String decode(byte[] target) throws CharacterCodingException {
        decoder.reset();
        CharBuffer cb = decoder.decode(ByteBuffer.wrap(target));
        return cb.toString();
    }
    
    /**
     * 文字列をバイト表現にエンコードします。
     * 
     * @param target エンコードを行う文字列
     * @return エンコード結果
     * @throws CharacterCodingException エンコード時に例外が発生した場合
     */
    public byte[] encode(String target) throws CharacterCodingException {
        encoder.reset();
        ByteBuffer bb = encoder.encode(CharBuffer.wrap(target));
        return Arrays.copyOfRange(bb.array(), 0, bb.limit());
    }
    
    /**
     * 文字コード変換を行なうための簡易メソッド。</br>
     * 入力をこのインスタンスに指定された文字エンコーディングに変換して出力ストリームに書き込みます。</br>
     * なお、このメソッド終了後にストリームはクローズされます。</br>
     * 
     * @param in 入力となるInputStreamReader.読み出し元を適切にエンコードして文字ストリーム化してください。
     * @param out 出力先のストリーム
     * @throws IOException 入出力例外発生時
     */
    public void translate(InputStreamReader in, OutputStream out) throws IOException {
        
        BufferedReader bufIn = null;
        BufferedWriter bufOut = null;
        try {
            bufIn = new BufferedReader(in);
            bufOut = new BufferedWriter(new OutputStreamWriter(out, this.getEncoder()));

            CharBuffer buf = CharBuffer.allocate(256);
            while(bufIn.read(buf) != -1){
                buf.flip();
                bufOut.write(buf.toString());
            }
            bufOut.flush();
            
        } finally {
            try {
                if (bufOut != null) {
                    bufOut.close();
                }
            } catch (@SuppressWarnings("unused") Exception e) {
                // do nothing
            }
            try {
                if (bufIn != null) {
                    bufIn.close();
                }
            } catch (@SuppressWarnings("unused") Exception e) {
                // do nothing
            }
        }
        
    }

    /**
     * インスタンス化時に指定された文字エンコーディング名称を返します。
     * 
     * @return エンコーディング名
     */
    public String getEncodeName() {
        return encodeName;
    }

    /**
     * この文字エンコーディングにおけるデコーダーを返します。
     * @return decoder バイトシーケンス → 文字 変換エンジン
     */
    public CharsetDecoder getDecoder() {
        return decoder;
    }

    /**
     * この文字エンコーディングにおけるデコーダーを返します。
     * @return encoder 文字 → バイトシーケンス 変換エンジン
     */
    public CharsetEncoder getEncoder() {
        return encoder;
    }

}
