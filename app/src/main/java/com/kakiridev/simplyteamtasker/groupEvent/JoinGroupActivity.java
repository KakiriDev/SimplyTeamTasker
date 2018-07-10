package com.kakiridev.simplyteamtasker.groupEvent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakiridev.simplyteamtasker.FB;
import com.kakiridev.simplyteamtasker.R;

import java.util.HashMap;

public class JoinGroupActivity extends AppCompatActivity {

    EditText groupName, groupPassword;
    Button joinGroupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        groupName = findViewById(R.id.groupName);
        groupPassword = findViewById(R.id.groupPassword);
        joinGroupButton = findViewById(R.id.joinGroup);

        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinToGroup(groupName.getText().toString(), groupPassword.getText().toString(), getFirebaseUser());
            }
        });
    }

    private void joinToGroup(final String name, final String password, final FirebaseUser user) {
        if (groupName.getText().toString().length() > 0) {
            if (groupPassword.getText().toString().length() > 0) {

                FB fb = new FB();
                fb.validateJoin(new ValidateJoinInterface() {
                    @Override
                    public void validateJoin(boolean correctName, boolean correctPassword, boolean newUserId) {

                        if (correctName) {
                            if (correctPassword) {
                                if (newUserId) {
                                    addUserToGroup(name, user);
                                    finish();
                                    Toast.makeText(JoinGroupActivity.this, "You joined to group " + groupName.getText().toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(JoinGroupActivity.this, "You are in this group", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(JoinGroupActivity.this, "Password is incorrect", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(JoinGroupActivity.this, "Group " + groupName.getText().toString() + " does not exist.", Toast.LENGTH_LONG).show();
                        }
                    }
                }, groupName.getText().toString(), groupPassword.getText().toString(), getFirebaseUserId());
            } else {
                Toast.makeText(JoinGroupActivity.this, "Password cannot be void", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(JoinGroupActivity.this, "Group name cannot be void", Toast.LENGTH_LONG).show();
        }
    }

    public void addUserToGroup(String name, FirebaseUser user) {
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child(name);
        HashMap<String, String> dataMap2 = new HashMap<String, String>();
        dataMap2.put("User", user.getUid());
        dataMap2.put("User Name", user.getDisplayName());
        dataMap2.put("User Email", user.getEmail());
        mdatabase.child("Users").child(user.getUid()).setValue(dataMap2);
    }



    public String getFirebaseUserId() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid().toString();
        return uid;
    }

    public FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

}
