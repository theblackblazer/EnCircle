package com.khatarnak.gingerly;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class home extends AppCompatActivity {
    private Button signout,join;
    private FirebaseAuth auth;
    DatabaseReference onlineRef,currentUser,counterRef;
    FirebaseRecyclerAdapter<user,holder> adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        signout=(Button)findViewById(R.id.signout);
        join=(Button)findViewById(R.id.join);



        recyclerView=(RecyclerView)findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        onlineRef= FirebaseDatabase.getInstance().getReference().child(".info/connected");
        counterRef=FirebaseDatabase.getInstance().getReference("lastOnline");
        currentUser=FirebaseDatabase.getInstance().getReference("lastOnline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        setup();
        updateList();
        join();
        signOut();
    }

    private void signOut() {
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth=FirebaseAuth.getInstance();
                auth.signOut();
                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener()
                {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            startActivity(new Intent(home.this, MainActivity.class));
                            currentUser.removeValue();
                        }
                    }
                };
            }
        });


    }

    private void join() {
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(new user(FirebaseAuth.getInstance().getCurrentUser().getEmail(),"online"));

            }
        });
    }

    private void updateList() {
        FirebaseRecyclerOptions<user> options = new FirebaseRecyclerOptions.Builder<user>()
                .setQuery(counterRef, user.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<user, holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull holder holder, int position, @NonNull user model) {
                holder.txt.setText(model.getEmail());
            }

            @NonNull
            @Override
            public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rholder, parent, false);
                return new holder(v);
            }
        };
    adapter.notifyDataSetChanged();
    recyclerView.setAdapter(adapter);
    }

    private void setup() {


        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue(Boolean.class))
                {
                    currentUser.onDisconnect().removeValue();
                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new user(FirebaseAuth.getInstance().getCurrentUser().getEmail(),"online"));
                    adapter.notifyDataSetChanged();

                }
                counterRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            user user=postSnapshot.getValue(user.class);
                            Log.d("LOG",""+user.getEmail()+"is"+user.getStatus());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

