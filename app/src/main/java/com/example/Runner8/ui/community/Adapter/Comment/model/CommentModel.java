package com.example.Runner8.ui.community.Adapter.Comment.model;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.ui.community.Adapter.Reply.Model.ReplyModel;
import com.example.Runner8.ui.community.Adapter.Reply.ReplyAdapter;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentModel {

    String comment, date, upCount, uid, time, index,
            comment_final_index, user_img, nickName, timeValue, comment_count, reply_count, reply_final_index;
    boolean reply_check, up_check, board_up_check, reply_view_check;

    ArrayList<ReplyModel> replyModels = new ArrayList<>();
    Map<Integer, ReplyAdapter> replyAdapterList = new HashMap<>();

    RecyclerView replyView;
    ReplyAdapter replyAdapter;
    TextView reply_view;

    DocumentReference dr_comment;

    public void setReply_view(TextView reply_view) {
        this.reply_view = reply_view;
    }

    public TextView getReply_view() {
        return reply_view;
    }

    public void addReplyAdapterList(int index, ReplyAdapter replyAdapter){
        this.replyAdapterList.put(index, replyAdapter);
    }
    public ReplyAdapter getReplyAdapter(int index){
        return replyAdapterList.get(index);
    }

    public Map<Integer, ReplyAdapter> getReplyAdapterList() {
        return replyAdapterList;
    }

    public DocumentReference getDr_comment() {
        return dr_comment;
    }

    public void setDr_comment(DocumentReference dr_comment) {
        this.dr_comment = dr_comment;
    }

    public String getDate() {
        return date;
    }
    public String getComment() {
        return comment;
    }

    public String getUpCount() {
        return upCount;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setUpCount(String upCount) {
        this.upCount = upCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment_final_index() {
        return comment_final_index;
    }

    public void setComment_final_index(String comment_final_index) {
        this.comment_final_index = comment_final_index;
    }
    public boolean getReply_check(){
        return this.reply_check;
    }

    public void setReply_check(boolean reply_check) {
        this.reply_check = reply_check;
    }

    public String getNickName() {
        return nickName;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public void setUp_check(boolean up_check) {
        this.up_check = up_check;
    }
    public boolean getUp_check(){
        return this.up_check;
    }

    public void setBoard_up_check(boolean board_up_check) {
        this.board_up_check = board_up_check;
    }
    public boolean getBoard_up_check(){
        return this.board_up_check;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }

    public void addReplyModel(ReplyModel replyModel){
        this.replyModels.add(replyModel);
    }

    public void clearReplies(){
        this.replyModels.clear();
    }

    public ArrayList<ReplyModel> getReplyModels() {
        return replyModels;
    }

    public boolean isReply_view_check() {
        return reply_view_check;
    }

    public void setReply_view_check(boolean reply_view_check) {
        this.reply_view_check = reply_view_check;
    }

    public String getReply_final_index() {
        return reply_final_index;
    }

    public String getReply_count() {
        return reply_count;
    }

    public void setReply_final_index(String reply_final_index) {
        this.reply_final_index = reply_final_index;
    }

    public void setReply_count(String reply_count) {
        this.reply_count = reply_count;
    }

    public RecyclerView getReplyView() {
        return replyView;
    }

    public void setReplyView(RecyclerView replyView) {
        this.replyView = replyView;
    }

    public void setReplyAdapter(ReplyAdapter replyAdapter) {
        this.replyAdapter = replyAdapter;
    }

    public ReplyAdapter getReplyAdapter() {
        return replyAdapter;
    }

    public void newReplyModel(ReplyModel replyModel){
        this.replyModels.add(0, replyModel);
    }

    public void removeReplyModel(int position){
        this.replyModels.remove(position);
    }
}
