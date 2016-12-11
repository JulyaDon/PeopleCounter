package model.DataClasses;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrew on 11.12.2016.
 */
public class Data {


    private int in;
    private int out;
    private boolean transmitted = false;
    private Date date = new Date();


    public Data(int in, int out){
        this.in = in;
        this.out = out;
    }

    private Data(){

    }

    public Data clone(){
        Data newData = new Data();
        newData.in = this.in;
        newData.out = this.out;
        newData.date = this.date;
        newData.transmitted = this.transmitted;

        return  newData;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public int getOut() {
        return out;
    }

    public void setOut(int out) {
        this.out = out;
    }

    public Date getDate() {

        return date;
    }

    public String getDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date).toString();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isTransmitted() {
        return transmitted;
    }

    public void setTransmitted(boolean transmitted) {
        this.transmitted = transmitted;
    }
}
