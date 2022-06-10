package com.example.Runner8.ui.Loading;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.Runner8.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class LoadingActivity extends AppCompatActivity {

    ShimmerFrameLayout shimmer;
    TextView txt, txtToolbarTitle;
    ImageView img;
    LinearProgressIndicator loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        shimmer = findViewById(R.id.shimmer_view);
        txt = findViewById(R.id.text_shimmer);
        img = findViewById(R.id.img_icon);
        loadingProgress = findViewById(R.id.loading_progress);

        txt.setText("The Runner 8 is an App created to guide " +
                "people's healthy lives through the interest that arises from running competition."
                + "\n\n" + "The Runner 8는 런닝 경쟁에서 생기는 흥미를 통해서 " +
                "사람들의 건강한 삶을 인도해주기 위해서 만든 App입니다. ");

        shimmer.startShimmer();

        img.setOnClickListener(view -> {
            DialogFragment dialogue = new LoadingDialogue();
            dialogue.show(getSupportFragmentManager(),"Dialog");
        });

    }
}