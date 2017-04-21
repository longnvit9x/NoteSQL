package com.example.vanlong.controllers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.vanlong.models.Note;
import com.example.vanlong.models.Provider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vanlong on 4/13/2017.
 */

public class NoteController {
    private Context mContext;
    private ContentResolver mContentResolver;

    public NoteController(Context Context) {
        this.mContext = Context;
        this.mContentResolver = mContext.getContentResolver();
    }

    public boolean insertNote(Note note) {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(Provider.TITLE, note.getmTitle());
        contentvalues.put(Provider.CONTENT, note.getmContent());
        contentvalues.put(Provider.DATE, note.getmDate());
        boolean ischeck = false;
        try {
            mContentResolver.insert(Provider.CONTENT_URI, contentvalues);
            return true;
        } catch (Exception ex) {
            Log.e("Insert:  ", ex.toString());
        }

        return false;
    }

    public List<Note> getAllData() {
        try {
            List<Note> noteList = new ArrayList<>();
            Cursor cursor = mContentResolver.query(Provider.CONTENT_URI, null, null, null, null);
            Note note;
            while (cursor.moveToNext()) {

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String date = cursor.getString(3);
                note = new Note();
                note.setmId(id);
                note.setmTitle(title);
                note.setmContent(content);
                note.setmDate(date);
                noteList.add(note);
            }
            return noteList;
        } catch (Exception ex) {
            Log.e("Get:  ", ex.toString());
        }

        return null;
    }

    public boolean updateData(Note note) {
        Uri uri= Uri.parse(Provider.CONTENT_URI+"/"+note.getmId());
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(Provider.TITLE, note.getmTitle());
        contentvalues.put(Provider.CONTENT, note.getmContent());
        contentvalues.put(Provider.DATE, note.getmDate());
        boolean isupdate= false;
        try {
            int i=  mContentResolver.update(uri, contentvalues, null, null);
            if (i>0) isupdate=true;
        } catch (Exception ex) {
            Log.e("update:  ", ex.toString());
        }

        return isupdate;
    }

    public boolean deleteData(Note note) {
        Uri uri= Uri.parse(Provider.CONTENT_URI+"/"+note.getmId());
       boolean isdelete= false;
        try {
          int i=  mContentResolver.delete(uri, null, null);
        if (i>0) isdelete=true;

        } catch (Exception ex) {
            Log.e("delete:  ", ex.toString());
        }

        return isdelete;

    }

}
