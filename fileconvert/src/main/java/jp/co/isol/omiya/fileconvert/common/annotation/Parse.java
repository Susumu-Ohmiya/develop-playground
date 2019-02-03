package jp.co.isol.omiya.fileconvert.common.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jp.co.isol.omiya.fileconvert.common.component.Parser;

/**
 * 固定長データ項目の変換指定注釈型
 * 
 * @author susumu.omiya
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Parse {
    /** ソース文字列から変換を行うクラス */
    Class<? extends Parser<?>> parser();
    /** 変換クラスに渡すパラメーター */
    String[] parameter() default {};
}
