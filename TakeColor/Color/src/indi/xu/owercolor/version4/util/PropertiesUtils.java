package indi.xu.owercolor.version4.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;

/**配置文件类
 * @author a_apple
 * @create 2019-09-25 19:20
 */
public class PropertiesUtils {

    private static Properties prop;
    private static final String PATH = "D:\\color.properties";
    static Logger logger = Logger.getLogger(PropertiesUtils.class);

    static {
        //加载配置文件，如果没有，则先创建一个配置文件
        prop = new Properties();
        try {
            File properties = new File(PATH);
            if (!properties.exists()) {
                //文件不存在，生成
                boolean newFile = properties.createNewFile();
                logger.warn("无配置文件--生成配置文件"+newFile);
            }
            FileInputStream fis = new FileInputStream(properties);
            prop.load(fis);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //通过键，获取值
    public String getValue(String key) {
        return this.getValue(key, null);
    }

    public String getValue(String key, String dufault) {
        return prop.getProperty(key, dufault);
    }

    /**
     * 设置键值对到配置文件.store与 load 方法相反
     *
     * @param key   存储的键
     * @param value 值
     */
    public void setValue(String key, String value) {
        prop.setProperty(key, value);
        try (OutputStream outputStream = new FileOutputStream(PATH)) {
            prop.store(outputStream, "");
        } catch (IOException e) {
            logger.error("设置键值对失败");
        }
    }
}
