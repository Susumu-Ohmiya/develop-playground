/**
 * 
 */
package jp.co.isol.omiya.fileconvert.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 固定長データ項目の開始・終了位置
 * 
 * @author susumu.omiya
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Position {
    /** レコード内での開始位置 */
    int start();
    /** レコード内での終了位置 */
    int end();
}
