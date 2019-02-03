package jp.co.isol.omiya.fileconvert.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.isol.omiya.fileconvert.common.component.FileInfo;
import jp.co.isol.omiya.fileconvert.common.exception.ApplicationRuntimeException;
import jp.co.isol.omiya.fileconvert.common.exception.ErrorInfo;
import jp.co.isol.omiya.fileconvert.common.exception.Errors;
import jp.co.isol.omiya.fileconvert.common.exception.RecordInvalidException;
import jp.co.isol.omiya.fileconvert.common.type.RecordType;
import jp.co.isol.omiya.fileconvert.entity.StaffEntity;
import jp.co.isol.omiya.fileconvert.reader.FixcedDataReader;


/**
 * 雇用者基本情報の変換サービス実装クラス。
 * 
 * @author susumu.omiya
 */
public class StaffFileConvertService extends ConvertService<StaffEntity> {
    
    // 入力ファイルリーダー
    private FixcedDataReader<StaffEntity> reader;

    // 外部からのインスタンス化を許可しない
    private StaffFileConvertService(FileInfo targetFileInfo) {
        super(targetFileInfo);
    }

    /**
     * サービスインスタンスを生成する。
     * 
     * @param targetFileInfo 対象のファイル情報
     * @return
     */
    public static StaffFileConvertService createInstance(FileInfo targetFileInfo) {
        if (targetFileInfo.getRecordType() != RecordType.STAFF) {
            throw new IllegalArgumentException("RecordType is unmatch : " + targetFileInfo.getRecordType());
        }
        
        StaffFileConvertService service = new StaffFileConvertService(targetFileInfo);
        
        service.reader = new FixcedDataReader<StaffEntity>(targetFileInfo);
        
        return service;
    }
    
    @Override
    public List<StaffEntity> load() throws RecordInvalidException {
        
        List<StaffEntity> result = new ArrayList<StaffEntity>();
        Errors errors = new Errors();
        int rowNum = 1;
        try {
            while (reader.ready()) {
                try {
                    result.add(reader.readRecord(StaffEntity.class));
                } catch (RecordInvalidException e) {
                    Errors ei = e.getErrorInfo();
                    ei.bluksetErrorRow(rowNum);
                    ei.bulksetRecordType(RecordType.STAFF);
                    errors.addAll(ei);
                }
                rowNum++;
            }
        } catch (IOException e) {
            errors.add(new ErrorInfo(e.getMessage(), rowNum, RecordType.STAFF));
            throw new ApplicationRuntimeException("I/O Error on record loading.", errors, e);
        }
        
        if (errors.hasErrorInfo()) {
            System.out.println(errors);
            throw new RecordInvalidException("File contains some error.", errors, null);
        }
        
        return result;
    }
}
