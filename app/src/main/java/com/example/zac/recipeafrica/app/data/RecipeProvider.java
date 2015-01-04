package com.example.zac.recipeafrica.app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by ZAC on 12/12/2014.
 */
public class RecipeProvider extends ContentProvider {

    RecipeDbHelper dbHelper;
    private static final UriMatcher rUriMatcher = buildUriMatcher();
    private static final int RECIPES = 400;
    private static final int RECIPE_ID = 401;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;

        //Uri types
        matcher.addURI(authority, RecipeContract.RECIPE_PATH, RECIPES);
        matcher.addURI(authority, RecipeContract.RECIPE_PATH + "/#", RECIPE_ID);

        return matcher;
    }



    @Override
    public boolean onCreate() {
        dbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (rUriMatcher.match(uri)) {
            case RECIPE_ID: {
                retCursor = dbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.RECIPE_TABLE,
                        projection,
                        RecipeContract.RecipeEntry._ID + " = " +
                                ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case RECIPES: {
                retCursor = dbHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.RECIPE_TABLE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = rUriMatcher.match(uri);

        switch (match) {
            case RECIPES:
                return RecipeContract.RecipeEntry.CONTENT_TYPE;
            case RECIPE_ID:
                return RecipeContract.RecipeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = rUriMatcher.match(uri);
        Uri returnUri;

        if (match == RECIPES) {
            long _id = db.insert(RecipeContract.RecipeEntry.RECIPE_TABLE, null, contentValues);
            if ( _id > 0 )
                returnUri = RecipeContract.RecipeEntry.buildRecipeUri(_id);
            else
                throw new android.database.SQLException("Failed to insert row into " + uri);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

//        switch (match) {
//            case RECIPES:{
//                long _id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, contentValues);
//            if ( _id > 0 )
//                returnUri = RecipeContract.RecipeEntry.buildRecipeUri(_id);
//            else
//                throw new android.database.SQLException("Failed to insert row into " + uri);
//            break;
//            }
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = rUriMatcher.match(uri);
        int rowsDeleted;

        if (match == RECIPES) {
            rowsDeleted = db.delete(
                    RecipeContract.RecipeEntry.RECIPE_TABLE, selection, selectionArgs);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

//        switch (match) {
//
//            case RECIPES:
//                rowsDeleted = db.delete(
//                        RecipeContract.RecipeEntry.TABLE_NAME, selection, selectionArgs);
//                break;
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = rUriMatcher.match(uri);
        int rowsUpdated;

        if (match == RECIPES) {
            rowsUpdated = db.update(RecipeContract.RecipeEntry.RECIPE_TABLE, values, selection,
                    selectionArgs);
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

//        switch (match) {
//
//            case RECIPES:
//                rowsUpdated = db.update(RecipeContract.RecipeEntry.TABLE_NAME, values, selection,
//                        selectionArgs);
//                break;
//            default:
//                throw new UnsupportedOperationException("Unknown uri: " + uri);
//        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = rUriMatcher.match(uri);

        if (match == RECIPES) {
            db.beginTransaction();
            int returnCount = 0;
            try {
                for (ContentValues value : values) {
                    long _id = db.insert(RecipeContract.RecipeEntry.RECIPE_TABLE, null, value);
                    if (_id != -1) {
                        returnCount++;
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return returnCount;
        } else {
            return super.bulkInsert(uri, values);
        }

//        switch (match) {
//            case RECIPES:
//                db.beginTransaction();
//                int returnCount = 0;
//                try {
//                    for (ContentValues value : values) {
//                        long _id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME, null, value);
//                        if (_id != -1) {
//                            returnCount++;
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//                getContext().getContentResolver().notifyChange(uri, null);
//                return returnCount;
//            default:
//                return super.bulkInsert(uri, values);
//        }
    }
}
