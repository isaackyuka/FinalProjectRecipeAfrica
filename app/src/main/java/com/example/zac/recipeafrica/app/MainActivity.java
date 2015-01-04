package com.example.zac.recipeafrica.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class MainActivity extends ActionBarActivity implements RecipeFragment.Callback {

    private DrawerLayout mDrawerLayout;
    private Context mContext;
    // ListView represents Navigation Drawer
    private ListView mDrawerList;
    // ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
    private ActionBarDrawerToggle mDrawerToggle;
    // Title of the action bar
    private String mTitle = "";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, new DetailFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        mTitle = (String) getTitle();
// Getting reference to the DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Getting reference to the ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();

            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

        };

        // Setting DrawerToggle on DrawerLayout
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Creating an ArrayAdapter to add items to the listview mDrawerList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
        R.layout.drawer_list_item, getResources().getStringArray(R.array.menu_items));
        // Setting the adapter on mDrawerList
        mDrawerList.setAdapter(adapter);
        // Enabling Home button
        getSupportActionBar().setHomeButtonEnabled(true);
        // Enabling Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Setting item click listener for the listview mDrawerList
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Getting an array of rivers
                String[] menuItems = getResources().getStringArray(R.array.menu_items);
                // Currently selected option
                mTitle = menuItems[position];
                // Select fragment
                selectItem(position);

                       }
        });

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    // Method to select fragments

    public void selectItem(int position) {

        Fragment newFragment;
        // Getting reference to the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();

        switch (position) {
            case 0:
                newFragment = new RecipeFragment();
                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, newFragment);
                // Committing the transaction
                ft.commit();
                break;
            case 1:
                newFragment = new AddRecipeFragment();
                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, newFragment);
                // Committing the transaction
                ft.commit();
                break;

            case 2:
                newFragment = new SearchFragment();
                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, newFragment);
                // Committing the transaction
                ft.commit();
                break;

            default:
                break;
        }
        // Closing the drawer
        mDrawerLayout.closeDrawer(mDrawerList);

            }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /** Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onItemSelected(String recipeID) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putString(RecipeDetailActivity.RECIPE_KEY, recipeID);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class)
                    .putExtra(RecipeDetailActivity.RECIPE_KEY, recipeID);
            startActivity(intent);
        }
    }
}
