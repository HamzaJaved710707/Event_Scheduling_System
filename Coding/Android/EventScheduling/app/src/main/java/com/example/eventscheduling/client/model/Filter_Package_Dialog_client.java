package com.example.eventscheduling.client.model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.eventscheduling.R;

public class Filter_Package_Dialog_client extends AppCompatDialogFragment implements View.OnClickListener {
    private RelativeLayout relevanceLayout;
    private RelativeLayout distanceLayout;
    private RelativeLayout ratingLayout;
    private RelativeLayout price1_Layout;
    private RelativeLayout price2_Layout;
    private RelativeLayout price3_layout;
    private CheckedTextView eventOrg_txt;
    private CheckedTextView venue_txt;
    private CheckedTextView caterer_txt;
    private CheckedTextView decoration;
    private CheckedTextView card_txt;
    private CheckedTextView car_rent_txt;
    private AlertDialog.Builder builder;
    private ExampleDialogListener listener;

    public Filter_Package_Dialog_client() {
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


    public Filter_Package_Dialog_client(ExampleDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.client_filter_package_dialog, null);
        relevanceLayout = view.findViewById(R.id.relevance_layout_box);
        relevanceLayout.setOnClickListener(this);
        distanceLayout = view.findViewById(R.id.distance_layout_box);
        distanceLayout.setOnClickListener(this);
        ratingLayout = view.findViewById(R.id.rating_layout_box);
        ratingLayout.setOnClickListener(this);
        price1_Layout = view.findViewById(R.id.price1_layout_box);
        price1_Layout.setOnClickListener(this);
        price2_Layout = view.findViewById(R.id.price2_layout_box);
        price2_Layout.setOnClickListener(this);
        price3_layout = view.findViewById(R.id.price3_layout_box);
        price3_layout.setOnClickListener(this);
        eventOrg_txt = view.findViewById(R.id.eventOrg_cat_txt);
        eventOrg_txt.setOnClickListener(this);
        venue_txt = view.findViewById(R.id.venue_provider_txt);
        venue_txt.setOnClickListener(this);
        caterer_txt = view.findViewById(R.id.caterer_provider_txt);
        caterer_txt.setOnClickListener(this);
        decoration = view.findViewById(R.id.decoration_provider_txt);
        decoration.setOnClickListener(this);
        car_rent_txt = view.findViewById(R.id.carRent_provider_txt);
        car_rent_txt.setOnClickListener(this);
        card_txt = view.findViewById(R.id.cardInvitation_provider_txt);
        card_txt.setOnClickListener(this);

        builder.setView(view)
                .setTitle("Apply Filter")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        listener.sendView(eventOrg_txt, venue_txt, caterer_txt, decoration, card_txt, car_rent_txt);
                    }
                });
        //    editTextUsername = view.findViewById(R.id.edit_username);
        // editTextPassword = view.findViewById(R.id.edit_password);
        return builder.create();
    }

    public void setExampleDialog(ExampleDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relevance_layout_box:
                listener.relevanceLayout();
                dismiss();
                break;
            case R.id.distance_layout_box:
                listener.distanceLayout();
                dismiss();
                break;
            case R.id.rating_layout_box:
                listener.ratingLayout();
                dismiss();
                break;
            case R.id.price1_layout_box:
                listener.price1Layout();
                dismiss();
                break;
            case R.id.price2_layout_box:
                listener.price2Layout();
                dismiss();
                break;
            case R.id.price3_layout_box:
                listener.price3Layout();
                dismiss();
                break;
            case R.id.eventOrg_cat_txt:
                eventOrg_txt.toggle();
                break;
            case R.id.venue_provider_txt:
                venue_txt.toggle();
                break;
            case R.id.caterer_provider_txt:
                caterer_txt.toggle();
                break;
                case R.id.decoration_provider_txt:
                    decoration.toggle();
                break;
            case R.id.carRent_provider_txt:
                car_rent_txt.toggle();
                case R.id.cardInvitation_provider_txt:
                    card_txt.toggle();
                break;



            default:
                break;
        }
    }

    public interface ExampleDialogListener {
        void sendView(CheckedTextView evntOrg, CheckedTextView venue, CheckedTextView caterer, CheckedTextView decoration, CheckedTextView card, CheckedTextView car_rent);

        void relevanceLayout();

        void distanceLayout();

        void ratingLayout();

        void price1Layout();

        void price2Layout();

        void price3Layout();

    }

}