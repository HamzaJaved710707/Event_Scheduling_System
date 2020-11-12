package com.example.eventscheduling.client.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventscheduling.R;
import com.example.eventscheduling.client.model.Filter_Package_Dialog_client;
import com.example.eventscheduling.client.model.client_package_adapter;
import com.example.eventscheduling.client.util.client_package_values;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class client_packages_frag extends Fragment implements Filter_Package_Dialog_client.ExampleDialogListener, client_package_adapter.onItemClickListner {
    private static final String TAG = "client_packages_frag";
    RecyclerView recyclerView;
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
    private ArrayList<String> userList = new ArrayList<>();
    private client_package_adapter package_adapter;
    private TextView noDataTxt;
    private ArrayList<String> tempUserList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_packages_frag, container, false);
        setHasOptionsMenu(true);
        // Initialize the variables of firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        noDataTxt = view.findViewById(R.id.client_package_noData_txt);
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
                        .commit();
            }
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
        Query query = userCollection.whereEqualTo("type", 0);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    userList.add(queryDocumentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            tempUserList.clear();
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);

                            }
                            package_adapter = new client_package_adapter(getContext(), packageList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            package_adapter.setOnClick(client_packages_frag.this);
                            recyclerView.setAdapter(package_adapter);
                        }
                    });

                }


            }

        });


    }

    private void setData(QueryDocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
            ArrayList<String> foodList = (ArrayList<String>) documentSnapshot.get("Food");
            ArrayList<String> serviceList = (ArrayList<String>) documentSnapshot.get("Services");
            long ratingStar;
            if (documentSnapshot.getLong("ratingStar") != null) {
                ratingStar = documentSnapshot.getLong("ratingStar");
            } else {
                ratingStar = 0;
            }


            packageList.add(new client_package_values(documentSnapshot.getString("PackageName"), documentSnapshot.getString("image"), foodList, serviceList, " ", documentSnapshot.getString("price"), documentSnapshot.getString("businessName"), documentSnapshot.getString("id"), documentSnapshot.getString("userId"), ratingStar));


        }

    }

    private void setRecyclerView() {
        if (packageList.size() == 0) {
            noDataTxt.setVisibility(View.VISIBLE);
        } else {
            noDataTxt.setVisibility(View.INVISIBLE);
            package_adapter = new client_package_adapter(getContext(), packageList);
            package_adapter.setOnClick(client_packages_frag.this);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.swapAdapter(package_adapter, true);

        }
    }

    @Override
    public void sendView(CheckedTextView evntOrg, CheckedTextView venue, CheckedTextView caterer, CheckedTextView decoration, CheckedTextView card, CheckedTextView car_rent) {
        // Only event organizer is selected
        if (evntOrg.isChecked() && !caterer.isChecked() && !decoration.isChecked() && !venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereEqualTo("businessCat", "Event_Organizer").whereEqualTo("type", 0);
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));

                }

                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();

                        }
                    });
                }
            });
        }
        // Only venue is selected
        else if (!evntOrg.isChecked() && !caterer.isChecked() && !decoration.isChecked() && venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereEqualTo("businessCat", "Venue_Provider").whereEqualTo("type", 0);
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));

                }

                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // Only caterer is selected
        else if (!evntOrg.isChecked() && caterer.isChecked() && !decoration.isChecked() && !venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereEqualTo("businessCat", "Caterer").whereEqualTo("type", 0);
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // Only decoration is selected
        else if (!evntOrg.isChecked() && !caterer.isChecked() && decoration.isChecked() && !venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereEqualTo("businessCat", "Decoration").whereEqualTo("type", 0);
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });

        }
        // Only card invitation is selected
        else if (!evntOrg.isChecked() && !caterer.isChecked() && !decoration.isChecked() && !venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereEqualTo("businessCat", "Invitation_Card").whereEqualTo("type", 0);
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // ONly event organizer and venue provider is selected
        else if (evntOrg.isChecked() && !caterer.isChecked() && !decoration.isChecked() && venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Venue_Provider"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // Only event organizer and caterer is checked
        else if (evntOrg.isChecked() && caterer.isChecked() && !decoration.isChecked() && !venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Caterer"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));

                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });

        }
        // Only event organizer and decoration is selected
        else if (evntOrg.isChecked() && !caterer.isChecked() && decoration.isChecked() && !venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Decoration"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));

                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });

        }
        // only event organizer and card is selected
        else if (evntOrg.isChecked() && !caterer.isChecked() && !decoration.isChecked() && !venue.isChecked() && card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Invitation_Card"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // ONly venue and caterer is checked
        else if (!evntOrg.isChecked() && caterer.isChecked() && !decoration.isChecked() && venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Venue_Provider", "Caterer"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // Only venue and decoration is checked
        else if (!evntOrg.isChecked() && !caterer.isChecked() && decoration.isChecked() && venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Venue_Provider", "Decoration"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));

                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });

        }
        // Only venue and card is checked
        else if (!evntOrg.isChecked() && !caterer.isChecked() && !decoration.isChecked() && venue.isChecked() && card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Venue_Provider", "Invitation_Card"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));

                }

                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // Only caterer and decoration is checked
        else if (!evntOrg.isChecked() && caterer.isChecked() && decoration.isChecked() && !venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Caterer", "Decoration"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // caterer and card
        else if (!evntOrg.isChecked() && caterer.isChecked() && !decoration.isChecked() && !venue.isChecked() && card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Caterer", "Invitation_Card"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });

        }
        // decoration and card
        else if (!evntOrg.isChecked() && !caterer.isChecked() && decoration.isChecked() && !venue.isChecked() && card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Decoration", "Invitation_Card"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));


                    for (String user : userList) {
                        userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    setData(documentSnapshot);
                                }
                                setRecyclerView();
                            }
                        });
                    }
                }
            });
        }

        // Event organier , venue , caterer
        else if (evntOrg.isChecked() && caterer.isChecked() && !decoration.isChecked() && venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Venue_Provider", "Caterer"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // Event organizer , venue , decoration
        else if (evntOrg.isChecked() && venue.isChecked() && decoration.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Venue_Provider", "Decoration"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // Event organizer , venue and card
        else if (evntOrg.isChecked() && !caterer.isChecked() && !decoration.isChecked() && venue.isChecked() && card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Venue_Provider", "Invitation_Card"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));

                    for (String user : userList) {
                        userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    setData(documentSnapshot);
                                }
                                setRecyclerView();
                            }
                        });
                    }
                }
            });
        }
        // Event organizer , caterer , decoration
        else if (evntOrg.isChecked() && caterer.isChecked() && decoration.isChecked() && !venue.isChecked() && !card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Caterer", "Decoration"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // Event organizer , caterer , card
        else if (evntOrg.isChecked() && caterer.isChecked() && !decoration.isChecked() && !venue.isChecked() && card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Caterer", "Invitation_Card"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
        }
        // Event organizer , decoration, card
        else if (evntOrg.isChecked() && decoration.isChecked() && card.isChecked()) {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereIn("businessCat", Arrays.asList("Event_Organizer", "Decoration", "Invitation_Card"));
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            });
        }

        // Alll categories
        else {
            package_adapter.deleteRecyclerData();
            Query query = userCollection.whereEqualTo("type", 0);
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                packageList.clear();
                userList.clear();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    userList.add(documentSnapshot.getString("id"));
                }
                for (String user : userList) {
                    userCollection.document(user).collection("Packages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                setData(documentSnapshot);
                            }
                            setRecyclerView();
                        }
                    });
                }
            });
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
        package_adapter.deleteRecyclerData();
        Query query = userCollection.whereEqualTo("type", 0);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            packageList.clear();
            userList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                userList.add(documentSnapshot.getString("id"));
            }
            for (String user : userList) {
                Query price2query = userCollection.document(user).collection("Packages").orderBy("rating", Query.Direction.DESCENDING);
                price2query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            setData(documentSnapshot);
                        }
                        setRecyclerView();
                    }
                });
            }
        });
    }

    @Override
    public void price1Layout() {
        package_adapter.deleteRecyclerData();
        Query query = userCollection.whereEqualTo("type", 0);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            packageList.clear();
            userList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                userList.add(documentSnapshot.getString("id"));
            }
            for (String user : userList) {
                Query price1query = userCollection.document(user).collection("Packages").orderBy("price", Query.Direction.ASCENDING);
                price1query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            setData(documentSnapshot);
                        }
                        setRecyclerView();
                    }
                });
            }
        });
    }

    @Override
    public void price2Layout() {
        package_adapter.deleteRecyclerData();
        Query query = userCollection.whereEqualTo("type", 0);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            packageList.clear();
            userList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                userList.add(documentSnapshot.getString("id"));
            }
            for (String user : userList) {
                Query price2query = userCollection.document(user).collection("Packages").orderBy("price", Query.Direction.DESCENDING);
                price2query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            setData(documentSnapshot);
                        }
                        setRecyclerView();
                    }
                });
            }
        });
    }

    @Override
    public void price3Layout() {
        package_adapter.deleteRecyclerData();
        Query query = userCollection.whereEqualTo("type", 0);
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            packageList.clear();
            userList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                userList.add(documentSnapshot.getString("id"));
            }
            for (String user : userList) {
                Query price2query = userCollection.document(user).collection("Packages").orderBy("price", Query.Direction.DESCENDING);
                price2query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            setData(documentSnapshot);
                        }
                        setRecyclerView();
                    }
                });
            }
        });

    }

    @Override
    public void onDetailButtonClick(String id, String userid) {
        Bundle bundle = new Bundle();
        bundle.putString("packageId", id);
        bundle.putString("userId", userid);
        //set Fragmentclass Arguments
        client_package_detail_default frag = new client_package_detail_default();
        frag.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout_clientHome, frag)
                .addToBackStack(null).commit();

    }
}
