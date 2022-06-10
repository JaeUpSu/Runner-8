package com.example.Runner8.ui.community.Adapter.Reply;

import android.app.Activity;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Runner8.R;
import com.example.Runner8.ui.community.Adapter.Reply.Model.ReplyModel;
import com.example.Runner8.ui.community.OnReplyItemLongClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder>
        implements OnReplyItemLongClickListener {

    private Activity activity;

    private ArrayList<ReplyModel> items;
    boolean comment_upCheck;
    String comment_index;

    //
    OnReplyItemLongClickListener listener;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ArrayList<ReplyModel> getItems() {
        return items;
    }

    public ReplyAdapter(Activity activity , ArrayList<ReplyModel> items,
                        boolean comment_upCheck, String comment_index){

        this.items = items;
        this.comment_upCheck = comment_upCheck;
        this.comment_index = comment_index;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ReplyAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reply_list_item,parent,false);

        return new ReplyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReplyAdapter.ViewHolder holder, int position) {
        ReplyModel replyModel = items.get(position);

        Log.i("ReplyModel", replyModel.getComment());
        Log.i("ReplyModel", replyModel.getIndex());


        // comment ref
        DocumentReference dr_comLike = replyModel.getDr_comLike();

        DocumentReference dr_replyLike = dr_comLike.collection("reply")
                .document(replyModel.getIndex());

        DocumentReference dr_comment = replyModel.getDr_comment();

        // profile, nickName
        db.collection("Users").document(replyModel.getUid())
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

                    replyModel.setUser_img(document.get("user_img").toString());
                    replyModel.setNickName(document.get("nickName").toString());
                });

        //
        holder.comment.setText(replyModel.getComment());
        holder.tv_upCount.setText(replyModel.getUpCount());

        // TimeValue
        holder.tv_date.setText(replyModel.getTimeValue());

        // 좋아요 버튼

        // up setting
        dr_replyLike.get().addOnCompleteListener(task -> {
            if(task.getResult().exists()) {
                if(Boolean.valueOf(task.getResult().get("up_check").toString())){
                    // background
                    holder.btn_up.setBackground(ContextCompat
                            .getDrawable(activity, R.drawable.up_check));
                    // checking
                    holder.btn_up.setChecked(true);
                    replyModel.setUp_check(true);
                }else{
                    holder.btn_up.setChecked(false);
                    replyModel.setUp_check(false);
                }
            }
            else {
                holder.btn_up.setChecked(false);
                replyModel.setUp_check(false);
            }
        });

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
                replyModel.setUp_check(false);
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
                replyModel.setUp_check(true);
            }
            // up_check
            dr_replyLike.set(user_comm);
            // total_comm update
            total_comm.put("upCount", up_count);

            dr_comment.update(total_comm);
            // setText
            holder.tv_upCount.setText(String.valueOf(up_count));
            replyModel.setUpCount(String.valueOf(up_count));
        });


        // To..

        holder.To_writer.setText(" To " + replyModel.getComment_writer_nickName());

    }

    public String getComment_index() {
        return comment_index;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onReplyItemLongClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onReplyItemLongClick(holder, view, position);
        }
    }

    public void setListener(OnReplyItemLongClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_user;
        ToggleButton btn_up;
        TextView reply_nickName, comment, tv_date, reply_comment, tv_upCount, To_writer;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);

            img_user = view.findViewById(R.id.img_user);
            btn_up = view.findViewById(R.id.btn_up);
            reply_nickName = view.findViewById(R.id.reply_nickName);
            comment = view.findViewById(R.id.comment);
            tv_date = view.findViewById(R.id.tv_date);
            reply_comment = view.findViewById(R.id.reply_comment);
            tv_upCount = view.findViewById(R.id.tv_upCount);
            To_writer = view.findViewById(R.id.To_writer);

            view.setOnLongClickListener(v -> {
                Log.i("view", "setOnLongClickListener!!");
                int position = ViewHolder.this.getAdapterPosition();

                if (listener != null) {
                    listener.onReplyItemLongClick(ViewHolder.this, v, position);
                }
                return true;
            });

        }
    }

}
