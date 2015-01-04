package com.example.zac.recipeafrica.app;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.zac.recipeafrica.app.data.RecipeContract;

/**
 * Created by ZAC on 12/23/2014.
 */
public class ReviewAdapter extends CursorAdapter {
    CheckBox star;
    public ReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_review, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView userTextView = (TextView) view.findViewById(R.id.userView);
        userTextView.setText(cursor.getString(cursor.getColumnIndex(RecipeContract.CommentEntry.COMMENT_COLUMN_USER)));

        TextView commentTextView = (TextView) view.findViewById(R.id.commentView);
        commentTextView.setText(cursor.getString(cursor.getColumnIndex(RecipeContract.CommentEntry.COMMENT_COLUMN_COMMENT)));

        for (int i = 1; i <= 5; i++) {
            star = (CheckBox) view.findViewWithTag(String.valueOf(i));
            int tag = cursor.getInt(cursor.getColumnIndex(RecipeContract.CommentEntry.COMMENT_COLUMN_RATING));
            for (int j = 1; j <= tag; j++) {
                star = (CheckBox) view.findViewWithTag(String.valueOf(j));
                star.setChecked(true);
            }

            //---uncheck all remaining stars---
            for (int k = tag + 1; k <= 5; i++) {
                star = (CheckBox) view.findViewWithTag(String.valueOf(k));
                star.setChecked(false);
            }
        }
    }

}
