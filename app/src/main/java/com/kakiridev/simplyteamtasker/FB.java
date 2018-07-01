package com.kakiridev.simplyteamtasker;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FB {

    /** get all groups selected user
     * return ArrayList<String> - list of groups actually logged user
     * **/
    ArrayList<String> fbGroups;
    public void getListOfGroups(final UserGroupsInterface userGroupsInterface, final String userId) {

        DatabaseReference mDatabaseGroups = com.google.firebase.database.FirebaseDatabase.getInstance().getReference();

        mDatabaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                fbGroups = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String groupName = dsp.getKey().toString();

                    for (DataSnapshot dspp : dataSnapshot.child(groupName).child("Users").getChildren()){
                        String user = dspp.getKey().toString();

                        if (user.equals(userId)){
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

    /** check name of group
     * return boolean - false if name is currently in use, true if name is available
     * **/
    public void checkName(final CheckNameInterface checkNameInterface, final String name) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
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

    /** validate name/password/user id  before join to group
     * return boolean - true if name is correct
     * return boolean - true if password is correct
     * return boolean - true if userid is currently in group
     * **/
    public void validateJoin(final ValidateJoinInterface validateJoinInterface, final String name, final String password, final String user) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
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
                        if (appleSnapshot.getKey().equals(name) ) {
                            correctName = true;
                            if(dataSnapshot.child(name).child("Settings").child("Password").getValue(String.class).equals(password)){
                                correctPassword = true;
                                if(dataSnapshot.child(name).child("Users").hasChild(user)){
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

}
