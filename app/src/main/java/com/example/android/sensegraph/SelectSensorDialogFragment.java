package com.example.android.sensegraph;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SelectSensorDialogFragment extends DialogFragment {



    public interface NoticeDialogListener {
        public void onDialogPositiveClick(SelectSensorDialogFragment dialog);
        public void onDialogNegativeClick(SelectSensorDialogFragment dialog);
    }
    private NoticeDialogListener mListener;
    private ArrayList<Integer> mSelectedItems;
    private String[] mSensorList;

    public void setmSensorList(String[] mSensorList) {
        this.mSensorList = mSensorList;
    }

    public int[] getSelectedItems(){
        int[] res = new int[mSelectedItems.size()];
        for(int i = 0; i < mSelectedItems.size(); i++){
            res[i] = mSelectedItems.get(i);
        }
        return res;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Verify that the host activity implements the callback interface
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Set title
        builder.setTitle(R.string.dialog_select_sensors)
                .setMultiChoiceItems(mSensorList, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelectedItems.add(which);
                        } else if (mSelectedItems.contains(which)) {
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                    }
                })
        //set action buttons
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(SelectSensorDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();


    }
}

