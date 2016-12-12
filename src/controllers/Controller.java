package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import model.DataClasses.Data;
import model.DataClasses.DataCollect;
import model.DataClasses.DataLogger;
import sample.Parameters;
import sample.OurParameters;
import sample.Settings;
import sample.XMLwriterReader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML BarcodeController barcodeController;
    @FXML TabPane tabContainer;
    @FXML Button buttonTariff1;
    @FXML Button buttonTariff2;

    OurParameters ourParameters = new OurParameters();
    Settings ourSettings = new Settings();

    String fileWithSettings = "resources/settings.xml";
    XMLwriterReader<OurParameters> writerParameters = new XMLwriterReader<>("resources/parameters.xml");
    XMLwriterReader<Settings> writerSettings = new XMLwriterReader<>(fileWithSettings);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        controllerSerialControlPanel = ControllerSerialControlPanel.Instance;
        controllerPeopleDisplay = ControllerPeopleDisplay.Instance;

        barcodeController = BarcodeController.instance;
        barcodeController.init(this);

        //ЗАПИСЬ РЕПОРТОВ В XML
        try {
            writerParameters.WriteFile(ourParameters, OurParameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //СЧИТЫВАНИЕ РЕПОРТОВ ИЗ XML
        try {
            ourParameters = writerParameters.ReadFile(OurParameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }




        if (!(new File(fileWithSettings)).exists()) {
            //ЗАПИСЬ НАСТРОЕК В XML
            try {
                writerSettings.WriteFile(ourSettings, Settings.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //СЧИТЫВАНИЕ НАСТРОЕК ИЗ XML
        try {
            ourSettings = writerSettings.ReadFile(Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        buttonTariff1.setText(ourSettings.getArrayOfTariffs().get(1).tariff_title);
        buttonTariff2.setText(ourSettings.getArrayOfTariffs().get(2).tariff_title);
        controllerPeopleDisplay.init(this);
        controllerSerialControlPanel.init(this);

        t.start();

    }

    private void resetFocus(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                barcodeController.BarcodeTextField.requestFocus();
            }
        });
    }

    public void Connect1clicked(ActionEvent actionEvent) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                barcodeController.BarcodeTextField.requestFocus();
            }
        });
    }

    public void onTariff1clicked(ActionEvent actionEvent) {
        resetFocus();
    }

    public void onTariff2clicked(ActionEvent actionEvent) {
        resetFocus();
    }


    /////////////////////////////////ANDREW'S PART////////////////////////////////////////
    ControllerSerialControlPanel controllerSerialControlPanel;
    ControllerPeopleDisplay controllerPeopleDisplay;

    Parameters parameters = Parameters.getInstance();

    Timer t = new Timer(parameters.getDataSendTimeout(), e -> onTimer());

    public Sensor s1 = null;

    DataCollect c = new DataCollect(100);
    DataLogger dataLogger = new DataLogger();

    private void onTimer(){
        dataLogger.SendData();
    }

    public void CreateSensor(){
        Parameters parameters = Parameters.getInstance();

        s1 = new Sensor(controllerSerialControlPanel.getSerial());
        s1.AddOnDataAvailableHandler(data -> onNewData(data));
        s1.AddOnDataAvailableHandler(data -> Platform.runLater(() -> controllerPeopleDisplay.Draw(c.getDataFocusNew())) );
        c.addOnCountChange(() -> Platform.runLater(() -> controllerPeopleDisplay.setCount(c.getCount())));
        c.addOnCountChange(() -> {
            if (c.getCountDifference()!=0)
                dataLogger.addData( new Data(c.getCountDifference(),0));
        });
    }

    private void onNewData(int data){
        c.addData(data);
    }

    public void tabSelectionChange(Event event) {
        resetFocus();
    }

    public void onMouseClick(Event event) {
        resetFocus();
    }
}
