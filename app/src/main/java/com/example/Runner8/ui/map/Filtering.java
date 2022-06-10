package com.example.Runner8.ui.map;

import android.widget.ToggleButton;

import com.example.Runner8.R;
import com.google.firebase.firestore.Query;

public class Filtering {

    ToggleButton toggleButton;
    String filter_name;
    boolean merge = true;

    public Filtering(){
    }

    public String getFilter_name() {
        return filter_name;
    }

    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }

    public void setToggleButton(ToggleButton toggleButton) {
        this.toggleButton = toggleButton;
        toggleButton.setChecked(true);
        toggleButton.setBackgroundResource(R.drawable.customborder_checked);

    }

    public ToggleButton getToggleButton() {
        return toggleButton;
    }

    public void changedChecked(ToggleButton toggleButton){
        this.toggleButton.setChecked(false);
        this.toggleButton.setBackgroundResource(R.drawable.customborder);
        this.toggleButton = toggleButton;

        toggleButton.setChecked(true);
        toggleButton.setBackgroundResource(R.drawable.customborder_checked);
    }

    public void setMerge(boolean merge) {
        this.merge = merge;
    }

    public boolean isMerge() {
        return merge;
    }

    public Query.Direction getDirection() {
        if(isMerge())
            return Query.Direction.DESCENDING;

        else
            return Query.Direction.ASCENDING;
    }
}
