package com.example.eventscheduling.client.ui;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.Adapter_history;
import com.example.eventscheduling.client.model.Client_RecyclerAdptr_Msg;
import com.example.eventscheduling.client.util.history_values;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class client_history extends Fragment {

    private static final String TAG = "client_history";

    private ArrayList<history_values> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPieChart(view);
        initValues(view);

    }

    public void initPieChart(View view){
        PieChart pieChart = view.findViewById(R.id.piechart_client);
        List<PieEntry> historyValues = new ArrayList<>();
        historyValues.add(new PieEntry(40f));
        historyValues.add(new PieEntry(20f));
        historyValues.add(new PieEntry(30f));
        historyValues.add(new PieEntry(10f));

        PieDataSet pieDataSet = new PieDataSet(historyValues,"Expenses");
        PieData dataSet = new PieData(pieDataSet) ;
        pieChart.setData(dataSet);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.invalidate();

    }

    public void initValues(View view){
        Log.d(TAG, "initImages: is called");
        arrayList.add((new history_values(R.color.colorPrimaryLight, "Event Organizer", "$500")));
        arrayList.add((new history_values(R.color.tabLayout_color1, "Caterer", "$200 ")));
        arrayList.add((new history_values(R.color.secondarycolor, "Decoration", "$100")));
        arrayList.add((new history_values(R.color.colorAccent, "Rent", "$50")));
        initRecyclerView(view);
    }

    public void initRecyclerView(View view){
        Log.d(TAG, "initRecyclerView: is called");
        RecyclerView recyclerView = view.findViewById(R.id.client_history_recyclerView);
        Adapter_history adapter_history = new Adapter_history(view.getContext(), arrayList);
        recyclerView.setAdapter(adapter_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }



}
