package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import sample.Report;
import sample.ReportSender;
import sample.XMLwriterReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sergiy on 12/17/2016.
 */
public class ControllerTicketReport {

    @FXML TextArea TAticketsPerDay;
    @FXML ChoiceBox CBticketData;

    ArrayList<Report> reportList = new ArrayList<>();
    public static ControllerTicketReport Instance;

    Controller controller;

    public ControllerTicketReport(){
        Instance = this;
    }


    public void init(Controller controller) {
        this.controller = controller;
        //ОТРИМУЄМО ІМЕНА .XML ФАЙЛІВ В ПАПЦІ "reports"
        File[] fList;
        File F = new File("resources/reports");

        fList = F.listFiles();
        //заповнюємо ChoiceBox
        for (int i = 0; i < fList.length; i++) {
            String s = fList[i].getName();
            s = s.replace(".xml", "");
            CBticketData.getItems().add(s);
        }

        //обробляемо подію зміни значення ChoiceBox-a
        CBticketData.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            getReport();

        });

    }

    private void getReport(){
        if (/*CBticketData.getItems().get((Integer) number2)*/ CBticketData.getValue() != null) {
            TAticketsPerDay.setText("-------------------\n");

            //отримуємо текст вибраного елемента з ChoiceBox-a
            int temp = CBticketData.getSelectionModel().getSelectedIndex();
            String neededData = CBticketData.getItems().get(temp).toString();

            String fileReport = "resources/reports/" + neededData + ".xml";
            XMLwriterReader<ArrayList<Report>> writerReports = new XMLwriterReader<>(fileReport);

            //СЧИТЫВАНИЕ РЕПОРТОВ ИЗ XML
            try {
                reportList = writerReports.ReadFile(Report.class);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            ArrayList<String> tariffs = new ArrayList<>();
            //отримуємо імена існуючих в репорті тарифів
            for (Report rep : reportList
                    ) {
                tariffs.add(rep.getTariff_title());
                for (int i = 0; i < tariffs.size() - 1; i++) {
                    if (tariffs.get(i) == rep.getTariff_title()) {
                        tariffs.remove(i);
                    }
                }
            }

            //підраховуємо і виводимо дані по кожному тарифу
            for (String s : tariffs) {
                int cost = 0;
                int ticketsNumber = 0;
                for (Report r : reportList) {
                    if (r.getTariff_title() == s) {
                        cost += r.getTariff_cost();
                        ticketsNumber += 1;
                    }
                }
                TAticketsPerDay.setText(TAticketsPerDay.getText()
                        + "Назва тарифу: " + s + "\n"
                        + "Вартість: " + cost / ticketsNumber + "\n"
                        + "Кількість проданих квитків: " + ticketsNumber + "\n"
                        + "Загальна вартість квитків: " + cost + "\n-------------------" + "\n");
            }
        }
    }

    public void onRefreshClick(ActionEvent actionEvent) {
        getReport();
    }

    public void onSendClick(ActionEvent actionEvent) {
        getReport();
        ReportSender reportSender = new ReportSender(reportList);
        reportSender.sendReportTest();
        controller.dataLogger.SendData();
    }
}
