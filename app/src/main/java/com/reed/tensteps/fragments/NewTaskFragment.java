package com.reed.tensteps.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.reed.tensteps.utilities.PointPair;
import com.reed.tensteps.R;
import com.reed.tensteps.activities.NavActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTaskFragment extends DialogFragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewTaskFragment.
     */
    public static NewTaskFragment newInstance() {
        NewTaskFragment fragment = new NewTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NewTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceBundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v =inflater.inflate(R.layout.dialog_new_task, null);
        final EditText description = (EditText) v.findViewById(R.id.dialog_description);
        final EditText points = (EditText) v.findViewById(R.id.dialog_points);
        builder.setView(v);
        builder.setTitle(R.string.dialog_new_task_title);
        builder.setPositiveButton(R.string.dialog_new_task_accept,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ((NavActivity) getActivity()).addListItem(new PointPair(description.getText().toString(), Integer.parseInt(points.getText().toString())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        builder.setNegativeButton(R.string.dialog_new_task_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

}
