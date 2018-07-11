package com.kakiridev.simplyteamtasker.inGroup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kakiridev.simplyteamtasker.R;
import com.kakiridev.simplyteamtasker.Task;

import java.util.List;

public class TaskListViewAdapter extends ArrayAdapter<Task> {

    private final LayoutInflater mInflater;

    public TaskListViewAdapter(Context context, int textViewResourceId, List<Task> objects) {
        super(context, textViewResourceId, objects);

        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TaskListViewAdapter.ViewHolder holder;

        if (convertView == null) { //tylko w przypadku gdy layout nie istnieje to go tworzymy
            convertView = mInflater.inflate(R.layout.task_listview_row, null);
            holder = new TaskListViewAdapter.ViewHolder();
            holder.tv_task_name = (TextView) convertView.findViewById(R.id.tv_task_name);
            holder.tv_task_description = (TextView) convertView.findViewById(R.id.tv_task_description);
            holder.tv_task_owner = (TextView) convertView.findViewById(R.id.tv_task_owner);
            convertView.setTag(holder);
        } else {
            holder = (TaskListViewAdapter.ViewHolder) convertView.getTag();
        }

        holder.tv_task_name.setText(getItem(position).getName());
        holder.tv_task_description.setText(getItem(position).getDescription());
        holder.tv_task_owner.setText(getItem(position).getOwner());

        return convertView;
    }


    private static class ViewHolder {
        public TextView tv_task_name;
        public TextView tv_task_description;
        public TextView tv_task_owner;

    }
}
