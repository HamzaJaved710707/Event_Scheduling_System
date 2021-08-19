package com.example.eventscheduling.client.model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.eventscheduling.R;

public class Filter_Friend_Dialog  extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private EditText editTextPassword;
    CheckedTextView chkBox_All ;
    CheckedTextView chkBox_Friends;
    private Filter_Friend_Dialog.ExampleDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.client_friendlist_filter, null);
        // To toggle CheckTextview check button
        chkBox_All = view.findViewById(R.id.client_all_fitler_menu_txt);
        chkBox_All.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((CheckedTextView) v).toggle();

            }
        });
        chkBox_Friends = view.findViewById(R.id.client_friends_filter_menu_txt);
        chkBox_Friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((CheckedTextView) v).toggle();

            }
        });
        // show dialog box
        builder.setView(view)
                .setTitle("Filter")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyTexts(chkBox_All, chkBox_Friends);
                    }
                });
  
        //    editTextUsername = view.findViewById(R.id.edit_username);
        // editTextPassword = view.findViewById(R.id.edit_password);
        return builder.create();
    }
/*    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }*/


    public void setExampleDialog(Filter_Friend_Dialog.ExampleDialogListener listener) {
        this.listener = listener;
    }

    public Filter_Friend_Dialog() {}

    public Filter_Friend_Dialog(Filter_Friend_Dialog.ExampleDialogListener listener) {
        this.listener = listener;
    }
    public interface ExampleDialogListener {
        void applyTexts(CheckedTextView all, CheckedTextView friends);
    }


}
