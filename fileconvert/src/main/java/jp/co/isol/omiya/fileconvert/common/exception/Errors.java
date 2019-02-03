package jp.co.isol.omiya.fileconvert.common.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.co.isol.omiya.fileconvert.common.type.RecordType;
import jp.co.isol.omiya.fileconvert.common.util.StringUtils;

public class Errors implements Iterable<ErrorInfo>{

    
    private List<ErrorInfo> errors = new ArrayList<ErrorInfo>();
    
    /**
     * TODO:コメント書く
     * @return
     */
    public boolean hasErrorInfo() {
        return !errors.isEmpty();
    }
    
    /**
     * TODO:コメント書く
     * @param info
     */
    public void add(ErrorInfo info) {
        errors.add(info);
    }
    
    /**
     * TODO:コメント書く
     * @param addErrors
     */
    public void addAll(Errors addErrors) {
        this.errors.addAll(addErrors.getErrors());
    }

    /**
     * TODO:コメント書く
     * @param row
     */
    public void bluksetErrorRow(int row) {
        for (ErrorInfo errorInfo : errors) {
            errorInfo.setErrorRow(row);
        }
    }
    
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (ErrorInfo errorInfo : errors) {
            sb.append(errorInfo);
            sb.append(StringUtils.NEW_LINE);
        }
        return sb.toString();
    }
    
    /**
     * TODO:コメント書く
     * @param recordType
     */
    public void bulksetRecordType(RecordType recordType) {
        for (ErrorInfo errorInfo : errors) {
            errorInfo.setRecordType(recordType); ;
        }
    }
    private List<ErrorInfo> getErrors() {
        return errors;
    }
    
    @Override
    public Iterator<ErrorInfo> iterator() {
        return errors.iterator();
    }
}
