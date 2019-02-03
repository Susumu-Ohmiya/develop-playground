package jp.co.isol.omiya.fileconvert.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.isol.omiya.fileconvert.common.component.FileInfo;
import jp.co.isol.omiya.fileconvert.common.exception.ApplicationRuntimeException;
import jp.co.isol.omiya.fileconvert.common.exception.FileNameFormatIllegalException;
import jp.co.isol.omiya.fileconvert.common.exception.RecordInvalidException;
import jp.co.isol.omiya.fileconvert.common.util.FileUtils;
import jp.co.isol.omiya.fileconvert.common.util.ZipUtils;

/**
 * ZIPファイルに格納されたレコードファイルを処理する実装クラス。
 * 
 * @author susumu.omiya
 *
 */
public class ArchivedRecordsCollector {

    private FileInfo archiveFileInfo;
    
    /** 外部からのインスタンス化を許容しない */
    private ArchivedRecordsCollector(FileInfo fileInfo) {
        this.archiveFileInfo = fileInfo;
    }
    
    /**
     * TODO:コメント書く
     */
    public static ArchivedRecordsCollector createInstance(FileInfo fileInfo) {
        if (!fileInfo.isArchived()) {
            throw new IllegalArgumentException("This FileInfo is not Archive. : " + fileInfo.getInputFile().getAbsolutePath());
        }
        
        return new ArchivedRecordsCollector(fileInfo);
    }
    
    /**
     * TODO:コメント書く
     */
    public void process() {
        try {
            List<File> files = ZipUtils.extractTemporary(archiveFileInfo.getInputFile());
            List<File> outFiles = new ArrayList<File>();
            for (File file : files) {
                FileInfo innerFi = FileInfo.createInfo(file);
                if (innerFi.isRecordFile()) {
                    try {
                        outFiles.add(ConvertServiceFactory.factory(innerFi).convert());
                    } catch (RecordInvalidException rie) {
                        System.out.println(rie.getErrorInfo());
                    } finally {
                        innerFi.close();
                    }
                }
            }
            compressToZip(archiveFileInfo.getInputFile(), outFiles);
            
        } catch (IOException e) {
            throw new ApplicationRuntimeException(e);
        } catch (FileNameFormatIllegalException e) {
            throw new ApplicationRuntimeException(e);
        } finally {
            try {
                ZipUtils.deleteTemporary();
            } catch (@SuppressWarnings("unused") Exception e) {
                /* DoNothing */
            }
        }

    }

    /**
     * リストのファイルをTemporaryDirectoryに移動してZipにアーカイブする
     * 
     * @param outFiles 出力ファイルのリスト
     * @throws IOException Zipファイルの圧縮に失敗した場合
     */
    public static void compressToZip(File inputZip, List<File> outFiles) throws IOException {
        File outRootDir = new File(ZipUtils.TEMPORARY_DIR_OUT);
        FileUtils.moveFiles(outRootDir, new File(ZipUtils.TEMPORARY_DIR_IN), outFiles);
        File outArchive = new File(FileUtils.changeExtention(inputZip, "out.zip"));
        outArchive.createNewFile();
        File[] targetFiles = outRootDir.listFiles();
        if (targetFiles == null) {
            // この時点で対象ファイルがnullとなることは実質ありえないが、空の配列を作成しておく
            targetFiles = new File[0];
        }
        ZipUtils.compress(outArchive, outRootDir, targetFiles);
    }
    
}
