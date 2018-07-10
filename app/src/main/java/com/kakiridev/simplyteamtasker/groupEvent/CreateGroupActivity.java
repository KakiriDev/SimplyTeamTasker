package com.kakiridev.simplyteamtasker.groupEvent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
                validateName();
            }
        });
    }

    private void validateName() {
        if (groupName.getText().toString().length() > 0) {
            if (groupPassword.getText().toString().length() > 0) {
                FB fb = new FB();
                fb.checkName(new CheckNameInterface() {
                    @Override
                    public void checkName(boolean available) {

                        if (available) {
                            if (comparePassword(groupPassword.getText().toString(), groupRepeatPassword.getText().toString())) {
                                FB fb = new FB();
                                fb.createGroup(groupName.getText().toString(), groupPassword.getText().toString(), getFirebaseUser());
                                finish();
                            } else {
                                Toast.makeText(CreateGroupActivity.this, "Password is incorrect!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(CreateGroupActivity.this, "Name is already use", Toast.LENGTH_LONG).show();
                        }
                    }
                }, groupName.getText().toString());
            } else {
                Toast.makeText(CreateGroupActivity.this, "Password cannot be void", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CreateGroupActivity.this, "Group name cannot be void", Toast.LENGTH_LONG).show();
        }
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

    public String getFirebaseUserId() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid().toString();
        return uid;
    }

    public FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
