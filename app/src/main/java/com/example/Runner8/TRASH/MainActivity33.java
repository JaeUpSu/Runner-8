package com.example.Runner8.TRASH;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.Runner8.R;
import com.facebook.shimmer.ShimmerFrameLayout;


public class MainActivity33 extends AppCompatActivity {

    ShimmerFrameLayout shimmer;
    TextView txt, txtToolbarTitle;
    ImageView img;
    ProgressBar progressBar;
    Toolbar toolbar;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main32);

        shimmer = findViewById(R.id.shimmer_view);
        txt = findViewById(R.id.text_shimmer);
        img = findViewById(R.id.img_icon);

        txt.setText("The Runner 8 is an App created to guide " +
                "people's healthy lives through the interest that arises from competition."
                + "\n\n" + "The Runner 8는 경쟁에서 생기는 흥미를 통해서 " +
                "사람들의 건강한 삶을 인도해주기 위해서 만든 App입니다. ");

        shimmer.startShimmer();
   }

}