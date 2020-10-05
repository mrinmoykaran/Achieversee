package com.example.edukit;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TestFragment extends Fragment {
    DatabaseReference coreRef, pRef;
    CardView btnChange;
    String key;
    String choice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Getting User Interest
        SharedPreferences sharedPref = getContext().getSharedPreferences("UserDetails", getContext().MODE_PRIVATE);
        String defaultValue = "1";
        final String userInterest = sharedPref.getString("uInterestedGroup", defaultValue);
        //Getting User Interest
        final View view = inflater.inflate(R.layout.fragement_test, container, false);
        final TextView tvExamTitle = view.findViewById(R.id.txtExamTitle);
        final TextView tvExamSubTitle = view.findViewById(R.id.txtExamSubTitle);
        btnChange = view.findViewById(R.id.btnChange);
        coreRef = FirebaseDatabase.getInstance().getReference();

        switch (userInterest) {
            case "1":
                tvExamTitle.setText("WB Govt Exam");
                tvExamSubTitle.setText("WB Excise Constable,WB Clerk...");
                break;
            case "2":
                tvExamTitle.setText("WBCS");
                tvExamSubTitle.setText("WBCS Pre,WBCS Pre Package...");
                break;
            case "3":
                tvExamTitle.setText("ENTRANCE EXAM");
                tvExamSubTitle.setText("WBJEE,JEE MAIN,JEE Advanced...");
                break;
            case "4":
                tvExamTitle.setText("RAILWAY");
                tvExamSubTitle.setText("WB Railway Group D,WB Govt...");
                break;
            case "5":
                tvExamTitle.setText("BANKING");
                tvExamSubTitle.setText("IBPS Clerk-Pre,Banking Pre Pa...");
                break;
            case "6":
                tvExamTitle.setText("TEACHING");
                tvExamSubTitle.setText("PRIMARY TET,SSC...");
                break;
            case "7":
                tvExamTitle.setText("SSC");
                tvExamSubTitle.setText("SSC-CHSL,SSC Stenographer...");
                break;
            case "8":
                tvExamTitle.setText("DEFENCE");
                tvExamSubTitle.setText("ARMY,POLICE,AIRFORCE,NAVY...");
                break;
            case "9":
                tvExamTitle.setText("UPSC");
                tvExamSubTitle.setText("UPSC,Free Daily Dose UPSC");
                break;

        }
        loadMockTest(view, userInterest);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int choice;

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                bottomSheetDialog.setContentView(R.layout.filter_bottom);
                final CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9;
                cb1 = bottomSheetDialog.findViewById(R.id.cb1);
                cb2 = bottomSheetDialog.findViewById(R.id.cb2);
                cb3 = bottomSheetDialog.findViewById(R.id.cb3);
                cb4 = bottomSheetDialog.findViewById(R.id.cb4);
                cb5 = bottomSheetDialog.findViewById(R.id.cb5);
                cb6 = bottomSheetDialog.findViewById(R.id.cb6);
                cb7 = bottomSheetDialog.findViewById(R.id.cb7);
                cb8 = bottomSheetDialog.findViewById(R.id.cb8);
                cb9 = bottomSheetDialog.findViewById(R.id.cb9);

                cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //cb1.setChecked(true);
                            cb2.setChecked(false);
                            cb3.setChecked(false);
                            cb4.setChecked(false);
                            cb5.setChecked(false);
                            cb6.setChecked(false);
                            cb7.setChecked(false);
                            cb8.setChecked(false);
                            cb9.setChecked(false);
                            tvExamTitle.setText("WB Govt Exam");
                            tvExamSubTitle.setText("WB Excise Constable,WB Clerk...");
                            setUserInterest("1");
                            loadMockTest(view, "1");
                            bottomSheetDialog.dismiss();

                        }
                    }
                });
                cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cb1.setChecked(false);
                            // cb2.setChecked(true);
                            cb3.setChecked(false);
                            cb4.setChecked(false);
                            cb5.setChecked(false);
                            cb6.setChecked(false);
                            cb7.setChecked(false);
                            cb8.setChecked(false);
                            cb9.setChecked(false);
                            tvExamTitle.setText("WBCS");
                            tvExamSubTitle.setText("WBCS Pre,WBCS Pre Package...");
                            setUserInterest("2");
                            loadMockTest(view, "2");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cb1.setChecked(false);
                            cb2.setChecked(false);
                            //cb3.setChecked(true);
                            cb4.setChecked(false);
                            cb5.setChecked(false);
                            cb6.setChecked(false);
                            cb7.setChecked(false);
                            cb8.setChecked(false);
                            cb9.setChecked(false);
                            tvExamTitle.setText("ENTRANCE EXAM");
                            tvExamSubTitle.setText("WBJEE,JEE MAIN,JEE Advanced...");
                            setUserInterest("3");
                            loadMockTest(view, "3");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cb1.setChecked(false);
                            cb2.setChecked(false);
                            cb3.setChecked(false);
                            // cb4.setChecked(true);
                            cb5.setChecked(false);
                            cb6.setChecked(false);
                            cb7.setChecked(false);
                            cb8.setChecked(false);
                            cb9.setChecked(false);
                            tvExamTitle.setText("RAILWAY");
                            tvExamSubTitle.setText("WB Railway Group D,WB Govt...");
                            setUserInterest("4");
                            loadMockTest(view, "4");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                cb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cb1.setChecked(false);
                            cb2.setChecked(false);
                            cb3.setChecked(false);
                            cb4.setChecked(false);
                            // cb5.setChecked(true);
                            cb6.setChecked(false);
                            cb7.setChecked(false);
                            cb8.setChecked(false);
                            cb9.setChecked(false);
                            tvExamTitle.setText("BANKING");
                            tvExamSubTitle.setText("IBPS Clerk-Pre,Banking Pre Pa...");
                            setUserInterest("5");
                            loadMockTest(view, "5");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                cb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cb1.setChecked(false);
                            cb2.setChecked(false);
                            cb3.setChecked(false);
                            cb4.setChecked(false);
                            cb5.setChecked(false);
                            // cb6.setChecked(true);
                            cb7.setChecked(false);
                            cb8.setChecked(false);
                            cb9.setChecked(false);
                            tvExamTitle.setText("TEACHING");
                            tvExamSubTitle.setText("PRIMARY TET,SSC...");
                            setUserInterest("6");
                            loadMockTest(view, "6");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                cb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cb1.setChecked(false);
                            cb2.setChecked(false);
                            cb3.setChecked(false);
                            cb4.setChecked(false);
                            cb5.setChecked(false);
                            cb6.setChecked(false);
                            //  cb7.setChecked(true);
                            cb8.setChecked(false);
                            cb9.setChecked(false);
                            tvExamTitle.setText("SSC");
                            tvExamSubTitle.setText("SSC-CHSL,SSC Stenographer...");
                            setUserInterest("7");
                            loadMockTest(view, "7");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                cb8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cb1.setChecked(false);
                            cb2.setChecked(false);
                            cb3.setChecked(false);
                            cb4.setChecked(false);
                            cb5.setChecked(false);
                            cb6.setChecked(false);
                            cb7.setChecked(false);
                            // cb8.setChecked(true);
                            cb9.setChecked(false);
                            tvExamTitle.setText("DEFENCE");
                            tvExamSubTitle.setText("ARMY,POLICE,AIRFORCE,NAVY...");
                            setUserInterest("8");
                            loadMockTest(view, "8");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                cb9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            cb1.setChecked(false);
                            cb2.setChecked(false);
                            cb3.setChecked(false);
                            cb4.setChecked(false);
                            cb5.setChecked(false);
                            cb6.setChecked(false);
                            cb7.setChecked(false);
                            cb8.setChecked(false);
                            //  cb9.setChecked(true);
                            tvExamTitle.setText("UPSC");
                            tvExamSubTitle.setText("UPSC,Free Daily Dose UPSC");
                            setUserInterest("9");
                            loadMockTest(view, "9");
                            bottomSheetDialog.dismiss();
                        }
                    }
                });
                LinearLayout group1, group2, group3, group4, group5, group6, group7, group8, group9;
                group1 = bottomSheetDialog.findViewById(R.id.group1);
                group2 = bottomSheetDialog.findViewById(R.id.group2);
                group3 = bottomSheetDialog.findViewById(R.id.group3);
                group4 = bottomSheetDialog.findViewById(R.id.group4);
                group5 = bottomSheetDialog.findViewById(R.id.group5);
                group6 = bottomSheetDialog.findViewById(R.id.group6);
                group7 = bottomSheetDialog.findViewById(R.id.group7);
                group8 = bottomSheetDialog.findViewById(R.id.group8);
                group9 = bottomSheetDialog.findViewById(R.id.group9);

                group1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cb1.setChecked(true);
                    }
                });
                group2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cb2.setChecked(true);
                    }
                });
                group3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cb3.setChecked(true);
                    }
                });
                group4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cb4.setChecked(true);
                    }
                });
                group5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cb5.setChecked(true);
                    }
                });
                group6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cb6.setChecked(true);
                    }
                });
                group7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cb7.setChecked(true);
                    }
                });
                group8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cb8.setChecked(true);
                    }
                });
                group9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cb9.setChecked(true);
                    }
                });

                bottomSheetDialog.setCanceledOnTouchOutside(false);
                bottomSheetDialog.setCancelable(false);
                bottomSheetDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                            bottomSheetDialog.dismiss();
                            return true;
                        }
                        return false; // Don't capture
                    }
                });
                //Load user interest
                //   checkUserInterest(cb1, cb2, cb3, cb4, cb5,cb6,cb7,cb8,cb9);
                bottomSheetDialog.show();

            }
        });
        return view;
    }

    /*
        private void checkUserInterest(final CheckBox cb1, final CheckBox cb2, final CheckBox cb3, final CheckBox cb4, final CheckBox cb5, final CheckBox cb6, final CheckBox cb7, final CheckBox cb8, final CheckBox cb9) {
            pRef = coreRef.child("USERS");
            pRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (String.valueOf(ds.child("Email").getValue()).equals(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString())) {
                            switch (ds.child("InterestedGroup").getValue().toString())
                            {
                                case "1":
                                    if (Build.VERSION.SDK_INT < 21) {
                                        CompoundButtonCompat.setButtonTintList(cb1, ColorStateList.valueOf(Color.parseColor("#3BB54A")));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    } else {
                                        cb1.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));//setButtonTintList is accessible directly on API>19
                                    }
                                    break;
                                case "2":
                                    if (Build.VERSION.SDK_INT < 21) {
                                        CompoundButtonCompat.setButtonTintList(cb2, ColorStateList.valueOf(Color.parseColor("#3BB54A")));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    } else {
                                        cb2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));//setButtonTintList is accessible directly on API>19
                                    }
                                    break;
                                case "3":
                                    if (Build.VERSION.SDK_INT < 21) {
                                        CompoundButtonCompat.setButtonTintList(cb3, ColorStateList.valueOf(Color.parseColor("#3BB54A")));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    } else {
                                        cb3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));//setButtonTintList is accessible directly on API>19
                                    }
                                    break;
                                case "4":
                                    if (Build.VERSION.SDK_INT < 21) {
                                        CompoundButtonCompat.setButtonTintList(cb4, ColorStateList.valueOf(Color.parseColor("#3BB54A")));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    } else {
                                        cb4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));//setButtonTintList is accessible directly on API>19
                                    }
                                    break;
                                case "5":
                                    if (Build.VERSION.SDK_INT < 21) {
                                        CompoundButtonCompat.setButtonTintList(cb5, ColorStateList.valueOf(Color.parseColor("#3BB54A")));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    } else {
                                        cb5.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));//setButtonTintList is accessible directly on API>19
                                    }
                                    break;
                                case "6":
                                    if (Build.VERSION.SDK_INT < 21) {
                                        CompoundButtonCompat.setButtonTintList(cb6, ColorStateList.valueOf(Color.parseColor("#3BB54A")));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    } else {
                                        cb6.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));//setButtonTintList is accessible directly on API>19
                                    }
                                    break;
                                case "7":
                                    if (Build.VERSION.SDK_INT < 21) {
                                        CompoundButtonCompat.setButtonTintList(cb7, ColorStateList.valueOf(Color.parseColor("#3BB54A")));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    } else {
                                        cb7.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));//setButtonTintList is accessible directly on API>19
                                    }
                                    break;
                                case "8":
                                    if (Build.VERSION.SDK_INT < 21) {
                                        CompoundButtonCompat.setButtonTintList(cb8, ColorStateList.valueOf(Color.parseColor("#3BB54A")));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    } else {
                                        cb8.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));//setButtonTintList is accessible directly on API>19
                                    }
                                    break;
                                case "9":
                                    if (Build.VERSION.SDK_INT < 21) {
                                        CompoundButtonCompat.setButtonTintList(cb9, ColorStateList.valueOf(Color.parseColor("#3BB54A")));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
                                    } else {
                                        cb9.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#3BB54A")));//setButtonTintList is accessible directly on API>19
                                    }
                                    break;


                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    */
    private void loadMockTest(View view, final String userInterest) {

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

                        if (String.valueOf(ds.child("InterestedGroup").getValue()).equals(userInterest)) {
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

    private void setUserInterest(final String interest) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserDetails", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("uInterestedGroup", interest);
        String userID = sharedPref.getString("uId", "1");
        editor.commit();
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("USERS").child(userID);
        mRef.child("InterestedGroup").setValue(interest);

    }


}
