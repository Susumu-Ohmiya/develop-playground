package jp.co.isol.omiya.fileconvert.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.co.isol.omiya.fileconvert.common.type.FormatType;

/**
 * 固定長データ項目の書式
 * 
 * @author susumu.omiya
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Format {
    /** 書式:列挙型FormatTypeで指定 */
    FormatType value();
}
