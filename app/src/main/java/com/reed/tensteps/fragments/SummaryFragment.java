package com.reed.tensteps.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reed.tensteps.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFragment extends Fragment {

    private ProgressBar mProgressBar;
    private TextView mPointsView;
    private int mTotalPoints;

    private static final String PREF_NUM_POINTS_DAILY = "num_points_daily";
    private static final String TAG = "SummaryFragment";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SummaryFragment.
     */
    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        super.onCreate(savedInstanceState);
        mTotalPoints = sp.getInt(PREF_NUM_POINTS_DAILY, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_summary, container, false);
        mPointsView = (TextView) v.findViewById(R.id.point_text);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        updatePoints(mTotalPoints);
        return v;
    }

    public void updatePoints(int points) {
        mTotalPoints += points;
        if(mTotalPoints != 1) {
            mPointsView.setText(mTotalPoints + " Points");
        } else {
            mPointsView.setText(mTotalPoints + " Point");
        }
        mProgressBar.setProgress(mTotalPoints);
    }
}

