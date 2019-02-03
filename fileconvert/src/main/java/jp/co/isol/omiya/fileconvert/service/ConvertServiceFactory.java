package jp.co.isol.omiya.fileconvert.service;

import jp.co.isol.omiya.fileconvert.common.AbstractEntity;
import jp.co.isol.omiya.fileconvert.common.component.FileInfo;

/**
 * ファイル形式変換サービスのファクトリクラス
 * 
 * @author susumu.omiya
 *
 */
public class ConvertServiceFactory {

    /**
     * ファイル形式変換サービスのインスタンスを生成するファクトリメソッド
     * 
     * @param fileInfo ファイル情報
     * @return ファイル形式変換サービス
     */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractEntity> ConvertService<T> factory(FileInfo fileInfo) {
        
        if (fileInfo.getRecordType() == null) {
            throw new RuntimeException("RecordType is null.");
        }
        
        switch (fileInfo.getRecordType()) {
        case STAFF :
            return (ConvertService<T>) StaffFileConvertService.createInstance(fileInfo);
        default:
            // 列挙型で聞いているため、ここに来ることはありえない。
            throw new RuntimeException("Not initialized");
        }

    }
}
