package com.example.eventscheduling.client.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.Filter_Friend_Dialog;
import com.example.eventscheduling.client.model.Filter_Package_Dialog_client;
import com.example.eventscheduling.client.model.client_friendList_Adapter;
import com.example.eventscheduling.client.model.client_package_adapter;
import com.example.eventscheduling.client.util.client_package_values;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Packages;
import com.example.eventscheduling.eventorg.util.PackagesValues;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class client_packages_frag extends Fragment implements Filter_Package_Dialog_client.ExampleDialogListener, client_package_adapter.onItemClickListner{
    private static final String TAG = "client_packages_frag";
    // variable
    private AutoCompleteTextView typeEditText;
    private AutoCompleteTextView locationEditText;
    private ImageView filter_img;
    private RelativeLayout filter_rel_layout;
    private TextView filter_txt;
    // Variables of Firestore to fetch data from there
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserID;
    private FloatingActionButton floatingActionButton;
    private CollectionReference userCollection;
    private List<client_package_values> packageList = new ArrayList<>();
    private List<String> userList = new ArrayList<>();
    private client_package_adapter package_adapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_packages_frag, container, false);
        setHasOptionsMenu(true);
        // Initialize the variables of firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
            userCollection = firestore.collection("Users");
        }
        floatingActionButton = view.findViewById(R.id.client_package_flt_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getParentFragmentManager().beginTransaction().addToBackStack(null)
                        .replace(R.id.frameLayout_clientHome, new client_create_package())
                        .commit();            }
        });
        // REturning the view
        recyclerView = view.findViewById(R.id.client_package_recyclerview);
        initRecyclerVeiw(view);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear(); // clears all menu items..
        inflater.inflate(R.menu.filter_menu_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
        // This change the text color of overflow menu
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // This switch statement will work with Overflow menu...
        if (item.getItemId() == R.id.filter_menu) {
            openDialog();
        }
        return super.onOptionsItemSelected(item);

    }
    public void openDialog() {
        Filter_Package_Dialog_client filter_dialog = new Filter_Package_Dialog_client(client_packages_frag.this);
        filter_dialog.show(getParentFragmentManager(), "example dialog");
        filter_dialog.setExampleDialog(client_packages_frag.this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    // Initialize the recyclerView of this fragment
    private void initRecyclerVeiw(View view) {
       userCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                     userList.clear();
                     for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                         userList.add(documentSnapshot.getString("id"));
                     }
                     userCollection.whereIn(FieldPath.documentId(), userList).whereEqualTo("type", 0).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                         @Override
                         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (String user: userList){
                                userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                        ArrayList<String> foodList = (ArrayList<String>) documentSnapshot.get("Food");
                                        ArrayList<String> serviceList = (ArrayList<String>) documentSnapshot.get("Services");
                                        packageList.add(new client_package_values(documentSnapshot.getString("PackageName"), documentSnapshot.getString("image"), foodList,serviceList, documentSnapshot.getString("venue"),documentSnapshot.getString("price"), documentSnapshot.getString("businessName") ));
                                        }
                                        package_adapter = new client_package_adapter(getContext(),packageList);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        package_adapter.setOnClick(client_packages_frag.this);
                                        recyclerView.setAdapter(package_adapter);
                                    }
                                });
                            }
                         }
                     });
            }
        });


    }


    @Override
    public void sendView(CheckedTextView evntOrg, CheckedTextView venue, CheckedTextView caterer, CheckedTextView car, CheckedTextView card) {
            if(evntOrg.isChecked()){
                Query query = userCollection.whereEqualTo("businessCat", "Event_Organizer").whereEqualTo("type", 0);
                query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    packageList.clear();
                    for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    userList.add(documentSnapshot.getString("id"));

                    }

                for(String user: userList){
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                ArrayList<String> foodList = (ArrayList<String>) documentSnapshot.get("Food");
                                ArrayList<String> serviceList = (ArrayList<String>) documentSnapshot.get("Services");
                                packageList.add(new client_package_values(documentSnapshot.getString("PackageName"), documentSnapshot.getString("image"), foodList,serviceList, documentSnapshot.getString("venue"),documentSnapshot.getString("price"), documentSnapshot.getString("businessName") ));
                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.swapAdapter(package_adapter, true);


                        }
                    });
                }
                });
            }
            else if(venue.isChecked()){}
            else if(caterer.isChecked()){}
            else if(car.isChecked()){}
            else if(card.isChecked()){}
            else if(evntOrg.isChecked() && venue.isChecked()){}
            else if (evntOrg.isChecked() && caterer.isChecked()){}
            else if(evntOrg.isChecked() && car.isChecked()){}
            else if(evntOrg.isChecked() && card.isChecked()){}
            else if(venue.isChecked() && caterer.isChecked()){}
            else if(venue.isChecked() && car.isChecked()){}
            else if(venue.isChecked() && card.isChecked()){}
            else if(caterer.isChecked() && car.isChecked()){}
            else if(caterer.isChecked() &&  card.isChecked()){}
            else if(car.isChecked() && card.isChecked()){}
            else if(evntOrg.isChecked() && venue.isChecked() && caterer.isChecked()){}
            else if (evntOrg.isChecked() && venue.isChecked() && car.isChecked()){}
            else if (evntOrg.isChecked() && venue.isChecked() && card.isChecked()){}
            else if (evntOrg.isChecked() && caterer.isChecked() && car.isChecked()){}
            else if(evntOrg.isChecked() && caterer.isChecked() && card.isChecked()){}
            else if(evntOrg.isChecked() && car.isChecked() && card.isChecked()){}
            else if(evntOrg.isChecked() && venue.isChecked() && caterer.isChecked() && car.isChecked()){}
            else if(evntOrg.isChecked() && venue.isChecked() && caterer.isChecked() && card.isChecked()){}
            else{

            }


    }

    @Override
    public void relevanceLayout() {
    }

    @Override
    public void distanceLayout() {

    }

    @Override
    public void ratingLayout() {

    }

    @Override
    public void price1Layout() {

    }

    @Override
    public void price2Layout() {

    }

    @Override
    public void price3Layout() {

    }

    @Override
    public void onDetailButtonClick() {

    }
}
