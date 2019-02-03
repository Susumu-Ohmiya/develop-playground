package jp.co.isol.omiya.test.prop.reload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class TestProperty {

    @SuppressWarnings("javadoc")
    public static void main(String[] args) {
        System.out.println(TestProperty.getInstance().getReadTimeout());
        System.out.println(TestProperty.getInstance().getConnectTimeout());
        System.out.println(TestProperty.getInstance().getApiRequestUrl());
        System.out.println(TestProperty.getInstance().getApiRequestMethod());
        System.out.println(TestProperty.getInstance().getApiRequestEncoding().displayName());
        Map<String, String> m = TestProperty.getInstance().getApiRequestHeaders();
        for (Map.Entry<String, String> e : m.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue());
        }
    }


    /** TestProperty インスタンス：Singletonパターン */
    private static final TestProperty instance = new TestProperty();

    /** リソース名 */
    private static final String RESOURCE_NAME = "test.properties";

    private static final String KEY_FULLY_QUALIFIED_DOMAIN_NAME = "requestFqdn";
    private static final String KEY_READ_TIMEOUT = "readTimeout";
    private static final String KEY_CONNECT_TIMEOUT = "connectTimeout";

    private static final String REQUEST_PATH = "request.testapi.path";
    private static final String REQUEST_METHOD = "request.testapi.method";
    private static final String REQUEST_ENCODING = "request.testapi.encoding";
    private static final String REQUEST_HEADERS = "request.testapi.headers";

    /** Read Timeout */
    private int readTimeout;

    /** Connection Timeout */
    private int connectTimeout;

    /** リクエストURL */
    private String apiRequestUrl;

    /** リクエストMETHODL */
    private String apiRequestMethod;

    /** リクエスト HTTP Headers */
    private Map<String, String> apiRequestHeaders;

    /** リクエスト エンコーディング */
    private Charset apiRequestEncoding;

    /**
     * @return apiRequestUrl
     */
    public String getApiRequestUrl() {
        return apiRequestUrl;
    }

    /**
     * @return apiRequestMethod
     */
    public String getApiRequestMethod() {
        return apiRequestMethod;
    }

    /**
     * @return apiRequestHeaders
     */
    public Map<String, String> getApiRequestHeaders() {
        return apiRequestHeaders;
    }

    /**
     * @return apiRequestEncoding
     */
    public Charset getApiRequestEncoding() {
        return apiRequestEncoding;
    }

    /**
     * @return readTimeout
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * @return connectTimeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * デフォルトコンストラクタ
     */
    private TestProperty() {

        load();
    }

    /**
     *
     */
    protected void load() {
        InputStream in = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream(RESOURCE_NAME);

            if (in != null) {
                Properties props = new Properties();
                try {
                    props.load(in);
                    initParams(props);
                } catch (IOException e) {
                    System.out.println("Cannot load \"IllegalDetect.properties\". " + e.toString());
                }
            } else {
                System.out.println("*** DEBUG: Cannot load \"IllegalDetect.properties\".");
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
     * Instanceを取得する。
     *
     * @return インスタンス
     */
    public static TestProperty getInstance() {
        return instance;
    }

    private void initParams(Properties prop) {

        // 共通設定
        readTimeout = Integer.parseInt(prop.getProperty(KEY_READ_TIMEOUT));
        connectTimeout = Integer.parseInt(prop.getProperty(KEY_CONNECT_TIMEOUT));

        // APIの設定
        apiRequestUrl = prop.getProperty(KEY_FULLY_QUALIFIED_DOMAIN_NAME) + prop.getProperty(REQUEST_PATH);
        apiRequestMethod = prop.getProperty(REQUEST_METHOD);

        Map<String, String> m = new HashMap<String, String>();
        for (Object key : prop.keySet()) {
            if (key.toString().startsWith(REQUEST_HEADERS)) {
                m.put(key.toString().substring(REQUEST_HEADERS.length() + 1), prop.getProperty(key.toString()));
            }
        }
        apiRequestHeaders = Collections.unmodifiableMap(m);
        apiRequestEncoding = Charset.forName(prop.getProperty(REQUEST_ENCODING));
     }

}
