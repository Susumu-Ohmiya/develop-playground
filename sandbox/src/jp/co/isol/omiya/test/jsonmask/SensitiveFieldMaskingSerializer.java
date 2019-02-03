package jp.co.isol.omiya.test.jsonmask;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * 
 */
public class SensitiveFieldMaskingSerializer extends StdSerializer<Object> {

    /**
     * コンストラクタ
     */
    protected SensitiveFieldMaskingSerializer() {
        super(Object.class, false);
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        // 適当に置換する処理
        if (value != null) {
            jgen.writeRawValue("****");
        } else {
            jgen.writeNull();
        }
    }


}
