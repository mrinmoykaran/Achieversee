package com.example.edukit;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySavedFragment extends Fragment {
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;
    TextView tvHeading;
    DatabaseReference mRef;
    String publishername;
    private int type;

    public MySavedFragment(int type) {
        this.type = type;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_saved_fragment, container, false);
        toolbar = getActivity().findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.contentView);
        tvHeading = view.findViewById(R.id.txtHeading);
        mRef = FirebaseDatabase.getInstance().getReference();
        ImageView btnBack = view.findViewById(R.id.btnBack);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        toolbar.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        final CustomDialog customDialog = new CustomDialog(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                toolbar.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        if (type == 1) {

            tvHeading.setText("MY TESTS");
            customDialog.showDialog("Your tests are loading...");
            final List<SavedTestModel> mList = new ArrayList<>();
            final MySavedTestAdapter adapter = new MySavedTestAdapter(mList, getContext());
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("TRANSACTIONS");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (String.valueOf(ds.child("EMAIL").getValue()).equals(userEmail) && String.valueOf(ds.child("PAID_FOR").getValue()).equals("Mock Test")) {
                            // Toast.makeText(getContext(),String.valueOf(ds.child("PULISHER_ID")), Toast.LENGTH_SHORT).show();
                            mList.add(new SavedTestModel(
                                    String.valueOf(ds.child("PUBLISHER_ID").getValue()),
                                    String.valueOf(ds.child("CONTENT_ID").getValue()),
                                    String.valueOf(ds.child("DATE").getValue()),
                                    String.valueOf(ds.child("TIME").getValue())

                            ));
                        }
                    }
                    Collections.reverse(mList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    customDialog.dismissDialog();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else if (type == 2) {
            tvHeading.setText("MY NOTES");
            customDialog.showDialog("Your notes are loading...");
            final List<SavedNotesModel> mList = new ArrayList<>();
            final MySavedNotesAdapter adapter = new MySavedNotesAdapter(mList, getContext());
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("TRANSACTIONS");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (String.valueOf(ds.child("EMAIL").getValue()).equals(userEmail) && String.valueOf(ds.child("PAID_FOR").getValue()).equals("Notes")) {
                            mList.add(new SavedNotesModel(
                                    String.valueOf(ds.child("PUBLISHER_ID").getValue()),
                                    String.valueOf(ds.child("CONTENT_ID").getValue()),
                                    String.valueOf(ds.child("DATE").getValue()),
                                    String.valueOf(ds.child("TIME").getValue())

                            ));
                        }
                    }
                    Collections.reverse(mList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    customDialog.dismissDialog();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        onBackPress(view);
        return view;
    }

    private void onBackPress(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    toolbar.setVisibility(View.VISIBLE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }


}
