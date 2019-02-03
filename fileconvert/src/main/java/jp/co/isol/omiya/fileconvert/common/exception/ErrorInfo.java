package jp.co.isol.omiya.fileconvert.common.exception;

import jp.co.isol.omiya.fileconvert.common.type.RecordType;

/**
 * エラー情報を保持するためのネストクラス
 * 
 * @author susumu.omiya
 *
 */
public class ErrorInfo {
    
    /** エラーメッセージ */
    private String message;
    
    /** エラーの発生した行 */
    private int errorRow;
    
    /** レコードのタイプ */
    private RecordType recordType;
    
    /** エラーの発生した項目 */
    private String errorItem = null;
    
    /**
     * @param message エラーメッセージ
     */
    public ErrorInfo(String message) {
        this.message = message;
    }
    /**
     * @param message エラーメッセージ
     * @param errorRow エラーの発生した行
     */
    public ErrorInfo(String message, int errorRow) {
        this(message);
        this.errorRow = errorRow;
    }
    /**
     * @param message エラーメッセージ
     * @param errorRow エラーの発生した行
     * @param recordType レコードの形式
     */
    public ErrorInfo(String message, int errorRow, RecordType recordType) {
        this(message, errorRow);
        this.recordType = recordType;
    }
    
    /**
     * @param message エラーメッセージ
     * @param errorRow エラーの発生した行
     * @param recordType レコードの形式
     * @param errorItem エラーの発生した項目
     */
    public ErrorInfo(String message, int errorRow, RecordType recordType, String errorItem) {
        this(message, errorRow, recordType);
        this.errorItem = errorItem;
    }
    
    /**
     * @return エラーメッセージ
     */
    public String getMessage() {
        return message;
    }
    /**
     * @param message セットする エラーメッセージ
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * @return エラーの発生した行
     */
    public int getErrorRow() {
        return errorRow;
    }
    /**
     * @param errorRow セットする エラーの発生した行
     */
    public void setErrorRow(int errorRow) {
        this.errorRow = errorRow;
    }
    /**
     * @return レコードのタイプ
     */
    public RecordType getRecordType() {
        return recordType;
    }
    /**
     * @param recordType セットする レコードのタイプ
     */
    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }
    /**
     * @return エラーの発生した項目
     */
    public String getErrorItem() {
        return errorItem;
    }
    /**
     * @param errorItem セットする エラーの発生した項目
     */
    public void setErrorItem(String errorItem) {
        this.errorItem = errorItem;
    }
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Message[").append(message).append("]");
        sb.append(",ROW[").append(errorRow).append("]");
        sb.append(",RecordType[").append(recordType.typeName()).append("]");
        if (errorItem != null) {
            sb.append(",ErrorItem[").append(errorItem).append("]");
        }
        return sb.toString();
    }
}