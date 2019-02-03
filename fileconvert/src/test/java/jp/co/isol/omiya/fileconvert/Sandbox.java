package jp.co.isol.omiya.fileconvert;

import java.io.File;

import jp.co.isol.omiya.fileconvert.common.component.FileInfo;
import jp.co.isol.omiya.fileconvert.common.type.CharacterEncode;
import jp.co.isol.omiya.fileconvert.common.type.Delimiter;
import jp.co.isol.omiya.fileconvert.common.type.RecordType;
import jp.co.isol.omiya.fileconvert.service.ArchivedRecordsCollector;
import jp.co.isol.omiya.fileconvert.service.ConvertServiceFactory;

public class Sandbox {
    public static void main(String[] args) {
        
        try {
            // レコードのテスト
            String txt = "1000大宮　進        1977/12/25\r\n1001藤澤　尊        1970/12/25";
            FileInfo fi = new FileInfo(txt, CharacterEncode.UTF_8, RecordType.STAFF, Delimiter.COMMA);
            ConvertServiceFactory.factory(fi).convert();
            
            // テキストファイルのテスト
            fi = FileInfo.createInfo(new File("c:\\shain_0001_1110.in"));
            if (fi.isRecordFile()) {
                ConvertServiceFactory.factory(fi).convert();
            }
            
            // アーカイブファイルのテスト
            fi = FileInfo.createInfo(new File("c:\\shain_0001_1111.in.zip"));
            if (fi.isArchived()) {
                ArchivedRecordsCollector.createInstance(fi).process();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
