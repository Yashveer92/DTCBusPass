package com.example.dtcbuspass;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class PassDurationDialog extends DialogFragment {


    int postition=0;// default selected postion

    public interface SingleChoiceListner
    {
        void onPositiveButtonClicked(String[] list,int position);
        void onNegativeButtonClicked();

    }

    SingleChoiceListner mListner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListner= (SingleChoiceListner) context;


        }
        catch (Exception e){

            throw new ClassCastException(getActivity().toString()+"SingleChoiceListner must Implemented");
        }
    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final String[] list=getActivity().getResources().getStringArray(R.array.select_pass_duration);
        builder.setTitle("Select Pass Duration")
                .setSingleChoiceItems(list,postition, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                     postition=i;

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mListner.onPositiveButtonClicked(list,postition);
                    }
                })
              .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {

                  }
              });
        return builder.create();

    }
}
