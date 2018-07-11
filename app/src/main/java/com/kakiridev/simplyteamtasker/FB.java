package com.kakiridev.simplyteamtasker;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakiridev.simplyteamtasker.groupEvent.CheckNameInterface;
import com.kakiridev.simplyteamtasker.groupEvent.UserGroupsInterface;
import com.kakiridev.simplyteamtasker.groupEvent.ValidateJoinInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FB {

    /** STRUCTURE
     * ---| Users
     * ------| [usersId]
     * ---------| UserId : [userId]
     * ---------| UserEmail : [userEmail]
     * ---------| UserName : [userName]
     * ---| Groups
     * ------| [GroupName]
     * ---------| Settings
     * ------------| Name:[name]
     * ------------| Password:[password]
     * ---------| Users
     * ------------| [UserId]
     **/


    /**
     * get all groups selected user
     * return ArrayList<String> - list of groups actually logged user
     **/
    ArrayList<String> fbGroups;

    public void getListOfGroups(final UserGroupsInterface userGroupsInterface, final String userId) {

        DatabaseReference mDatabaseGroups = com.google.firebase.database.FirebaseDatabase.getInstance().getReference().child("Groups");

        mDatabaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                fbGroups = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String groupName = dsp.getKey().toString();

                    for (DataSnapshot dspp : dataSnapshot.child(groupName).child("Users").getChildren()) {
                        String user = dspp.getKey().toString();

                        if (user.equals(userId)) {
                            fbGroups.add(groupName);
                        }
                    }
                }
                //return list of groups loged user
                userGroupsInterface.getGroupList(fbGroups);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    /**
     * check name of group
     * return boolean - false if name is currently in use, true if name is available
     **/
    public void checkName(final CheckNameInterface checkNameInterface, final String name) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Groups");
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

    /**
     * validate name/password/user id  before join to group
     * return boolean - true if name is correct
     * return boolean - true if password is correct
     * return boolean - true if userid is currently in group
     **/
    public void validateJoin(final ValidateJoinInterface validateJoinInterface, final String name, final String password, final String user) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Groups");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean correctName = false;
            boolean correctPassword = false;
            boolean correctUserId = true;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    correctName = false;
                    correctPassword = false;
                    correctUserId = true;

                    if (dataSnapshot.getChildren() != null) {
                        if (appleSnapshot.getKey().equals(name)) {
                            correctName = true;
                            if (dataSnapshot.child(name).child("Settings").child("Password").getValue(String.class).equals(password)) {
                                correctPassword = true;
                                if (dataSnapshot.child(name).child("Users").hasChild(user)) {
                                    correctUserId = false;
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
                validateJoinInterface.validateJoin(correctName, correctPassword, correctUserId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DTAG", "onCancelled", databaseError.toException());
            }
        });
    }

    public void createGroup(String name, String password, FirebaseUser user) {

        HashMap<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("Password", password);
        dataMap.put("Name", name);
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("Groups").child(name);
        mdatabase.child("Settings").setValue(dataMap);

        HashMap<String, String> dataMap2 = new HashMap<String, String>();
        dataMap2.put("UserId", user.getUid());
        //dataMap2.put("User Name", user.getDisplayName());
        //dataMap2.put("User Email", user.getEmail());
        mdatabase.child("Users").child(user.getUid()).setValue(dataMap2);

    }

    public void createTask(Task task, String group) {
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference().child("Groups").child(group).child("Tasks");

        String key = mdatabase.push().getKey();
        task.setId(key);
        Map<String, Task> newTask = new HashMap<>();
        newTask.put(key, task);
        mdatabase.child(key).setValue(task);
    }

    public FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
