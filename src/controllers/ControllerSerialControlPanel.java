package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import jssc.*;
import sample.Main;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Created by Andrew on 08.12.2016.
 */
public class ControllerSerialControlPanel implements Initializable {

    private SerialPort serialPort;

    Controller controller;

    @FXML private ChoiceBox choiceBoxPortsList;

    public static ControllerSerialControlPanel Instance;

    public ControllerSerialControlPanel(){
        Instance = this;
    }

    /*private String[] getComPorts(){
        return SerialPortList.getPortNames();
    }*/

    private ObservableList getComPorts(){
        ObservableList observableList = FXCollections.observableList(Arrays.asList( SerialPortList.getPortNames() ) );
        return  observableList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBoxPortsList.setItems(getComPorts());
        choiceBoxPortsList.getSelectionModel().selectFirst();
    }

    public void init(Controller controller){
        this.controller = controller;
    }

    public void onConnectClick(ActionEvent actionEvent) {
        StartConnection();
        controller.CreateSensor();
        //sensor = new Sensor(choiceBoxPortsList.getValue().toString());
    }

    public void onRefreshClick(ActionEvent actionEvent) {
        choiceBoxPortsList.setItems(getComPorts());
        choiceBoxPortsList.getSelectionModel().selectFirst();
    }

    public void onDisconnectClick(ActionEvent actionEvent) {
        if (serialPort.isOpened())
            Disconnect();
    }

    public SerialPort getSerial(){
        return serialPort;
    }

    private void StartConnection() {
        try {
            //Открываем порт
            if (serialPort==null) {
                serialPort = new SerialPort(choiceBoxPortsList.getValue().toString());
                serialPort.openPort();
            }
            //Выставляем параметры
            //serialPort.setParams(SerialPort.BAUDRATE_9600,
            //        SerialPort.DATABITS_8,
            //        SerialPort.STOPBITS_1,
            //        SerialPort.PARITY_NONE);
            //Включаем аппаратное управление потоком
            //serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
            //        SerialPort.FLOWCONTROL_RTSCTS_OUT);
            //Устанавливаем ивент лисенер и маску
            //serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
            //Отправляем запрос устройству
            //serialPort.writeString("Get data");
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }

        Main.addRunnable(() -> {
            this.Disconnect();
        });
    }

    public void Disconnect(){

        if (serialPort.isOpened()) {
            try {
                System.out.println("CLOSING SERIAL: " + serialPort.getPortName());
                serialPort.closePort();
                System.out.println("PORT CLOSED!");
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
