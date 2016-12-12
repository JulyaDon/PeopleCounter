package controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import model.DataClasses.Data;
import model.DataClasses.DataCollect;
import model.DataClasses.DataLogger;
import sample.Parameters;
import sample.Report;
import sample.Settings;
import sample.XMLwriterReader;
import sample.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML BarcodeController barcodeController;
    @FXML TabPane tabContainer;
    @FXML Button buttonTariff1;
    @FXML Button buttonTariff2;
    @FXML TextArea textAreaLatest;

    Report ourParameters = new Report(99999,99999,"sssssssssssssssssss",99999);;// = new Report();
    Settings ourSettings = new Settings();

    String fileWithSettings = "resources/settings.xml";
    XMLwriterReader<Report> writerParameters = new XMLwriterReader<>("resources/parameters.xml");
    XMLwriterReader<Settings> writerSettings = new XMLwriterReader<>(fileWithSettings);
    XMLwriterReader<ArrayList<Report>> writerReports = new XMLwriterReader<>("resources/reports.xml");

    ArrayList<Report> ReportList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        controllerSerialControlPanel = ControllerSerialControlPanel.Instance;
        controllerPeopleDisplay = ControllerPeopleDisplay.Instance;

        barcodeController = BarcodeController.instance;
        barcodeController.init(this);

        //ЗАПИСЬ ПАРАМЕТРОВ В XML
        /*
        try {
            writerParameters.WriteFile(ourParameters, Report.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        //СЧИТЫВАНИЕ ПАРАМЕТРОВ ИЗ XML
        /*
        try {
            ourParameters = writerParameters.ReadFile(Report.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */
        //СЧИТЫВАНИЕ РЕПОРТОВ ИЗ XML
        try {
            ReportList = writerReports.ReadFile(Report.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //ReportList.add(new Report(99999,99999,"sssssssssssssssssss",99999));
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

        buttonTariff1.setText(ourSettings.getArrayOfTariffs().get(1).getTariff_title());
        buttonTariff2.setText(ourSettings.getArrayOfTariffs().get(2).getTariff_title());
        controllerPeopleDisplay.init(this);
        controllerSerialControlPanel.init(this);

        textAreaLatest.textProperty().addListener((observable, oldValue, newValue) -> {
            textAreaLatest.setScrollTop(Double.MIN_VALUE); //this will scroll to the bottom
            //use Double.MIN_VALUE to scroll to the top
        });
    }

    public Tariffs checkBarcodes(String barcodeFromTextField) {
        Tariffs tafiffWeLookFor = null;
        if(!(barcodeController.BarcodeTextField.getText().isEmpty())){
            int inputedBarCode = Integer.valueOf(barcodeFromTextField);
            int currentBarCode;
            for (int i = 0; i < ourSettings.getArrayOfTariffs().size(); i++) {
                for(int j = 0; j < ourSettings.getArrayOfTariffs().get(i).getBarcodes().size(); j++){
                    currentBarCode = ourSettings.getArrayOfTariffs().get(i).getBarcodes().get(j);
                    if (inputedBarCode == currentBarCode) {
                        tafiffWeLookFor = ourSettings.getArrayOfTariffs().get(i);
                        System.out.println(tafiffWeLookFor.getTariff_title());
                    }
                }
            }
        }
        return tafiffWeLookFor;
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

        try {
            int barcode = Integer.valueOf(barcodeController.BarcodeTextField.getText());

            Tariffs ourTariff = checkBarcodes(barcodeController.BarcodeTextField.getText());

            Report report = new Report(barcode, ourTariff);

            ReportList.add(report);

            String note = "Штрих-код: " + barcode + " Тариф: " + ourTariff.getTariff_title() + "\n";
            textAreaLatest.setText(textAreaLatest.getText() + note);
        } catch (Exception e){
            //e.printStackTrace();
            String note = "ШТРИХ-КОД НЕ ЗНАЙДЕНО! \n";
            textAreaLatest.setText(textAreaLatest.getText() + note);
        }

        //ЗАПИСЬ РЕПОРТОВ В XML
        try {
            writerReports.WriteFile(ReportList, Report.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onTariff2clicked(ActionEvent actionEvent) {
        resetFocus();

        Tariffs defaultTariff = new Tariffs();

        Report report = new Report(1001, new Tariffs());

        ReportList.add(report);

        String note = "Тариф: " + defaultTariff.getTariff_title() + "\n";
        textAreaLatest.setText(textAreaLatest.getText() + note);

        //ЗАПИСЬ РЕПОРТОВ В XML
        try {
            writerReports.WriteFile(ReportList, Report.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        t.start();

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
