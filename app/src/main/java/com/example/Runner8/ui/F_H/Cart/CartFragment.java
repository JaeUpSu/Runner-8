package com.example.Runner8.ui.F_H.Cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Runner8.R;
import com.example.Runner8.SingleTon.Sub_bundle;
import com.example.Runner8.ui.F_H.calorie.RecyclerDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<CartItem> cartItems = new ArrayList<>();
    CartListAdapter cartListAdapter;
    RecyclerView cartRecyclerList;
    Button cart_save;
    TextView textTotalKcal;
    Double total_kcal = 0.0;

    String foodName = "";

    CollectionReference docRef = db.collection("Users")
            .document(user.getUid()).collection("FoodCart");

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_food_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartRecyclerList = view.findViewById(R.id.cart_recycler);
        cartRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        cart_save = view.findViewById(R.id.cart_save);
        textTotalKcal = view.findViewById(R.id.total_kcal);

        // totalKcal 초기화
        TotalKcalInitial();

        // RecyclerView 간격 조절
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(20);
        cartRecyclerList.addItemDecoration(spaceDecoration);

        // 저장
        cart_save.setOnClickListener(v -> {

            Map<String, Object> map = new HashMap<>();
            Map<String, Object> totalKcal = new HashMap<>();
            double sum = 0.0;

            for(CartItem item : cartItems){
                map.put("Count", item.getFoodCount());

                db.collection("Users").document(user.getUid())
                        .collection("FoodCart").document(item.getFoodName())
                        .update(map);

                sum += Double.parseDouble(item.getFoodKcal())*Double.parseDouble(item.getFoodCount());
            }

            totalKcal.put("TotalKcalOfDay", sum);
            db.collection("Users").document(user.getUid())
                    .set(totalKcal);
            textTotalKcal.setText(Double.toString(Math.round(sum)));

           // SingleTon
            Sub_bundle.getInstance().setTotal_food_kcal(String.valueOf(sum));

            Toast.makeText(getContext(), "저장되었습니다!!", Toast.LENGTH_SHORT).show();
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                cartItems.remove(position);
                cartListAdapter.notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(cartRecyclerList);

    }

    public void TotalKcalInitial() {
        textTotalKcal.setText(Sub_bundle.getInstance().getTotal_food_kcal());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 데이터 받아오기
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot docSnap : task.getResult()){
                        Log.i("IdAndEng", docSnap.getId() + docSnap.get("Eng"));
                        CartItem cartItem = new CartItem(docSnap.getId(), docSnap.get("Eng").toString(), docSnap.get("Count").toString());
                        cartItems.add(cartItem);
                    }
                    cartListAdapter = new CartListAdapter(cartItems);
                    cartListAdapter.notifyDataSetChanged();
                    cartRecyclerList.setAdapter(cartListAdapter);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
