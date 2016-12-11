package controller;

import java.util.ArrayList;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * Created by Andrew on 08.12.2016.
 */
public class Sensor implements SerialPortEventListener {

    //private String PortName = "COM8";

    public Sensor (SerialPort serialPort){
        this.serialPort = serialPort;

        //Выставляем параметры
        try {
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            //Включаем аппаратное управление потоком
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);
            //Устанавливаем ивент лисенер и маску
            serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
            //Отправляем запрос устройству
            serialPort.writeString("Get data");
        }

        catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public interface OnDataAvailableHandler{
        void handle(int data);
    }

    private Sensor(){
    }

    SerialPort serialPort = null;//new SerialPort(PortName);

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if(serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0){
            try {
                //Получаем ответ от устройства, обрабатываем данные и т.д.
                String data = serialPort.readString(serialPortEvent.getEventValue());
                byte[] buffer = serialPort.readBytes(4);
                int dat = (buffer[3]&0xFF) | ((buffer[2]&0xFF)<<8);
                //System.out.println(dat);
                fireDataAvailableEvent(dat);
                //System.out.println(dat + " : " + (((int)buffer[3]) & 0xFF ));

            }
            catch (SerialPortException ex) {
                System.out.println(ex);
            }
        }
    }

    private ArrayList<OnDataAvailableHandler> onDataAvailableHandlers = new ArrayList<>();

    public void AddOnDataAvailableHandler(OnDataAvailableHandler handler){
        onDataAvailableHandlers.add(handler);
    }

    public void RemoveOnDataAvailableHandler(OnDataAvailableHandler handler){
        onDataAvailableHandlers.remove(handler);
    }

    private void fireDataAvailableEvent(int data){
        for ( OnDataAvailableHandler h : onDataAvailableHandlers ){
            h.handle(data);
        }
    }

    public boolean isConnected(){
        return serialPort.isOpened();
    }

}
