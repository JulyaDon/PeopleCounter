package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/sample.fxml"));
        primaryStage.setTitle("People counter");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> onClose());
    }


    public static void main(String[] args) {
        launch(args);
    }


    public void onClose(){
        for (Runnable r : runList) {
            r.run();
        }
    }

    private static ArrayList<Runnable> runList = new ArrayList();

    public static void addRunnable(Runnable runnable){
        runList.add(runnable);
    }

}
