package com.kakiridev.simplyteamtasker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView enterGroup;
    ImageView imageView;
    TextView textName, textEmail;
    FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    ArrayList<String> fbGroups;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterGroup = findViewById(R.id.listview);
        InitializeAuth();
        initListViewOnItemClick();
        imageView = findViewById(R.id.imageView);
        textName = findViewById(R.id.textViewName);
        textEmail = findViewById(R.id.textViewEmail);

        InitButton();

        FirebaseUser user = mAuth.getCurrentUser();

        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(imageView);

        textName.setText(user.getDisplayName());
        textEmail.setText(user.getEmail());

        setGroupListAdapter(user.getUid());

/*
        //back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
*/
    }

    private void InitializeAuth(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        /**
         mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        }
        }; **/
    }
    public void InitButton() {
        ImageButton buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                startActivity_LoginActivity();
            }
        });

        ImageButton createGroupButton = findViewById(R.id.buttonCreateGroup);
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity_CreateGroupActivity();
            }
        });

        ImageButton joinGroupButton = findViewById(R.id.buttonJoinGroup);
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity_JoinGroupActivity();
            }
        });

    }

    public void startActivity_LoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
    public void startActivity_CreateGroupActivity() {
        Intent i = new Intent(this, CreateGroupActivity.class);
        startActivity(i);
    }
    public void startActivity_JoinGroupActivity() {
        Intent i = new Intent(this, JoinGroupActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is not logged in
        //opening the login activity
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                // textViewUserEmail.setText(" ".toString());
            }
        });
    }

    // GROUP LIST VIEW //
    public void setGroupListAdapter(String userId) {
        FB fb = new FB();
        fb.getListOfGroups(new UserGroupsInterface() {
            @Override
            public void getGroupList(ArrayList groupList) {

                GroupListViewAdapter adapter = new GroupListViewAdapter(getApplicationContext(), R.layout.group_listview_row, groupList);
                ListView listView = findViewById(R.id.listview);
                listView.setAdapter(adapter);
                registerForContextMenu(listView);
                adapter.notifyDataSetChanged();

            }
        }, userId);
    }

    //Add Click Listener

    private void initListViewOnItemClick() {
        enterGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView selectedText = v.findViewById(R.id.tv_group_name);
                String groupName = selectedText.getText().toString();
                Toast.makeText(getApplicationContext(), groupName, Toast.LENGTH_LONG).show();
                startActivity_GroupView(groupName);
            }
        });
    }

    public void startActivity_GroupView (String groupName){
        Intent i = new Intent(this, GroupView.class);
        i.putExtra("GROUP_NAME", groupName);
        startActivity(i);
    }

}