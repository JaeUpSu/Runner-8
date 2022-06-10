package com.example.Runner8.ui.F_H.health.SingleTon;

import com.example.Runner8.ui.F_H.health.Adapter.Model.HealthData;

import java.util.ArrayList;

public class HealthSingleTon {

    ArrayList<HealthData> array_healthData = new ArrayList<>();

    private static final HealthSingleTon SINGLE_TON_INSTANCE = new HealthSingleTon();

    public static synchronized HealthSingleTon getInstance() {return SINGLE_TON_INSTANCE;}

    public ArrayList<HealthData> getArray_healthData() {
        return array_healthData;
    }
    public void addArray_healthData(HealthData healthData){
        this.array_healthData.add(healthData);
    }
    public void clearArray_healthData(){
        this.array_healthData.clear();
    }
    public void removeArray_healthData(int position){
        this.array_healthData.remove(position);
    }
    public void setArray_healthData(int position, HealthData healthData){
        this.array_healthData.set(position, healthData);
    }
}
