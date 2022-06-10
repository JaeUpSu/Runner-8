package com.example.Runner8.ui.community.Adapter.Notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Runner8.R;
import com.example.Runner8.ui.community.Adapter.Notice.model.NoticeModel;
import com.example.Runner8.ui.community.Activity.ComNoticeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{

    //
    private final String ADMIN = "JpPFT0etXLaBlhEZCbsddu9ZKhK2";
    private Activity activity;
    //
    ArrayList<NoticeModel> noticeModels;

    //
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public NoticeAdapter(Activity activity, ArrayList<NoticeModel> noticeModels){
        this.activity = activity;
        this.noticeModels = noticeModels;
    }

    @NonNull
    @NotNull
    @Override
    public NoticeAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notice_board_item,parent,false);
        return new NoticeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NoticeAdapter.ViewHolder holder, int position) {
        NoticeModel noticeModel = noticeModels.get(position);

        // name, profileImg
        db.collection("Users").document(ADMIN)
                .collection("Profile").document("diet_profile")
                .get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            holder.tv_user.setText("Admin");
            // profile
            noticeModel.setProfile(document.get("user_img").toString());
            Log.i("noticeModel.setProfile", "check!!");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.img_user.setClipToOutline(true);
            }
            Glide.with(holder.itemView)
                    .load(document.get("user_img").toString())
                    .into(holder.img_user);
        });

        // title
        holder.tv_title.setText(noticeModel.getTitle());
        // timeValue
        holder.tv_date.setText(noticeModel.getTimeValue());
        // views
        holder.tv_viewCount.setText(noticeModel.getViews());

    }

    @Override
    public int getItemCount() {
        return noticeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_user;
        TextView tv_user, tv_title, tv_date, tv_viewCount;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);

            img_user = view.findViewById(R.id.img_user);
            tv_title = view.findViewById(R.id.tv_title);
            tv_date = view.findViewById(R.id.tv_date);
            tv_viewCount = view.findViewById(R.id.tv_viewCount);
            tv_user = view.findViewById(R.id.tv_user);

            view.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    NoticeModel noticeModel = noticeModels.get(pos);

                    int views = Integer.valueOf(noticeModel.getViews()) + 1;
                    noticeModel.setViews(String.valueOf(views));

                    //
                    noticeModel.setArray_position(String.valueOf(pos));

                    Map<String, Object> map = new HashMap<>();
                    map.put("views", views);

                    // total_comm
                    db.collection("Community").document("noticeBoard")
                            .collection("item").document(noticeModel.getIndex()).update(map);

                    Intent intent = new Intent(activity.getApplicationContext(), ComNoticeActivity.class);
                    intent.putExtra("NoticeModel", noticeModel);

                    activity.startActivity(intent);
                }

            });

        }
    }
}
