package com.example.Runner8.ui.community.Adapter.Reply.Model;

import com.google.firebase.firestore.DocumentReference;

public class ReplyModel {

    String comment;                                         // 내용
    String comment_writer_nickName;                         // 댓글 작성자 닉네임
    String date;                                            // 날짜
    String time;                                            // 시간
    String upCount;                                         // 좋아요 수
    String uid;                                             // 작성자
    String index;                                           // 답글 index
    String reply_final_index;                               // 답글 마지막 index
    String user_img;                                        // 작성자 profile img
    String nickName;                                        // 작성자 닉네임
    String timeValue;                                       // TimeValue

    boolean isUp_check;                                     // 좋아요 체크

    DocumentReference dr_comLike;
    DocumentReference dr_comment;

    boolean reply_check, up_check, board_up_check;

    public String getIndex() {
        return index;
    }

    public String getTime() {
        return time;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public String getUser_img() {
        return user_img;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUid() {
        return uid;
    }

    public String getUpCount() {
        return upCount;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getReply_final_index() {
        return reply_final_index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUpCount(String upCount) {
        this.upCount = upCount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setReply_final_index(String reply_final_index) {
        this.reply_final_index = reply_final_index;
    }

    public String getComment_writer_nickName() {
        return comment_writer_nickName;
    }

    public void setComment_writer_nickName(String comment_writer_nickName) {
        this.comment_writer_nickName = comment_writer_nickName;
    }

    public DocumentReference getDr_comLike() {
        return dr_comLike;
    }

    public void setDr_comLike(DocumentReference dr_comLike) {
        this.dr_comLike = dr_comLike;
    }

    public boolean isUp_check() {
        return isUp_check;
    }

    public void setUp_check(boolean up_check) {
        isUp_check = up_check;
    }

    public void setDr_comment(DocumentReference dr_comment) {
        this.dr_comment = dr_comment;
    }

    public DocumentReference getDr_comment() {
        return dr_comment;
    }

}
