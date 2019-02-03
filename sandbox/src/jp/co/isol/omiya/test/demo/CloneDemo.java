package jp.co.isol.omiya.test.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CloneDemo {

    public static void main(String[] args) {
        
        Integer[] ia1 = {1, 2, 3, 4, 5};
        Integer[] ia2 = ia1.clone();
        ia2[0] = 0;
        disp(ia1);
        
        ArrayList<TestBean> l = new ArrayList<TestBean>();
        l.add(new TestBean());
        l.add(new TestBean());
        l.get(0).setName("OMIYA");
        l.get(0).setAge(17);
        l.get(1).setName("OKA");
        l.get(1).setAge(18);
        disp(l);
        List<TestBean> cl = (List<TestBean>) l.clone();
        cl.get(0).setName("FUJISAWA");
        
        disp(l);

    }
    
    private static <T> void disp(T[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (T t : array) {
            sb.append(t);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        System.out.println("[" + array.hashCode() + "]");
        System.out.println("---------------------------");
        System.out.println(sb.toString());
        System.out.println("---------------------------");
        
    }
    
    private static void disp(List<TestBean> list) {
        System.out.println("[" + list.hashCode() + "]");
        for (TestBean testBean : list) {
            System.out.println("---------------------------");
            System.out.println(testBean.hashCode());
            System.out.println(testBean.getName());
            System.out.println(testBean.getAge());
            System.out.println("---------------------------");
        }
    }
}
