package jp.co.isol.omiya.fileconvert.common.exception;

/**
 * TODO:コメント書く
 */
public class RecordInvalidException extends Exception {

    private static final long serialVersionUID = 126034025009126536L;

    private Errors errors = null;
    
    /**
     * TODO:コメント書く
     * @param message
     * @param errors
     * @param e
     */
    public RecordInvalidException(String message, Errors errors, Throwable e) {
        super(message, e);
        this.errors = errors;
    }
    
    /**
     * TODO:コメント書く
     * @return
     */
    public boolean hasErrorInfo() {
        return (errors != null && !errors.hasErrorInfo());
    }

    /**
     * TODO:コメント書く
     * @param errors
     */
    public void setErrorInfo(Errors errors) {
        this.errors = errors;
    }
    
    /**
     * TODO:コメント書く
     * @return
     */
    public Errors getErrorInfo() {
        return errors;
    }
}
