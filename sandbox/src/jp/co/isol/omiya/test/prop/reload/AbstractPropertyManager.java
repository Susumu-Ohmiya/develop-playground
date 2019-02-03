package jp.co.isol.omiya.test.prop.reload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractPropertyManager {

    
    /**
     * プロパティファイルをロードする
     */
    protected void load() {
        InputStream in = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream(getResourceName());

            if (in != null) {
                Properties props = new Properties();
                try {
                    props.load(in);
                    initParams(props);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(new FileNotFoundException(getResourceName()));
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (@SuppressWarnings("unused") IOException e) {
                // do nothing
            }
        }
    }
    
    /**
     * @return
     */
    abstract protected String getResourceName();
    
    protected void initParams(Properties prop) {
        
        Map<String, String> m = new HashMap<String, String>();
        for (String key : prop.stringPropertyNames()) {
            m.put(key, prop.getProperty(key));
        }
    }

}
