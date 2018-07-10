package com.kakiridev.simplyteamtasker.groupEvent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kakiridev.simplyteamtasker.R;

import java.util.List;


public class GroupListViewAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;

    public GroupListViewAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
        Log.v("DTAG", "Class name: WordListViewAdapter Method name: WordListViewAdapter");

        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("DTAG", "Class name: " + Thread.currentThread().getStackTrace()[2].getClassName() + " Method name: " + Thread.currentThread().getStackTrace()[2].getMethodName());

        ViewHolder holder;

        if(convertView == null) { //tylko w przypadku gdy layout nie istnieje to go tworzymy
            convertView = mInflater.inflate(R.layout.group_listview_row, null);
            holder = new ViewHolder();
            holder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_group_name.setText(getItem(position));

        return convertView;
    }


    private static class ViewHolder{
        public TextView tv_group_name;

    }


}

