package com.example.zac.recipeafrica.app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.zac.recipeafrica.app.data.RecipeContract;

/**
 * Created by ZAC on 12/27/2014.
 */
public class SearchAdapter extends CursorAdapter {
    TextView titleText;
    TextView descriptionText;

    public SearchAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_search, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

//        userTextView.setText(cursor.getString(cursor.getColumnIndex(RecipeContract.CommentEntry.COMMENT_COLUMN_USER)));
        titleText = (TextView) view.findViewById(R.id.search_recipe_title);
        descriptionText = (TextView) view.findViewById(R.id.search_recipe_description);
        titleText.setText(cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME)));

       descriptionText.setText(cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_COLUMN_DESCRIPTION)));
    }
}
