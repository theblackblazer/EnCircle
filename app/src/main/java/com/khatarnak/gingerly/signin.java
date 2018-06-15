package com.khatarnak.gingerly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signin extends AppCompatActivity {
    private Button signin;
    private TextView email,pass;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(signin.this, home.class));
            finish();
        }

        setContentView(R.layout.activity_signin);

        signin=(Button)findViewById(R.id.signinbtn);
        email=(EditText)findViewById(R.id.signinemail);
        pass=(EditText)findViewById(R.id.pswd);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        auth=FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semail = email.getText().toString();
                final String password = pass.getText().toString();
                if (TextUtils.isEmpty(semail)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(semail, password)
                        .addOnCompleteListener(signin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        pass.setError("Password length short!");
                                    }
                                    else {
                                        Toast.makeText(signin.this, "Unable to SignIn", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(signin.this, home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });

    }
}
