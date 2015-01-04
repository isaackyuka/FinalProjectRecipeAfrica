package com.example.zac.recipeafrica.app.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.example.zac.myapplication.backend.recipeApi.RecipeApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

/**
 * Created by ZAC on 12/28/2014.
 */
public class SearchIntentService extends IntentService {
    private RecipeApi recipeApiService = null;
    private Context context;
    public SearchIntentService() {
        super("SearchIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String searchStr = intent.getExtras().getString("searchStr");

        if (recipeApiService == null) {

            RecipeApi.Builder builder = new RecipeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://ace-fiber-802.appspot.com/_ah/api/");
            recipeApiService = builder.build();
        }


    }
}
