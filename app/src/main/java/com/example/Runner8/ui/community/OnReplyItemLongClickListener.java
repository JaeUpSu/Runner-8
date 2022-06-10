package com.example.Runner8.ui.community;

import android.view.View;

import com.example.Runner8.ui.community.Adapter.Reply.ReplyAdapter;

public interface OnReplyItemLongClickListener {
    public void onReplyItemLongClick(ReplyAdapter.ViewHolder holder, View view, int position);
}
