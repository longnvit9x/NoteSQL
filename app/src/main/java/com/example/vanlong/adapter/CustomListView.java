package com.example.vanlong.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.vanlong.models.Note;
import com.example.vanlong.noteapp.R;

import java.util.List;

/**
 * Created by vanlong on 4/14/2017.
 */

public class CustomListView extends ArrayAdapter<Note>  {
    private List<Note> mNoteisList;
    private Activity mActivity;
    private int resource;

    public CustomListView(Activity context, int resource, List<Note> objects) {
        super(context, resource, objects);
        this.mNoteisList = objects;
        this.resource = resource;
        this.mActivity = context;
    }

    ViewHolder viewHolder = new ViewHolder();

    public class ViewHolder {
        private CheckBox cbDelete;
        private TextView txtTiTile;
        private TextView txtDateTime;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mActivity).inflate(resource, null);
            holder = new ViewHolder();

            holder.cbDelete = (CheckBox) convertView.findViewById(R.id.cbCheck_Item);
            holder.txtTiTile = (TextView) convertView.findViewById(R.id.txtTitle_Item);
            holder.txtDateTime = (TextView) convertView.findViewById(R.id.txtDate_Item);
            holder.cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Note note = (Note) buttonView.getTag();
                    note.setSelected(isChecked);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.cbDelete.setVisibility(View.GONE);
        }
        Note note = mNoteisList.get(position);
        holder.txtTiTile.setText(note.getmTitle());
        holder.txtDateTime.setText(note.getmDate());
        if (note.isCheckbokShow()){
            holder.cbDelete.setVisibility(View.VISIBLE);
        }
        else {
            holder.cbDelete.setVisibility(View.GONE);

        }
        holder.cbDelete.setChecked(note.isSelected());
        holder.cbDelete.setTag(note);
        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getPosition(Note item) {

        return super.getPosition(item);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }
}
