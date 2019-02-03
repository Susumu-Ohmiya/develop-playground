package jp.co.isol.omiya.fileconvert.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jp.co.isol.omiya.fileconvert.common.AbstractEntity;
import jp.co.isol.omiya.fileconvert.common.component.FileInfo;
import jp.co.isol.omiya.fileconvert.common.exception.ApplicationRuntimeException;
import jp.co.isol.omiya.fileconvert.common.exception.RecordInvalidException;
import jp.co.isol.omiya.fileconvert.common.util.FileUtils;
import jp.co.isol.omiya.fileconvert.wirter.SeparatedFileWriter;

/**
 * ファイル変換サービスの抽象スーパークラス
 * 
 * @author susumu.omiya
 */
public abstract class ConvertService<T extends AbstractEntity> {

    /** 入力ファイル情報 */
    protected FileInfo fileInfo;

    /** コンストラクタ */
    public ConvertService(FileInfo targetFileInfo) {
        this.fileInfo = targetFileInfo;
    }

    /**
     * ファイルの変換を行う
     * @throws RecordInvalidException 
     */
    public File convert() throws RecordInvalidException {
        return write(load());
    }
    
    /**
     * 変換前ファイルの読み込みを行う
     * 
     * @return 入力ファイル情報で指定されたBeanのList
     * @throws RecordInvalidException 
     */
    abstract protected List<T> load() throws RecordInvalidException;
    
    /**
     * 変換後ファイルの書き出しを行う。
     * 
     * @param records 書き出しを行うBeanのList
     */
    /* writerの接続先にSystem.outを指定する可能性があるため、未close警告を抑制する。*/
    @SuppressWarnings("resource")
    protected File write(List<T> records) {
        
        File outputFile = null;
        boolean necessaryClose;
        PrintWriter pw;
        
        try {
            if (fileInfo.getInputFile() != null) {
                /* ファイルからの入力の場合 */
                outputFile = new File(FileUtils.changeExtention(fileInfo.getInputFile(), "out"));
                outputFile.createNewFile();
                pw = new PrintWriter(outputFile, fileInfo.getEncode().charset().name());
                necessaryClose = true;
            } else {
                /* 文字列を入力している場合:標準出力に応答させる */
                pw = new PrintWriter(System.out);
                necessaryClose = false;
            }
        } catch (FileNotFoundException e) {
            /* 出力ファイルは作成しているのでこの例外はありえない。*/
            throw new ApplicationRuntimeException(e);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(e);
        }
        
        SeparatedFileWriter writer = null;
        try {
            writer = new SeparatedFileWriter(fileInfo.getDelimiter(), new PrintWriter(pw));
            writer.write(records);
            return outputFile;
        } finally {
            if (writer != null && necessaryClose) {
                writer.close();
            }
        }
    }
}
