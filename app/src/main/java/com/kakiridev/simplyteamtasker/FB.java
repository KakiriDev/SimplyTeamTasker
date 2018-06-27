package com.kakiridev.simplyteamtasker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FB {

    /** get all groups selected user **/
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
                userGroupsInterface.getGroupList(fbGroups);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
}
