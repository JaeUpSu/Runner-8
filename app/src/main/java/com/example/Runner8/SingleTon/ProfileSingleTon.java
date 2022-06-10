package com.example.Runner8.SingleTon;

import java.util.ArrayList;

public class ProfileSingleTon {
    int memo_final_count;
    ArrayList<String> memo_array = new ArrayList<>();

    private static final ProfileSingleTon PROFILE_SINGLE_TON_INSTANCE = new ProfileSingleTon();

    public static synchronized ProfileSingleTon getInstance(){
        return PROFILE_SINGLE_TON_INSTANCE;
    }

    public void setMemo_array(ArrayList<String> memo_array) {
        this.memo_array = memo_array;
    }

    public void setMemo_final_count(int memo_final_count) {
        this.memo_final_count = memo_final_count;
    }

    public ArrayList<String> getMemo_array() {
        return memo_array;
    }

    public int getMemo_final_count() {
        return memo_final_count;
    }
    public void addMemo_array(String memo){
        this.memo_array.add(memo);
    }
    public void removeMemo_array(int pos){
        this.memo_array.remove(pos);
    }
}
