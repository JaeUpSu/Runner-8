package com.example.Runner8.TRASH.calendar;

public class Memo {
    String title;
    String contents;

    public Memo(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public String getTitle() {
        return title;
    }
}