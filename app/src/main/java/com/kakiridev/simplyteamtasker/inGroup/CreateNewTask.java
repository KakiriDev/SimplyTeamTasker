package com.kakiridev.simplyteamtasker.inGroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kakiridev.simplyteamtasker.R;

public class CreateNewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);

        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
    }
}
