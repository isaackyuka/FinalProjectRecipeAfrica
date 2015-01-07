package com.example.zac.recipeafrica.app;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zac.myapplication.backend.recipeApi.RecipeApi;
import com.example.zac.myapplication.backend.recipeApi.model.RecipeBean;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.Random;

/**
 * Created by ZAC on 12/18/2014.
 */

public class AddRecipeFragment extends Fragment {

    EditText etitle;
    Fragment newFragment;
    EditText edescription;
    EditText eingredients;
    EditText esteps;
    Uri recipeInsertUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_recipe,container, false);
        etitle = (EditText) view.findViewById(R.id.eTitle);
        edescription = (EditText) view.findViewById(R.id.eDescription);
        eingredients = (EditText) view.findViewById(R.id.eIngredients);
        esteps = (EditText) view.findViewById(R.id.eSteps);

        Button save = (Button) view.findViewById(R.id.save_btn);
        save.setOnClickListener(onSave);
        return view;
    }

     public View.OnClickListener onSave = new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             StoreRecipeTask storeRecipeTask = new StoreRecipeTask();
             storeRecipeTask.execute();

             Toast.makeText(getActivity(), "Recipe added.", Toast.LENGTH_LONG).show();

             // Getting reference to the FragmentManager
             FragmentManager fragmentManager = getFragmentManager();
             // Creating a fragment transaction
             FragmentTransaction ft = fragmentManager.beginTransaction();
                     newFragment = new RecipeFragment();
                     // Adding a fragment to the fragment transaction
                     ft.replace(R.id.content_frame, newFragment);
                     // Committing the transaction
                     ft.commit();

         }
     };

    public class StoreRecipeTask extends AsyncTask<Void, Void, Void> {
        private RecipeApi recipeApiService = null;
        private Context context;
        @Override
        protected Void doInBackground(Void... params) {
            if (recipeApiService == null) {

                RecipeApi.Builder builder = new RecipeApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://ace-fiber-802.appspot.com/_ah/api/");
                recipeApiService = builder.build();
            }

            String title = etitle.getText().toString();
            String description = edescription.getText().toString();
            String ingredients = eingredients.getText().toString();
            String steps = esteps.getText().toString();

            Random r = new Random();
//            r.setSeed(20);
            int recipeId = r.nextInt(Integer.MAX_VALUE);

            Long id = Long.valueOf(recipeId);
            try {

                RecipeBean recipeBean = new RecipeBean();
                recipeBean.setSteps(steps);
                recipeBean.setIngredients(ingredients);
                recipeBean.setDescription(description);
                recipeBean.setTitle(title);
                recipeBean.setId(id);
                recipeApiService.storeRecipe(recipeBean).execute();

            } catch (IOException e) {
                Log.e(AddRecipeFragment.class.getSimpleName(), "Error when storing recipe", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            Intent intent = new Intent(getActivity(), FetchRecipesService.class);
//            getActivity().startService(intent);
        }
    }

}