package com.example.zac.recipeafrica.app;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zac.recipeafrica.app.data.RecipeContract;

/**
 * Created by ZAC on 12/17/2014.
 */
public class RecipeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecipeAdapter recipeAdapter;
    private ListView listView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final int RECIPE_LOADER = 0;
    private Context mContext;

    private static final String SELECTED_KEY = "selected_position";

    private static final String[] RECIPE_COLUMNS = {
            RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_ID,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_KEY,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_NAME,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_DESCRIPTION,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_INGREDIENTS,
            RecipeContract.RecipeEntry.RECIPE_COLUMN_STEPS
    };


    public static final int COL_RECIPE_TITLE = 1;
    public static final int COL_RECIPE_DESC = 2;


    public interface Callback {
        /**
         * RecipeDetailFragmentCallback for when an item has been selected.
         * @param recipeName
         */
        public void onItemSelected(String recipeName);
    }
    public RecipeFragment() {
        setHasOptionsMenu(true);
    }

    public void updateRecipeDb() {
        FetchRecipesTask fetchRecipesTask = new FetchRecipesTask(mContext);
        fetchRecipesTask.execute();
//        Intent intent = new Intent(getActivity(), FetchRecipesService.class);
//        getActivity().startService(intent);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       updateRecipeDb();
        recipeAdapter = new RecipeAdapter(getActivity(), null, 0);
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        listView = (ListView) view.findViewById(R.id.list_view_recipe);

        listView.setAdapter(recipeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = recipeAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((Callback)getActivity()).onItemSelected(cursor.getString(COL_RECIPE_TITLE));
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(RECIPE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortOrder = RecipeContract.RecipeEntry.RECIPE_COLUMN_RECIPE_ID + " DESC";

        Uri recipeUri = RecipeContract.RecipeEntry.CONTENT_URI;

        return new CursorLoader(
                getActivity(),
                recipeUri,
                RECIPE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        recipeAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            listView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recipeAdapter.swapCursor(null);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_recipe_fragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateRecipeDb();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
