package controller;

import controllers.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Andrew on 08.12.2016.
 */
public class ControllerPeopleDisplay implements Initializable {
    public static ControllerPeopleDisplay Instance;

    Controller controller;

    @FXML private CategoryAxis xAxis = new CategoryAxis();
    @FXML final NumberAxis yAxis = new NumberAxis();

    @FXML LineChart lineChart;
    @FXML Label labelPeopleNumber;

    public ControllerPeopleDisplay(){
        Instance = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void init(Controller controller){
        this.controller = controller;
    }

    public void Draw(){

        int[] array = new int[50];

        Random r = new Random(System.currentTimeMillis());
        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < array.length ; i++) {
            array[i] = r.nextInt(200);
        }

        for (int i = 0; i < array.length ; i++) {
            series.getData().add(new XYChart.Data( Integer.toString(i),array[i]));
        }

        lineChart.getData().clear();

        lineChart.getData().add(series);
        labelPeopleNumber.setText("test");
    }

    public void Draw(int[] array){

        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < array.length ; i++) {
            series.getData().add(new XYChart.Data( Integer.toString(i),array[i]));
        }
        lineChart.getData().clear();
        lineChart.getData().add(series);
    }

    public void Draw(int[] array, int index){
        Draw(array);

        lineChart.setStyle("CHART_COLOR_2: #0000FF;");

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data( Integer.toString(index),array[index]));

        labelPeopleNumber.setText( Integer.toString( array[index] ));

        lineChart.getData().add(series);
    }

    public void setCount(int count){
        labelPeopleNumber.setText(Integer.toString(count));
    }

}
