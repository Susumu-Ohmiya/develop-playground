package jp.co.isol.omiya.fileconvert.common.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO:コメント書く
 */
public class FileUtils {

    /**
     * TODO:コメント書く
     */
    public static String changeExtention(File source, String destExtention) {
        if (!source.isFile()) {
            throw new IllegalArgumentException("Source is not a File.");
        }
        
        try {
            
            int sepCount = 0;
            for (int i = 0; i < destExtention.length(); ++i) {
                if (destExtention.charAt(i) == '.') {
                    sepCount++;
                }
            }
            
            String sourcePath = source.getCanonicalPath();
            int endIndex = sourcePath.length();
            for (; endIndex > -1; endIndex--) {
                endIndex = sourcePath.lastIndexOf(".", endIndex);
                if (--sepCount < 0) {
                    break;
                }
            }

            
            return sourcePath.substring(0, endIndex + 1) + destExtention;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * TODO:コメント書く
     */
    public static String getSelfName(File file) {
        File ap = file.getAbsoluteFile();
        return ap.getAbsolutePath().replace(ap.getParent(), "");
    }

    /**
     * TODO:コメント書く
     */
    public static boolean removeCompletely(File target) {
        
        if (target.isFile()) {
            return target.delete();
        }
        
        if (!target.isDirectory()) {
            return false;
        }
        
        File[] files = target.listFiles();
        if (files == null) {
            return false;
        }
        
        for(File f : files) {
            if (!removeCompletely(f)) {
                return false;
            }
        }
        
        return target.delete();
    }
    
    /**
     * TODO:コメント書く
     */
    public static List<File> moveFiles(File destination, File root, List<File> files) {
        if (!destination.exists()) {
            if (!destination.mkdirs()) {
                throw new RuntimeException("Can't create destination directory : " + destination.getAbsolutePath());
            }
        }
        if (!destination.isDirectory()) {
            throw new IllegalArgumentException(destination.getAbsolutePath() + " is not Directory.");
        }

        List<File> movedFiles = new ArrayList<File>();
        try {
            for (File file : files) {
                if (!file.getCanonicalPath().startsWith(root.getCanonicalPath())) {
                    System.err.println("移動対象外のディレクトリ配下ファイルのためスキップします:" + file.getAbsolutePath());
                    continue;
                }
                String destPath = destination.getCanonicalPath() + file.getCanonicalPath().substring(root.getCanonicalPath().length(), file.getCanonicalPath().length());
                File destfile = new File(destPath); 
                
                if (!file.renameTo(destfile)) {
                    System.err.println("ファイルの移動に失敗しました。: " + file.getAbsolutePath() + " -> " + destfile.getAbsolutePath());
                    continue;
                }
                movedFiles.add(destfile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return movedFiles;
    }
    
    /**
     * 引数に指定されたリソースをすべてcloseします。</br>
     * </br>
     * 引数に渡されたリソースオブジェクトのclose()を呼び出します。</br>
     * 発生した例外はすべて握りつぶします。</br>
     * 
     * @param closeables close()を呼び出す対象
     */
    public static void closeAll(Closeable... closeables) {
        try {
            closeAll(null, null, closeables);
        } catch (@SuppressWarnings("unused") Exception e) {
            // do Nothing
        }
    }
    
    /**
     * 引数に指定されたリソースをすべてcloseします。</br>
     * </br>
     * 引数に渡されたリソースオブジェクトのclose()を呼び出します。</br>
     * 発生した例外は戻り値のListに追加されます。</br>
     * 
     * @param closeables close()を呼び出す対象
     * @return 発生した例外のリスト
     */
    public static List<Exception> closeAllWithReport(Closeable... closeables) {
        List<Exception> eList = new ArrayList<Exception>();
        try {
            closeAll(null, eList, closeables);
        } catch (Exception e) {
            eList.add(e);
        }
        return eList;
    }
    
    /**
     * 引数に指定されたリソースをすべてcloseします。</br>
     * </br>
     * 引数に渡されたリソースオブジェクトのclose()を呼び出します。</br>
     * 例外発生時は即座にエラー応答されます</br>
     * 
     * @param closeables close()を呼び出す対象
     * @throws IOException リソース開放時に入出力例外の発生した場合
     */
    public static void closeAllCheckException(Closeable... closeables) throws IOException {
        try {
            closeAll(IOException.class, null, closeables);
        } catch (IOException e) {
            throw e;
        }
    }
    
    /**
     * 引数に指定されたリソースをすべてcloseします。</br>
     * </br>
     * @param closeables close()を呼び出す対象
     * @param exceptionClazz close呼び出し時に例外が発生した場合に例外を投げたい場合設定する。
     *                        設定された場合、そこで処理は中断される。
     * @param occurredException close呼び出し時に例外が発生した例外を保持するリスト。
     * @return リソース開放時にすべて正常終了した場合：true / 例外の発生した場合：false
     */
    private static <T extends Exception> boolean closeAll(Class<T> exceptionClazz, List<Exception> occurredException, Closeable... closeables) throws T {
        
        // 発生エラー=なし で初期化
        boolean noError = true;
        
        // 指定された例外のコンストラクタを作成
        Constructor<T> exceptionConstructor = null;
        if (exceptionClazz != null) {
            try {
                exceptionConstructor = exceptionClazz.getConstructor(Throwable.class);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
        }
        
        // 引数なしの場合処理しない
        if (closeables == null) {
            return noError;
        }
        
        // 引数のCloseableを逐次close()する
        for (Closeable c : closeables) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (Exception e) {
                // エラー時処理
                
                // エラー時例外発生指定の場合、指定された例外を投げる。
                if (exceptionConstructor != null) {
                    try {
                        throw exceptionConstructor.newInstance(e);
                    } catch (IllegalArgumentException e1) {
                        throw e1;
                    } catch (InstantiationException e1) {
                        throw new IllegalArgumentException(e1);
                    } catch (IllegalAccessException e1) {
                        throw new IllegalArgumentException(e1);
                    } catch (InvocationTargetException e1) {
                        throw new IllegalArgumentException(e1);
                    }
                }
                
                // 例外リストが存在している場合は、例外を追加する。
                if (occurredException != null) {
                    occurredException.add(e);
                }
                
                // 発生エラー=あり
                noError = false;
            }
        }
        
        return noError;
    }
}
