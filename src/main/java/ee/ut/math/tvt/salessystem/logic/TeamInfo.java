package ee.ut.math.tvt.salessystem.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TeamInfo {

    private String name;



    public String getName() {
        return name;
    }
    public TeamInfo(){
        ClassLoader classloader = getClass().getClassLoader();
        try (InputStream resourceStream = classloader.getResourceAsStream("application.properties")){
            Properties properties = new Properties();
            properties.load(resourceStream);
            this.name = properties.getProperty("name");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
