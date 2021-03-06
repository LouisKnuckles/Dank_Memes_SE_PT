package core.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.*;

import java.io.File;


/**
 * Created by louie on 10/03/2017.
 */
public class Main extends Application{

    private static final Logger log = LogManager.getLogger(Main.class.getName());

    @Override
    public void start (Stage primaryStage){
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("resources/LoginPage.fxml"));
            primaryStage.setTitle("Appointment Booking System");
            primaryStage.setScene(new Scene(root, 384, 600));
            primaryStage.show();
        }
        catch(Exception e){
            log.error(e.getMessage());
        }
    }

    public static void main(String[] args) {

        log.debug("Beginning Program");

        File file = new File(".");
        for(String fileNames : file.list()) System.out.println(fileNames);

        /* to do driver */
        Database.getInstance();
        launch(args);




    }
}