package jp.co.isol.omiya.fileconvert.common.exception;

/**
 * TODO:コメント書く
 */
public class FileNameFormatIllegalException extends RecordInvalidException {

    private static final long serialVersionUID = 8936403586825483443L;
    
    /**
     * TODO:コメント書く
     * @param message
     * @param errors
     */
    public FileNameFormatIllegalException(String message) {
        super(message, new Errors(), null);
    }
    
}
