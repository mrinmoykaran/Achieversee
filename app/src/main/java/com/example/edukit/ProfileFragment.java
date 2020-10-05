package com.example.edukit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference mRef;
    RecyclerView rvNotification;
    CustomDialog customDialog;
    int testCount, notesCount;
    TextView testCounter, notesCounter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Getting User Interest
        SharedPreferences sharedPref = getContext().getSharedPreferences("UserDetails", getContext().MODE_PRIVATE);
        String defaultValue = "1";
        final String uName = sharedPref.getString("uName", defaultValue);
        //Getting User Interest
        customDialog = new CustomDialog(getContext());

        View view = inflater.inflate(R.layout.fragement_profile, container, false);
        customDialog.showDialog("Loading...");
        LinearLayout btnEditProfile = view.findViewById(R.id.btnEditProfile);
        LinearLayout btnMyTest, btnMyNotes;
        btnMyTest = view.findViewById(R.id.btnMyTest);
        btnMyNotes = view.findViewById(R.id.btnMyNotes);
        testCounter = view.findViewById(R.id.testcount);
        notesCounter = view.findViewById(R.id.notescount);
        rvNotification = view.findViewById(R.id.recyclerviewNoti);


        final TextView tvName, tvEmail;
        tvName = view.findViewById(R.id.txtUserName);
        tvEmail = view.findViewById(R.id.txtUserEmail);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("USERS");
        final String userEmail = mAuth.getCurrentUser().getEmail();
        tvEmail.setText(userEmail);
        tvName.setText(uName);


        // loadNotification();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences settings = getContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();

            }
        });
        btnMyTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MySavedFragment(1)).commit();

            }
        });
        btnMyNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MySavedFragment(2)).commit();

            }
        });


        return view;
    }

    private void loadNotification() {
        final List<NotificationModel> mList = new ArrayList<>();
        rvNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        final NotificationAdapter adapter = new NotificationAdapter(mList);
        rvNotification.setAdapter(adapter);
        DatabaseReference nRef = FirebaseDatabase.getInstance().getReference().child("TRANSACTIONS");
        final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        nRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        if (String.valueOf(ds.child("EMAIL").getValue()).equals(userEmail)) {
                            String paid_for = String.valueOf(ds.child("PAID_FOR").getValue());
                            String cost = String.valueOf(ds.child("COST").getValue());
                            String date = String.valueOf(ds.child("DATE").getValue());
                            String time = String.valueOf(ds.child("TIME").getValue());
                            mList.add(new NotificationModel(paid_for, cost, date, time));
                            if (paid_for.equals("Mock Test")) {
                                testCount++;
                            }
                            if (paid_for.equals("Notes")) {
                                notesCount++;
                            }
                        }

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                Collections.reverse(mList);
                rvNotification.setAdapter(adapter);
                customDialog.dismissDialog();
                testCounter.setText("MY TESTS (" + String.valueOf(testCount) + ")");
                notesCounter.setText("MY NOTES (" + String.valueOf(notesCount) + ")");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            loadNotification();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
