package com.example.Runner8.ui.community;

import android.view.View;

import com.example.Runner8.ui.community.Adapter.Comment.CommentAdapter;

public interface OnCommentItemClickListener {
    public void onItemClick(CommentAdapter.ViewHolder holder, View view, int position);
}
