package com.example.zac.recipeafrica.app.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.zac.myapplication.backend.recipeApi.RecipeApi;
import com.example.zac.myapplication.backend.recipeApi.model.ReviewBean;
import com.example.zac.recipeafrica.app.data.RecipeDbHelper;
import com.example.zac.recipeafrica.app.data.ReviewRecord;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZAC on 1/6/2015.
 */
public class FetchReviewsTask extends AsyncTask<Long, Void, List<ReviewBean>> {
    private Context context;

//    ProgressDialog pleaseWaitDialog;

    public FetchReviewsTask(Context mContext) {
        context = mContext;
    }

    @Override
    protected List<ReviewBean> doInBackground(Long... params) {
        RecipeApi recipeApiService = null;

        if (recipeApiService == null) {

            RecipeApi.Builder builder = new RecipeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://ace-fiber-802.appspot.com/_ah/api/");
            recipeApiService = builder.build();
        }
        Long recipeId = params[0];
        RecipeDbHelper dbHelper = new RecipeDbHelper(context);
        try {
            List<ReviewBean> remoteReviews = recipeApiService.getReviewsForRecipe(recipeId).execute().getItems();

            if (remoteReviews != null) {
                List<ReviewRecord> reviewList = new ArrayList<ReviewRecord>();
                for (ReviewBean reviewBean : remoteReviews) {
                    reviewList.add(new ReviewRecord(reviewBean.getRecipeId(), reviewBean.getUsername(),
                            reviewBean.getComment(), reviewBean.getRating()));
                }

                for (int i = 0; i < reviewList.size(); i++) {
                    ReviewRecord reviewRecord = reviewList.get(i);
                    dbHelper.saveReview(reviewRecord.getRecipeID(), reviewRecord.getUsername(),
                            reviewRecord.getComment(), reviewRecord.getRating());
                }
            }

        } catch (IOException e) {
            Log.e(FetchReviewsTask.class.getSimpleName(), "Error when retrieving reviews", e);
        }

        return null;
    }
}
