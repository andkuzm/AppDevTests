package ee.ut.math.tvt.salessystem.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class TeamController implements Initializable {
    private static final Logger log = LogManager.getLogger(TeamController.class);

    private String name;
    private String leader;
    private String mail;
    private String members;

    public String getName() {
        return name;
    }

    public String getLeader() {
        return leader;
    }

    public String getMail() {
        return mail;
    }

    public String getMembers() {
        return members;
    }
    @FXML
    private TextArea membersTextArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getResourceAsStream("/application.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            log.error("Error loading application.properties file: " + e.getMessage(), e);
            e.printStackTrace();
        }
        name = properties.getProperty("name");
        leader = properties.getProperty("leader");
        mail = properties.getProperty("mail");
        members = properties.getProperty("members");
        membersTextArea.setText(members);
    }
}
