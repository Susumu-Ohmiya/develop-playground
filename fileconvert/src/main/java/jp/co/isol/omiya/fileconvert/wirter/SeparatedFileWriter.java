package jp.co.isol.omiya.fileconvert.wirter;

import java.io.Closeable;
import java.io.PrintWriter;
import java.util.List;

import jp.co.isol.omiya.fileconvert.common.AbstractEntity;
import jp.co.isol.omiya.fileconvert.common.type.Delimiter;
import jp.co.isol.omiya.fileconvert.common.util.StringUtils;

public class SeparatedFileWriter implements Closeable {

    private Delimiter delimiter;
    private PrintWriter writer;

    /**
     * TODO:・ｽR・ｽ・ｽ・ｽ・ｽ・ｽg・ｽ・ｽ・ｽ・ｽ
     * @param delimiter
     * @param writer
     */
    public SeparatedFileWriter(Delimiter delimiter, PrintWriter writer) {
        this.delimiter = delimiter;
        this.writer = writer;
    }

    /**
     * TODO:・ｽR・ｽ・ｽ・ｽ・ｽ・ｽg・ｽ・ｽ・ｽ・ｽ
     * @param recoreds
     */
    public void write(List<? extends AbstractEntity> recoreds) {

        for (AbstractEntity e : recoreds) {
            writer.write(e.toSeparatedRecord(delimiter.value()));
            writer.write(StringUtils.NEW_LINE);
        }
        writer.flush();
    }

    @Override
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }

}
