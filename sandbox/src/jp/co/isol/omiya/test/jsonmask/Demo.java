package jp.co.isol.omiya.test.jsonmask;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * デモ
 */
public class Demo {

    /**
     * デモ
     * @param args dummy
     */
    public static void main(String[] args) {
        TestBean bean = new TestBean();
        bean.setCardNo("4980123412340000");
        bean.setHoge("ほげほげ～");
        bean.setBirth(new Date());
        bean.setSecreatFlg(Boolean.TRUE);
        
        System.out.println(toJsonString(bean));
        
        System.out.println(toMaskedJsonString(bean));
        
        
    }
    
    /**
     * BeanからJson形式に変換します
     * @param bean 文字列化するオブジェクト
     * @return JSON形式のString
     */
    public static String toJsonString(Object bean) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw  new RuntimeException(e);
            
        }
    }
    
    /**
     * BeanからJson形式に変換します。秘匿項目はマスクします。
     * @param bean 文字列化するオブジェクト
     * @return JSON形式のString
     */
    public static String toMaskedJsonString(Object bean) {
        ObjectMapper logObjectMapper = new ObjectMapper()
                .registerModule(new SensitiveFieldMaskingModule());
        logObjectMapper.setSerializationInclusion(Include.NON_NULL);
        logObjectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return logObjectMapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw  new RuntimeException(e);
        }
        
    }


}
