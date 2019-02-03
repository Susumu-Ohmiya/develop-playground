package jp.co.isol.omiya.fileconvert.common.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * TODO:コメント書く
 */
public class Objects {

    /**
     * TODO:コメント書く
     */
    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }

    /**
     * TODO:コメント書く
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }

    
    /**
     * Beanオブジェクトを文字列表現します。</br>
     * 
     * @param obj
     *            出力するオブジェクト
     */
    public static String objectFieldToString(Object obj) {

        if (obj == null) {
            return "<null>";
        }

        return listMember(obj, 2);

    }
    
    /**
     * オブジェクトを文字列表現します。
     * 
     * @param obj
     *            メンバ変数をリストアップするオブジェクト
     * @param recursionCount
     *            再帰回数
     * @return リストアップされた文字列
     */
    private static String listMember(Object obj, int recursionCount) {
        List<String> l = new ArrayList<String>();

        Class<?> targetClass = obj.getClass();
        Field[] targetField = obj.getClass().getDeclaredFields();
        List<Field> fieldList = new ArrayList<Field>();
        for (Class<?> superClass = obj.getClass().getSuperclass(); superClass != null; superClass = superClass
                .getSuperclass()) {
            fieldList.addAll(Arrays.asList(superClass.getDeclaredFields()));
        }
        if (!fieldList.isEmpty()) {
            fieldList.addAll(Arrays.asList(targetField));
            targetField = fieldList.toArray(targetField);
        }

        for (Field f : targetField) {
            String targetFieldName = f.getName();

            Method targetGetter = null;
            Class<?> targetType = null;
            try {
                PropertyDescriptor targetProp = new PropertyDescriptor(targetFieldName, targetClass);
                targetGetter = targetProp.getReadMethod();
                targetType = targetProp.getPropertyType();
            } catch (@SuppressWarnings("unused") IntrospectionException e) {
                continue;
            }

            try {
                Object targetData = targetGetter.invoke(obj);
                if (targetData == null) {
                    l.add(targetFieldName + " = <null>");
                    continue;
                }
                if (targetType.isPrimitive() || targetType.equals(String.class) || targetType.isArray()) {
                    l.add(targetFieldName + " = " + targetData);
                    continue;
                }
                if (targetType.equals(BigDecimal.class)) {
                    l.add(targetFieldName + " = " + ((BigDecimal) targetData).toPlainString());
                    continue;
                }
                if (targetType.equals(Number.class)) {
                    l.add(targetFieldName + " = " + targetData.toString());
                    continue;
                }
                if (targetType.equals(Date.class)) {
                    l.add(targetFieldName + " = "
                            + (new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")).format(targetData));
                    continue;
                }
                if (targetType.isEnum()) {
                    l.add(targetFieldName + " = " + ((Enum<?>) targetData).name());
                    continue;
                }
                if (targetData instanceof AbstractCollection) {
                    l.add(targetFieldName + " = [" + targetData.toString() + "]"); 
                    continue;
                }
                if (recursionCount > 0) {
                    l.add(targetFieldName + " [" + listMember(targetData, (recursionCount - 1)) + "]");
                } else {
                    l.add(targetFieldName + " [" + targetData.toString() + "]");
                }

            } catch (@SuppressWarnings("unused") Exception e) {
                continue;
            }
        }
        return StringUtils.join(",", l);
    }
    

}
