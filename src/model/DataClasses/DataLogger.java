package model.DataClasses;

import sample.XMLwriterReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Andrew on 11.12.2016.
 */
public class DataLogger implements DataCollect.ICountChangeHandler {
    @Override
    public void handle() {

    }

    private ArrayList<Data> dataLog = new ArrayList<>();

    public DataLogger(){
        XMLwriterReader<model.DataClasses.DataLogger> xmlWriterReader = new XMLwriterReader("resources/dataLog.xml");
/*
        try {
            xmlWriterReader.WriteFile(this, DataLogger.class);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            dataLog = xmlWriterReader.ReadFile(model.DataClasses.DataLogger.class).dataLog;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addData(Data data){
        dataLog.add(data);
        XMLwriterReader xmlWriterReader = new XMLwriterReader("resources/dataLog.xml");
        try {
            xmlWriterReader.WriteFile(this,DataLogger.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendData(){

        ArrayList<Data> GroupedDataLog = groupByHour (dataLog);

        dataLog = GroupedDataLog;

        DataSender dataSender = new DataSender();
        for (int i = 0; i < dataLog.size() ; i++) {
            if(!dataLog.get(i).isTransmitted()) dataLog.get(i).setTransmitted( dataSender.SendData(dataLog.get(i)) );
        }

        XMLwriterReader xmlWriterReader = new XMLwriterReader("resources/dataLog.xml");
        try {
            xmlWriterReader.WriteFile(this,DataLogger.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Data> groupByHour (ArrayList<Data> list){

        ArrayList<Data> newList = new ArrayList();
        Calendar cal = Calendar.getInstance();
        int hours = 0;

        Data currentData = list.get(0).clone();
        cal.setTime(currentData.getDate());
        hours = cal.get(Calendar.HOUR_OF_DAY);

        for (int i = 1; i < list.size(); i++) {
            cal.setTime(list.get(i).getDate());
            int currentHours = cal.get(Calendar.HOUR_OF_DAY);

            if (hours == currentHours ){
                if ((currentData.isTransmitted() == list.get(i).isTransmitted()))
                    currentData.setIn(currentData.getIn()+list.get(i).getIn());
                else
                    newList.add(list.get(i).clone());
            } else if (hours < currentHours){
                newList.add(currentData.clone());
                currentData = list.get(i).clone();
                hours = currentHours;
            }
        }
        newList.add(currentData.clone());
        return newList;
    }
}
