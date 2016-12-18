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
import javafx.scene.control.TextArea;
import model.DataClasses.Data;
import model.DataClasses.DataCollect;
import model.DataClasses.DataLogger;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Parameters;
import sample.Report;
import sample.Settings;
import sample.XMLwriterReader;
import sample.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML BarcodeController barcodeController;
    @FXML TabPane tabContainer;
    @FXML Button buttonTariff1;
    @FXML Button buttonTariff2;
    @FXML TextArea textAreaLatest;
    @FXML Tab tabCounter;

    Date date = new Date();

    Date dateStart = new Date();

    //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String todayDate = dateFormat.format(date).toString();

    Settings ourSettings = Settings.getInstance();

    //String fileWithSettings = "resources/settings.xml";
    String fileReportsForToday = "resources/reports/" + todayDate + ".xml";
    //XMLwriterReader<Settings> writerSettings = new XMLwriterReader<>(fileWithSettings);
    XMLwriterReader<ArrayList<Report>> writerReports = new XMLwriterReader<>(fileReportsForToday);

    ArrayList<Report> ReportList = new ArrayList<>();

    ControllerSerialControlPanel controllerSerialControlPanel;
    ControllerPeopleDisplay controllerPeopleDisplay;
    ControllerTicketReport controllerTicketReport;

    Parameters parameters = Parameters.getInstance();

    Timer t = new Timer(parameters.getDataSendTimeout(), e -> onTimer());
    Timer tDateControl = new Timer(200, e -> onDateControlTimer());

    public Sensor s1 = null;

    DataCollect c = new DataCollect(100);
    public DataLogger dataLogger = new DataLogger();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        controllerSerialControlPanel = ControllerSerialControlPanel.Instance;
        controllerPeopleDisplay = ControllerPeopleDisplay.Instance;
        controllerTicketReport = ControllerTicketReport.Instance;

        barcodeController = BarcodeController.instance;
        barcodeController.init(this);

        if (new File(fileReportsForToday).exists()) {
            //СЧИТЫВАНИЕ РЕПОРТОВ ИЗ XML
            try {
                ReportList = writerReports.ReadFile(Report.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        controllerPeopleDisplay.init(this);
        controllerSerialControlPanel.init(this);
        controllerTicketReport.init(this);



        //Запис репортів по закриттю
        Main.addRunnable(() -> WriteReports(ReportList));
        //Відправка репортів по закриттю
        Main.addRunnable(() -> {
            ReportSender reportSender = new ReportSender(ReportList);
            reportSender.sendReportTest();});
        //Відправка логу на сервер
        Main.addRunnable(() -> dataLogger.SendData());

        Main.addRunnable(() -> {
            if (t.isRunning()) t.stop();
            if (tDateControl.isRunning()) tDateControl.stop();
        });

        tDateControl.start();

        Parameters parameters = Parameters.getInstance();

        if(parameters.getShowCounter()==false){
            tabCounter.setDisable(true);
        }

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

        textAreaLatest.setScrollTop(Double.MAX_VALUE);

        //ЗАПИСЬ РЕПОРТОВ В XML
        WriteReports(ReportList);
    }

    public void onTariff2clicked(ActionEvent actionEvent) {
        resetFocus();

        Tariffs defaultTariff = new Tariffs();

        Report report = new Report(1001, new Tariffs());

        ReportList.add(report);

        String note = "Тариф: " + defaultTariff.getTariff_title() + "\n";
        textAreaLatest.setText(textAreaLatest.getText() + note);

        textAreaLatest.setScrollTop(Double.MAX_VALUE);

        //ЗАПИСЬ РЕПОРТОВ В XML
        WriteReports(ReportList);

    }

    private void WriteReports(ArrayList<Report> reports){

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-d");

        String address = "resources/reports/" + dateFormat.format(currentDate).toString() + ".xml";

        XMLwriterReader<ArrayList<Report>> writer = new XMLwriterReader<>(address);

        try {
            writer.WriteFile(reports, Report.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        textAreaLatest.setScrollTop(Double.MAX_VALUE);
    }

    private void WriteReports(String date, ArrayList<Report> reports){

        String address = "resources/reports/" + date + ".xml";

        XMLwriterReader<ArrayList<Report>> writer = new XMLwriterReader<>(address);

        try {
            writer.WriteFile(reports, Report.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        textAreaLatest.setScrollTop(Double.MAX_VALUE);
    }

    /////////////////////////////////ANDREW'S PART////////////////////////////////////////

    private void onTimer(){
        dataLogger.SendData();
/*
        if (s1 != null && s1.isConnected()) {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            controllerSerialControlPanel.setStatus(dateFormat.format(date).toString() + ": Підключено");
        }
        else{
            controllerSerialControlPanel.setStatus("НЕ ПІДКЛЮЧЕНО!");
        }*/
    }

    private void onDateControlTimer() {
        Date currentDate = new Date();

        DateFormat dateFormat = new SimpleDateFormat("dd");
        DateFormat dateFormatFull = new SimpleDateFormat("YYYY-MM-dd");

        int dateOld = Integer.valueOf(dateFormat.format(dateStart).toString());
        int dateCurrent = Integer.valueOf(dateFormat.format(currentDate).toString());

        if (dateCurrent > dateOld){
            WriteReports(dateFormatFull.format(dateStart).toString(),ReportList);
            //Відправка репортів по закриттю
            ReportSender reportSender = new ReportSender(ReportList);
            reportSender.sendReport();

            ReportList = new ArrayList<Report>();
            dateStart = currentDate;
        }
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
