package com.example.Runner8.ui.community.Fragment;


public class BoardData {

    String title, uid;
    Integer img_src, comment, up;
    Boolean checked = true;

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title;}

    public Integer getImg_src() { return img_src; }
    public void setImg_src(Integer img_src) { this.img_src = img_src; }

    public Integer getComment() { return comment; }
    public void setComment(Integer comment) { this.comment = comment; }

    public Boolean getChecked() { return checked; }
    public void setChecked(Boolean checked) { this.checked = checked; }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public Integer getUp() {
        return up;
    }
}

