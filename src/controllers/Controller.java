package controllers;

import controller.ControllerPeopleDisplay;
import controller.ControllerSerialControlPanel;
import controller.Sensor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.DataClasses.Data;
import model.DataClasses.DataCollect;
import model.DataClasses.DataLogger;
import sample.Parameters;
import sample.OurParameters;
import sample.Settings;
import sample.XMLwriterReader;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML BarcodeController barcodeController;
    OurParameters ourParameters = new OurParameters();
    Settings ourSettings = new Settings();

    XMLwriterReader<OurParameters> writerParameters = new XMLwriterReader<>("src/files/parameters.xml");
    XMLwriterReader<Settings> writerSettings = new XMLwriterReader<>("src/files/settings.xml");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        controllerSerialControlPanel = ControllerSerialControlPanel.Instance;
        controllerPeopleDisplay = ControllerPeopleDisplay.Instance;

        barcodeController = BarcodeController.instance;
        barcodeController.init(this);
        try {
            writerParameters.WriteFile(ourParameters, OurParameters.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            writerSettings.WriteFile(ourSettings, Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            ourSettings = writerSettings.ReadFile(Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int a = 0;

        controllerPeopleDisplay.init(this);
        controllerSerialControlPanel.init(this);

        t.start();
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

    public void onTariff1clicked(ActionEvent actionEvent) {
    }

    public void onTariff2clicked(ActionEvent actionEvent) {
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
}
