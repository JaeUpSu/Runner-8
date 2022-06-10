package com.example.Runner8.ui.community;

import android.view.View;

import com.example.Runner8.ui.community.Adapter.Comment.CommentAdapter;

public interface OnCommentItemLongClickListener {
    public void onItemLongClick(CommentAdapter.ViewHolder holder, View view, int position);
}
