package com.example.Runner8.ui.community.Adapter.HotBoard;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
import com.example.Runner8.SingleTon.ScreenChangeDetect;
import com.example.Runner8.ui.community.Adapter.Board.model.BoardModel;
import com.example.Runner8.ui.community.Activity.ComPickBoardActivity;
import com.example.Runner8.ui.community.Activity.ComUserActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HotBoardAdapter extends RecyclerView.Adapter<HotBoardAdapter.ViewHolder> {

    private ArrayList<BoardModel> dataArrayList;
    private Activity activity;

    //
    private boolean com_user_activity = false;
    private boolean hot_board_check = false;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference cr_total_item = db.collection("Community").document("board")
            .collection("item");


    public HotBoardAdapter(Activity activity , ArrayList<BoardModel> dataArrayList){
        this.activity = activity;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listhotboardlist,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoardModel data = dataArrayList.get(position);

        DocumentReference dr_commLike = db.collection("Users").document(user.getUid())
                .collection("Comm_Like").document(data.getBoard_index());

        db.collection("Users").document(data.getUid())
                .collection("Profile").document("diet_profile")
                .get().addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();
                    holder.name.setText(document.get("nickName").toString());
                    // profile

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.imgUser.setClipToOutline(true);
                    }
                    Glide.with(holder.itemView)
                            .load(document.get("user_img").toString())
                            .into(holder.imgUser);
                    data.setProfile(document.get("user_img").toString());
                    data.setNickName(document.get("nickName").toString());

                    holder.imgUser.setOnClickListener(v -> {
                        Intent intent = new Intent(activity, ComUserActivity.class);
                        intent.putExtra("uid", data.getUid());
                        activity.startActivity(intent);
                    });
                });

        holder.up_count.setText(data.getUp_count());
        holder.date.setText(data.getTimeValue());

        if(Integer.valueOf(data.getTitle().length()) > 12)
            holder.title.setText(data.getTitle().substring(0, 12) + "...");
        else
            holder.title.setText(data.getTitle());

        // views
        holder.views.setText("조회수 " + data.getViews());
        // 댓글 수
        if(data.getComment_count().equals("0")) holder.commentCount.setText("");
        else holder.commentCount.setText("+ " + data.getComment_count());

        // img check
        if(data.getImg().equals("")) holder.imgImg.setVisibility(View.INVISIBLE);

        // up setting
        dr_commLike.get().addOnCompleteListener(task -> {
            if(task.getResult().exists()) {
                if(Boolean.valueOf(task.getResult().get("up_check").toString())){
                    // background
                    holder.btn_up.setBackground(ContextCompat
                            .getDrawable(activity, R.drawable.up_check));
                    // checking
                    holder.btn_up.setChecked(true);
                    data.setUp_check(true);
                }else{
                    holder.btn_up.setChecked(false);
                    data.setUp_check(false);
                }
            }
            else {
                holder.btn_up.setChecked(false);
                data.setUp_check(false);
            }
        });
        /*
        // up clickListener
        holder.btn_up.setOnClickListener(v -> {

            Map<String, Object> user_comm = new HashMap<>();
            Map<String, Object> total_comm = new HashMap<>();

            int up_count = Integer.valueOf(holder.up_count.getText().toString());

            // 좋아요 취소
            if(!holder.btn_up.isChecked()){
                // background
                holder.btn_up.setBackground(ContextCompat
                                .getDrawable(activity, R.drawable.up));

                // user_comm update
                user_comm.put("up_check", false);

                up_count -= 1;

                //
                data.setUp_check(false);
            // 좋아요
            }else{
                // background
                holder.btn_up.setBackground(ContextCompat
                        .getDrawable(activity, R.drawable.up_check));

                up_count += 1;

                // user_comm update
                user_comm.put("up_check", true);

                //
                data.setUp_check(true);
            }
            // up_check
            dr_commLike.update(user_comm);
            // total_comm update
            total_comm.put("up_count", up_count);
            cr_total_item.document(data.getBoard_index()).update(total_comm);
            // setText
            holder.up_count.setText(String.valueOf(up_count));
            data.setUp_count(String.valueOf(up_count));
        });

         */
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgUser,imgImg, imgVideo;
        ToggleButton btn_up;
        TextView name, date, views, up_count, title, commentCount, tv_boardContent;

        public ViewHolder(@NonNull View view){
            super(view);

            imgUser = view.findViewById(R.id.img_user);
            imgImg = view.findViewById(R.id.img_picture);
            imgVideo = view.findViewById(R.id.img_video);
            name = view.findViewById(R.id.tv_user);
            date = view.findViewById(R.id.tv_date);
            views = view.findViewById(R.id.tv_viewCount);
            up_count = view.findViewById(R.id.tv_upCount);
            title = view.findViewById(R.id.tv_title);
            commentCount= view.findViewById(R.id.tv_comment_count);
            btn_up = view.findViewById(R.id.btn_up);
            tv_boardContent = view.findViewById(R.id.tv_boardContent);

            view.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    BoardModel boardModel = dataArrayList.get(pos);

                    int views = Integer.valueOf(boardModel.getViews()) + 1;
                    boardModel.setViews(String.valueOf(views));

                    Map<String, Object> map = new HashMap<>();
                    map.put("views", views);

                    // total_comm
                    cr_total_item.document(boardModel.getBoard_index()).update(map);

                    // user_comm
                    db.collection("Users").document(boardModel.getUid())
                            .collection("Comm").document(boardModel.getWriter_index())
                            .update(map);

                    Intent intent = new Intent(activity.getApplicationContext(), ComPickBoardActivity.class);
                    intent.putExtra("BoardModel", boardModel);

                    ScreenChangeDetect.getInstance().setHotBoardToPick(true);

                    activity.startActivity(intent);
                }
            });
        }
    }

    public void setCom_user_activity(boolean com_user_activity) {
        this.com_user_activity = com_user_activity;
    }

    public void setHot_board_check(boolean hot_board_check) {
        this.hot_board_check = hot_board_check;
    }
}
