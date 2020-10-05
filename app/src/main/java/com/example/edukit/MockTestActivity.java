package com.example.edukit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class MockTestActivity extends AppCompatActivity {

    RecyclerView rvMQuestion;
    ArrayList<MockQuestionModel> mList;
    Button btnSubmit;
    String QuestionSet;
    MockQuestionAdapter adapter;
    TextView tvTitle, tvFM, tvNM, tvTime;
    LinearLayout tvHeading;
    Dialog epicDialog;
    private CountDownTimer mcountDownTimer;
    private boolean mTimerRunning;
    private long mTimerLeftInMilies;
    CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_test);

        customDialog = new CustomDialog(MockTestActivity.this);
        customDialog.showDialog("Loading...");

        tvTitle = findViewById(R.id.mTitle);
        tvFM = findViewById(R.id.mFM);
        tvNM = findViewById(R.id.mNM);
        tvTime = findViewById(R.id.mTime);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvHeading = findViewById(R.id.mock_heading);
        tvTitle.setText(getIntent().getStringExtra("title"));
        tvFM.setText("F.M : " + getIntent().getStringExtra("fm"));//F.M : 100
        tvNM.setText("N.M: -" + getIntent().getStringExtra("nm") + "/WA");//N.M: -0.25/WA
        //tvTime.setText(getIntent().getStringExtra("time"));//Time


        try {
            mTimerLeftInMilies = Long.parseLong(getIntent().getStringExtra("time")) * 60000;
        } catch (NumberFormatException e) {
            Log.d("Number", e.getMessage());
        }

        QuestionSet = getIntent().getStringExtra("q_set_id");
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("MOCK_QUESTION_SET").child(QuestionSet);
        rvMQuestion = findViewById(R.id.questionRecyclerView);


        rvMQuestion = findViewById(R.id.questionRecyclerView);
        mList = new ArrayList<>();
        rvMQuestion.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new MockQuestionAdapter(getApplicationContext(), mList);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    // Toast.makeText(MockTestActivity.this,String.valueOf(ds.child("CORRECT").getValue()), Toast.LENGTH_SHORT).show();
                }
                rvMQuestion.setAdapter(adapter);
                showInstruction();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog epicDialog = new Dialog(MockTestActivity.this);
                epicDialog.setContentView(R.layout.submit_test_confirmation);
                TextView txtUnattemptedQuestion = epicDialog.findViewById(R.id.txtUnattemptedQuestion);
                epicDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                LinearLayout btnCancel, btnExit;
                btnCancel = epicDialog.findViewById(R.id.btnCancel);
                btnExit = epicDialog.findViewById(R.id.btnExit);
                //
                int _unattempted, _answered = 0;
                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getCorrect().equals(mList.get(i).userAnswer)) {
                    }
                    if (mList.get(i).isAnswerd()) {
                        _answered++;
                    }
                }
                _unattempted = mList.size() - _answered;
                if (_unattempted > 0) {
                    txtUnattemptedQuestion.setVisibility(View.VISIBLE);
                    txtUnattemptedQuestion.setText(String.valueOf(_unattempted) + " UNATTEMPTED QUESTIONS FOUND");
                }
                //
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        epicDialog.dismiss();
                    }
                });
                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitResult();
                        finish();
                    }
                });
                epicDialog.setCancelable(false);
                epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                epicDialog.show();
            }
        });

    }

    public void submitResult() {
        if (mTimerRunning == true) {
            mTimerRunning = false;
            mcountDownTimer.cancel();
        }
        int correct = 0, incorrect = 0, unattemed = 0, answered = 0, outof;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getCorrect().equals(mList.get(i).userAnswer)) {
                correct++;
            }
            if (mList.get(i).isAnswerd()) {
                answered++;
            }
        }
        incorrect = answered - correct;
        unattemed = mList.size() - answered;
        outof = Integer.valueOf(getIntent().getStringExtra("fm"));
        Intent resultIntent = new Intent(MockTestActivity.this, ResultActivity.class);
        resultIntent.putExtra("Correct", correct);
        Log.d("Correct", String.valueOf(correct));
        resultIntent.putExtra("Incorrect", incorrect);
        Log.d("InCorrect", String.valueOf(incorrect));
        resultIntent.putExtra("Unattempted", unattemed);
        resultIntent.putExtra("NegetiveMarking", Float.valueOf(getIntent().getStringExtra("nm")));
        resultIntent.putExtra("Outof", outof);
        Log.d("Outof", String.valueOf(outof));
        resultIntent.putExtra("TotalQuestion", mList.size());
        resultIntent.putExtra("TestTitle", getIntent().getStringExtra("title"));
        resultIntent.putExtra("QuestionSet", QuestionSet);
        startActivity(resultIntent);
    }

    private void startTimer() {

        mcountDownTimer = new CountDownTimer(mTimerLeftInMilies, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimerLeftInMilies = millisUntilFinished;
                updateCoundownText();
            }

            @Override
            public void onFinish() {
                // Toast.makeText(MockTestActivity.this, "Finished", Toast.LENGTH_SHORT).show();
                submitResult();
                mTimerRunning = false;
                mcountDownTimer.cancel();
                finish();

            }
        }.start();
        mTimerRunning = true;

    }

    private void updateCoundownText() {
        int seconds = (int) (mTimerLeftInMilies / 1000) % 60;
        int minutes = (int) ((mTimerLeftInMilies / (1000 * 60)) % 60);
        int hours = (int) ((mTimerLeftInMilies / (1000 * 60 * 60)) % 24);
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        if (hours == 0 && minutes == 0 && seconds <= 60) {
            tvTime.setTextColor(getResources().getColor(R.color.red1));
        }
        tvTime.setText(timeLeftFormatted);
    }

    private void showInstruction() {
        TextView tvMinutes, tvQuestion, tvMarks;
        LinearLayout btnAccept;
        final Dialog epicDialog;
        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.exam_instruction);
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        tvMinutes = epicDialog.findViewById(R.id.time);
        tvQuestion = epicDialog.findViewById(R.id.question);
        tvMarks = epicDialog.findViewById(R.id.marks);
        btnAccept = epicDialog.findViewById(R.id.btnAccept);
        tvMinutes.setText(getIntent().getStringExtra("time"));
        tvQuestion.setText(String.valueOf(mList.size()));
        tvMarks.setText(getIntent().getStringExtra("fm"));
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
                btnSubmit.setVisibility(View.VISIBLE);
                startTimer();
            }
        });
        customDialog.dismissDialog();

        epicDialog.show();

    }

    @Override
    public void onBackPressed() {
        final Dialog epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.exam_stop_confirmation);
        LinearLayout btnCancel, btnExit;
        btnCancel = epicDialog.findViewById(R.id.btnCancel);
        btnExit = epicDialog.findViewById(R.id.btnExit);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                epicDialog.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning == true) {
                    mTimerRunning = false;
                    mcountDownTimer.cancel();
                }
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    finishAndRemoveTask();
                } else {
                    finish();

                }
            }
        });
        epicDialog.setCancelable(false);
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimerRunning == true) {
            mTimerRunning = false;
            mcountDownTimer.cancel();
        }
    }
}
