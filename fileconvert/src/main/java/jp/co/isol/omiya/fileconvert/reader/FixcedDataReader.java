package jp.co.isol.omiya.fileconvert.reader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import jp.co.isol.omiya.fileconvert.common.AbstractEntity;
import jp.co.isol.omiya.fileconvert.common.component.FieldInfo;
import jp.co.isol.omiya.fileconvert.common.component.FileInfo;
import jp.co.isol.omiya.fileconvert.common.component.Parser;
import jp.co.isol.omiya.fileconvert.common.exception.RecordInvalidException;
import jp.co.isol.omiya.fileconvert.common.exception.ApplicationRuntimeException;
import jp.co.isol.omiya.fileconvert.common.exception.ErrorInfo;
import jp.co.isol.omiya.fileconvert.common.exception.Errors;
import jp.co.isol.omiya.fileconvert.common.type.CharacterEncode;

/**
 * 固定長テキストデータの読み込み実装クラス
 * 
 * @author susumu.omiya
 * 
 */
public class FixcedDataReader<T extends AbstractEntity> {
    
    private FileInfo fileInfo;
    private List<FieldInfo> fieldInfomations;
    
    /**
     * TODO:コメント書く
     */
    public FixcedDataReader(FileInfo info) {
        this.fileInfo = info;
        fieldInfomations = FieldInfo.getFieldInfomations(info.getRecordType().getEntityClass());
    }
    
    /**
     * TODO:コメント書く
     */
    public boolean ready() throws IOException {
        return (fileInfo.getStream() != null && fileInfo.getStream().ready());
    }
    
    /**
     * TODO:コメント書く
     * @throws RecordInvalidException 
     */
    public T readRecord(Class<T> clazz) throws RecordInvalidException {
        try {
            return readRecord(clazz.newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * TODO:コメント書く
     * @throws RecordInvalidException 
     */
    public T readRecord(T entity) throws RecordInvalidException {
        Errors errors = new Errors();
        try {
            int recordLength = fileInfo.getRecordType().getRecordLength();
            String line = null;
            if ((line = fileInfo.getStream().readLine()) != null ) {
                // レコードはUTF-8で処理する
                byte[] b = line.getBytes(CharacterEncode.UTF_8.charset());
                if (b.length != recordLength) {
                    errors.add(new ErrorInfo("レコード長が不正です。",0,null,"RECORD_LENGTH"));
                }
                for (FieldInfo f : fieldInfomations) {
                    byte[] target = Arrays.copyOfRange(b, f.getStartIndex(), f.getEndIndex() + 1);
                    String s = new String(target, CharacterEncode.UTF_8.charset());
                    
                    // 必須チェック
                    if (f.isRequired()) {
                        if (s.trim().isEmpty()) {
                            errors.add(new ErrorInfo("必須エラー",0,null,f.getLabel()));
                        }
                    }
                    // 書式が設定されている場合、バリデーションを行う。
                    if (f.getPattern() != null) {
                        if (!validate(s.trim(), f.getPattern())) {
                            errors.add(new ErrorInfo("書式エラー",0,null,f.getLabel()));
                        }
                    }
                    
                    // 値をEntityにセットする
                    f.getWriteMethod().invoke(entity, f.getFieldDataType().cast(parseValue(s, f.getFieldParser(), f.getParserOptions()))); 
                    
                }
            } else {
                // 既にストリームが終端に達していた場合、空エンティティを返す
                return entity;
            }
        } catch (IllegalArgumentException e) {
            throw new ApplicationRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new ApplicationRuntimeException(e);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new ApplicationRuntimeException(e);
        } catch (ParseException e) {
            throw new ApplicationRuntimeException(e);
        } catch (SecurityException e) {
            throw new ApplicationRuntimeException(e);
        }
        
        if (errors.hasErrorInfo()) {
            throw new RecordInvalidException("Some error has occurred.", errors, null);
        }
        
        return entity;
    }
    
    private Object parseValue(String source, Parser<?> parser, String[] options) throws ParseException {
        if (options != null && options.length != 0) {
            return parser.parse(source, options);
        } else {
            return parser.parse(source);
        }
    }
    
    private boolean validate(String target, Pattern pattern) {
        return pattern.matcher(target).matches();
    }
}
