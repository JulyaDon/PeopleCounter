package model.DataClasses;

import model.ParametersClasses.XMLwriterReader;

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
        XMLwriterReader xmlWriterReader = new XMLwriterReader();
        try {
            dataLog = xmlWriterReader.ReadDataLog().dataLog;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addData(Data data){
        dataLog.add(data);
        XMLwriterReader xmlWriterReader = new XMLwriterReader();
        try {
            xmlWriterReader.WriteDataLog(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendData(){

        ArrayList<Data> GroupedDataLog = groupByHour (dataLog);

        dataLog = GroupedDataLog;

        XMLwriterReader xmlWriterReader = new XMLwriterReader();
        try {
            xmlWriterReader.WriteDataLog(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataSender dataSender = new DataSender();
        for (Data d: dataLog) {
            if(!d.isTransmitted()) dataSender.SendData(d);
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
