package jp.co.isol.omiya.fileconvert.common.util;

/**
 * TODO:コメント書く
 */
public class StringUtils {
    
    /**
     * TODO:コメント書く
     */
    public static final String NEW_LINE = System.getProperty("line.separator");
    
    /**
     * TODO:コメント書く
     */
    public static String join(CharSequence delimiter, CharSequence... elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs: elements) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    /**
     * TODO:コメント書く
     */
    public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs: elements) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    /**
     * TODO:コメント書く
     */
    public static String concat(String... strings) {
        Objects.requireNonNull(strings);
        StringBuffer sb = new StringBuffer();
        for (String str : strings) {
            sb.append(str);
        }
        return sb.toString();
    }
}
