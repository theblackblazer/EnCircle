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

public class signup extends AppCompatActivity {

    private Button reg;
    private TextView email,pass,cpass;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth=FirebaseAuth.getInstance();
        reg=(Button)findViewById(R.id.register);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        cpass=(EditText)findViewById(R.id.cpass);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String e=email.getText().toString().trim();
            String p=pass.getText().toString().trim();
            String cp=cpass.getText().toString().trim();
                if (TextUtils.isEmpty(e)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(p)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (p.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!p.contentEquals(cp))
                {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
            progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(e, p)
                        .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(signup.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                               if (!task.isSuccessful()) {
                                    Toast.makeText(signup.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(signup.this, signin.class));
                                    finish();
                                }
                            }
                        });
            }




        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
