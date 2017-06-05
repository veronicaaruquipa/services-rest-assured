package support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @project services-rest-assured
 * @user veronica.aruquipa
 * @date 6/1/2017 12:01 AM
 */
public class Constants {
    private static Constants _instance = null;
    public static String baseHost = null;
    public static String service1BasePath = null;
    public static String service2BasePath = null;
    public static String service3BasePath = null;
    public static String requestParam1 = null;
    public static String requestParam2 = null;
    public static String requestParam3 = null;
    public static String requestAuth = null;
    public static String requestAuthValue = null;
    public static String testDataFile = null;
    public static String testReportFile = null;

    protected Constants() throws IOException {
        String configFileName = "config.properties";
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream(configFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + configFileName + "' not found in the classpath");
            }
            baseHost = prop.getProperty("service.host");
            service1BasePath = prop.getProperty("service1.base.path");
            service2BasePath = prop.getProperty("service2.base.path");
            service3BasePath = prop.getProperty("service3.base.path");
            requestParam1 = prop.getProperty("request.param1");
            requestParam2 = prop.getProperty("request.param2");
            requestParam3 = prop.getProperty("request.param3");
            requestAuth = prop.getProperty("request.auth");
            requestAuthValue = prop.getProperty("request.auth.value");
            testDataFile = prop.getProperty("file.input");
            testReportFile = prop.getProperty("file.output");

        }catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
    }

    public static Constants instance() throws IOException {
        if (_instance == null) {
            _instance = new Constants();
        }
        return _instance;
    }
}
