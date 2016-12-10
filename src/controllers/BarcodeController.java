package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Created by July on 09.12.2016.
 */
public class BarcodeController {
    private Controller main;
    @FXML public TextField BarcodeTextField;

    public static BarcodeController instance;

    public BarcodeController()
    {
        instance = this;
    }


    public void init(Controller controller) {
        main = controller;
    }

    @FXML
    public void onEnter(ActionEvent ae){
        BarcodeTextField.setText("");
    }

}
