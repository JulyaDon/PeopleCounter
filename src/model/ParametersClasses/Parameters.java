package model.ParametersClasses;

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
            XMLwriterReader reader = new XMLwriterReader();

            try {
                instance = reader.ReadParameters();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Can't read Parameters File");
                instance = new Parameters();
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
}
