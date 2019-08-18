package property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
    // ��������ַ
    public static String addr = getProperty("addr");
    // �����˿�
    public static Integer port = Integer.parseInt(getProperty("port"));

    static {
        // ��ȡ������Ϣ
        if(addr == null || addr.equals("")){
            addr = "0.0.0.0";
        }
        if(port == null || port == 0){
            port = 4321;
        }
    }

    private static String getProperty(String key){
        Properties prop = new Properties();
        InputStream in = Property.class.getResourceAsStream("/conf/server.conf");
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }
}
