package jp.co.isol.omiya.test.patternmatch;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * @author susumu.omiya
 *
 */
public class RegExpMatchList extends ArrayList<String> {

    private static final long serialVersionUID = 1L;
    
    public static void main(String[] args) {
        List<String> list = new RegExpMatchList();
        list.add("hoge.*");
        list.add("foo");
        
        System.out.println("hoge[123]:" + list.contains("hoge[123]"));
        System.out.println("foo[123]:" + list.contains("foo[123]"));
        System.out.println("hoge[abc]:" + list.contains("hoge[123]"));
        System.out.println("foo:" + list.contains("foo"));
        
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < super.size(); i++)
            if (get(i)==null)
                return i;
        } else if (o instanceof String) {
            for (int i = 0; i < super.size(); i++) {
               try {
                   if (((String) o).matches(get(i))) {
                       return i;
                   }
               } catch (PatternSyntaxException e) {
                   continue;
               }
            }
        } else {
            o = o.toString();
            for (int i = 0; i < super.size(); i++) {
                try {
                    if (((String) o).matches(get(i))) {
                        return i;
                    }
                } catch (PatternSyntaxException e) {
                    continue;
                }
             }
        }
        return -1;
    }

}
