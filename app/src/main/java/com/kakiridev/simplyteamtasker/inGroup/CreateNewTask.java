package com.kakiridev.simplyteamtasker.inGroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kakiridev.simplyteamtasker.FB;
import com.kakiridev.simplyteamtasker.R;
import com.kakiridev.simplyteamtasker.Task;

public class CreateNewTask extends AppCompatActivity {

    EditText taskName, taskDescription;
    Button createTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);

        taskName = findViewById(R.id.taskName);
        taskDescription = findViewById(R.id.taskDescription);
        createTaskButton = findViewById(R.id.createTaskButton);

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTask(taskName.getText().toString(), taskDescription.getText().toString());
                finish();
            }
        });
    }

    public void createTask(String name, String description){
        Bundle extras = getIntent().getExtras();
        String groupName = extras.getString("GROUP_NAME");

        FB fb = new FB();
        String userId = fb.getFirebaseUser().getUid();


        Task task = new Task();
        task.setId("");
        task.setName(name);
        task.setDescription(description);
        task.setAuthor(userId);
        task.setOwner("");
        task.setStatus("0");
        fb.createTask(task, groupName);
    }
}
