package com.example.zac.recipeafrica.app;

/**
 * Created by ZAC on 12/21/2014.
 */


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zac.myapplication.backend.recipeApi.RecipeApi;
import com.example.zac.myapplication.backend.recipeApi.model.ReviewBean;
import com.example.zac.recipeafrica.app.data.RecipeContract;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.Random;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    ReviewAdapter reviewAdapter;
    private  static final String RECIPE_SHARE_HASHTAG = "#RecipeAfrica";
    public static final String RECIPE_KEY = "recipeName";
    private String recipe;
    public Long recipeKey;
    public int tag;
    private ShareActionProvider mShareActionProvider;

    private static final int DETAIL_LOADER = 0;

    private static final String[] RECIPE_COLUMNS = {
            RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_ID,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_DESCRIPTION,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_INGREDIENTS,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_STEPS
    };

    private TextView titleView;
    private TextView descriptionView;
    private TextView ingredientsView;
    private TextView stepsView;

    private EditText username;
    private EditText comment;
    private CheckBox star;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(RECIPE_KEY, recipe);
        super.onSaveInstanceState(outState);
    }

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(RecipeDetailActivity.RECIPE_KEY) &&
                recipe != null) {
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            recipe = arguments.getString(RecipeDetailActivity.RECIPE_KEY);
        }

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getString(RECIPE_KEY);
        }

      final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

      titleView = (TextView) rootView.findViewById(R.id.detail_title);
        descriptionView = (TextView) rootView.findViewById(R.id.detail_description);
        ingredientsView = (TextView) rootView.findViewById(R.id.detail_ingredients);
       stepsView = (TextView) rootView.findViewById(R.id.detail_steps);

        for (int i = 1; i <= 5; i++) {
            star = (CheckBox) rootView.findViewWithTag(String.valueOf(i));
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   tag = Integer.valueOf((String) v.getTag());
//---check all the stars up to the one touched---
                    for (int i = 1; i <= tag; i++) {
                        star = (CheckBox) rootView.findViewWithTag(String.valueOf(i));
                        star.setChecked(true);
                    }
//---uncheck all remaining stars---
                    for (int i = tag + 1; i <= 5; i++) {
                        star = (CheckBox) rootView.findViewWithTag(String.valueOf(i));
                        star.setChecked(false);
                    }
                }
            });
        }

        // Review form
       username = (EditText) rootView.findViewById(R.id.usernameEdit);
       comment = (EditText) rootView.findViewById(R.id.reviewEdit);

//       Save the review
        Button addReview = (Button) rootView.findViewById(R.id.addReviewBtn);
        addReview.setOnClickListener(onAddReview);

        return rootView;
    }


    public View.OnClickListener onAddReview = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          AddReviewTask addReviewTask = new AddReviewTask();
            addReviewTask.execute();
            username.setText("");
            comment.setText("");
            Toast.makeText(getActivity(), "Thank you for the review.", Toast.LENGTH_LONG).show();
        }
    };


    public static DetailFragment newInstance(String recipe) {
        DetailFragment f = new DetailFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("recipe", recipe);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            recipe = savedInstanceState.getString(RECIPE_KEY);
        }

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(RecipeDetailActivity.RECIPE_KEY)) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (recipe != null) {
            mShareActionProvider.setShareIntent(createShareRecipeIntent());
        }
    }

    @Override

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null || !intent.hasExtra(RECIPE_KEY)) {
            return  null;
        }
        String recipeName = intent.getStringExtra(RECIPE_KEY);
        String selection = RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME + "=\""
                + recipeName + "\" ";

        String sortOrder = RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_ID + " DESC";

        Uri recipeUri = RecipeContract.RecipeEntry.CONTENT_URI;

        return new CursorLoader(
                getActivity(),
                recipeUri,
                RECIPE_COLUMNS,
                selection,
                null,
                sortOrder
        );
    }



    private Intent createShareRecipeIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipe + RECIPE_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        recipeKey = data.getLong(data.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY));

        String title = data.getString(data.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME));
        ((TextView) getView().findViewById(R.id.detail_title)).setText(title);

        String description = data.getString(data.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_COLUMN_DESCRIPTION));
        ((TextView) getView().findViewById(R.id.detail_description)).setText(description);

        String ingredients = data.getString(data.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_COLUMN_INGREDIENTS));
        ((TextView) getView().findViewById(R.id.detail_ingredients)).setText(ingredients);

        String steps = data.getString(data.getColumnIndex(RecipeContract.RecipeEntry.RECIPE_COLUMN_STEPS));
        ((TextView) getView().findViewById(R.id.detail_steps)).setText(steps);

        recipe = String.format("%s - %s ", title, description);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareRecipeIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public class AddReviewTask extends AsyncTask<Void, Void, Void> {
        private RecipeApi recipeApiService = null;

        @Override
        protected Void doInBackground(Void... params) {
            if (recipeApiService == null) {
                RecipeApi.Builder builder = new RecipeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://ace-fiber-802.appspot.com/_ah/api/");
                recipeApiService = builder.build();
            }

            String userText = username.getText().toString();
            String commentText = comment.getText().toString();

//            Long recipeId = Long.valueOf(recipeID);
            Random r = new Random();
            int reviewId = r.nextInt(Integer.MAX_VALUE);
            Long id = Long.valueOf(reviewId);

            try {
                ReviewBean reviewBean = new ReviewBean();
                reviewBean.setRating(Long.valueOf(tag));
                reviewBean.setComment(commentText);
                reviewBean.setUsername(userText);
                reviewBean.setRecipeId(recipeKey);
                reviewBean.setId(id);

                recipeApiService.saveReview(reviewBean).execute();

            } catch (IOException e) {
                Log.e(DetailFragment.class.getSimpleName(), "Error when saving review", e);
            }

            return null;
        }
    }

}