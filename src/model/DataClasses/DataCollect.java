package model.DataClasses;

import sample.Parameters;

import java.util.ArrayList;

/**
 * Created by Andrew on 10.12.2016.
 */
public class DataCollect {

    Parameters parameters = Parameters.getInstance();

    public interface ICountChangeHandler{
        void handle();
    }

    /**
     * Подія на зміну значення лічильника
     */
    private ArrayList<ICountChangeHandler> onChangehandlers = new ArrayList<>();

    /**
     * Додати обробник події зміни лічильника
     * @param handler
     */
    public void addOnCountChange(ICountChangeHandler handler){
        onChangehandlers.add(handler);
    }

    /**
     * Видалити обробник події зміни лічильника
     * @param handler
     */
    public void removeOnCountChange(ICountChangeHandler handler){
        onChangehandlers.remove(handler);
    }

    /**
     * Запуск події зміни лічильника
     */
    private void fireOnCountEvent(){
        for (ICountChangeHandler h : onChangehandlers)
            h.handle();
    }

    private int[] sensorData;
    private int sensorDataIndex = 0;

    private int lastCountPointer;
    private double Count = 0;
    private double CountOld = 0;
    private double CountDifference = 0;
    private double remainder = 0;

    boolean newZone = true;

    /**
     * Об'єкт збору даних
     * @param size розмір масиву даних, що аналізуватиметься
     */
    public DataCollect(int size){
        int size1 = size;

        sensorData = new int[size1];
        lastCountPointer = sensorData.length;

    }

    /**
     * Метод додає нові дані та проводить їх аналіз
     * @param data нові дані
     */
    public void addData(int data){
        if (sensorDataIndex == sensorData.length) {
            sensorDataIndex = 0;
            //sensorData = sensorDataDefault.clone();
        }

        ///////////////////////////////////////////////////

        if (lastCountPointer == 0){
            int[] array = getDataFocusNew();
            //int acceptLevel = 40;
            int pulseWidth = 0;
            //int pulseThreshold = 1;

            for (int i = 0; i < array.length ; i++) {

                if (array[i] <= parameters.getAcceptLevel()) {

                    pulseWidth++;

                    if ((newZone == true)&&(pulseWidth > parameters.getPulseWidthThreshold())) {
                        Count++;
                        newZone = false;
                    }
                } else {
                    newZone = true;
                    pulseWidth = 0;
                }
            }
            lastCountPointer = sensorData.length;
        } else {
            lastCountPointer--;
        }


        if (Count>CountOld){

            CountDifference = Count-CountOld;

            int result = (int)(CountDifference/2 + remainder);
            remainder = (CountDifference/2) + remainder - (int)(CountDifference/2);

            if (remainder == 1) remainder = 0;

            CountDifference = result;

            CountOld = Count;
            fireOnCountEvent();
        }

        ///////////////////////////////////////////////////

        sensorData[sensorDataIndex] = data;
        sensorDataIndex++;

    }

    /**
     * Повертає кількість проходів
     * @return
     */
    public int getCount(){
        return (int)(Count/2);
    }


    public int getCountDifference(){

        return (int)CountDifference;
    }


    public int getCurrentIndex(){
        return sensorDataIndex;
    }

    /**
     * Повертає кільцевий масив даних у чистому вигляді
     * @return
     */
    private int[] getData(){
        return sensorData.clone();
    }

    /**
     * Повертає масив даних, де самий новий елемент - останній
     * @return
     */
    public int[] getDataFocusNew(){
        int[] array = getData();

        int[] arrayNew = new int[array.length];

        int index = sensorDataIndex;

        for (int i = 0, j=index; i < array.length-index; i++, j++) {
            arrayNew[i] = array[j];
        }

        for (int i = array.length - index, j=0; i < arrayNew.length; i++, j++) {
            arrayNew[i] = array[j];
        }

        return arrayNew;
    }
}
