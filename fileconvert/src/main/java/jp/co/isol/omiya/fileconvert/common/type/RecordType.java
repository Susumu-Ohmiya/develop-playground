package jp.co.isol.omiya.fileconvert.common.type;

import jp.co.isol.omiya.fileconvert.common.AbstractEntity;
import jp.co.isol.omiya.fileconvert.entity.StaffEntity;

/**
 * 読み取るレコードの種類
 * 
 * @author susumu.omiya
 *
 */
public enum RecordType {

    /** 社員基本情報 */
    STAFF("1", "社員基本情報", 34, StaffEntity.class);
    
    private String code;
    private String typeName;
    private int recordLength;
    private Class<? extends AbstractEntity> entityClass;
    
    RecordType(String code, String typeName, int recordLength, Class<? extends AbstractEntity> entityClass) {
        this.code = code;
        this.typeName = typeName;
        this.recordLength = recordLength;
        this.entityClass = entityClass;
    }
    
    public Class<? extends AbstractEntity> getEntityClass() {
        return entityClass;
    }
    
    public String typeName(){
        return typeName;
    }
    
    public int getRecordLength() {
        return recordLength;
    }
    
    public static RecordType of(String code) {
        if (code == null) {
            return null;
        }
        for (RecordType r : RecordType.values()) {
            if (r.code.equals(code)) {
                return r;
            }
        }
        return null;
    }
}
