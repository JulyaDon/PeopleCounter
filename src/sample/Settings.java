package sample;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by July on 11.12.2016.
 */
public class Settings {

    private Settings(){

    }

    public static Settings getInstance(){
        if(instance == null){
            XMLwriterReader<sample.Settings> reader = new XMLwriterReader<>("resources/settings.xml");

            try {
                instance = reader.ReadFile(Settings.class);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Can't read Parameters File");
                instance = new Settings();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private static Settings instance;

    private String app_title;// = "zone1";
    private int app_id;// = 111;
    private int event_id;// = 1;
    private String event_title;// = "NY Party";
    private String location;// = "zone1_east";
    private String reportApiLink;//= "http://tlreport.cifr.us/reportAccepter";
    private String reportApiToken;// = "tls";
    private String counterApiLink;// = "http://tlcounter.cifr.us/api/cam-add";
    ArrayList<Tariffs> OurTariffs = new ArrayList<>();
    /*
    public void obtainTariffs(){
        AllTariffs.addBarcodes();
        OurTariffs.add(AllTariffs);
    }
    */

    public ArrayList<Tariffs> getArrayOfTariffs(){
        return this.OurTariffs;
    }

    public String getApp_title() {
        return app_title;
    }

    public int getApp_id() {
        return app_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public String getLocation() {
        return location;
    }

    public String getReportApiLink() {
        return reportApiLink;
    }

    public String getReportApiToken() {
        return reportApiToken;
    }

    public String getCounterApiLink() {
        return counterApiLink;
    }
}
