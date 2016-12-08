package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private TextField BarcodeTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(BarcodeTextField.getText() == "barcode"){
            BarcodeTextField.positionCaret(0);
        }
    }
    public void Connect1clicked(ActionEvent actionEvent) {
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
