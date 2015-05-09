package com.reed.tensteps.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.reed.tensteps.utilities.PointPair;
import com.reed.tensteps.R;
import com.reed.tensteps.activities.NavActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavDrawerFragment extends Fragment {

    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private Button mNewTask;
    private MyAdapter mAdapter;
    private int mCurrentSelectedPosition = 0;
    private boolean mUserLearnedDrawer;
    private ArrayList<PointPair> points;

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String POINT_KEY = "point_key:";
    private static final String TAG = "NavDrawerFragment";

    public ActionBarDrawerToggle mDrawerToggle;

    /**
     * Creates a new instance of the fragment
     * @return A new {@link com.reed.tensteps.fragments.NavDrawerFragment}
     */
    public static NavDrawerFragment newInstance() {
        NavDrawerFragment fragment = new NavDrawerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NavDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Read if the user has seen the drawer from the shared preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
        points = new ArrayList<>();
        Map<String, ?> keys = sp.getAll();
        for(Map.Entry<String, ?> entry : keys.entrySet()) {
            if(entry.getKey().contains(POINT_KEY)) {
                points.add(new PointPair(entry.getKey().replace(POINT_KEY, ""), (Integer)entry.getValue()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nav_drawer, container, false);
        mDrawerListView = (ListView) v.findViewById(R.id.drawer_list_view);
        mNewTask = (Button) v.findViewById(R.id.button_new_task);
        mNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavActivity)getActivity()).launchDialog();
            }
        });
        // Load the
        mAdapter = new MyAdapter(v.getContext() ,points);
        mDrawerListView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Sets up the nav drawer interactions
     * @param fragmentID The android:id of the fragment in the activity layout
     * @param drawerLayout The layout of the UI
     * @param toolbar
     */
    public void init(int fragmentID, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentID);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if(!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!isAdded()) {
                    return;
                }
                if(!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().invalidateOptionsMenu();
            }
        };
        // If the user hasn't seen the drawer, then show it to them
        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }
        // Defer code dependent on previous state restoration
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }


    public void addItem(PointPair pair) {
        // Add it to the adapter
        mAdapter.addItem(pair);
        // Store it to the shared preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.edit().putInt(POINT_KEY + pair.getDescription(), pair.getValue()).apply();
    }

    private class MyAdapter extends ArrayAdapter<PointPair> {

        private ArrayList<PointPair> mData;
        private LayoutInflater mLayoutInflater;
        private Context mContext;

        public MyAdapter(Context context, ArrayList<PointPair> data) {
            super(getActivity(), R.layout.adapter_array_row, data);
            mContext = context;
            mData = data;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public void addItem(PointPair item) {
            mData.add(item);
            mCurrentSelectedPosition++;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public PointPair getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds(){
            return true;
        }

        @Override
        public int getItemViewType(int pos){
            return IGNORE_ITEM_VIEW_TYPE;
        }

        @Override
        public int getViewTypeCount(){
            return 1;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView;
            if (convertView == null) {
                rowView = mLayoutInflater.inflate(R.layout.adapter_array_row, null);
                TextView description = (TextView) rowView.findViewById(R.id.description);
                TextView point_value = (TextView) rowView.findViewById(R.id.point_value);
                CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);
                description.setText(mData.get(position).getDescription());
                if(mData.get(position).getValue() != 1) {
                    point_value.setText(mData.get(position).getValue() + " Points");
                } else {
                    point_value.setText(mData.get(position).getValue() + " Point");
                }
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        NavActivity activity = (NavActivity) getActivity();
                        if(isChecked) {
                            activity.updatePoints(mData.get(position).getValue());
                        } else {
                            activity.updatePoints(-mData.get(position).getValue());
                        }
                    }
                });
            } else {
                rowView = convertView;
            }
            return rowView;
        }
    }

}
