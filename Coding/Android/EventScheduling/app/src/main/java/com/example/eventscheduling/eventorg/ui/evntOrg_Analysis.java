package com.example.eventscheduling.eventorg.ui;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.eventscheduling.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class evntOrg_Analysis extends Fragment {


    public evntOrg_Analysis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evnt_org__analysis, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.timespan));
        spinner.setAdapter(arrayAdapter);
        initBarGraph(view);
        initPieChart(view);
    }


    public void initPieChart(View view){
        PieChart pieChart = view.findViewById(R.id.pie_chart);

        List<PieEntry> serviceUsed = new ArrayList<>();

        serviceUsed.add(new PieEntry(18.5f, "Service 1"));
        serviceUsed.add(new PieEntry(26.7f, "Service 2"));
        serviceUsed.add(new PieEntry(24.0f, "Service 3"));
        serviceUsed.add(new PieEntry(30.8f, "Service 4"));
        PieDataSet pieDataSet = new PieDataSet(serviceUsed ,"#Service used");
        PieData data = new PieData(pieDataSet);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(data);
        pieChart.invalidate();

    }
    public void initBarGraph(View view) {
        BarChart chart = view.findViewById(R.id.bar_graph);

        ArrayList<BarEntry> NoOfOrder = new ArrayList<>();

        NoOfOrder.add(new BarEntry(0,45f ));
        NoOfOrder.add(new BarEntry(1,40f));
        NoOfOrder.add(new BarEntry(2,33f));
        NoOfOrder.add(new BarEntry(3,12f));
        NoOfOrder.add(new BarEntry(4,45f));
        NoOfOrder.add(new BarEntry(5,40f));
        NoOfOrder.add(new BarEntry(6,33f));
        NoOfOrder.add(new BarEntry(7,12f));
        NoOfOrder.add(new BarEntry(8,45f));

        BarDataSet Orderbardataset = new BarDataSet(NoOfOrder, "No Of Orders");
        chart.animateY(2000);
        BarData data = new BarData (Orderbardataset);
        Orderbardataset.setColors(ColorTemplate.JOYFUL_COLORS);
        Orderbardataset.setBarBorderWidth(2f);
        chart.setData(data);

    }

}
