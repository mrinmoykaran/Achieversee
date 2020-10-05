package com.example.edukit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {
    CustomDialog customDialog;
    RecyclerView recyclerViewRec, recyclerViewQue;
    CourseListAdapter courseListAdapter;
    MyQuestionAdapter myQuestionAdapter;
    DatabaseReference mRef, qRef;
    LinearLayout menuLayout;
    CardView card1, card2, card3, card4, card5, card6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragement_home, container, false);
        //Global Refernce
        mRef = FirebaseDatabase.getInstance().getReference().child("MOCK_TEST").child("CourseList");
        qRef = FirebaseDatabase.getInstance().getReference().child("QUESTIONS").child("Question_Info");
        menuLayout = view.findViewById(R.id.menu);
        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);
        card4 = view.findViewById(R.id.card4);
        card5 = view.findViewById(R.id.card5);
        card6 = view.findViewById(R.id.card6);
        //Recomemded list Code
        customDialog = new CustomDialog(getContext());
        customDialog.showDialog("Loading...");

        recyclerViewRec = view.findViewById(R.id.recyclerViewRecomemded);
        recyclerViewQue = view.findViewById(R.id.question_list);
        final List<CourseDataModel> list = new ArrayList<>();
        final List<QuestionDataModel> listQ = new ArrayList<>();

        recyclerViewRec.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewQue.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        recyclerViewRec.setItemAnimator(new DefaultItemAnimator());
        recyclerViewQue.setItemAnimator(new DefaultItemAnimator());

        courseListAdapter = new CourseListAdapter(getContext(), list);
        myQuestionAdapter = new MyQuestionAdapter(listQ, getContext());
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out);
        //menuLayout.startAnimation(animation);
        card1.startAnimation(animation);
        card2.startAnimation(animation);
        card3.startAnimation(animation);
        card4.startAnimation(animation);
        card5.startAnimation(animation);
        card6.startAnimation(animation);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListMenu("1")).commit();
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListMenu("2")).commit();
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListMenu("3")).commit();
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListMenu("4")).commit();
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListMenu("5")).commit();
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListMenu("6")).commit();
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (String.valueOf(ds.child("isTrending").getValue()) == "true") {
                        list.add(new CourseDataModel(
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
                Collections.reverse(list);
                recyclerViewRec.setAdapter(courseListAdapter);
                customDialog.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //Recomemded list Code
        qRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listQ.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (String.valueOf(ds.child("isTrending").getValue()).equals("true")) {
                        listQ.add(new QuestionDataModel(
                                String.valueOf(ds.getKey()),
                                String.valueOf(ds.child("Title").getValue()),
                                String.valueOf(ds.child("Language").getValue()),
                                "1",
                                String.valueOf(ds.child("Demo_PDF").getValue()),
                                String.valueOf(ds.child("Download_Link").getValue()),
                                String.valueOf(ds.child("Group").getValue()),
                                String.valueOf(ds.child("Price").getValue()),
                                String.valueOf(ds.child("Publisher_Id").getValue())
                        ));
                    }
                }
                Collections.reverse(listQ);
                recyclerViewQue.setAdapter(myQuestionAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }

}
