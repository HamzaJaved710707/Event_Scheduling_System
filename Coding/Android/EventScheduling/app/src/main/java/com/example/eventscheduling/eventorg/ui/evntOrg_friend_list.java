package com.example.eventscheduling.eventorg.ui;

import android.app.Dialog;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.Filter_Friend_Dialog;
import com.example.eventscheduling.client.ui.client_friendList;
import com.example.eventscheduling.client.ui.client_profile_view;
import com.example.eventscheduling.client.util.client_friendList_values;
import com.example.eventscheduling.eventorg.model.friendList_Adapter;
import com.example.eventscheduling.eventorg.util.friendList_values;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class evntOrg_friend_list extends Fragment implements friendList_Adapter.onitemClickListener, Filter_Friend_Dialog.ExampleDialogListener {
    private static final String TAG = "evntOrg_friend_list";
    friendList_Adapter friendList_adapter;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference userCollection;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String current_email;
    private String addBackStack;
    private String currentUserId;
    private CollectionReference firendsCollection;
    private ProgressBar progressBar;
    private Dialog mOverlayDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_evnt_friend_list, container, false);
        currentUser = mAuth.getCurrentUser();
        setHasOptionsMenu(true);
        progressBar = view.findViewById(R.id.evntOrg_friends_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mOverlayDialog = new Dialog(view.getContext(), android.R.style.Theme_Panel); //display an invisible overlay dialog to prevent user interaction and pressing back
        mOverlayDialog.setCancelable(false);
        mOverlayDialog.show();
        if (currentUser != null) {
            current_email = currentUser.getEmail();
            currentUserId = currentUser.getUid();
            userCollection = firebaseFirestore.collection("Users");
            firendsCollection = userCollection.document(currentUserId).collection("Friends");
            initializeRecyclerView(view);

        }

        return view;
    }

    private void initializeRecyclerView(View view) {
        Query query = userCollection.whereEqualTo("type", 0);
        FirestoreRecyclerOptions<friendList_values> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<friendList_values>()
                .setQuery(query, friendList_values.class).build();
        friendList_adapter = new friendList_Adapter(getContext(), firestoreRecyclerOptions);
        RecyclerView recyclerView = view.findViewById(R.id.friend_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendList_adapter.setitemOnclickListener(this);
        recyclerView.setAdapter(friendList_adapter);
        progressBar.setVisibility(View.INVISIBLE);
        mOverlayDialog.dismiss();

    }

    @Override
    public void onStart() {
        super.onStart();
        ((evntOrg_home) getActivity()).setTitleActionBar("Followers");
        friendList_adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        friendList_adapter.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu_actionbar, menu);

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
        Filter_Friend_Dialog filter_dialog = new Filter_Friend_Dialog(evntOrg_friend_list.this);
        filter_dialog.show(getParentFragmentManager(), "example dialog");
        filter_dialog.setExampleDialog(evntOrg_friend_list.this);
    }

    @Override
    public void itemClick(String id, long type) {

   if(type == 0){
       evnt_profile_view obj = new evnt_profile_view();
       Bundle bundle = new Bundle();
       bundle.putString("id", id);
       obj.setArguments(bundle);
       getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, obj).addToBackStack(getResources().getString(R.string.addToBackStack)).commit();
   }
   else if(type == 1){
       client_profile_view obj = new client_profile_view();
       Bundle bundle = new Bundle();
       bundle.putString("id", id);
       obj.setArguments(bundle);
       getParentFragmentManager().beginTransaction().replace(R.id.fragment_test_id, obj).addToBackStack(addBackStack).commit();
   }

    }

    @Override
    public void applyTexts(CheckedTextView all, CheckedTextView friends) {
        Log.d(TAG, "applyTexts: ");
        progressBar.setVisibility(View.VISIBLE);
        mOverlayDialog.show();
        if (all.isChecked()) {
            initialize_All_RecyclerView();
            Log.d(TAG, "applyTexts: all is checked");
        } else if (friends.isChecked()) {
            // initialize_Friend_RecyclerView();
            initialize_Friends_Recyclerview();
            Log.d(TAG, "applyTexts: friends is checked");
        } else {
            Log.d(TAG, "applyTexts: both");
            initialize_All_RecyclerView();
        }
    }

    private void initialize_All_RecyclerView() {
        progressBar.setVisibility(View.INVISIBLE);
        mOverlayDialog.dismiss();


    }

    private void initialize_Friends_Recyclerview() {
        progressBar.setVisibility(View.INVISIBLE);
        mOverlayDialog.dismiss();
    }
}
