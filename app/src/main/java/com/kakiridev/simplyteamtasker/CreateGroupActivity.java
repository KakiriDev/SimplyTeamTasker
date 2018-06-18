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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateGroupActivity extends AppCompatActivity {

    EditText groupName, groupPassword, groupRepeatPassword;
    Button createGroupButton;

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
                if (groupName.getText().toString().length()>0){
                    if(groupNameAvailable(groupName.getText().toString())){
                        if (comparePassword(groupPassword.getText().toString(), groupRepeatPassword.getText().toString())) {
                            createGroup(groupName.getText().toString(), groupPassword.getText().toString(), getFirebaseUserId());
                            finish();
                        } else {
                            Toast.makeText(CreateGroupActivity.this, "Password is incorrect!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CreateGroupActivity.this, "Name is already use", Toast.LENGTH_LONG).show();
                    }

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

    boolean available;
    public Boolean groupNameAvailable(final String name) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("Settings");
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//TODO not working, dont check name
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    String group = appleSnapshot.getKey();

                    if (group.equals(name)) {
                        available = false;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DTAG", "onCancelled", databaseError.toException());
            }
        });


        return available;
    }

    public String getFirebaseUserId() {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentFirebaseUser.getUid().toString();
        return uid;
    }
}
