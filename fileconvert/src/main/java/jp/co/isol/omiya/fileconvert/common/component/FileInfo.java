package jp.co.isol.omiya.fileconvert.common.component;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.isol.omiya.fileconvert.common.exception.FileNameFormatIllegalException;
import jp.co.isol.omiya.fileconvert.common.type.CharacterEncode;
import jp.co.isol.omiya.fileconvert.common.type.Delimiter;
import jp.co.isol.omiya.fileconvert.common.type.RecordType;
import jp.co.isol.omiya.fileconvert.common.util.ZipUtils;

public class FileInfo {
    
    /** ファイル名パターン(拡張子を除く) */
    private static final String FILE_NAME_PATTERN = "^shain_\\d{4}_(\\d)(\\d)(\\d)([10])(.+)$";
    
    private static final String ZIPPED_MARK = "1";
    
    /** 入力ファイル */
    private File inputFile;

    /** ファイルの文字コードセット */
    private CharacterEncode encode;
    
    /** ファイルのレコード種別 */
    private RecordType recordType;
    
    /** 出力ファイルのデリミタ */
    private Delimiter delimiter;
    
    /** アーカイブファイルか否か */
    private boolean isArchived;
    
    /** 入力ファイルのReader */
    private BufferedReader reader;
    
    /**
     * @return 入力ファイル
     */
    public File getInputFile() {
        return inputFile;
    }

    /**
     * @param inputFile 入力ファイル
     */
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * @return ファイルの文字コードセット
     */
    public CharacterEncode getEncode() {
        return encode;
    }

    /**
     * @param encode ファイルの文字コードセット
     */
    public void setEncode(CharacterEncode encode) {
        this.encode = encode;
    }

    /**
     * @return ファイルのレコード種別
     */
    public RecordType getRecordType() {
        return recordType;
    }

    /**
     * @param recordType ファイルのレコード種別
     */
    public void setRecordType(RecordType recordType) {
        this.recordType = recordType;
    }

    /**
     * @return 出力ファイルのデリミタ
     */
    public Delimiter getDelimiter() {
        return delimiter;
    }

    /**
     * @param delimiter 出力ファイルのデリミタ
     */
    public void setDelimiter(Delimiter delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * @return アーカイブファイルか否か
     */
    public boolean isArchived() {
        return isArchived;
    }

    /**
     * @param isArchived アーカイブファイルか否か
     */
    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    /**
     * @return 入力ファイルのReader
     */
    public BufferedReader getStream() {
        return reader;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
    
    /**
     * ストリームが開かれている場合にcloseします。
     */
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (@SuppressWarnings("unused") Exception e) {
            // DoNothing
        }
    }
    
    /**
     * TODO:コメント書く
     */
    public FileInfo() {
    }
    
    /**
     * TODO:コメント書く
     */
    public FileInfo(String str,CharacterEncode encode, RecordType recordType, Delimiter delimiter) {
        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(str.getBytes(encode.charset())), encode.charset()));
        this.encode = encode;
        this.recordType = recordType;
        this.delimiter = delimiter;
    }
    
    /**
     * TODO:コメント書く
     */
    public boolean isRecordFile() {
        return recordType != null;
    }
    
    /**
     * FileオブジェクトよりFileInfoオブジェクトを生成する
     * @param input 入力ファイルのFileオブジェクト
     * @return FileInfoオブジェクト
     * @throws IOException 対象がファイルではない、破損している等の場合
     * @throws FileNameFormatIllegalException ファイル名がルールに則っていない場合
     */
    public static FileInfo createInfo(File input) throws IOException, FileNameFormatIllegalException {
        
        // 基本チェック
        if (!input.isFile()) {
            throw new FileNotFoundException(input.getAbsolutePath() + " is not File.");
        }
        if (!input.canRead()) {
            throw new IOException(input.getAbsolutePath() + " can't read.");
        }
        
        String filename = input.getName().toLowerCase();
        FileInfo info = new FileInfo();
        info.setInputFile(input);
        
        // ファイル名のパターンチェックを行う
        Matcher m = Pattern.compile(FILE_NAME_PATTERN).matcher(filename);
        if (!m.matches()) {
            throw new FileNameFormatIllegalException("Filename is Illegal : " + input.getAbsolutePath());
        }
        MatchResult mr = m.toMatchResult();
        if (mr.groupCount() != 5) {
            throw new FileNameFormatIllegalException("Filename is Illegal : " + input.getAbsolutePath());
        }
        
        // 書庫判定
        if (ZIPPED_MARK.equals(mr.group(4))){
            if (!filename.endsWith(".in.zip")) {
                throw new FileNameFormatIllegalException("Filename is Illegal : " + input.getAbsolutePath());
            }
            // Zipファイルチェック
            if (ZipUtils.checkZip(input)) {
                // 書庫ファイルの場合はアーカイブとマークして形式判定せずに返す
                info.setArchived(true);
                return info;
            }
            throw new IOException(input.getAbsolutePath() + " is Broken.");
        } else {
            if (!filename.endsWith(".in")) {
                throw new FileNameFormatIllegalException("Filename is Illegal : " + input.getAbsolutePath());
            }
            info.setArchived(false);
        }
        
        // レコード形式
        RecordType recordType = RecordType.of(mr.group(1));
        if (recordType == null) {
            throw new FileNameFormatIllegalException("Assigned RecordType is Illegal : " + input.getAbsolutePath());
        }
        info.setRecordType(recordType);
        
        // 文字コード
        CharacterEncode characterEncode = CharacterEncode.of(mr.group(2));
        if (characterEncode == null) {
            throw new FileNameFormatIllegalException("Assigned CharacterEncode is Illegal : " + input.getAbsolutePath());
        }
        info.encode = characterEncode;
        
        // 出力ファイルの区切り文字
        Delimiter delimiter = Delimiter.of(mr.group(3));
        if (delimiter == null) {
            throw new FileNameFormatIllegalException("Assigned CharacterEncode is Illegal : " + input.getAbsolutePath());
        }
        info.delimiter = delimiter;
        
        // Readerを用意する
        info.reader = new BufferedReader(new InputStreamReader(new FileInputStream(input), characterEncode.charset()));
        
        return info;
    }
}
