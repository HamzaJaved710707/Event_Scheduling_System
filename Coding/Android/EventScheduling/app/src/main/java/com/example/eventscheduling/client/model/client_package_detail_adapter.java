package com.example.eventscheduling.client.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class client_package_detail_adapter extends RecyclerView.Adapter<client_package_detail_adapter.client_package_detail_holder> {


    List<String> packageDetial = new ArrayList<>();
    private Context context;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private client_friendList_Adapter.OnItemClicked onClick;
    private String id;

    public client_package_detail_adapter(Context context, List<String> list) {
        this.context = context;
        packageDetial = list;
    }

    @NonNull
    @Override
    public client_package_detail_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_package_detail_item, parent, false);
        return new client_package_detail_adapter.client_package_detail_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull client_package_detail_holder holder, int position) {
        if (packageDetial.get(position) != null) {
            holder.name.setText(packageDetial.get(position));
        } else {
            holder.itemView.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        if (packageDetial != null) {
            return packageDetial.size();
        }
        return 0;
    }

    static class client_package_detail_holder extends RecyclerView.ViewHolder {
        TextView name;


        public client_package_detail_holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.client_package_detail_package_name);


        }
    }
}
