package com.example.edukit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    CardView btnSignup;
    EditText edName, edEmail, edMobile, edPassword, edConPassword, edGender;
    TextView btnSignin, dialogResult;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignup = findViewById(R.id.btnSignUp);
        btnSignin = findViewById(R.id.btnSignin);
        edName = findViewById(R.id.txtName);
        edEmail = findViewById(R.id.txtEmail);
        edMobile = findViewById(R.id.txtMobile);
        edPassword = findViewById(R.id.txtPassword);
        edConPassword = findViewById(R.id.txtConPass);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String email = edEmail.getText().toString().trim();
                final String password = edPassword.getText().toString().trim();
                final String name = edName.getText().toString();
                String confirm_password = edConPassword.getText().toString().trim();


                final String mobileNo = edMobile.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Name Required", Toast.LENGTH_SHORT).show();
                    edName.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if (email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Email Required", Toast.LENGTH_SHORT).show();
                    edEmail.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (mobileNo.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Mobile No Required", Toast.LENGTH_SHORT).show();
                    edMobile.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (mobileNo.length() < 10 || mobileNo.length() > 10) {
                    Toast.makeText(SignupActivity.this, "Enter a Valid Mobile No", Toast.LENGTH_SHORT).show();
                    edMobile.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Enter a Valid Email Address", Toast.LENGTH_SHORT).show();
                    edEmail.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Password Required", Toast.LENGTH_SHORT).show();
                    edPassword.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password Must Contain Atleast six Character", Toast.LENGTH_SHORT).show();
                    edPassword.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (confirm_password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Confirm Password Required", Toast.LENGTH_SHORT).show();
                    edConPassword.requestFocus();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (!password.equals(confirm_password)) {
                    edConPassword.requestFocus();
                    Toast.makeText(SignupActivity.this, "Password Not Mached", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }


                mRef = FirebaseDatabase.getInstance().getReference().child("USERS");
                //Signing Up
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    // Toast.makeText(SignupActivity.this, "You can sign in now", Toast.LENGTH_SHORT).show();
                                    saveUserData(name, email, mobileNo, password, "1");
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(SignupActivity.this, "You are already registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
        });

    }

    private void saveUserData(String Name, String Email, String Mobile, String Passowrd, String InterestedGroup) {
        String key = mRef.push().getKey();

        SharedPreferences sharedPref = getSharedPreferences("UserDetails", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("uName", Name);
        editor.putString("uMobile", Mobile);
        editor.putString("uId", key);
        editor.commit();

        mRef.child(key).child("Name").setValue(Name);
        mRef.child(key).child("Email").setValue(Email);
        mRef.child(key).child("Mobile").setValue(Mobile);
        mRef.child(key).child("Password").setValue(Passowrd);
        mRef.child(key).child("InterestedGroup").setValue(InterestedGroup);
        mRef.child(key).child("InterestedGroup").setValue(InterestedGroup);
    }

}
