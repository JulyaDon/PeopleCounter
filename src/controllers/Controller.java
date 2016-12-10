package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML BarcodeController barcodeController;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        barcodeController = BarcodeController.instance;
        barcodeController.init(this);

    }


    public void Connect1clicked(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                barcodeController.BarcodeTextField.requestFocus();
            }
        });
    }

    public void Disconnect1clicked(ActionEvent actionEvent) {
    }

    public void Refresh1clicked(ActionEvent actionEvent) {
    }

    public void Connect2clicked(ActionEvent actionEvent) {
    }

    public void Disconnect2clicked(ActionEvent actionEvent) {
    }

    public void Refresh2clicked(ActionEvent actionEvent) {
    }
}
