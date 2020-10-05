package com.example.edukit;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    int correct, incorrect, unattemed, outof, totalQuestion;
    float score, NegetiveMarking;
    TextView tvScore, tvOutof, tvCorrect, tvIncorrect, tvUnattempted, tvNegetive, tvTitle;
    LinearLayout btnViewAnswerKey, layoutToHide, result;
    List<MockQuestionModel> mList;
    RecyclerView rvMQuestion;
    MockQuestionAnswerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        List<MockQuestionModel> mList;

        correct = Integer.valueOf(getIntent().getIntExtra("Correct", 0));
        incorrect = Integer.valueOf(getIntent().getIntExtra("Incorrect", 0));
        unattemed = Integer.valueOf(getIntent().getIntExtra("Unattempted", 0));
        outof = Integer.valueOf(getIntent().getIntExtra("Outof", 0));
        NegetiveMarking = Float.valueOf(getIntent().getFloatExtra("NegetiveMarking", 0));
        totalQuestion = Integer.valueOf(getIntent().getIntExtra("TotalQuestion", 0));
        float perQuestionMarks = outof / totalQuestion;
        score = (correct * perQuestionMarks) - (incorrect * NegetiveMarking);

        tvScore = findViewById(R.id.txtScore);
        tvOutof = findViewById(R.id.txtOutof);
        tvCorrect = findViewById(R.id.txtCorrect);
        tvIncorrect = findViewById(R.id.txtIncorrect);
        tvUnattempted = findViewById(R.id.txtUnattempted);
        tvNegetive = findViewById(R.id.txtNegetive);
        tvTitle = findViewById(R.id.txtTitle);
        btnViewAnswerKey = findViewById(R.id.btnViewAnswerKey);
        rvMQuestion = findViewById(R.id.questionRecyclerView);
        layoutToHide = findViewById(R.id.layoutToHide);
        result = findViewById(R.id.result);

        tvOutof.setText(String.valueOf(outof));
        tvCorrect.setText(String.valueOf(correct));
        tvIncorrect.setText(String.valueOf(incorrect));
        tvUnattempted.setText(String.valueOf(unattemed));
        tvNegetive.setText(String.valueOf(NegetiveMarking));
        DecimalFormat format = new DecimalFormat("0.##");
        tvScore.setText(String.valueOf(format.format(score)));
        tvTitle.setText(getIntent().getStringExtra("TestTitle"));
        final String QuestionSet = getIntent().getStringExtra("QuestionSet");
        if (score < 0) {
            tvScore.setTextColor(getResources().getColor(R.color.red1));
        } else {
            tvScore.setTextColor(getResources().getColor(R.color.green));
        }
        btnViewAnswerKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutToHide.setVisibility(View.GONE);
                result.setVisibility(View.GONE);
                tvTitle.setText("Answer Key");
                tvTitle.setGravity(Gravity.CENTER);

                loadAnswerKey(QuestionSet);

            }
        });
        ImageView bntback = findViewById(R.id.bntback);
        bntback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadAnswerKey(String QuestionSet) {
        mList = new ArrayList<>();
        rvMQuestion.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new MockQuestionAnswerAdapter(getApplicationContext(), mList);
        FirebaseDatabase.getInstance().getReference().child("MOCK_QUESTION_SET").child(QuestionSet).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mList.add(new MockQuestionModel(
                            String.valueOf(ds.child("Q_TYPE").getValue()),
                            String.valueOf(ds.child("Q").getValue()),
                            String.valueOf(ds.child("OPT1").getValue()),
                            String.valueOf(ds.child("OPT2").getValue()),
                            String.valueOf(ds.child("OPT3").getValue()),
                            String.valueOf(ds.child("OPT4").getValue()),
                            String.valueOf(ds.child("CORRECT").getValue())

                    ));
                }
                rvMQuestion.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
