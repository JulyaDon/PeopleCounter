package sample;

import java.util.ArrayList;

/**
 * Created by Andrew on 14.12.2016.
 */
public class ReportSender {

    private ArrayList<Report> report;
    private String token = "tls";

    public ReportSender(ArrayList<Report> reportsToSend){
        report = reportsToSend;
    }

    public void sendReport(){

    }
}
