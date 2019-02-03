package jp.co.isol.omiya.fileconvert.common.component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import jp.co.isol.omiya.fileconvert.common.AbstractEntity;
import jp.co.isol.omiya.fileconvert.common.annotation.ByteLength;
import jp.co.isol.omiya.fileconvert.common.annotation.Format;
import jp.co.isol.omiya.fileconvert.common.annotation.Label;
import jp.co.isol.omiya.fileconvert.common.annotation.Order;
import jp.co.isol.omiya.fileconvert.common.annotation.Position;
import jp.co.isol.omiya.fileconvert.common.annotation.Required;
import jp.co.isol.omiya.fileconvert.common.exception.ApplicationRuntimeException;
import jp.co.isol.omiya.fileconvert.common.annotation.Parse;

/**
 * データレコードエンティティの項目定義を保持するクラス
 * 
 * @author susumu.omiya
 *
 */
public class FieldInfo implements Comparable<FieldInfo> {
    
    /** フィールドの出現順 */
    private int order;
    /** 固定長レコードにおける開始インデックス */
    private int startIndex;
    /** 固定長レコードにおける終了インデックス */
    private int endIndex;
    /** フィールドが必須か否か */
    private boolean isRequired;
    /** フィールドの書式（正規表現） */
    private Pattern pattern;
    /** フィールド長（Byte単位） */
    private int byteLength;
    /** フィールドのgetterメソッド */
    private Method readMethod;
    /** フィールドのsetterメソッド */
    private Method writeMethod;
    /** フィールドのデータ型 */
    private Class<?> fieldDataType;
    /** 項目のParserオブジェクト */
    private Parser<?> fieldParser;
    /** 項目のParserに渡すOption */
    private String[] parserOptions;
    /** 項目名等、項目に付与されたラベル */
    private String label;
    
