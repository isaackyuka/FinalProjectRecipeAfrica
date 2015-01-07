package com.example.zac.recipeafrica.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ZAC on 12/12/2014.
 */
public class RecipeDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_NAME = "recipes.db";

    // If you change the database schema, you must increment the database version.
    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table if not exists " + RecipeContract.RecipeEntry.RECIPE_TABLE + " ( " +
                        RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_ID + " integer primary key autoincrement, " +
                        RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY + " Long," +
                        RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME +" text," +
                        RecipeContract.RecipeEntry.RECIPE_COLUMN_DESCRIPTION + " text, " +
                        RecipeContract.RecipeEntry.RECIPE_COLUMN_INGREDIENTS + " text, " +
                        RecipeContract.RecipeEntry.RECIPE_COLUMN_STEPS + " text, " +
                        RecipeContract.RecipeEntry.RECIPE_COLUMN_IMAGE + " text, " +
                        RecipeContract.RecipeEntry.RECIPE_COLUMN_VIDEO + " text)"
        );

        sqLiteDatabase.execSQL("create table if not exists " + RecipeContract.CommentEntry.COMMENT_TABLE + " ( " +
                        RecipeContract.CommentEntry.COMMENT_COLUMN_COMMENT_ID + " integer primary key autoincrement, " +
                        RecipeContract.CommentEntry.COMMENT_COLUMN_RECIPE_KEY +" Long," +
                        RecipeContract.CommentEntry.COMMENT_COLUMN_USER + " text, " +
                        RecipeContract.CommentEntry.COMMENT_COLUMN_COMMENT + " text, " +
                        RecipeContract.CommentEntry.COMMENT_COLUMN_RATING + " Long, " +
                        " FOREIGN KEY (" + RecipeContract.CommentEntry.COMMENT_COLUMN_RECIPE_KEY + ") REFERENCES " +
                        RecipeContract.RecipeEntry.RECIPE_TABLE + " (" + RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY + "))"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.RECIPE_TABLE + "");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.CommentEntry.COMMENT_TABLE + "");
        onCreate(sqLiteDatabase);
    }

    public void saveReview(Long recipeID, String username, String comment,Long rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues commentValues = new ContentValues();

        commentValues.put(RecipeContract.CommentEntry.COMMENT_COLUMN_RECIPE_KEY, recipeID);
        commentValues.put(RecipeContract.CommentEntry.COMMENT_COLUMN_USER, username);
        commentValues.put(RecipeContract.CommentEntry.COMMENT_COLUMN_COMMENT, comment);
        commentValues.put(RecipeContract.CommentEntry.COMMENT_COLUMN_RATING, rating);

        db.insert(RecipeContract.CommentEntry.COMMENT_TABLE, null, commentValues);

    }


    public RecipeRecord findRecipe(String recipeName) {
        String query = "Select * FROM " + RecipeContract.RecipeEntry.RECIPE_TABLE +
                " WHERE " + RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME + " =  \"" + recipeName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        RecipeRecord recipe = new RecipeRecord(null, null, null, null, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            recipe.setRecipeID(cursor.getLong(0));
            recipe.setTitle(cursor.getString(1));
            recipe.setDescription(cursor.getString(2));
            recipe.setIngredients(cursor.getString(3));
            recipe.setSteps(cursor.getString(4));

            cursor.close();
        } else {
            recipe = null;
        }
        db.close();
        return recipe;
    }

    public Cursor searchRecipe(String recipeName) {
        String query = "Select * FROM " + RecipeContract.RecipeEntry.RECIPE_TABLE +
                " WHERE " + RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME + " LIKE  '%" + recipeName + "%'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor getReviews() {
        String query = "SELECT * FROM " + RecipeContract.CommentEntry.COMMENT_TABLE +
                " ORDER BY " + RecipeContract.CommentEntry.COMMENT_COLUMN_COMMENT_ID + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        return cursor;

    }

    public Long getRecipeId() {
        String query = "SELECT * FROM " + RecipeContract.CommentEntry.COMMENT_TABLE +
                " ORDER BY " + RecipeContract.CommentEntry.COMMENT_COLUMN_COMMENT_ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Long recipeID = Long.valueOf(1);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            recipeID = cursor.getLong(0);
        }
        return recipeID;


    }

    public Cursor getRecipe(String search_str) {
        String query = "SELECT * FROM " + RecipeContract.RecipeEntry.RECIPE_TABLE +
                " WHERE " + RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME + " LIKE '%"+search_str+"%'";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


}

