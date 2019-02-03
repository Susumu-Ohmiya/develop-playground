package jp.co.isol.omiya.test.copyobject;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Beanのデータコピーを行います。コピーのデータの紐づけは FieldName Annotationを使用します。
 */
public class Map2BeanUtil {
    
    /*----------------------------------------------------------------------------------------------------
     * テスト用コード
     *---------------------------------------------------------------------------------------------------*/
    
    /**
     * テスト用のメインメソッド
     * @param args 未使用
     */
    public static void main(String args[]) {
        
        // Bean の名前紐づけ移送を行うサンプル
        Map<String, Object> s = new HashMap<String, Object>();
        DestinationObject d = new DestinationObject();
        s.put("ITEM","HOGE");
        s.put("AMOUNT", "3000");
        copy(s, d, null, null);
        System.out.println(d.getGoods());
        System.out.println(d.getAmount());
        System.out.println(d.getPrice());
        
        // 連想配列形式のMapを普通のMapにするサンプル
        Map<String, Object> in = new HashMap<String, Object>();
        in.put("http_header[HOGE]", "hoge-hoge");
        in.put("http_header[FOO]", "foo");
        in.put("http_header[AMT]", new BigDecimal("100000"));
        in.put("cookie[test1]", "first-cookie");
        in.put("cookie[test2]", "second-cookie");
        in.put("cookie[test3]", new BigDecimal("3"));
        System.out.println("input_Map");
        System.out.print(disp(in));
        Map<String, Object> headers = associativeArray2Map(in, "http_header");
        System.out.println("http_header_Map");
        System.out.print(disp(headers));
        Map<String, Object> cookies = associativeArray2Map(in, "cookie");
        System.out.println("cookie_Map");
        System.out.print(disp(cookies));
        
    }
    
    /**
     * Map表示用
     */
    private static String disp(Map<String, Object> m) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> e : m.entrySet()) {
            sb.append(e.getKey());
            sb.append(":");
            sb.append(e.getValue().toString());
            sb.append("\n");
        }
        return sb.toString();
        
    }

    
    /*----------------------------------------------------------------------------------------------------
     * 実装
     *---------------------------------------------------------------------------------------------------*/
    
    /**
     * 連想配列形式のキーになっているMapを通常のMapに展開する
     * @param source 「Key=配列名[実キー] Value=値」となっているMap
     * @param arrayName Keyで使用されている配列名
     * @return 「Key=実キー value=値」となっているMap
     */
    public static Map<String, Object> associativeArray2Map(Map<String,Object> source, String arrayName) {
        Map<String, Object> ret = new HashMap<String, Object>();
        
        Pattern p = Pattern.compile(arrayName + "\\[(.*)\\]");
        
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            Matcher m = p.matcher(entry.getKey());
            if (m.matches()) {
                ret.put(m.group(1), entry.getValue()); 
            }
        }
        
        return ret;
    }

    
    
    /**
     * MapからBeanのへデータコピーを行います。コピーするフィールドの紐づけは FieldName Annotationを使用します。
     * @param source コピー元
     * @param destination コピー先
     */
    public static void copy(Map<String, Object> source, Object destination, String prefix, String suffix) {
        
        String pre = (prefix == null) ? "" : prefix;
        String suf = (suffix == null) ? "" : suffix;
        
        
        // 出力オブジェクトのフィールドに対して入力値の辞書の値を書き込んでいく
        Field[] dfl = getFieldList(destination);
        for (Field f : dfl) {
            // FieldName Annotation がはられている場合のみ辞書に登録していく
            FieldName at = f.getAnnotation(FieldName.class);
            if (at != null) {
                // コピー元の値と型を取得
                Object value = source.get(pre + at.value() + suf);
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(f.getName(), destination.getClass());
                    Method setter = pd.getWriteMethod();
                    if (setter.getParameterTypes().length == 1 && setter.getParameterTypes()[0].equals(String.class)) {
                        // Setterの形式が一致する場合、値をそのままセットする。
                        setter.invoke(destination, value);
                    } else if (setter.getParameterTypes().length == 1 && !setter.getParameterTypes()[0].equals(String.class)){
                        // Setterの引数の型が一致しない場合、String値を引数にインスタンス化してセットする。
                        Constructor<?> c = setter.getParameterTypes()[0].getConstructor(new Class<?>[]{String.class});
                        setter.invoke(destination, c.newInstance(value));
                    } else {
                        System.out.println("Skip.[" + at.value() +"] Reason:Setter Signature non suitable.");
                    }
                } catch (Exception e) {
                    System.err.println("Not Write.[" + at.value() + "] Reason:" + e.getMessage());
                    continue;
                }
            }
        }
    }

    /**
     * 指定オブジェクトのフィールドのリストを取得する
     * @param target フィールドリスト取得対象オブジェクト
     */
    private static Field[] getFieldList(Object target) {
        // 対象クラスの宣言フィールドを取得する
        Field[] targetField = target.getClass().getDeclaredFields();
        
        // superクラスにさかのぼって、継承フィールドを取得する
        List<Field> fieldList = new ArrayList<Field>();
        for (Class<?> superClass = target.getClass().getSuperclass(); superClass != null; superClass = superClass
                .getSuperclass()) {
            fieldList.addAll(Arrays.asList(superClass.getDeclaredFields()));
        }
        if (!fieldList.isEmpty()) {
            fieldList.addAll(Arrays.asList(targetField));
            targetField = fieldList.toArray(targetField);
        }
        
        return targetField;
    }
}
