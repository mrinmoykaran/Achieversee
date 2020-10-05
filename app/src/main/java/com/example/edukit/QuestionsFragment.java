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
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class QuestionsFragment extends Fragment {
    DatabaseReference coreRef, pRef;
    CardView btnChange;
    String key;
    String choice;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    boolean showTrending;

    public QuestionsFragment(boolean showTrending) {
        this.showTrending = showTrending;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Getting User Interest
        SharedPreferences sharedPref = getContext().getSharedPreferences("UserDetails", getContext().MODE_PRIVATE);
        String defaultValue = "1";
        final String userInterest = sharedPref.getString("uInterestedGroup", defaultValue);
        //Getting User Interest
        final View view = inflater.inflate(R.layout.fragement_questions, container, false);
        final TextView tvNotesTitle = view.findViewById(R.id.txtNotesTitle);
        LinearLayout examchooser = view.findViewById(R.id.examchooser);
        final TextView tvNotesSubTitle = view.findViewById(R.id.txtNotesSubTitle);
        btnChange = view.findViewById(R.id.btnChange);
        coreRef = FirebaseDatabase.getInstance().getReference();
        if (showTrending == true) {
            examchooser.setVisibility(View.GONE);

            toolbar = getActivity().findViewById(R.id.toolbar);
            bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
            toolbar.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
        }

        switch (userInterest) {
            case "1":
                tvNotesTitle.setText("WB Govt Exam");
                tvNotesSubTitle.setText("WB Excise Constable,WB Clerk...");
                break;
            case "2":
                tvNotesTitle.setText("WBCS");
                tvNotesSubTitle.setText("WBCS Pre,WBCS Pre Package...");
                break;
            case "3":
                tvNotesTitle.setText("ENTRANCE EXAM");
                tvNotesSubTitle.setText("WBJEE,JEE MAIN,JEE Advanced...");
                break;
            case "4":
                tvNotesTitle.setText("RAILWAY");
                tvNotesSubTitle.setText("WB Railway Group D,WB Govt...");
                break;
            case "5":
                tvNotesTitle.setText("BANKING");
                tvNotesSubTitle.setText("IBPS Clerk-Pre,Banking Pre Pa...");
                break;
            case "6":
                tvNotesTitle.setText("TEACHING");
                tvNotesSubTitle.setText("PRIMARY TET,SSC...");
                break;
            case "7":
                tvNotesTitle.setText("SSC");
                tvNotesSubTitle.setText("SSC-CHSL,SSC Stenographer...");
                break;
            case "8":
                tvNotesTitle.setText("DEFENCE");
                tvNotesSubTitle.setText("ARMY,POLICE,AIRFORCE,NAVY...");
                break;
            case "9":
                tvNotesTitle.setText("UPSC");
                tvNotesSubTitle.setText("UPSC,Free Daily Dose UPSC");
                break;

        }
        loadNotes(view, userInterest);

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
                            tvNotesTitle.setText("WB Govt Exam");
                            tvNotesSubTitle.setText("WB Excise Constable,WB Clerk...");
                            setUserInterest("1");
                            loadNotes(view, "1");
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
                            tvNotesTitle.setText("WBCS");
                            tvNotesSubTitle.setText("WBCS Pre,WBCS Pre Package...");
                            setUserInterest("2");
                            loadNotes(view, "2");
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
                            tvNotesTitle.setText("ENTRANCE EXAM");
                            tvNotesSubTitle.setText("WBJEE,JEE MAIN,JEE Advanced...");
                            setUserInterest("3");
                            loadNotes(view, "3");
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
                            tvNotesTitle.setText("RAILWAY");
                            tvNotesSubTitle.setText("WB Railway Group D,WB Govt...");
                            setUserInterest("4");
                            loadNotes(view, "4");
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
                            tvNotesTitle.setText("BANKING");
                            tvNotesSubTitle.setText("IBPS Clerk-Pre,Banking Pre Pa...");
                            setUserInterest("5");
                            loadNotes(view, "5");
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
                            tvNotesTitle.setText("TEACHING");
                            tvNotesSubTitle.setText("PRIMARY TET,SSC...");
                            setUserInterest("6");
                            loadNotes(view, "6");
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
                            tvNotesTitle.setText("SSC");
                            tvNotesSubTitle.setText("SSC-CHSL,SSC Stenographer...");
                            setUserInterest("7");
                            loadNotes(view, "7");
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
                            tvNotesTitle.setText("DEFENCE");
                            tvNotesSubTitle.setText("ARMY,POLICE,AIRFORCE,NAVY...");
                            setUserInterest("8");
                            loadNotes(view, "8");
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
                            tvNotesTitle.setText("UPSC");
                            tvNotesSubTitle.setText("UPSC,Free Daily Dose UPSC");
                            setUserInterest("9");
                            loadNotes(view, "9");
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
                // checkUserInterest(cb1, cb2, cb3, cb4, cb5,cb6,cb7,cb8,cb9);
                bottomSheetDialog.show();

            }
        });
        return view;
    }

    private void loadNotes(View view, final String userInterest) {

        try {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("QUESTIONS").child("Question_Info");
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewNotes);
            final List<QuestionDataModel> nlist = new ArrayList<>();

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            final MyQuestionAdapter myQuestionAdapter = new MyQuestionAdapter(nlist, getContext());
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nlist.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (showTrending == true) {
                            if (String.valueOf(ds.child("isTrending").getValue()).equals("true")) {
                                nlist.add(new QuestionDataModel(
                                        String.valueOf(ds.getKey()),
                                        String.valueOf(ds.child("Title").getValue()),
                                        String.valueOf(ds.child("Language").getValue()),
                                        "2",
                                        String.valueOf(ds.child("Demo_PDF").getValue()),
                                        String.valueOf(ds.child("Download_Link").getValue()),
                                        String.valueOf(ds.child("Group").getValue()),
                                        String.valueOf(ds.child("Price").getValue()),
                                        String.valueOf(ds.child("Publisher_Id").getValue())
                                ));
                            }
                        } else {
                            if (String.valueOf(ds.child("Group").getValue()).equals(userInterest)) {
                                nlist.add(new QuestionDataModel(
                                        String.valueOf(ds.getKey()),
                                        String.valueOf(ds.child("Title").getValue()),
                                        String.valueOf(ds.child("Language").getValue()),
                                        "2",
                                        String.valueOf(ds.child("Demo_PDF").getValue()),
                                        String.valueOf(ds.child("Download_Link").getValue()),
                                        String.valueOf(ds.child("Group").getValue()),
                                        String.valueOf(ds.child("Price").getValue()),
                                        String.valueOf(ds.child("Publisher_Id").getValue())
                                ));
                            }
                        }
                    }
                    Collections.reverse(nlist);
                    recyclerView.setAdapter(myQuestionAdapter);
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
