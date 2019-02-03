package jp.co.isol.omiya.fileconvert.common.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 項目名を示す注釈型
 * 
 * @author susumu.omiya
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Label {
    /** 項目名 */
    String value();
}
