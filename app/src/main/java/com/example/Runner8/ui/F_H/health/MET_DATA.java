package com.example.Runner8.ui.F_H.health;

import java.util.HashMap;
import java.util.Map;

public class MET_DATA {

    Map<String, Object> met_data = new HashMap<>();

    public MET_DATA() {
        this.met_data.put("자전거타기", 3.0);
        this.met_data.put("체 조", 3.5);
        this.met_data.put("골 프", 3.5);
        this.met_data.put("속 보", 4.0);
        this.met_data.put("걷 기", 4.0);
        this.met_data.put("배드민턴", 4.5);
        this.met_data.put("야 구", 5.0);
        this.met_data.put("웨이트", 6.0);
        this.met_data.put("농 구", 6.0);
        this.met_data.put("에어로빅", 6.5);
        this.met_data.put("조 깅", 7.0);
        this.met_data.put("축 구", 7.0);
        this.met_data.put("테니스", 7.0);
        this.met_data.put("스 키", 7.0);
        this.met_data.put("등 산", 7.5);
        this.met_data.put("사이클", 8.0);
        this.met_data.put("런 닝", 8.0);
        this.met_data.put("수 영", 8.0);
        this.met_data.put("유 도", 10.0);
        this.met_data.put("태권도", 10.0);
        this.met_data.put("계단오르기", 10.5);

    }
    public double getMet_num(String name){
        return Double.valueOf(met_data.get(name).toString());
    }
}
