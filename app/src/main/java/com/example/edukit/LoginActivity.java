package com.example.edukit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    public static int userName;
    CardView btnSignin;
    EditText edEmail, edPassword;
    TextView btnSignUp, btnResetPassword;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignin = findViewById(R.id.cardView);
        edEmail = findViewById(R.id.txtEmail);
        edPassword = findViewById(R.id.txtPassword);
        progressBar = findViewById(R.id.progressBar);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        mRef = FirebaseDatabase.getInstance().getReference().child("USERS");
        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String email = edEmail.getText().toString().trim();
                final String password = edPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email Required", Toast.LENGTH_SHORT).show();
                    edEmail.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password Required", Toast.LENGTH_SHORT).show();
                    edPassword.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            mRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (String.valueOf(ds.child("Email").getValue()).equals(email)) {
                                            SharedPreferences sharedPref = getSharedPreferences("UserDetails", getApplicationContext().MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("uName", String.valueOf(ds.child("Name").getValue()));
                                            editor.putString("uMobile", String.valueOf(ds.child("Mobile").getValue()));
                                            editor.putString("uInterestedGroup", String.valueOf(ds.child("InterestedGroup").getValue()));
                                            editor.putString("uId", String.valueOf(ds.getKey()));

                                            editor.commit();

                                            // Toast.makeText(LoginActivity.this,ds.child("InterestedGroup").getValue().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordFragement resetPasswordFragement = new ResetPasswordFragement();
                resetPasswordFragement.show(getSupportFragmentManager(), "Reset");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }
    }

}
