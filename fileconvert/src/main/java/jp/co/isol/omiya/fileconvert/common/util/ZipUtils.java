package jp.co.isol.omiya.fileconvert.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * TODO:コメント書く
 */
public class ZipUtils {

    /**
     * TODO:コメント書く
     */
    public static final String TEMPORARY_DIR_ROOT = System.getProperty("java.io.tmpdir") + "fileconvert" + File.separator;
    /**
     * TODO:コメント書く
     */
    public static final String TEMPORARY_DIR_IN = TEMPORARY_DIR_ROOT + "in" + File.separator;
    /**
     * TODO:コメント書く
     */
    public static final String TEMPORARY_DIR_OUT = TEMPORARY_DIR_ROOT + "out" + File.separator;
    
    /**
     * ZIPファイルチェックを行う。<br/>
     * ※ZIPのエントリ情報を全取得できるかで確認する。解凍してCRCチェックを行うなどはしない。
     * 
     * @param zipFile 対象ファイル
     * @return チェック通過時:true / エラー発生時:false
     */
    public static boolean checkZip(File zipFile) {
        try {
            getZipEntry(zipFile);
            return true;
        } catch (@SuppressWarnings("unused") IOException e) {
            return false;
        }
    }

    /**
     * ZIPファイルのエントリ情報を格納されているファイルについて取得する。<br/>
     * ※ディレクトリのエントリ情報は返さない。
     * 
     * @param zipFile 対象ファイル
     * @return エントリ情報のリスト
     */
    public static List<ZipEntry> getZipEntry(File zipFile) throws IOException {
        List<ZipEntry> result = new ArrayList<ZipEntry>();
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                result.add(entry);
            }
            return result;
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (@SuppressWarnings("unused") IOException e) { /* DoNothing; */ }
            }
        }
    }
    
    /**
     * 入力ZIPファイルをTemporaryDirectoryに展開する。
     * @param archive ZIPファイル
     * @return 展開されたファイルのリスト
     * @throws IOException 作業ディレクトリの作成に失敗した場合
     */
    public static List<File> extractTemporary(File archive) throws IOException {
        File tempDir = new File(TEMPORARY_DIR_IN);
        tempDir.mkdirs();
        tempDir.deleteOnExit();
        extract(archive,tempDir);
        return listFiles(tempDir);
    }
    
    private static List<File> listFiles(File baseDir) {
        return traverseFiles(baseDir, new ArrayList<File>());
    }
    
    private static List<File> traverseFiles(File baseDir, List<File> list) {
        if (!baseDir.isDirectory() || !baseDir.canRead()) {
            return list;
        }
        File[] files = baseDir.listFiles();
        if (files == null) {
            return list;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                traverseFiles(f, list);
            } else if(f.isFile()) {
                list.add(f);
            }
        }
        return list;
    }
    
    private static void extract( File zip, File extractDir ){
        FileInputStream  fileIn  = null;
        FileOutputStream fileOut = null;
        ZipInputStream zipIn = null;
        try{
            fileIn = new FileInputStream( zip );
            zipIn = new ZipInputStream( fileIn );
            
            ZipEntry entry = null;
            while( ( entry = zipIn.getNextEntry() ) != null ){
                if( entry.isDirectory() ){
                    (new File( extractDir, entry.getName())).mkdirs();
                } else {
                    String relativePath = entry.getName();
                    File   outFile = new File( extractDir, relativePath );
                    File   parentFile = outFile.getParentFile();
                    parentFile.mkdirs();
                    
                    fileOut = new FileOutputStream( outFile );
                    byte[] buf  = new byte[ 1024 ];
                    int    size = 0;
                    while( ( size = zipIn.read( buf ) ) > 0 ){
                        fileOut.write( buf, 0, size );
                    }
                    fileOut.close();
                    fileOut = null;
                }
                zipIn.closeEntry();
            }
            
        }catch( Exception e){
            e.printStackTrace();
            
        } finally {
            if( zipIn != null ){
                try{
                    zipIn.close();
                }catch(@SuppressWarnings("unused") Exception e){ /* DoNothng */ }
            }
            if( fileIn != null ){
                try{
                    fileIn.close();
                }catch(@SuppressWarnings("unused") Exception e){ /* DoNothng */ }
            }
            if( fileOut != null ){
                try{
                    fileOut.close();
                }catch(@SuppressWarnings("unused") Exception e){ /* DoNothng */ }
            }            
        }
    }

    /**
     * ファイルをZIPに圧縮します。
     * @param archive ZIPファイル（書込み可能なファイルが存在していることが必要です）
     * @param files 圧縮対象のファイル群
     */
    public static void compress(File archive, File currentDir, File[] files) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(archive));
            encode(zos, currentDir, files);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void encode(ZipOutputStream zos, File cur, File[] files) {
        
        for (File f : files) {
            if (f.isDirectory()) {
                encode(zos, cur, f.listFiles());
            } else {
                ZipEntry entry = new ZipEntry(f.getAbsolutePath().replace(cur.getAbsolutePath(), "").replace('\\', '/'));
                InputStream is = null;
                try {
                    zos.putNextEntry(entry);
                    is = new BufferedInputStream(new FileInputStream(f));
                    byte[] buf = new byte[1024];
                    for (;;) {
                        int len = is.read(buf);
                        if (len < 0) break;
                        zos.write(buf, 0, len);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    
    /**
     * TemporaryDirectoryを掃除します。
     */
    public static void deleteTemporary() {
        File tempDir = new File(TEMPORARY_DIR_ROOT);
        if (!FileUtils.removeCompletely(tempDir)) {
            System.err.println("TemporaryDirectoryの削除に失敗しました。:" + tempDir.getAbsolutePath());
        }
    }
}