    /**
     * @return フィールドの出現順
     */
    public int getOrder() {
        return order;
    }
    /**
     * @param order フィールドの出現順
     */
    public void setOrder(int order) {
        this.order = order;
    }
    /**
     * @return 固定長レコードにおける開始インデックス
     */
    public int getStartIndex() {
        return startIndex;
    }
    /**
     * @param startIndex 固定長レコードにおける開始インデックス
     */
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
    /**
     * @return 固定長レコードにおける終了インデックス
     */
    public int getEndIndex() {
        return endIndex;
    }
    /**
     * @param endIndex 固定長レコードにおける終了インデックス
     */
    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
    /**
     * @return フィールドが必須か否か
     */
    public boolean isRequired() {
        return isRequired;
    }
    /**
     * @param isRequired フィールドが必須か否か
     */
    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }
    /**
     * @return フィールドの書式（正規表現）
     */
    public Pattern getPattern() {
        return pattern;
    }
    /**
     * @param pattern フィールドの書式（正規表現）
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
    /**
     * @return フィールド長（Byte単位）
     */
    public int getByteLength() {
        return byteLength;
    }
    /**
     * @param byteLength フィールド長（Byte単位）
     */
    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }
    /**
     * @return readMethod フィールドのgetterメソッド
     */
    public Method getReadMethod() {
        return readMethod;
    }
    /**
     * @param readMethod フィールドのgetterメソッド
     */
    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }
    /**
     * @return フィールドのsetterメソッド
     */
    public Method getWriteMethod() {
        return writeMethod;
    }
    /**
     * @param writeMethod フィールドのsetterメソッド
     */
    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }
    
    /**
     * @return フィールドのデータ型
     */
    public Class<?> getFieldDataType() {
        return fieldDataType;
    }
    /**
     * @param fieldDataType フィールドのデータ型
     */
    public void setFieldDataType(Class<?> fieldDataType) {
        this.fieldDataType = fieldDataType;
    }
    /**
     * @return 項目のParserオブジェクト
     */
    public Parser<?> getFieldParser() {
        return fieldParser;
    }
    /**
     * @param fieldParser 項目のParserオブジェクト
     */
    public void setFieldParser(Parser<?> fieldParser) {
        this.fieldParser = fieldParser;
    }
    /**
     * @return 項目のParserに渡すOption
     */
    public String[] getParserOptions() {
        return parserOptions;
    }
    /**
     * @param parserOptions 項目のParserに渡すOption
     */
    public void setParserOptions(String[] parserOptions) {
        this.parserOptions = parserOptions;
    }
    /**
     * @return 項目名等、項目に付与されたラベル
     */
    public String getLabel() {
        return label;
    }
    /**
     * @param label セットする 項目名等、項目に付与されたラベル
     */
    public void setLabel(String label) {
        this.label = label;
    }
    /**
     * 対象クラスのフィールドについて FieldInfo のリストを作成する。
     * ※ソートはOrder注釈の順になります
     * @param clazz 対象クラス
     * @return FieldInfo のリスト
     */
    public static List<FieldInfo> getFieldInfomations(Class<? extends AbstractEntity> clazz) {

        /*-------------------------------------------*
         * フィールドのリストを作成する。
         * 親クラスが存在する場合は１段階遡る。
         *-------------------------------------------*/
        Field[] targetField = clazz.getDeclaredFields();
        List<Field> fieldList = new ArrayList<Field>();
        for (Class<?> superClass = clazz.getSuperclass(); superClass != null; superClass = superClass
                .getSuperclass()) {
            fieldList.addAll(Arrays.asList(superClass.getDeclaredFields()));
        }
        if (!fieldList.isEmpty()) {
            fieldList.addAll(Arrays.asList(targetField));
            targetField = fieldList.toArray(targetField);
        }
        
        List<FieldInfo> l = new ArrayList<FieldInfo>();
        
        for (Field f : targetField) {
            String targetFieldName = f.getName();
            FieldInfo fieldInfo = new FieldInfo();
            
            /*------------------------------------------------------------------*
             * フィールドのgetter,setterを取得する 取得できない場合は読み飛ばす
             *------------------------------------------------------------------*/
            try {
                PropertyDescriptor targetProp = new PropertyDescriptor(targetFieldName, clazz);
                if ((targetProp.getWriteMethod() == null) || (targetProp.getReadMethod() == null)) {
                    continue;
                }
                fieldInfo.setReadMethod(targetProp.getReadMethod());
                fieldInfo.setWriteMethod(targetProp.getWriteMethod());
                fieldInfo.setFieldDataType(targetProp.getPropertyType());
                
            } catch (@SuppressWarnings("unused") IntrospectionException e) {
                continue;
            }

            /*----------------------------------------------*
             * フィールドのannotationから定義情報を取得する
             *----------------------------------------------*/
            // 順序
            Order order = f.getAnnotation(Order.class);
            if (order == null) {
                continue;
            }
            fieldInfo.setOrder(order.value());
            // レコード文字列内での位置（開始、終了）
            Position position = f.getAnnotation(Position.class);
            if (position == null) {
                continue;
            }
            fieldInfo.setStartIndex(position.start());
            fieldInfo.setEndIndex(position.end());
            // バイト長
            ByteLength length = f.getAnnotation(ByteLength.class);
            if (length != null) {
                fieldInfo.setByteLength(length.value());
            }
            // 必須項目チェック対象
            Required required = f.getAnnotation(Required.class);
            if (required != null) {
                fieldInfo.setRequired(required.value());
            } else {
                fieldInfo.setRequired(false);
            }
            // 書式チェック対象
            Format format = f.getAnnotation(Format.class);
            if (format != null) {
                fieldInfo.setPattern(format.value().getPattern());
            }
            // データパース(注釈型が省略された場合文字列型として扱う)
            Parse parse = f.getAnnotation(Parse.class);
            if (parse != null) {
                try {
                    // 対象のデータ型に変換するクラス
                    Class<?> parserClass = parse.parser();
                    // 変換時に指定するパラメーター
                    String[] parserOptions = parse.parameter();
                    if (!parserClass.getMethod("parse", String.class).getReturnType().equals(fieldInfo.getFieldDataType())) {
                        // Parserが応答するデータ型と対象のデータ型が一致しない場合エラー
                        throw new IllegalArgumentException("ParserType / DataType unmatch:" + f.getName());
                    }
                    fieldInfo.setFieldParser((Parser<?>) parserClass.newInstance());
                    fieldInfo.setParserOptions(parserOptions);
                } catch (SecurityException e) {
                    throw new ApplicationRuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new ApplicationRuntimeException(e);
                } catch (InstantiationException e) {
                    throw new ApplicationRuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new ApplicationRuntimeException(e);
                }
            } else {
                // データ型のannotationが指定されていない場合は文字列(トリム有り)として処理する
                if (!f.getType().equals(String.class)) {
                    throw new IllegalArgumentException(f.getName() + " is not String Field.");
                }
                fieldInfo.setFieldParser(new DefaultStringParser());
                fieldInfo.setParserOptions(new String[]{"true"});
            }
            // ラベル
            Label label = f.getAnnotation(Label.class);
            if (label == null) {
                fieldInfo.setLabel(targetFieldName);
            } else {
                fieldInfo.setLabel(label.value());
            }
            
            l.add(fieldInfo);
        }
        
        Collections.sort(l);
        
        return l;
    }

    @Override
    public String toString() {
        
        StringBuffer sb = new StringBuffer();
        sb.append("Order[").append(order).append("] ");
        sb.append("Start[").append(startIndex).append("] ");
        sb.append("End[").append(endIndex).append("] ");
        sb.append("Type[").append(fieldDataType.getSimpleName()).append("] ");
        sb.append("Parser[").append(fieldParser.getClass().getSimpleName()).append("] ");
        sb.append("ParserOptions[").append(Arrays.toString(parserOptions)).append("] ");
        sb.append("Required[").append(isRequired).append("] ");
        sb.append("Length[").append(byteLength).append("] ");
        sb.append("Pattern[").append(pattern).append("] ");
        sb.append("Getter[").append(readMethod.getName()).append("] ");
        sb.append("Setter[").append(writeMethod.getName()).append("] ");
        
        return sb.toString();
        
    }
    @Override
    public int compareTo(FieldInfo anatoherFieldInfo) {
        int thisVal = this.order;
        int anotherVal = anatoherFieldInfo.order;
        return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
    }
}