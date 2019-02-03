package jp.co.isol.omiya.fileconvert.common.component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser implements Parser<Date> {

    @Override
    public Date parse(String source) throws ParseException {
        return parse(source, "yyyyMMdd");
    }

    @Override
    public Date parse(String source, String[] parameter) throws ParseException {
        String format = "yyyyMMdd";
        if (parameter != null && parameter.length != 0) {
            format = parameter[0];
        }
        return parse(source, format);
    }

    private Date parse(String source, String format) throws ParseException {
        if (source == null) {
            throw new NullPointerException();
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(source);
        } catch (RuntimeException e) {
            throw new ParseException(e.getMessage(),0);
        }

    }

    @Override
    public String format(Date data) {
        return SimpleDateFormat.getInstance().format(data);
    }

    @Override
    public String format(Date data, String[] parameter) {
        String format = "yyyyMMdd";
        if (parameter != null && parameter.length != 0) {
            format = parameter[0];
        }
        return (new SimpleDateFormat(format)).format(data);
    }

}
