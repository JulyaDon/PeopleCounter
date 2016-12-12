package sample;

import java.util.ArrayList;

/**
 * Created by July on 11.12.2016.
 */
public class Tariffs {
    private String tariff_title = "Бесплатный ТМ Шостка";
    private int tariff_id = 1;
    private int tariff_cost = 0;
    boolean show = false; // скрытый	 тариф, не будет показан кассиру
    ArrayList<Integer> Barcodes = new ArrayList<>();

    public void addBarcodes(){
        Barcodes.add(79668321);
        Barcodes.add(79668322);
        Barcodes.add(79668323);
    }
    public ArrayList<Integer> getBarcodes(){
        //addBarcodes();
        return Barcodes;
    }

    public String getTariff_title(){
        return this.tariff_title;
    }
}
