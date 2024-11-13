package ee.ut.math.tvt.salessystem.logic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TeamInfo {

    private String name;



    public String getName() {
        return name;
    }
    public TeamInfo(){
        String file = getClass().getClassLoader().getResource("application.properties").getFile();
        try (FileReader fileReader = new FileReader(file)){
            Properties properties = new Properties();
            properties.load(fileReader);
            this.name = properties.getProperty("name");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
