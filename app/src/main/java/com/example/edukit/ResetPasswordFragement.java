package com.example.edukit;

import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordFragement extends BottomSheetDialogFragment {
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView buttonText, instruction;
    ImageView img1;
    ConstraintLayout constraintLayout;
    private CardView btnResetPassword;
    private EditText edEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.reset_password, container, false);
        btnResetPassword = view.findViewById(R.id.cardView);
        edEmail = view.findViewById(R.id.txtEmail);
        mAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progressBar);
        buttonText = view.findViewById(R.id.tvButtonText);
        constraintLayout = view.findViewById(R.id.constrainLayout);
        instruction = view.findViewById(R.id.instruction);
        img1 = view.findViewById(R.id.img1);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email = edEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(getContext(), "Email Required", Toast.LENGTH_SHORT).show();
                    edEmail.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getContext(), "Email is not valid", Toast.LENGTH_SHORT).show();
                    edEmail.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            constraintLayout.setBackgroundColor(getResources().getColor(R.color.green));
                            buttonText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                            buttonText.setText("Password link sent sucessfully");

                            instruction.setText("CHECK YOUR EMAIL");
                            instruction.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            instruction.setGravity(Gravity.CENTER);
                            img1.setVisibility(View.GONE);
                            edEmail.setVisibility(View.GONE);


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    dismiss();

                                }
                            }, 2000);


                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
