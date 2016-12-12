package sample;

import java.io.IOException;


/**
 * Created by July on 23.09.2016.
 */
//TODO: Rework using Singleton
public class Parameters {

    private Parameters(){

    }

    public static Parameters getInstance(){
        if(instance == null){
            XMLwriterReader<sample.Parameters> reader = new XMLwriterReader<>("resources/controller_parameters.xml");

            try {
                instance = reader.ReadFile(Parameters.class);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Can't read Parameters File");
                instance = new Parameters();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private static Parameters instance;

    private String location = "";
    private String apiURL = "";
    private int acceptLevel;
    private int pulseWidthThreshold;
    private int dataSendTimeout;
    private boolean sendData;

    /*
    private int cashier_id;
    private String cashier_name;
    private int cash_id;
    private String cash_title;
    private int controller_id;
    private int enter_id;
    private String enter_title;
    private int app_id;
    private String app_title;
*/
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAcceptLevel() {
        return acceptLevel;
    }

    public void setAcceptLevel(int acceptLevel) {
        this.acceptLevel = acceptLevel;
    }

    public int getPulseWidthThreshold() {
        return pulseWidthThreshold;
    }

    public void setPulseWidthThreshold(int pulseWidthThreshold) {
        this.pulseWidthThreshold = pulseWidthThreshold;
    }

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public int getDataSendTimeout() {
        return dataSendTimeout;
    }

    public void setDataSendTimeout(int dataSendTimeout) {
        this.dataSendTimeout = dataSendTimeout;
    }

    public boolean isSendData() {
        return sendData;
    }

    public void setSendData(boolean sendData) {
        this.sendData = sendData;
    }
/*
    public int getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(int cashier_id) {
        this.cashier_id = cashier_id;
    }

    public String getCashier_name() {
        return cashier_name;
    }

    public void setCashier_name(String cashier_name) {
        this.cashier_name = cashier_name;
    }

    public int getCash_id() {
        return cash_id;
    }

    public void setCash_id(int cash_id) {
        this.cash_id = cash_id;
    }

    public String getCash_title() {
        return cash_title;
    }

    public void setCash_title(String cash_title) {
        this.cash_title = cash_title;
    }

    public int getController_id() {
        return controller_id;
    }

    public void setController_id(int controller_id) {
        this.controller_id = controller_id;
    }

    public int getEnter_id() {
        return enter_id;
    }

    public void setEnter_id(int enter_id) {
        this.enter_id = enter_id;
    }

    public String getEnter_title() {
        return enter_title;
    }

    public void setEnter_title(String enter_title) {
        this.enter_title = enter_title;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public String getApp_title() {
        return app_title;
    }

    public void setApp_title(String app_title) {
        this.app_title = app_title;
    }*/
}
