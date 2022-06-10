package com.example.Runner8.ui.Graph;

import android.util.Log;

import com.example.Runner8.SingleTon.Sub_bundle;

import java.util.Calendar;

public class DateRange {

    int start;
    int destination;

    int overlap_start;
    int overlap_des;
    int overlap_count = 0;
    boolean overlap_check;
    boolean data_check = true;

    int prev_st = 0;
    int prev_des = 0;
    int after_st = 0;
    int after_des = 0;
    int current_st = 0;
    int current_des = 0;

    Today_Date today_date = new Today_Date();

    int[] result = new int[2];

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDestination() {
        return destination;
    }

    public int getStart() {
        return start;
    }

    public boolean isData_check() {
        return data_check;
    }

    public int getAfter_des() {
        return after_des;
    }

    public int getAfter_st() {
        return after_st;
    }

    public int getCurrent_des() {
        return current_des;
    }

    public int getCurrent_st() {
        return current_st;
    }

    public int getPrev_des() {
        return prev_des;
    }

    public int getPrev_st() {
        return prev_st;
    }

    // 입력받은 날짜에 대한 st_num, final_num 을 출력해주는 함수
    public void getWeekRange(int year , int month, int day){

        today_date.setNow();

        int st_num = 0;
        int final_num = 0;
        int day_of_week = 0;

        int first_year = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[0]);
        int first_month = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[1]);
        int first_day = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[2]);

        int max_day = 0;

        // st_num :
        //      - 이번달 첫번째 일에 영향을 받음
        //      - first_date 의 영향을 받음
        //
        // final_num :
        //      - 이번달 마지막 일에 영향을 받음
        //      - 오늘 날짜의 영향을 받음

        // 확인 순서 :
        //      1. 입력받은 날짜를 기준으로 왼쪽 오른쪽 끝 값을 구함
        //      2. 왼쪽 값이 1보다 작으면 이번달 첫번째 일에 영향을 받은 것. -> st_num = 1
        //      3. 오른쪽 값이 이번달 마지막 일 보다 크면 영향을 받은 것. -> final_num = 이번달 마지막 일
        //      4. final_num 값이 오늘 날짜보다 큰다면 오늘 날짜의 영향을 받은 것 -> final_num = 오늘 일 -1
        //      5. first_date 이 st_num 과 final_num 사이 값 이라면 -> st_num = first_date.day

        // 이번달에 첫번째 주인지 확인
        // 맞다면 저번달에 마지막 주

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int tmp_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        int tmp = 7 - tmp_of_week;

        if(Sub_bundle.getInstance().getFirst_date().equals("")){
            data_check = false;
            return;
        }
        else {
            data_check = true;
            // 저번주를 확인하고 싶을 때의 경우임
            if (day <= 0) {
                // 확인하려는 주가 저번달로 넘어간 경우
                if (1 <= day + 7 && day + 7 <= 1 + tmp) {
                    calendar.set(year, month - 2, 1);
                    max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    int real_day = max_day + day;
                    calendar.set(year, month - 2, real_day);

                    day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                    st_num = real_day - day_of_week + 1;
                    final_num = real_day + (7 - day_of_week);

                    prev_st = st_num;
                    prev_des = final_num;

                } else {
                    // prev
                    calendar.set(year, month - 2, 1);
                    max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    prev_st = (max_day - tmp_of_week) + 2;
                    prev_des = max_day;

                    // current
                    calendar.set(year, month - 1, 1);
                    current_st = 1;
                    current_des = 1 + (7 - tmp_of_week);
                }

                if (first_year == year && first_month == month - 1) {
                    if (first_day >= prev_st && first_day <= prev_des)
                        prev_st = first_day;
                }
                else if(first_year == year && first_month == month) {
                    if (first_day >= current_st && first_day <= current_des) {
                        current_st = first_day;
                        prev_st = 0;
                        prev_des = 0;
                    }
                }
            }
            else {
                calendar = Calendar.getInstance();
                calendar.set(year, month - 1, day);

                /////////////////////////////////////////////////////////////////////////////////////////////
                //      1. 입력받은 날짜를 기준으로 왼쪽 오른쪽 끝 값을 구함
                day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                current_st = day - day_of_week + 1;
                current_des = day + (7 - day_of_week);

                if(current_st < 1) {
                    calendar.set(year, month - 2, 1);
                    max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                    current_st = 1;
                    prev_st = (max_day - tmp_of_week) + 2;
                    prev_des = max_day;
                }

                int month_final_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                calendar.set(year, month, month_final_day);
                int final_day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

                if (current_des >= today_date.getDay() - 1) {
                    current_des = today_date.getDay() - 1;
                } else {
                    if (current_des > month_final_day && final_day_of_week != 7) {
                        after_st = 1;
                        after_des = 7 - final_day_of_week;
                        current_des = month_final_day;
                    }
                }

                if (first_year == year && first_month == month) {
                    if (first_day >= current_st && first_day <= current_des) {
                        current_st = first_day;
                        prev_st = 0;
                        prev_des = 0;
                    }
                }
                else if(first_year == year && first_month == month + 1) {
                    if (first_day >= after_st && first_day <= after_des) {
                        after_st = first_day;
                        current_st = 0;
                        current_des = 0;
                    }
                }
                else if(first_year == year && first_month == month - 1) {
                    if (first_day >= prev_st && first_day <= prev_des){
                        prev_st = first_day;
                    }
                }
            }
        }

        /*
        calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        /////////////////////////////////////////////////////////////////////////////////////////////
        //      1. 입력받은 날짜를 기준으로 왼쪽 오른쪽 끝 값을 구함
        day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        st_num = day - day_of_week + 1;
        final_num = day + (7 - day_of_week);

        Log.i("prev_day_of_week", day_of_week + "");

        /////////////////////////////////////////////////////////////////////////////////////////////
        //      2. 왼쪽 값이 1보다 작으면 이번달 첫번째 일에 영향을 받은 것. -> st_num = 1
        if(st_num < 1) {
            calendar.set(year, month - 2, 1);
            overlap_check = true;
            overlap_count = -st_num;
            st_num = 1;
        }

        int month_final_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, month_final_day);
        int final_day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

        /////////////////////////////////////////////////////////////////////////////////////////////
        //      3. 오른쪽 값이 이번달 마지막 일 보다 크면 영향을 받은 것. -> final_num = 이번달 마지막 일
        if(final_num > month_final_day && final_day_of_week != 7)
            final_num = month_final_day;

        /////////////////////////////////////////////////////////////////////////////////////////////
        //      4. final_num 값이 오늘 날짜보다 큰다면 오늘 날짜의 영향을 받은 것 -> final_num = 오늘 일 -1
        if(final_num >= today_date.getDay() - 1) final_num = today_date.getDay() - 1;


        /////////////////////////////////////////////////////////////////////////////////////////////
        //      5. first_date 이 st_num 과 final_num 사이 값 이라면 -> st_num = first_date.day
        if(Sub_bundle.getInstance().getFirst_date().equals("")){
            data_check = false;
        }
        else {
            Log.i("getInstance()", Sub_bundle.getInstance().getFirst_date());
            first_year = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[0]);
            first_month = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[1]);
            first_day = Integer.valueOf(Sub_bundle.getInstance().getFirst_date().split("[.]")[2]);

            if (first_year == year && first_month == month)
                if (first_day >= st_num && first_day <= final_num) {
                    st_num = first_day;
                }

        }
        result[0] = st_num;
        result[1] = final_num;

         */

    }

    public boolean check_this_week(String today_date, String other_date){
        int today_year = Integer.valueOf(today_date.split("[.]")[0]);
        int today_month = Integer.valueOf(today_date.split("[.]")[1]);
        int today_day = Integer.valueOf(today_date.split("[.]")[2]);

        int prev_year = Integer.valueOf(other_date.split("[.]")[0]);
        int prev_month = Integer.valueOf(other_date.split("[.]")[1]);
        int prev_day = Integer.valueOf(other_date.split("[.]")[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(today_year, today_month - 1, today_day);
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        int st_day = today_day - day_of_week;
        int des_day = 0;
        int prev_st_day = 0;
        int prev_des_day = 0;

        // 저번주가 전 달로 넘어갈 때 사용될 데이터
        calendar.set(today_year, today_month - 2, 1);
        int max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if(st_day < 0){
            // 전달 확인

            calendar.set(today_year, today_month - 2, today_day);
            prev_st_day = max_day + st_day + 1;
            prev_des_day = max_day;

            st_day = 1;
            des_day = today_day + 7 - day_of_week;

            if (prev_year == today_year && prev_month == today_month - 1
                    && prev_st_day <= prev_day && prev_day <= prev_des_day) {
                Log.i("check_range", "prev_st_day : " + prev_st_day + "prev_des_day : " + prev_des_day);
                return true;
            }
        }
        else{

            calendar.set(today_year, today_month - 1, 1);
            max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            st_day += 1;
            des_day = today_day + 7 - day_of_week;
            if(des_day > max_day)
                des_day = max_day;
        }

        if (prev_year == today_year && prev_month == today_month
                && st_day <= prev_day && prev_day <= des_day) {
            Log.i("check_range", "st_day : " + st_day + "des_day : " + des_day);
            return true;
        }

        Log.i("check_range", "st_day : " + st_day + "des_day : " + des_day);
        return false;
    }

    public boolean check_next_week(String today_date_format, String prev_date_format){

        // Solution : 오늘 날짜를 기준으로 저번주에 해당하는 범위안에 prev_date 가 존재하는지 확인한다.
        // Problem 1
        //      - 저번주를 확인하기 위해 today_day - 7 을 할 경우 해당 값이 음수가 되었을 때 (check_day)
        //          > 아예 전 달로 넘어간 경우
        //          > 이번달 첫번째 주인 경우
        //
        // Problem 2
        //      - prev_date 가 이번주라면 아무것도 하지 않음

        // Problem 3
        //      - Week_data_initial() : prev_date 가 이번주와 저번주가 아니면 초기화 하는 작업
        //

        // 순서
        // 1. prev_date 가 이번주인지 확인, 이번주면 return false (아무것도 안함)
        // 2. 오늘이 이번달의 첫번째 주인 경우 -> 전 달만 확인함
        // 3. check day(day - 7) 가 0보다 작거나 같은지 확인
        // 4. 0보다 클 때 st_num 이 음수인지 확인
        // 구한 prev_st_des, current_st_des 범위에 prev_date 가 있는지 확인

        Calendar calendar = Calendar.getInstance();

        int today_year = Integer.valueOf(today_date_format.split("[.]")[0]);
        int today_month = Integer.valueOf(today_date_format.split("[.]")[1]);
        int today_day = Integer.valueOf(today_date_format.split("[.]")[2]);

        int prev_year = Integer.valueOf(prev_date_format.split("[.]")[0]);
        int prev_month = Integer.valueOf(prev_date_format.split("[.]")[1]);
        int prev_day = Integer.valueOf(prev_date_format.split("[.]")[2]);

        int check_day = today_day - 7;          // 저번주를 확인하기 위한 임시 day 값
        int day_of_week = 0;
        int max_day = 0;
        int prev_check_st = 0;
        int prev_check_des = 0;
        int check_st = 0;
        int check_des = 0;

        // 달의 첫 번째 주인 경우에 사용될 데이터들
        calendar.set(today_year, today_month - 1, 1);
        int tmp_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        int tmp = 7 - tmp_of_week;

        // 오늘 기준으로 사용될 데이터
        calendar.set(today_year, today_month - 1, today_day);
        day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

        // 저번주가 전 달로 넘어갈 때 사용될 데이터
        calendar.set(today_year, today_month - 2, 1);
        max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // prev_date 가 이번주인 경우 확인
        if (today_year == prev_year && prev_month == today_month
                && today_day - day_of_week + 1 <= prev_day && prev_day <= today_day) {

            return false;
        }
        //
        else {

            // 이번달의 첫번째 주인 경우
            if(today_day - day_of_week < 0) {
                // 전 달만 보면 되기 때문에 확인 후 return

                int real_day = max_day + check_day;
                calendar.set(today_year, today_month - 2, real_day);

                day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                check_st = real_day - day_of_week + 1;
                check_des = real_day + (7 - day_of_week);

                if (prev_year == today_year && prev_year == today_month - 1
                        && check_st <= prev_day && prev_day <= check_des) return true;

            }

            // day - 7 이 0보다 작거나 같은 경우 (전 주가 이번달의 첫번째 주임)
            if (check_day <= 0) {

                // prev
                prev_check_st = (max_day - tmp_of_week) + 2;
                prev_check_des = max_day;

                // current
                calendar.set(today_year, today_month - 1, 1);
                check_st = 1;
                check_des = 1 + (7 - tmp_of_week);

            } else {
                calendar.set(today_year, today_month - 1, today_day);

                /////////////////////////////////////////////////////////////////////////////////////////////
                //      1. 입력받은 날짜를 기준으로 왼쪽 오른쪽 끝 값을 구함
                day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                check_st = check_day - day_of_week + 1;
                check_des = check_day + (7 - day_of_week);

                if (check_st < 1) {
                    calendar.set(today_year, today_month - 2, 1);
                    check_st = 1;

                    day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
                    max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    prev_check_st = (max_day - day_of_week) + 2;
                    prev_check_des = max_day;
                }
            }

            if (today_year == prev_year && today_month == prev_month) {
                if (prev_check_st <= prev_day && prev_check_des >= prev_day) return true;
                else if (check_st <= prev_day && check_des >= prev_day) return true;
            }
        }

        return false;
    }

    public int getOverlap_count() {
        return overlap_count;
    }

    public boolean isOverlap_check() {
        return overlap_check;
    }
}
