package com.example.Runner8.ui.trash;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import com.example.Runner8.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RepresentativeFoodActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TableLayout tableLayout;
    private int count=0, incrementID = 1;

    private List foodName = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_classification);

        tableLayout = findViewById(R.id.main_table);
        tableLayout.setShrinkAllColumns(true);

        Intent intent = getIntent();
        String classification = intent.getStringExtra("분류");

        Log.i("classification",classification.toString());

        db.collection("Calorie").document("Food")
                .collection(classification)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult())
                                foodName.add(document.getId());

                            int imgCount = count;

                            Log.i("foodName",foodName.toString() + "size : " + foodName.size());

                            while(count<foodName.size()){

                                TableRow tableRow1 = new TableRow(getApplication());
                                tableRow1.setLayoutParams(new TableRow.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT));

                                for(int i=0;i<4 && imgCount<foodName.size();i++){
                                    ImageView imageView = new ImageView(getApplication());
                                    imageView.setImageResource(R.drawable.image4_2);
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                                    String food_name = foodName.get(imgCount).toString();

                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplication(), FoodDetailActivity.class);
                                            Log.i("food_name",food_name);
                                            intent.putExtra("FoodName", food_name);
                                            startActivity(intent);
                                        }
                                    });

                                    tableRow1.addView(imageView);
                                    imgCount++;
                                }
                                tableLayout.addView(tableRow1);

                                TableRow tableRow2 = new TableRow(getApplication());
                                tableRow2.setLayoutParams(new TableRow.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT));

                                for(int i=0;i<4 && count<foodName.size() ;i++){
                                    TextView textView = new TextView(getApplication());
                                    textView.setText(foodName.get(count).toString());
                                    textView.setTypeface(null, Typeface.BOLD_ITALIC);
                                    textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
                                    TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                                    tableRow2.addView(textView);
                                    count++;
                                }

                                tableLayout.addView(tableRow2);
                            }
                        }
                        else Log.d("QueryError", "Error getting documents: ", task.getException());
                    }
                });
    }
}
