package jp.co.isol.omiya.test.jsonmask;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;

/**
 *
 */
public class SensitiveFieldMaskingAnnotationIntrospector extends NopAnnotationIntrospector {

    /**
     * SerialVersionUID
     */
    private static final long serialVersionUID = 6445537552101775633L;

    @Override
    public Object findSerializer(Annotated am) {
        if (am.hasAnnotation(Sensitive.class)) {
            return SensitiveFieldMaskingSerializer.class;
        }
        return null;
    }
}
