package com.example.zac.recipeafrica.app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by ZAC on 12/17/2014.
 */
public class RecipeAdapter extends CursorAdapter {

    public static class ViewHolder {
        public final TextView titleText;
        public final TextView descriptionText;


        public ViewHolder(View view) {
            titleText = (TextView) view.findViewById(R.id.list_view_recipe_title);
            descriptionText = (TextView) view.findViewById(R.id.list_view_recipe_description);
        }
    }

    public RecipeAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recipe, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String title =
                cursor.getString(RecipeFragment.COL_RECIPE_TITLE);
        viewHolder.titleText.setText(title);

        String description = cursor.getString(RecipeFragment.COL_RECIPE_DESC);
        viewHolder.descriptionText.setText(description);

    }

}