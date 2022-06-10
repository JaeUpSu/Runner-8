package com.example.Runner8.ui.community.Adapter.Search;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Runner8.R;
import com.example.Runner8.SingleTon.ScreenChangeDetect;
import com.example.Runner8.ui.community.Activity.ComPickBoardActivity;
import com.example.Runner8.ui.community.Adapter.Board.model.BoardModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComSearchAdapter extends RecyclerView.Adapter<ComSearchAdapter.ViewHolder> {

    private ArrayList<BoardModel> arrayList;
    private Activity activity;
    private String edit_search;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ComSearchAdapter(Activity activity, ArrayList<BoardModel> arr, String edit_search){
        this.activity = activity;
        this.arrayList = arr;
        this.edit_search = edit_search;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_list,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoardModel boardModel = arrayList.get(position);

        db.collection("Users").document(boardModel.getUid())
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
            boardModel.setProfile(document.get("user_img").toString());
            boardModel.setNickName(document.get("nickName").toString());

            /*
            holder.imgUser.setOnClickListener(v -> {
                Intent intent = new Intent(activity, ComUserActivity.class);
                intent.putExtra("uid", boardModel.getUid());
                activity.startActivity(intent);
            });

             */
        });

        holder.up_count.setText(boardModel.getUp_count());
        holder.date.setText(boardModel.getTimeValue());

        if(Integer.valueOf(boardModel.getTitle().length()) > 57)
            holder.title.setText(boardModel.getTitle().substring(0, 57) + "...");
        else
            holder.title.setText(boardModel.getTitle());

        // views
        holder.views.setText("조회수 " + boardModel.getViews());
        // 댓글 수
        if(boardModel.getComment_count().equals("0")) holder.commentCount.setText("");
        else holder.commentCount.setText("+ " + boardModel.getComment_count());

        // img check
        if(boardModel.getImg().equals("")) holder.imgImg.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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
                    BoardModel boardModel = arrayList.get(pos);

                    //
                    int views = Integer.valueOf(boardModel.getViews()) + 1;
                    boardModel.setViews(String.valueOf(views));

                    Map<String, Object> map = new HashMap<>();
                    map.put("views", views);

                    // total_comm
                    db.collection("Community").document("board")
                            .collection("item").document(boardModel.getBoard_index()).update(map);

                    // 최근 검색 데이터 쌓기
                    DocumentReference dr_recentlySearched =
                            db.collection("Users").document(user.getUid())
                            .collection("Community").document("RecentlySearched");

                    dr_recentlySearched.get().addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        int final_index;

                        if(document.get("final_index") != null) final_index = Integer.valueOf(document.get("final_index").toString());
                        else final_index = 0;

                        Map<String, Object> rs = new HashMap<>();
                        Map<String, Object> text = new HashMap<>();
                        rs.put("final_index", ++final_index);
                        text.put("word", edit_search);
                        text.put("index", final_index);
                        dr_recentlySearched.update(rs);

                        dr_recentlySearched.collection("item").document(String.valueOf(final_index))
                                .set(text);
                    });

                    Intent intent = new Intent(activity.getApplicationContext(), ComPickBoardActivity.class);
                    intent.putExtra("BoardModel", boardModel);
                    ScreenChangeDetect.getInstance().setSearchToPick(true);

                    activity.startActivity(intent);
                }
            });
        }
    }
}
