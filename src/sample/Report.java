package sample;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by July on 10.12.2016.
 */
public class Report {

    public Report(int ticket_barcode, int tariff_id, String tariff_title, int tariff_cost){

        Report defaultReport = null;

        XMLwriterReader<Report> reader = new XMLwriterReader("resources/parameters.xml");
        try {
            defaultReport = reader.ReadFile(Report.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.ticket_activated_at = dateFormat.format(date).toString();

        this.ticket_barcode = ticket_barcode;
        this.tariff_id = tariff_id;
        this.tariff_title = tariff_title;
        this.tariff_cost = tariff_cost;

        this.cashier_id = defaultReport.cashier_id;
        this.cashier_name = defaultReport.cashier_name;
        this.controller_id = defaultReport.controller_id;
        this.enter_id = defaultReport.enter_id;
        this.enter_title = defaultReport.enter_title;
        this.app_id = defaultReport.app_id;
        this.app_title = defaultReport.app_title;

        this.event_id = defaultReport.event_id;
        this.event_title = defaultReport.event_title;
        this.event_from = defaultReport.event_from;
        this.event_to = defaultReport.event_to;
        this.ticket_active = defaultReport.ticket_active;
        this.tariff_color = defaultReport.tariff_color;
        this.tariff_icon = defaultReport.tariff_icon;
        this.ticket_type_id = defaultReport.ticket_type_id;
        this.ticket_type_title = defaultReport.ticket_type_title;
        this.currency_title = defaultReport.currency_title;
        this.cash_id = defaultReport.cash_id;
        this.cash_title = defaultReport.cash_title;
        this.controller_name = defaultReport.controller_name;
        this.ticket_cancel = defaultReport.ticket_cancel;
        this.ticket_canceled_at = defaultReport.ticket_canceled_at;
    }

    public Report(int ticket_barcode, Tariffs tariff){

        Report defaultReport = null;

        XMLwriterReader<Report> reader = new XMLwriterReader("resources/parameters.xml");
        try {
            defaultReport = reader.ReadFile(Report.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.ticket_activated_at = dateFormat.format(date).toString();

        this.ticket_barcode = ticket_barcode;
        this.tariff_id = tariff.getTariff_id();
        this.tariff_title = tariff.getTariff_title();
        this.tariff_cost = tariff.getTariff_cost();

        this.cashier_id = defaultReport.cashier_id;
        this.cashier_name = defaultReport.cashier_name;
        this.controller_id = defaultReport.controller_id;
        this.enter_id = defaultReport.enter_id;
        this.enter_title = defaultReport.enter_title;
        this.app_id = defaultReport.app_id;
        this.app_title = defaultReport.app_title;

        this.event_id = defaultReport.event_id;
        this.event_title = defaultReport.event_title;
        this.event_from = defaultReport.event_from;
        this.event_to = defaultReport.event_to;
        this.ticket_active = defaultReport.ticket_active;
        this.tariff_color = defaultReport.tariff_color;
        this.tariff_icon = defaultReport.tariff_icon;
        this.ticket_type_id = defaultReport.ticket_type_id;
        this.ticket_type_title = defaultReport.ticket_type_title;
        this.currency_title = defaultReport.currency_title;
        this.cash_id = defaultReport.cash_id;
        this.cash_title = defaultReport.cash_title;
        this.controller_name = defaultReport.controller_name;
        this.ticket_cancel = defaultReport.ticket_cancel;
        this.ticket_canceled_at = defaultReport.ticket_canceled_at;
    }

    private String event_id = "7";
    private String event_title = "Party Party";
    private String event_from = "2016-12-01 09:45:27";
    private String event_to = "2017-01-30 23:55:00";
    private int ticket_barcode;// = 1001;
    private int ticket_active = 1;
    private String ticket_activated_at;// = "2016-12-08 17:46:01";
    private  int tariff_id;// = 1;
    private String tariff_title;// = "Полный";
    private String tariff_color = "success";
    private String tariff_icon = "fa fa-man";
    private int ticket_type_id = 3;
    private String ticket_type_title = "Обычный";
    private int tariff_cost;// = 4;
    private String currency_title = "UAH";
    private int cashier_id;// = 111;
    private String cashier_name;// = "Zone 1";
    private int cash_id = 111;
    private String cash_title = "Zone 1";
    private int controller_id;// = 1;
    private String controller_name = "Virtual";
    private int enter_id;// = 111;
    private String enter_title;// = "Zone 1";
    private int ticket_cancel = 0;
    private String ticket_canceled_at = "null";
    private int app_id;// = 1;
    private String app_title;// = "Zone 1";
}
