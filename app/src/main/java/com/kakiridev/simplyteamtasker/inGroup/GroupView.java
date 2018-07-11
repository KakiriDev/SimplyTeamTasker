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
    }

    private void floatingOnClickInit() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                Intent i = new Intent(GroupView.this, CreateNewTask.class);
                i.putExtra("GROUP_NAME", extras.getString("GROUP_NAME"));
                startActivity(i);
            }
        });
    }

}
