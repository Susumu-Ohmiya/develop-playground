package jp.co.isol.omiya.test.jsonmask;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

/**
 *
 */
public class SensitiveFieldMaskingModule extends Module {

    @Override
    public String getModuleName() {
        return getClass().getSimpleName();
    }

    @Override
    public Version version() {
        return Version.unknownVersion();
    }

    @Override
    public void setupModule(SetupContext context) {
        context.insertAnnotationIntrospector(new SensitiveFieldMaskingAnnotationIntrospector());
    }

}
