package com.example.Runner8.ui.F_H.calorie;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class RecyclerDecoration extends RecyclerView.ItemDecoration {
    private final int divHeight;

    public RecyclerDecoration(int divHeight){
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect,
                               @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
            outRect.bottom = divHeight;

    }
}
