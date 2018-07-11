package com.kakiridev.simplyteamtasker.inGroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.kakiridev.simplyteamtasker.FB;
import com.kakiridev.simplyteamtasker.groupEvent.GroupListViewAdapter;
import com.kakiridev.simplyteamtasker.groupEvent.MainActivity;
import com.kakiridev.simplyteamtasker.R;
import com.kakiridev.simplyteamtasker.groupEvent.UserGroupsInterface;

import java.util.ArrayList;

public class GroupView extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        Bundle extras = getIntent().getExtras();
        String groupName = extras.getString("GROUP_NAME");
        i = new Intent(GroupView.this, CreateNewTask.class);
        i.putExtra("GROUP_NAME", groupName);

        floatingOnClickInit();
        setTaskListAdapter(groupName);
    }

    private void floatingOnClickInit() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(i);
            }
        });
    }

    // GROUP LIST VIEW //
    public void setTaskListAdapter(String groupName) {
        FB fb = new FB();
        fb.getListOfTasks(new GetTaskInterface() {
            @Override
            public void getTaskList(ArrayList taskList) {

                TaskListViewAdapter adapter = new TaskListViewAdapter(getApplicationContext(), R.layout.group_listview_row, taskList);
                ListView listView = findViewById(R.id.listview);
                listView.setAdapter(adapter);
                registerForContextMenu(listView);
                adapter.notifyDataSetChanged();

            }
        }, groupName);
    }

}
