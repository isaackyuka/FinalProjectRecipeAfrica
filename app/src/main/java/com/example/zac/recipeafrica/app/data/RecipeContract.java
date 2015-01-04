package com.example.zac.recipeafrica.app.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ZAC on 12/12/2014.
 */
public class  RecipeContract {

    public static final String CONTENT_AUTHORITY = "com.example.zac.recipeafrica.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

   public static final String RECIPE_PATH = "recipe";

    public static class RecipeEntry implements BaseColumns {
        public static final String RECIPE_TABLE = "recipeRecords";

        public static final String RECIPE_COLUMN_RECIPE_ID = "_id";
        public static final String RECIPE_COLUMN_RECIPE_KEY = "recipeId";
        public static final String RECIPE_COLUMN_RECIPE_NAME = "recipeName";
        public static final String RECIPE_COLUMN_DESCRIPTION= "description";
        public static final String RECIPE_COLUMN_INGREDIENTS = "ingredients";
        public static final String RECIPE_COLUMN_STEPS = "steps";
        public static final String RECIPE_COLUMN_IMAGE = "image";
        public static final String RECIPE_COLUMN_VIDEO = "video";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(RECIPE_PATH).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + RECIPE_PATH;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + RECIPE_PATH;

        public static Uri buildRecipeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static class CommentEntry implements BaseColumns {
        public static final String COMMENT_TABLE = "comments";

        public static final String COMMENT_COLUMN_COMMENT_ID = "_id";
        public static final String COMMENT_COLUMN_RECIPE_KEY = "recipe_id";
        public static final String COMMENT_COLUMN_USER = "username";
        public static final String COMMENT_COLUMN_COMMENT= "comment";
        public static final String COMMENT_COLUMN_RATING = "rating";
    }
}
