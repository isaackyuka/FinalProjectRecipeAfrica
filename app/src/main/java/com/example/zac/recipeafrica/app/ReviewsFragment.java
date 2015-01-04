package com.example.zac.recipeafrica.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.zac.recipeafrica.app.data.RecipeDbHelper;


/**
 * Created by ZAC on 12/23/2014.
 */
public class ReviewsFragment extends Fragment {
    RecipeDbHelper dbHelper;
    ReviewAdapter reviewAdapter;
    CheckBox star;
    public int tag;
  private ListView reviewList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        dbHelper = new RecipeDbHelper(getActivity().getBaseContext());

        reviewList = (ListView) view.findViewById(R.id.list_view_reviews);
        reviewAdapter = new ReviewAdapter(getActivity().getBaseContext(),
                dbHelper.getReviews(),
                0
                );


        reviewList.setAdapter(reviewAdapter);

        return view;
    }

    }
