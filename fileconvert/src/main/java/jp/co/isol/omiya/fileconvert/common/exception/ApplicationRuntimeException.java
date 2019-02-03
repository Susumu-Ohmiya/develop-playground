package jp.co.isol.omiya.fileconvert.common.exception;

public class ApplicationRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 7070267948759372145L;

    private Errors errors = null;
    
    /**
     * TODO:コメント書く
     * @param message
     * @param errors
     * @param e
     */
    public ApplicationRuntimeException(String message, Errors errors, Throwable e) {
        super(message, e);
        this.errors = errors;
    }
    
    /**
     * TODO:コメント書く
     * @param message
     */
    public ApplicationRuntimeException(String message) {
        super(message);
        
        this.errors = new Errors();
        this.errors.add(new ErrorInfo(message));
    }
    
    /**
     * TODO:コメント書く
     * @param e
     */
    public ApplicationRuntimeException(Throwable e) {
        super(e);
        this.errors = new Errors();
        this.errors.add(new ErrorInfo(e.getMessage()));
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
