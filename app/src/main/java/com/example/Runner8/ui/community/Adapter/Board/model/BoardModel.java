package com.example.Runner8.ui.community.Adapter.Board.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BoardModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String profile, nickName, date, up, up_count, title, content,
            views, record, video, img, timeValue, board_index, uid, writer_index, comment_count
            , comment_final_index, array_position;
    private boolean ableRecord, up_check;

    private Map<String, Object> boardData = new HashMap<>();

    public void InitialBoardData(){
        boardData.put("board_index", board_index);

    }

    public String getProfile(){
        return profile;
    }
    public void setProfile(String profile){
        this.profile = profile;
    }
    public String getNickName(){
        return nickName;
    }
    public void setNickName(String nickName){
        this.nickName = nickName;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getUp(){
        return up;
    }
    public void setUp(String up){
        this.up = up;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getViews(){
        return views;
    }
    public void setViews(String views){
        this.views = views;
    }
    public String getRecord(){
        return record;
    }
    public void setRecord(String record){
        this.record = record;
    }
    public Boolean getAbleRecord(){
        return ableRecord;
    }
    public void setAbleRecord(Boolean ableRecord){
        this.ableRecord = ableRecord;
    }

    public String getImg() {
        return img;
    }

    public String getVideo() {
        return video;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public String getBoard_index() {
        return board_index;
    }

    public void setBoard_index(String board_index) {
        this.board_index = board_index;
    }

    public void setUp_check(boolean up_check) {
        this.up_check = up_check;
    }

    public boolean getUp_check(){
        return up_check;
    }

    public String getUp_count() {
        return up_count;
    }

    public void setUp_count(String up_count) {
        this.up_count = up_count;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWriter_index() {
        return writer_index;
    }

    public void setWriter_index(String writer_index) {
        this.writer_index = writer_index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public void setComment_final_index(String comment_final_index) {
        this.comment_final_index = comment_final_index;
    }

    public String getComment_final_index() {
        return comment_final_index;
    }

    public String getArray_position() {
        return array_position;
    }

    public void setArray_position(String array_position) {
        this.array_position = array_position;
    }
}
