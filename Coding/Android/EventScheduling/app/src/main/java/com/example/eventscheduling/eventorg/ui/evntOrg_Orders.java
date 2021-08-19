package com.example.eventscheduling.eventorg.ui;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.ui.client_package_detail_custom;
import com.example.eventscheduling.client.ui.client_package_detail_default;
import com.example.eventscheduling.client.util.client_orders_data;
import com.example.eventscheduling.eventorg.model.RecyclerView_Adapter_Order;
import com.example.eventscheduling.eventorg.util.Filter_Orders;
import com.example.eventscheduling.eventorg.util.OrderValues;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// Fragment to display orders to event Organizer
public class evntOrg_Orders extends Fragment implements  RecyclerView_Adapter_Order.OnItemClicked, Filter_Orders.ExampleDialogListener {
    private static final String TAG = "evntOrg_Orders";
    private List<client_orders_data> client_order_data_list = new ArrayList<>();
    // Array to initialize the values for order
   List<OrderValues> orderList = new ArrayList<>();
    private List<String> userList = new ArrayList<>();
    private ArrayList<String> packageUser = new ArrayList<>();
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference dbReference = firebaseFirestore.collection("Users");
    private CollectionReference orderCollection;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String currentUserID;
    private ArrayList<String > packageId = new ArrayList<>();
    private RecyclerView_Adapter_Order order_apapter;
    private DocumentReference packageDocument;
    private ProgressBar progressBar;
    private boolean custom = false;
    private Dialog mOverlayDialog;
    public evntOrg_Orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evnt_org__orders, container, false);
        recyclerView = view.findViewById(R.id.order_recyclerView_event);
        progressBar = view.findViewById(R.id.evntOrg_orders_progressBar);
        mOverlayDialog = new Dialog(view.getContext(), android.R.style.Theme_Panel); //display an invisible overlay dialog to prevent user interaction and pressing back
        mOverlayDialog.setCancelable(false);
        setHasOptionsMenu(true);
        progressBar.setVisibility(View.VISIBLE);
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUserID = currentUser.getUid();
            orderCollection = dbReference.document(currentUserID).collection("Orders");
            initOrders(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: is called");



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

    private void openDialog() {
        Filter_Orders filter_dialog = new Filter_Orders(evntOrg_Orders.this);
        filter_dialog.show(getParentFragmentManager(), "example dialog");
        filter_dialog.setExampleDialog(evntOrg_Orders.this);
    }

    public void initOrders(View view) {
        Log.d(TAG, "initOrders: is called");
        userList.clear();
        client_order_data_list.clear();
        orderList.clear();
        // SeT adapter
        orderCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    userList.add(documentSnapshot.getString("from"));
                    client_order_data_list.add(new client_orders_data(documentSnapshot.getString("packageUser"), documentSnapshot.getString("packageId"), documentSnapshot.getId(), documentSnapshot.getString("from")));

                }
                if(userList.size() ==0){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "No order to display", Toast.LENGTH_LONG).show();
                }
