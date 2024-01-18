package tm.model.classes;

import java.util.Properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ConfigParser {

    public static String parse(String url, String key) {
        Properties properties = new Properties();

        try ( InputStream inputStream = new FileInputStream("app/src/main/resources/config.properties") ) {
            properties.load(inputStream);

            String className = properties.getProperty(key);
            
            return className;
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        return null;
    }

}