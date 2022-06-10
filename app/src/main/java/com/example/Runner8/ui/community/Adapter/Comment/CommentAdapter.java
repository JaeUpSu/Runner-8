package com.example.Runner8.ui.community.Adapter.Comment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Runner8.R;
import com.example.Runner8.ui.community.Adapter.Comment.model.CommentModel;
import com.example.Runner8.ui.community.Adapter.Reply.Model.ReplyModel;
import com.example.Runner8.ui.community.Adapter.Reply.ReplyAdapter;
import com.example.Runner8.ui.community.OnCommentItemClickListener;
import com.example.Runner8.ui.community.OnCommentItemLongClickListener;
import com.example.Runner8.ui.community.TimeValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
        implements OnCommentItemClickListener, OnCommentItemLongClickListener {

    private Context context;
    private ArrayList<CommentModel> dataArrayList;
    private Activity activity;

    // 게시물 index
    String board_index;
    // 게시물 좋아요 확인
    boolean board_up_check;

    OnCommentItemClickListener reply_view_click_listener;
    OnCommentItemLongClickListener comment_long_click_listener;


    int count=1;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference cr_comment;

    // Adapter
    ReplyAdapter replyAdapter;

    // Class
    TimeValue timeValue;
    ReplyModel replyModel;

    public CommentAdapter(Activity activity , ArrayList<CommentModel> dataArrayList,
                          String board_index, boolean board_up_check){
        this.activity = activity;
        this.dataArrayList = dataArrayList;
        this.board_index = board_index;
        this.board_up_check = board_up_check;

        cr_comment = db.collection("Community").document("board")
                .collection("item").document(board_index).collection("comment");
    }

    public CollectionReference getCr_comment() {
        return cr_comment;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentAdapter.ViewHolder holder, int position) {
        CommentModel commentModel = dataArrayList.get(position);

        commentModel.setReply_view(holder.reply_view);

        //
        commentModel.setReplyView(holder.reply);
        Log.i("CommentModel", commentModel.getIndex());

        // Reference initial
        // 댓글 좋아요를 업데이트 하기 위해 게시물 좋아요의 확인 여부와 함께 up_check setting
        if (!board_up_check) {
            Map<String, Object> map = new HashMap<>();
            map.put("up_check", false);
            db.collection("Users").document(user.getUid())
                    .collection("Comm_Like").document(board_index).set(map);
        }

        // board comm_like
        DocumentReference dr_commLike = db.collection("Users").document(user.getUid())
                .collection("Comm_Like").document(board_index);

        // comment data
        DocumentReference dr_comment = dr_commLike.collection("Comment_Like")
                .document(commentModel.getIndex());
        //
        commentModel.setDr_comment(dr_comment);

        // profile, nickName
        db.collection("Users").document(commentModel.getUid())
                .collection("Profile").document("diet_profile")
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();
                    holder.reply_nickName.setText(document.get("nickName").toString());
                    // profile

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.img_user.setClipToOutline(true);
                    }
                    Glide.with(holder.itemView)
                            .load(document.get("user_img").toString())
                            .into(holder.img_user);

                    commentModel.setUser_img(document.get("user_img").toString());
                    commentModel.setNickName(document.get("nickName").toString());
                });

        // 내용
        holder.comment.setText(commentModel.getComment());
        // 좋아요 수
        holder.tv_upCount.setText(commentModel.getUpCount());
        // 시간
        holder.tv_date.setText(commentModel.getTimeValue());

        // 답글
        if(commentModel.getReply_count().equals("0"))
            holder.reply_view.setText("");
        else{
            cr_comment.document(commentModel.getIndex())
                    .get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        String reply_count = document.get("reply_count").toString();
                        holder.reply_view.setText("답글 " + reply_count + "개 ┐");
                    });

            // 답글 보기
            holder.reply_view.setOnClickListener(v -> {

                if(commentModel.isReply_view_check()){
                    commentModel.clearReplies();
                    replyAdapter = new ReplyAdapter(activity, commentModel.getReplyModels(),
                            commentModel.getUp_check(), commentModel.getIndex());
                    holder.reply.setAdapter(replyAdapter);

                    cr_comment.document(commentModel.getIndex())
                            .get()
                            .addOnCompleteListener(task -> {
                                DocumentSnapshot document = task.getResult();
                                String reply_count = document.get("reply_count").toString();
                                holder.reply_view.setText("답글 " + reply_count + "개 ┐");
                            });

                    commentModel.setReply_view_check(false);
                }
                else{
                    commentModel.setReply_view_check(true);
                    Log.i("Reply_view_check(true)", "check!!");

                    // 답글 리스트 뿌리기

                    cr_comment.document(commentModel.getIndex()).collection("reply")
                            .orderBy("index", Query.Direction.DESCENDING)
                            .get()
                            .addOnCompleteListener(task -> {

                                ArrayList<Map> map = new ArrayList<>();

                                for(QueryDocumentSnapshot document : task.getResult()){
                                    map.add(document.getData());
                                }

                                for(Map data : map){

                                    replyModel = new ReplyModel();

                                    String content = data.get("content").toString();
                                    String date = data.get("date").toString();
                                    String time = data.get("time").toString();
                                    String uid = data.get("uid").toString();
                                    String upCount = data.get("upCount").toString();
                                    String index = data.get("index").toString();

                                    timeValue = new TimeValue(date, time);

                                    replyModel.setIndex(index);
                                    replyModel.setComment(content);
                                    replyModel.setDate(date);
                                    replyModel.setTime(time);
                                    replyModel.setUid(uid);
                                    replyModel.setUpCount(upCount);
                                    replyModel.setTimeValue(timeValue.getTimeValue());
                                    replyModel.setDr_comLike(dr_comment);

                                    replyModel.setDr_comment(cr_comment.document(commentModel.getIndex())
                                            .collection("reply").document(index));

                                    // 수정 사항
                                    replyModel.setComment_writer_nickName(commentModel.getNickName());
                                    commentModel.addReplyModel(replyModel);
                                }

                                replyAdapter = new ReplyAdapter(activity, commentModel.getReplyModels(),
                                        commentModel.getUp_check(), commentModel.getIndex());

                                commentModel.setReplyView(holder.reply);
                                commentModel.addReplyAdapterList(Integer.valueOf(commentModel.getIndex()), replyAdapter);

                                //
                                holder.reply.setAdapter(replyAdapter);

                                replyAdapter.setListener((holder1, view, position1) -> {
                                    replyModel = replyAdapter.getItems().get(position1);

                                    if(replyModel.getUid().equals(user.getUid())){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                                                .setTitle("확인")
                                                .setMessage(" 정말로 답글을 삭제하시겠습니까??")
                                                .setNegativeButton("예", (dialog, which) -> {

                                                    DocumentReference dr_commentItem = db.collection("Community")
                                                            .document("board").collection("item")
                                                            .document(board_index).collection("comment")
                                                            .document(commentModel.getIndex());

                                                    DocumentReference dr_replyItem = dr_commentItem.collection("reply")
                                                            .document(replyModel.getIndex());

                                                    commentModel.setReply_count(
                                                            String.valueOf(Integer.valueOf(commentModel.getReply_count()) - 1));

                                                    Map<String, Object> map2 = new HashMap<>();
                                                    map2.put("reply_count", commentModel.getReply_count());

                                                    dr_commentItem.update(map2);

                                                    // delete
                                                    dr_replyItem.delete();
                                                    commentModel.removeReplyModel(position1);

                                                    // adapter reset
                                                    replyAdapter = new ReplyAdapter(activity, commentModel.getReplyModels(),
                                                            commentModel.getUp_check(), commentModel.getIndex());
                                                    commentModel.getReplyView().setAdapter(replyAdapter);


                                                    // 삭제시 답글이 없을 경우
                                                    if(Integer.valueOf(commentModel.getReply_count()) == 0){
                                                        // 답글 보기 text 없애줘야함
                                                        commentModel.getReply_view().setVisibility(View.INVISIBLE);
                                                    }

                                                })
                                                .setPositiveButton("아니오", (dialog, which) -> {

                                                });

                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    }
                                    else{

                                    }
                                });
                            });

                    holder.reply_view.setText("답글 닫기");
                }
            });
        }

        // up setting
        // 좋아요를 클릭하지 않았다면 document 가 만들어지지 않았을 것임.
        dr_comment.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if(document.exists()) {
                if(Boolean.valueOf(document.get("up_check").toString())){
                    // background
                    holder.btn_up.setBackground(ContextCompat
                            .getDrawable(activity, R.drawable.up_check));
                    // checking
                    holder.btn_up.setChecked(true);
                    commentModel.setUp_check(true);
                }
                else{
                    holder.btn_up.setChecked(false);
                    commentModel.setUp_check(false);
                }
            }
            else {
                holder.btn_up.setChecked(false);
                commentModel.setUp_check(false);
            }
        });
        //////////////////////////////

        // up clickListener
        holder.btn_up.setOnClickListener(v -> {

            Map<String, Object> user_comm = new HashMap<>();
            Map<String, Object> total_comm = new HashMap<>();
            int up_count;

            // get up_count
            if(holder.tv_upCount.getText().toString().equals("")) up_count = 0;
            else up_count = Integer.valueOf(holder.tv_upCount.getText().toString());

            // 좋아요 취소
            if(!holder.btn_up.isChecked()){
                // background
                holder.btn_up.setBackground(ContextCompat
                        .getDrawable(activity, R.drawable.up));

                // user_comm update
                user_comm.put("up_check", false);
                //
                up_count -= 1;
                //
                commentModel.setUp_check(false);
                // 좋아요
            }else{
                // background
                holder.btn_up.setBackground(ContextCompat
                        .getDrawable(activity, R.drawable.up_check));
                //
                up_count += 1;
                // user_comm update
                user_comm.put("up_check", true);
                //
                commentModel.setUp_check(true);
            }
            // up_check
            dr_comment.set(user_comm);
            // total_comm update
            total_comm.put("upCount", up_count);
            cr_comment.document(commentModel.getIndex()).update(total_comm);
            // setText
            holder.tv_upCount.setText(String.valueOf(up_count));
            commentModel.setUpCount(String.valueOf(up_count));
        });


    }



    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public void setOnItemClickListener(OnCommentItemClickListener listener){
        this.reply_view_click_listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(reply_view_click_listener != null){
            reply_view_click_listener.onItemClick(holder, view, position);
        }
    }

    public void setComment_long_click_listener(OnCommentItemLongClickListener comment_long_click_listener) {
        this.comment_long_click_listener = comment_long_click_listener;
    }

    @Override
    public void onItemLongClick(ViewHolder holder, View view, int position) {
        if(comment_long_click_listener != null){
            comment_long_click_listener.onItemLongClick(holder, view, position);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_user;
        ToggleButton btn_up;
        TextView reply_nickName, comment, tv_date, reply_comment, tv_upCount, reply_view;

        RecyclerView reply;

        public ViewHolder(@NonNull @NotNull View  view) {
            super(view);

            img_user = view.findViewById(R.id.img_user);
            btn_up = view.findViewById(R.id.btn_up);
            reply_nickName = view.findViewById(R.id.reply_nickName);
            comment = view.findViewById(R.id.comment);
            tv_date = view.findViewById(R.id.tv_date);
            reply_comment = view.findViewById(R.id.reply_comment);
            tv_upCount = view.findViewById(R.id.tv_upCount);
            reply_view = view.findViewById(R.id.reply_view);
            reply = view.findViewById(R.id.comment_reply);

            view.setOnLongClickListener(v -> {
                Log.i("view", "setOnLongClickListener!!");
                int position = ViewHolder.this.getAdapterPosition();
                CommentModel commentModel = dataArrayList.get(position);

                if (comment_long_click_listener != null) {
                    comment_long_click_listener.onItemLongClick(ViewHolder.this, v, position);
                }
                return true;
            });

            reply_comment.setOnClickListener(v -> {
                Log.i("reply_view", "setOnClickListener!!");

                int position = ViewHolder.this.getAdapterPosition();
                if (reply_view_click_listener != null) {
                    reply_view_click_listener.onItemClick(ViewHolder.this, v, position);
                }
            });

            reply.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            reply.setHasFixedSize(true);

        }

    }
    public void InVisibleReply_View(ViewHolder viewHolder){
        viewHolder.reply_view.setVisibility(View.INVISIBLE);
    }

    public CommentModel getItem(int position){
        return dataArrayList.get(position);
    }

    public void setReplyViewText(String text){

    }

    public void addReply(CommentModel commentModel, ReplyModel replyModel){
        commentModel.newReplyModel(replyModel);
        commentModel.getReplyAdapter().notifyItemInserted(0);
    }

}
