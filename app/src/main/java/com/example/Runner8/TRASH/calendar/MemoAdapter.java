package com.example.Runner8.TRASH.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.Runner8.R;

import java.util.ArrayList;

public class MemoAdapter extends BaseAdapter {
    ArrayList<Memo> memoArrayList;
    Context mContext;

    public MemoAdapter(){
        this.memoArrayList = memoArrayList;
        this.mContext = mContext;
    }

    public MemoAdapter(ArrayList<Memo> memoArrayList, Context mContext) {
        this.memoArrayList = memoArrayList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return memoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return memoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void removeItem(int position) {
        memoArrayList.remove(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_memo,parent,false);

        }
        TextView tvTitle = convertView.findViewById(R.id.memo_tv_title);
        TextView tvContents = convertView.findViewById(R.id.memo_tv_contents);

        tvTitle.setText(memoArrayList.get(position).title);
        tvContents.setText(memoArrayList.get(position).contents);

        return convertView;
    }
}