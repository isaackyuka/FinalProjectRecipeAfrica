package com.example.zac.recipeafrica.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.example.zac.recipeafrica.app.data.RecipeDbHelper;
import com.example.zac.recipeafrica.app.service.SearchIntentService;

/**
 * Created by ZAC on 12/18/2014.
 */
public class SearchFragment extends Fragment {

    AutoCompleteTextView actv;
    RecipeDbHelper dbHelper;
    ListView searchResults;
    SearchAdapter searchAdapter;

    String recipes[] = {"Luwombo", "Nsenene", "Matooke", "Luwo", "Nswa", "Omena"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                recipes
                    );

        actv = (AutoCompleteTextView) view.findViewById(R.id.search_view);

        actv.setThreshold(2);
        actv.setAdapter(adapter);
      searchResults = (ListView) view.findViewById(R.id.list_view_reviews);

        dbHelper = new RecipeDbHelper(getActivity().getBaseContext());

        searchAdapter = new SearchAdapter(getActivity().getBaseContext(),
                dbHelper.searchRecipe(actv.getText().toString()),
                0
        );

        searchResults.setAdapter(searchAdapter);

        Button searchbtn = (Button) view.findViewById(R.id.search_button);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getActivity(), SearchIntentService.class);
                intent.putExtra("searchStr", actv.getText().toString());
                getActivity().startService(intent);

            }
        });

        return view;
    }


}
