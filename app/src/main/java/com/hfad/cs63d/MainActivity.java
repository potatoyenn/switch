package com.hfad.cs63d;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private TextView textHome;
  private TextView textHistory;
  private TextView textFavorites;
  private TextView textAZ;

  private Fragment tempFragemnt;

  private ArrayList<BaseFragment> fragments;
  private int position = 0;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    textHome = (TextView) findViewById(R.id.text_home);
    textHistory = (TextView) findViewById(R.id.text_history);
    textFavorites = (TextView) findViewById(R.id.text_favorites);
    textAZ = (TextView) findViewById(R.id.text_az);
    BottomNavigationView bottomNavigationView =
        (BottomNavigationView) findViewById(R.id.bottom_navigation);

    bottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.action_home:
                textHome.setVisibility(View.VISIBLE);
                textHistory.setVisibility(View.GONE);
                textFavorites.setVisibility(View.GONE);
                textAZ.setVisibility(View.GONE);
                position = 0;
                break;
              case R.id.action_history:
                textHome.setVisibility(View.GONE);
                textHistory.setVisibility(View.VISIBLE);
                textFavorites.setVisibility(View.GONE);
                textAZ.setVisibility(View.GONE);
                position = 1;
                break;
              case R.id.action_favorites:
                textHome.setVisibility(View.GONE);
                textHistory.setVisibility(View.GONE);
                textFavorites.setVisibility(View.VISIBLE);
                textAZ.setVisibility(View.GONE);
                position = 2;
                break;
              case R.id.action_az:
                textHome.setVisibility(View.GONE);
                textHistory.setVisibility(View.GONE);
                textFavorites.setVisibility(View.GONE);
                textAZ.setVisibility(View.VISIBLE);
                position = 3;
                break;
            }

            BaseFragment baseFragment = getFragment(position);

            switchFragment(tempFragemnt, baseFragment);
            return false;
          }
        });
    initFragment();

    BaseFragment baseFragment = getFragment(position);

    switchFragment(tempFragemnt, baseFragment);
  }

  private void initFragment() {
    fragments = new ArrayList<>();
    fragments.add(new HomeFragment());
    fragments.add(new UserFragment());
    fragments.add(new UserFragment());
    fragments.add(new UserFragment());
  }

  private BaseFragment getFragment(int position) {
    if (fragments != null && fragments.size() > 0) {
      BaseFragment baseFragment = fragments.get(position);
      return baseFragment;
    }
    return null;
  }

  @Override protected void onStart() {
    super.onStart();
  }

  //silly comment
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the options menu from XML
    // MenuInflater inflater = getMenuInflater();
    // inflater.inflate(R.menu.options_menu, menu);
    getMenuInflater().inflate(R.menu.search_menu, menu);

    // Get the SearchView and set the searchable configuration
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
    ComponentName cn = new ComponentName(this, ResultActivity.class);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

    return true;
  }

  @Override protected void onNewIntent(Intent intent) {
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      //            doSearch(query);
    }
  }


  private void switchFragment(Fragment fromFragment, BaseFragment nextFragment) {
    if (tempFragemnt != nextFragment) {
      tempFragemnt = nextFragment;
      if (nextFragment != null) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!nextFragment.isAdded()) {

          if (fromFragment != null) {
            transaction.hide(fromFragment);
          }

          transaction.add(R.id.fragmelayout, nextFragment).commit();
        } else {

          if (fromFragment != null) {
            transaction.hide(fromFragment);
          }
          transaction.show(nextFragment).commit();
        }
      }
    }
  }
}