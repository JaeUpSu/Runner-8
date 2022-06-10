package com.example.Runner8.ui.community.singleTon;

public class TotalCounts {
    String board_count;

    private static final TotalCounts TOTAL_COUNTS_INSTANCE = new TotalCounts();

    public static synchronized TotalCounts getInstance() {return TOTAL_COUNTS_INSTANCE;}

    public String getBoard_count() {
        return board_count;
    }

    public void setBoard_count(String board_count) {
        this.board_count = board_count;
    }
}
