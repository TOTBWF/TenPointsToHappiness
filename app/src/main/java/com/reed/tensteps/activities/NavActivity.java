package com.reed.tensteps.activities;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.reed.tensteps.utilities.PointPair;
import com.reed.tensteps.R;
import com.reed.tensteps.fragments.NavDrawerFragment;
import com.reed.tensteps.fragments.NewTaskFragment;
import com.reed.tensteps.fragments.SummaryFragment;

import java.util.ArrayList;


public class NavActivity extends ActionBarActivity {

    private NavDrawerFragment mNavDrawerFragment;
    private SummaryFragment mSummaryFragment;
    private CharSequence mTitle;
    private ArrayList<PointPair> points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mNavDrawerFragment =  (NavDrawerFragment) fragmentManager.findFragmentById(R.id.nav_drawer);
        mSummaryFragment = SummaryFragment.newInstance();
        fragmentManager.beginTransaction().add(R.id.container, mSummaryFragment).commit();
        mTitle = "10 Points To Happiness";
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        points = new ArrayList<>();
        //TODO: Remove debug code
        points.add(new PointPair("Run", 1));
        points.add(new PointPair("Work", 2));
        points.add(new PointPair("Play", 4));
        mNavDrawerFragment.init(R.id.nav_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(mNavDrawerFragment != null) {
            if(!mNavDrawerFragment.isDrawerOpen()) {
                //getMenuInflater().inflate(R.menu.menu_nav, menu);
                restoreActionBar();
                return true;
            }
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void updatePoints(int points) {
        mSummaryFragment.updatePoints(points);
    }

    public void launchDialog() {
        NewTaskFragment dialog = NewTaskFragment.newInstance();
        dialog.show(getSupportFragmentManager(), "NewTaskFragment");
    }

    public void addListItem(PointPair pair) {
        mNavDrawerFragment.addItem(pair);
    }
}