for(String id: userList){
    dbReference.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            orderList.add(new OrderValues(documentSnapshot.getString("Name"), documentSnapshot.getString("imgUrl"), documentSnapshot.getString("id"))) ;

            order_apapter = new RecyclerView_Adapter_Order(view.getContext(), orderList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            order_apapter.getValues(client_order_data_list);
            order_apapter.setOnClick(evntOrg_Orders.this::itemClick);
            recyclerView.setAdapter(order_apapter);
            progressBar.setVisibility(View.INVISIBLE);
        }
    });
}

                }






    });
    }

    @Override
    public void itemClick(String id,String packageId, String packageUserString, String orderId, String from) {
        progressBar.setVisibility(View.VISIBLE);

     dbReference.document(packageUserString).collection("Packages").document(packageId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
         @Override
         public void onSuccess(DocumentSnapshot documentSnapshot) {
             progressBar.setVisibility(View.INVISIBLE);
             custom = documentSnapshot.getBoolean("custom");
             if (custom) {
                 getCustomPackage(packageId, id, orderId, from);
             } else {
                 getDefaultPackage(packageId, packageUserString, orderId, from);
             }
         }
     }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
         }
     });
    }

    private void getCustomPackage(String packageId, String userId, String orderId, String from) {

        client_package_detail_custom frag = new client_package_detail_custom();
        Bundle bundle = new Bundle();
        bundle.putString("packageId", packageId);
        bundle.putString("userId", userId);
        bundle.putBoolean("orders", true);
        bundle.putBoolean("evnt", true);
        bundle.putString("orderId", orderId);
        bundle.putString("from", from);
        frag.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, frag).addToBackStack(null).commit();

    }
    private void getDefaultPackage(String packageId, String userId, String orderId, String from){

        client_package_detail_default frag = new client_package_detail_default();
        Bundle bundle = new Bundle();
        bundle.putString("packageId", packageId);
        bundle.putString("userId", userId);
        bundle.putBoolean("orders", true);
        bundle.putString("orderId", orderId);
        bundle.putBoolean("evnt", true);
        bundle.putString("from", from);
        frag.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, frag).addToBackStack(null).commit();

    }


    @Override
    public void applyTexts(CheckedTextView all, CheckedTextView completed, CheckedTextView pending) {
if(all.isChecked()){
// Show all orders
    all_orders();
}
else if(completed.isChecked()){
// Show completed orders
    completed_orders();
}
else if(pending.isChecked()){
    // show pending orders
    pending_orders();
}
    }

    private void pending_orders() {
        userList.clear();
        client_order_data_list.clear();
        orderList.clear();
        // SeT adapter
        orderCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (documentSnapshot.getLong("status") != null && documentSnapshot.getLong("status") == 0) {
                        userList.add(documentSnapshot.getString("from"));
                        client_order_data_list.add(new client_orders_data(documentSnapshot.getString("packageUser"), documentSnapshot.getString("packageId"), documentSnapshot.getId(), documentSnapshot.getString("from")));

                    }
                    for (String id : userList) {
                        dbReference.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(order_apapter != null) {
                                    orderList.add(new OrderValues(documentSnapshot.getString("Name"), documentSnapshot.getString("imgUrl"), documentSnapshot.getString("id")));

                                    order_apapter = new RecyclerView_Adapter_Order(getContext(), orderList);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    order_apapter.getValues(client_order_data_list);
                                    order_apapter.setOnClick(evntOrg_Orders.this);
                                    recyclerView.setAdapter(order_apapter);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                                else{
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    order_apapter.setOnClick(evntOrg_Orders.this);
                                    recyclerView.swapAdapter(order_apapter, true);
                                }
                            }
                        });
                    }

                }


            }
        });

    }

    private void completed_orders() {
        userList.clear();
        client_order_data_list.clear();
        orderList.clear();
        // SeT adapter
        orderCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    if(documentSnapshot.getLong("status") != null && documentSnapshot.getLong("status") == 1) {
                        userList.add(documentSnapshot.getString("from"));
                        client_order_data_list.add(new client_orders_data(documentSnapshot.getString("packageUser"), documentSnapshot.getString("packageId"), documentSnapshot.getId(), documentSnapshot.getString("from")));

                    }
                }
                for(String id: userList){
                    dbReference.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(order_apapter != null) {
                                orderList.add(new OrderValues(documentSnapshot.getString("Name"), documentSnapshot.getString("imgUrl"), documentSnapshot.getString("id")));

                                order_apapter = new RecyclerView_Adapter_Order(getContext(), orderList);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                order_apapter.getValues(client_order_data_list);
                                order_apapter.setOnClick(evntOrg_Orders.this::itemClick);
                                recyclerView.setAdapter(order_apapter);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            else{
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                order_apapter.setOnClick(evntOrg_Orders.this);
                                recyclerView.swapAdapter(order_apapter, true);
                            }
                        }
                    });
                }

            }






        });
    }

    private void all_orders() {
        userList.clear();
        client_order_data_list.clear();
        orderList.clear();
        // SeT adapter
        orderCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    userList.add(documentSnapshot.getString("from"));
                    client_order_data_list.add(new client_orders_data(documentSnapshot.getString("packageUser"), documentSnapshot.getString("packageId"), documentSnapshot.getId(), documentSnapshot.getString("from")));

                }
                for(String id: userList){
                    dbReference.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(order_apapter != null) {
                                orderList.add(new OrderValues(documentSnapshot.getString("Name"), documentSnapshot.getString("imgUrl"), documentSnapshot.getString("id")));

                                order_apapter = new RecyclerView_Adapter_Order(getContext(), orderList);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                order_apapter.getValues(client_order_data_list);
                                order_apapter.setOnClick(evntOrg_Orders.this::itemClick);
                                recyclerView.setAdapter(order_apapter);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            else{
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                order_apapter.setOnClick(evntOrg_Orders.this);
                                recyclerView.swapAdapter(order_apapter, true);
                            }
                        }
                    });
                }

            }






        });
    }


}
