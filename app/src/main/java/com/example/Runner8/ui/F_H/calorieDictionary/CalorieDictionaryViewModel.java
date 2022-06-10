package com.example.Runner8.ui.F_H.calorieDictionary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalorieDictionaryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CalorieDictionaryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is calendar Fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}