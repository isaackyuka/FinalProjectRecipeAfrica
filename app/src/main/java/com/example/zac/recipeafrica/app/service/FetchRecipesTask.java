package com.example.zac.recipeafrica.app.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.example.zac.myapplication.backend.recipeApi.RecipeApi;
import com.example.zac.myapplication.backend.recipeApi.model.RecipeBean;
import com.example.zac.recipeafrica.app.data.RecipeContract;
import com.example.zac.recipeafrica.app.data.RecipeRecord;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZAC on 1/3/2015.
 */
public class FetchRecipesTask extends AsyncTask<Void, Void, List<RecipeRecord>> {

    private Context context;

//    ProgressDialog pleaseWaitDialog;

    public FetchRecipesTask(Context mContext) {
        context = mContext;
    }

//    @Override
//    protected void onPreExecute() {
//        pleaseWaitDialog = ProgressDialog.show(
//                context,
//                "Synching",
//                "Fetching data ...",
//                true, true);
//
//        pleaseWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            public void onCancel(DialogInterface dialog) {
//                FetchRecipesTask.this.cancel(true);
//            }
//        });
//    }


    @Override
    protected List<RecipeRecord> doInBackground(Void... params) {
        RecipeApi recipeApiService = null;
        if (recipeApiService == null) {

            RecipeApi.Builder builder = new RecipeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://ace-fiber-802.appspot.com/_ah/api/");
            recipeApiService = builder.build();
        }

        try {
            List<RecipeBean> remoteRecipes = recipeApiService.getRecipes().execute().getItems();

            if (remoteRecipes != null) {
                List<RecipeRecord> recipeList = new ArrayList<RecipeRecord>();
                for (RecipeBean recipeBean : remoteRecipes) {
                    recipeList.add(new RecipeRecord(recipeBean.getId(), recipeBean.getTitle(), recipeBean.getDescription(),
                            recipeBean.getIngredients(), recipeBean.getSteps()));
                }
//                Toast.makeText(context, recipeList.size(), Toast.LENGTH_LONG);
////                return recipeList;
                ContentValues recipeValues = new ContentValues();
                for (int i = 0; i < recipeList.size(); i++) {
                    RecipeRecord recipeRecord = recipeList.get(i);

                    Cursor cursor = context.getContentResolver().query(
                        RecipeContract.RecipeEntry.CONTENT_URI,
                        new String[]{RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY},
                        RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY + " = ?",
                        new String[]{recipeRecord.getRecipeID().toString()},
                        null);
                if (cursor.moveToFirst()) {
                    int recipeIdIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY);
//                    return cursor.getLong(recipeIdIndex);
                } else {


                    recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY, recipeRecord.getRecipeID());
                    recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME, recipeRecord.getTitle());
                    recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_DESCRIPTION, recipeRecord.getDescription());
                    recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_INGREDIENTS, recipeRecord.getIngredients());
                    recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_STEPS, recipeRecord.getSteps());

//            Uri recipeInsertUri =
                    context.getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI, recipeValues);
                }
                }
            }
//                Toast.makeText(context, (CharSequence) recipeList, Toast.LENGTH_LONG);
//                storeRecipeDb(recipeList);


        } catch (IOException e) {
            Log.e(FetchRecipesTask.class.getSimpleName(), "Error when loading tasks", e);
        }


        return null;
    }
}

