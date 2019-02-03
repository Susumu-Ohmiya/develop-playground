package jp.co.isol.omiya.fileconvert.common.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 必須項目を示す注釈型
 * 
 * @author susumu.omiya
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Required {
    /** 必須チェックを行うか(デフォルト:true = 行う) */
    boolean value() default true;
}
