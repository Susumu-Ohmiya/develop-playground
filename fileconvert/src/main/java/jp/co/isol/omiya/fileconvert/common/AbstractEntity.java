package jp.co.isol.omiya.fileconvert.common;

/**
 * エンティティの抽象スーパークラス
 * 
 * @author susumu.omiya
 * 
 */
public abstract class AbstractEntity {

    @Override
    public abstract String toString();
    
    
    /**
     * Entity を指定された Delimiter で区切った文字列に変換する。
     * @param Delimiter 区切り文字
     * @return Delimiterで区切られた文字列
     */
    public abstract String toSeparatedRecord(String Delimiter);
}
