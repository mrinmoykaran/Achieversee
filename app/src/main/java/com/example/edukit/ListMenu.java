package com.example.edukit;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ListMenu extends Fragment {
    DatabaseReference coreRef;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    String catagory;
    TextView menu_heading;

    public ListMenu(String catagory) {
        this.catagory = catagory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.menu_list, container, false);
        final TextView tvExamTitle = view.findViewById(R.id.txtExamTitle);
        final TextView tvExamSubTitle = view.findViewById(R.id.txtExamSubTitle);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        ImageView btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                toolbar.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);

            }
        });
        coreRef = FirebaseDatabase.getInstance().getReference();
        menu_heading = view.findViewById(R.id.menu_heading);
        switch (catagory) {
            case "1":
                menu_heading.setText("Aptitude");
                break;
            case "2":
                menu_heading.setText("Reasoning");
                break;
            case "3":
                menu_heading.setText("English");
                break;
            case "4":
                menu_heading.setText("Current Affairs");
                break;
            case "5":
                menu_heading.setText("General Science");
                break;
            case "6":
                menu_heading.setText("General Knowledge");
                break;
        }
        loadMockTest(view);
        onBackPress(view);
        return view;
    }

    private void loadMockTest(View view) {

        try {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("MOCK_TEST").child("CourseList");
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewMockTest);
            final List<CourseDataModel> mlist = new ArrayList<>();

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            final CourseListAdapter courseListAdapter = new CourseListAdapter(getContext(), mlist);
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mlist.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if (ds.child("TestCatagory").exists()) {
                            if (String.valueOf(ds.child("TestCatagory").getValue()).equals(catagory)) {
                                mlist.add(new CourseDataModel(
                                        String.valueOf(ds.getKey()),
                                        String.valueOf(ds.child("Title").getValue()),
                                        String.valueOf(ds.child("Rating").getValue()),
                                        String.valueOf(ds.child("PubOn").getValue()),
                                        String.valueOf(ds.child("PubBy").getValue()),
                                        String.valueOf(ds.child("Price").getValue()),
                                        String.valueOf(ds.child("Thumbnail").getValue()),
                                        String.valueOf(ds.child("PublisherID").getValue()),
                                        String.valueOf(ds.child("Language").getValue())
                                ));
                            }
                        }
                        Log.d("Catagory", String.valueOf(ds.child("TestCatagory").getValue()));
                        Log.d("Catagory", catagory);
                    }
                    Collections.reverse(mlist);
                    recyclerView.setAdapter(courseListAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public void onBackPress(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    toolbar.setVisibility(View.VISIBLE);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }


}
