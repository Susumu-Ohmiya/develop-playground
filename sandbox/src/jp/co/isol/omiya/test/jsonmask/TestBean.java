package jp.co.isol.omiya.test.jsonmask;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 */
public class TestBean {

    @Sensitive
    String cardNo;
    
    @Sensitive
    Boolean secreatFlg;
    
    String hoge;

    @Sensitive
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" , timezone="Etc/GMT")
    Date birth;
    
    /**
     * @return secreatFlg
     */
    public Boolean getSecreatFlg() {
        return secreatFlg;
    }

    /**
     * @param secreatFlg セットする secreatFlg
     */
    public void setSecreatFlg(Boolean secreatFlg) {
        this.secreatFlg = secreatFlg;
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

    /**
     * @return cardNo
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * @param cardNo セットする cardNo
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * @return hoge
     */
    public String getHoge() {
        return hoge;
    }

    /**
     * @param hoge セットする hoge
     */
    public void setHoge(String hoge) {
        this.hoge = hoge;
    }
    
}
