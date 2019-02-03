package jp.co.isol.omiya.fileconvert.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 固定長データ項目の出現順序
 * 
 * @author susumu.omiya
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    /**
     * TODO:コメント書く
     */
    int value();
}
