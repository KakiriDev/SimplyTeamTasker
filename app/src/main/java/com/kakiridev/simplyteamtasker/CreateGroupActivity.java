package com.kakiridev.simplyteamtasker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateGroupActivity extends AppCompatActivity {

    EditText groupName, groupPassword, groupRepeatPassword;
    Button createGroupButton;
    String name;
    boolean groupNameAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupName = findViewById(R.id.groupName);
        groupPassword = findViewById(R.id.groupPassword);
        groupRepeatPassword = findViewById(R.id.groupRepeatPassword);
        createGroupButton = findViewById(R.id.createGroup);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupName.getText().toString().length() > 0) {

                    readData(new CheckNameInterface() {
                        @Override
                        public void checkName(boolean available) {

                            if (available) {
                                if (comparePassword(groupPassword.getText().toString(), groupRepeatPassword.getText().toString())) {
                                    createGroup(groupName.getText().toString(), groupPassword.getText().toString(), getFirebaseUserId());
                                    finish();
                                } else {
                                    Toast.makeText(CreateGroupActivity.this, "Password is incorrect!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(CreateGroupActivity.this, "Name is already use", Toast.LENGTH_LONG).show();
                            }

                            groupNameAvailable = available;
                        }
                    }, groupName.getText().toString());

                } else {
                    Toast.makeText(CreateGroupActivity.this, "Group name cannot be void", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void createGroup(String name, String password, String user) {

        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("password", password);
        dataMap.put("name", name);
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child(name);
        mdatabase.child("Settings").setValue(dataMap);

        HashMap<String, String> dataMap2 = new HashMap<String, String>();
        dataMap2.put("user", user);
        mdatabase.child("Users").setValue(dataMap2);

    }

    public boolean comparePassword(String pass1, String pass2) {
        boolean compare;
        if (pass1.equals(pass2)) {
            compare = true;
        } else {
            compare = false;
        }
        return compare;
    }


    DatabaseReference ref;

    private void readData(final CheckNameInterface checkNameInterface, final String name) {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean available = true;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    String group = appleSnapshot.getKey();
                    if (dataSnapshot.getChildren() != null) {
                        if (group.equals(name)) {
                            available = false;
                        }
                    }
                }
                checkNameInterface.checkName(available);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DTAG", "onCancelled", databaseError.toException());
            }
        });
    }

    private interface CheckNameInterface {
        void checkName(boolean available);
    }

    public String getFirebaseUserId() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid().toString();
        return uid;
    }
}
