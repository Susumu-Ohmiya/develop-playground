package jp.co.isol.omiya.test.patternmatch;

public class PatterMatch {

    public static void main(String[] args) {
        
        System.out.println("20190124101010".matches("\\d{14}"));
        System.out.println("2019012410101".matches("\\d{14}"));
        
    }
    
}
