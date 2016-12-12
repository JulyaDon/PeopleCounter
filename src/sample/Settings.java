package sample;

import java.util.ArrayList;

/**
 * Created by July on 11.12.2016.
 */
public class Settings {
    public Settings(){
        obtainTariffs();
    }
    private String app_title = "zone1";
    private int app_id = 111;
    private int event_id = 1;
    private String event_title = "NY Party";
    private String location = "zone1_east";
    private String reportApiLink = "http://tlreport.cifr.us/reportAccepter";
    private String reportApiToken = "tls";
    private String counterApiLink = "http://tlcounter.cifr.us/api/cam-add";
    ArrayList<Tariffs> OurTariffs = new ArrayList<>();
    Tariffs AllTariffs = new Tariffs();
    public void obtainTariffs(){
        AllTariffs.addBarcodes();
        OurTariffs.add(AllTariffs);
    }

    public ArrayList<Tariffs> getArrayOfTariffs(){
        return this.OurTariffs;
    }

}
