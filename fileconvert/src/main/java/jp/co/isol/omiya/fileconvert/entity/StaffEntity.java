package jp.co.isol.omiya.fileconvert.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.isol.omiya.fileconvert.common.AbstractEntity;
import jp.co.isol.omiya.fileconvert.common.annotation.ByteLength;
import jp.co.isol.omiya.fileconvert.common.annotation.Format;
import jp.co.isol.omiya.fileconvert.common.annotation.Label;
import jp.co.isol.omiya.fileconvert.common.annotation.Order;
import jp.co.isol.omiya.fileconvert.common.annotation.Position;
import jp.co.isol.omiya.fileconvert.common.annotation.Parse;
import jp.co.isol.omiya.fileconvert.common.component.BigDecimalParser;
import jp.co.isol.omiya.fileconvert.common.component.DateParser;
import jp.co.isol.omiya.fileconvert.common.type.FormatType;
import jp.co.isol.omiya.fileconvert.common.util.StringUtils;

/**
 * TODO:コメント書く
 */
public class StaffEntity extends AbstractEntity {
    
    @Order(0)
    @Position(start = 0, end = 3)
    @Parse(parser = BigDecimalParser.class)
    @ByteLength(4)
    @Format(FormatType.HALF_NUMERIC)
    @Label("雇用者ID")
    private BigDecimal staffId;
    
    @Order(1)
    @ByteLength(20)
    @Position(start = 4, end = 23)
    @Format(FormatType.FULL)
    @Label("氏名")
    private String name;
    
    @Order(2)
    @ByteLength(10)
    @Position(start = 24, end = 33)
    @Parse(parser = DateParser.class, parameter = { "yyyy/MM/dd" })
    @Format(FormatType.SLASHED_YYYYMMDD)
    @Label("生年月日")
    private Date birth;
    
    /**
     * @return staffId
     */
    public BigDecimal getStaffId() {
        return staffId;
    }

    /**
     * @param staffId セットする staffId
     */
    public void setStaffId(BigDecimal staffId) {
        this.staffId = staffId;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name セットする name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return birth
     */
    public Date getBirth() {
        return birth;
    }

    /**
     * @param birth セットする birth
     */
    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return StringUtils.concat("staffId[",getStaffIdString(),"]"," name[",name,"]"," birth[",getBirthString(),"]");
    }

    @Override
    public String toSeparatedRecord(String delimiter) {
        return StringUtils.join(delimiter, 
                                 staffId.toPlainString(), 
                                 name, 
                                 getBirthString());
    }
    
    private String getStaffIdString() {
        return (staffId != null) ? staffId.toPlainString() : "null";
    }
    
    private String getBirthString() {
        return (birth != null) ? (new SimpleDateFormat("yyyy/MM/dd")).format(birth) : "null";
    }

}
