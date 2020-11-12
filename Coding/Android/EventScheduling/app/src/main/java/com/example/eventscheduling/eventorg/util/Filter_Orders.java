package com.example.eventscheduling.eventorg.util;

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

public class Filter_Orders  extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private EditText editTextPassword;
    CheckedTextView chkBox_All ;
    CheckedTextView chkBox_completed;
    CheckedTextView chkBox_pending;
    private com.example.eventscheduling.eventorg.util.Filter_Orders.ExampleDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.evnt_filter_orders, null);
        // To toggle CheckTextview check button
        chkBox_All = view.findViewById(R.id.evnt_Orders_all_fitler_menu_txt);
        chkBox_All.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((CheckedTextView) v).toggle();

            }
        });
        chkBox_completed = view.findViewById(R.id.evnt_Orders_completed_fitler_menu_txt);
        chkBox_completed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                ((CheckedTextView) v).toggle();

            }
        });
        chkBox_pending = view.findViewById(R.id.evnt_Orders_pending_fitler_menu_txt);
        chkBox_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        listener.applyTexts(chkBox_All, chkBox_completed, chkBox_pending);
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


    public void setExampleDialog(com.example.eventscheduling.eventorg.util.Filter_Orders.ExampleDialogListener listener) {
        this.listener = listener;
    }

    public  Filter_Orders() {}

    public Filter_Orders(com.example.eventscheduling.eventorg.util.Filter_Orders.ExampleDialogListener listener) {
        this.listener = listener;
    }
    public interface ExampleDialogListener {
        void applyTexts(CheckedTextView all, CheckedTextView completed, CheckedTextView pending);
    }


}
