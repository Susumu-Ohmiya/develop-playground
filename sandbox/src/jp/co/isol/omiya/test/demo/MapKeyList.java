package jp.co.isol.omiya.test.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MapKeyList {

    public static void main(String[] args) {

        Map<String,String> mapA = new HashMap<String, String>();
        Map<String,String> mapB = new HashMap<String, String>();

        mapA.put("1", "HOGE1");
        mapA.put("2", "HOGE2");

        mapB.put("1", "HOGE1");
        mapB.put("3", "HOGE3");
        
        Set<String> keys = new TreeSet<String>();
        keys.addAll(mapA.keySet());
        keys.addAll(mapB.keySet());
        
        for (String key : keys) {
            System.out.println(key);
        }
    }

}
