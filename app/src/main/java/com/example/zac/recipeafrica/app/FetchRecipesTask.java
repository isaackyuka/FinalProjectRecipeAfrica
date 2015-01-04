package com.example.zac.recipeafrica.app;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
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
public class FetchRecipesTask extends AsyncTask<Void, Void, Void> {
    private RecipeApi recipeApiService = null;
    private Context context;
    Uri recipeInsertUri;
    ProgressDialog pleaseWaitDialog;

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

    public void storeRecipeDb(ArrayList<RecipeRecord> recipeList) {
        ContentValues recipeValues = new ContentValues();
        for (int i = 0; i < recipeList.size(); i++) {
            RecipeRecord recipeRecord = recipeList.get(i);

            recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY, recipeRecord.getRecipeID());
            recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME, recipeRecord.getTitle());
            recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_DESCRIPTION, recipeRecord.getDescription());
            recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_INGREDIENTS, recipeRecord.getIngredients());
            recipeValues.put(RecipeContract.RecipeEntry.RECIPE_COLUMN_STEPS, recipeRecord.getSteps());

            recipeInsertUri = context.getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI, recipeValues);
        }
    }
    @Override
    protected Void doInBackground(Void... params) {

        if (recipeApiService == null) {

            RecipeApi.Builder builder = new RecipeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://ace-fiber-802.appspot.com/_ah/api/");
            recipeApiService = builder.build();
        }

        try {
            List<RecipeBean> remoteRecipes = recipeApiService.getRecipes().execute().getItems();

            if(remoteRecipes != null) {
                ArrayList<RecipeRecord> recipeList = new ArrayList<RecipeRecord>();
                for (RecipeBean recipeBean : remoteRecipes) {
                    recipeList.add(new RecipeRecord(recipeBean.getId(), recipeBean.getTitle(), recipeBean.getDescription(),
                    recipeBean.getIngredients(), recipeBean.getSteps()));
                }
                storeRecipeDb(recipeList);
            }

        } catch (IOException e) {
            Log.e(FetchRecipesTask.class.getSimpleName(), "Error when loading tasks", e);
        }

        return null;
    }

//    @Override
//    protected void onPostExecute(Void aVoid) {
//        pleaseWaitDialog.dismiss();
//        }
 }

