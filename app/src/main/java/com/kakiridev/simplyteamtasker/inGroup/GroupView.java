package com.kakiridev.simplyteamtasker.inGroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kakiridev.simplyteamtasker.groupEvent.MainActivity;
import com.kakiridev.simplyteamtasker.R;

public class GroupView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        floatingOnClickInit();

        /**
        //back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
**/
    }

    private void floatingOnClickInit(){
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(GroupView.this, CreateNewTask.class));
             //   Toast.makeText(GroupView.this, "FAB clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTask(){

    }



/**
    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    **/
}
