package com.reed.tensteps.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
    private boolean mUserLearnedDrawer;
    private ArrayList<PointPair> mDeletionFlags;
    private ArrayList<PointPair> mPoints;

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private static final String POINT_KEY = "point_key:";
    private static final String CHECKED_KEY = "checked_key:";
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
        mPoints = new ArrayList<>();
        mDeletionFlags = new ArrayList<>();
        Map<String, ?> keys = sp.getAll();
        for(Map.Entry<String, ?> entry : keys.entrySet()) {
            if(entry.getKey().contains(POINT_KEY)) {
                mPoints.add(new PointPair(entry.getKey().replace(POINT_KEY, ""), (Integer) entry.getValue()));
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
        mAdapter = new MyAdapter(v.getContext() , mPoints);
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
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sp.edit();
                // Delete all entries flagged for deletion
                for(PointPair p : mDeletionFlags) {
                    editor.remove(POINT_KEY + p.getDescription());
                    editor.remove(CHECKED_KEY + p.getDescription());
                    mAdapter.removeItem(p);
                }
                editor.apply();
                mPoints.removeAll(mDeletionFlags);
                mDeletionFlags = new ArrayList<>();
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
                mDrawerListView.bringToFront();
                mDrawerListView.requestLayout();
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
            notifyDataSetChanged();
        }

        public void removeItem(PointPair item) {
            mData.remove(item);
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
                final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);
                final ImageButton imageButton = (ImageButton) rowView.findViewById(R.id.row_delete);
                rowView.setOnTouchListener(new View.OnTouchListener() {
                    private final float displaceNeeded = 50f;
                    private float displacement;
                    private float lastX;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN :
                                lastX = event.getX();
                            case MotionEvent.ACTION_MOVE :
                                displacement += event.getX() - lastX;
                                lastX = event.getX();
                                Log.d(TAG, "Displacement = " + displacement);
                                break;
                            case MotionEvent.ACTION_OUTSIDE:
                            case MotionEvent.ACTION_CANCEL :
                            case MotionEvent.ACTION_UP:
                                if(displacement >= displaceNeeded) {
                                    imageButton.setVisibility(View.VISIBLE);
                                    imageButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDeletionFlags.remove(mData.get(position));
                                            imageButton.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                    mDeletionFlags.add(mData.get(position));
                                    checkBox.setChecked(false);
                                    Log.d(TAG, "Triggered delete reveal");
                                }
                                displacement = 0;
                                break;
                            default:
                                Log.d(TAG, "Event Triggered! Event ID: " + event.getAction());
                                break;
                        }
                        return true;
                    }
                });
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
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        if(isChecked) {
                            sp.edit().putBoolean(CHECKED_KEY + mData.get(position).getDescription(), true).apply();
                            activity.updatePoints(mData.get(position).getValue());
                        } else {
                            sp.edit().putBoolean(CHECKED_KEY + mData.get(position).getDescription(), false).apply();
                            activity.updatePoints(-mData.get(position).getValue());
                        }
                    }
                });
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                checkBox.setChecked(sp.getBoolean(CHECKED_KEY + mData.get(position).getDescription(), false));
            } else {
                rowView = convertView;
            }
            return rowView;
        }
    }

}
