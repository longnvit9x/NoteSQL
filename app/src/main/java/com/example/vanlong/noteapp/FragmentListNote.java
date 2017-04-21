package com.example.vanlong.noteapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.vanlong.adapter.CustomListView;
import com.example.vanlong.controllers.NoteController;
import com.example.vanlong.models.Note;
import com.example.vanlong.models.OnFragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vanlong on 4/13/2017.
 */

public class FragmentListNote extends Fragment {
    ListView lvShow;
    private NoteController mNoteControler;
    private CustomListView mNoteAdapter;
    private List<Note> noteList;
    boolean isDelete = false;
    private OnFragmentManager mListener;
    private ProgressDialog mpProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_note, container, false);
        lvShow = (ListView) view.findViewById(R.id.lvShow);
        addControl();
        addEvent();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentManager) {
            mListener = (OnFragmentManager) context;
        } else {
            Log.e("Warning", "must implement onViewSelected");
        }
    }

    private void addEvent() {
        lvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("day la item","dasd");
                Intent intent = new Intent(getActivity(), DetailNoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Note", noteList.get(position));
                intent.putExtra("Data", bundle);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        lvShow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i <noteList.size(); i++) {
                    noteList.get(i).setCheckbokShow(true);

                }
                mNoteAdapter.notifyDataSetChanged();
                getActivity().findViewById(R.id.btnDelete).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.btnCancel).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.btnNew).setVisibility(View.INVISIBLE);
                return true;
            }
        });
    }

    private void addControl() {
        mNoteControler = new NoteController(getActivity());
        noteList = new ArrayList<>();
        mNoteAdapter = new CustomListView(getActivity(), R.layout.item_listview_note, noteList);
        NotesTask notesTask = new NotesTask();
        notesTask.execute();
    }

    public boolean reloadData(Note note) {
        boolean isadd = false;
        try {
            noteList.add(note);
            mNoteAdapter.notifyDataSetChanged();
            if(mNoteControler.insertNote(note))
            isadd = true;
        } catch (Exception ex) {
            Log.e("Fail add data", ex.toString());
        }
        return isadd;

    }

    @Override
    public void onStart() {
        super.onStart();
}

    public boolean deleteData() {
        boolean isdelete = false;
        try {
            for (int i = 0; i < noteList.size(); i++) {
                if (noteList.get(i).isSelected()) {
                    mNoteControler.deleteData(noteList.get(i));
                    noteList.remove(i);
                    i--;
                }
            }
            cancelDeleteData();
            getActivity().findViewById(R.id.btnDelete).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.btnCancel).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.btnNew).setVisibility(View.VISIBLE);
            isdelete = true;
        } catch (Exception ex) {
            Log.e("Delete Fail", ex.toString());
        }
        return isdelete;
    }

    public void cancelDeleteData() {
        for (int i = 0; i <noteList.size(); i++) {
            noteList.get(i).setCheckbokShow(false);
        }
        mNoteAdapter.notifyDataSetChanged();
    }

    private int mCount;

    private class NotesTask extends AsyncTask<Void, Note, List<Note>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mpProgressDialog = new ProgressDialog(getActivity());
            mpProgressDialog.setTitle("Data :)");
            mpProgressDialog.setMessage("Loading...");
            mpProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mpProgressDialog.setCancelable(true);
            mpProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Note... values) {
            super.onProgressUpdate(values);
            mNoteAdapter.add(values[0]);
            lvShow.setAdapter(mNoteAdapter);
        }

        @Override
        protected void onPostExecute(List<Note> list) {
            super.onPostExecute(list);
            mpProgressDialog.dismiss();
        }

        @Override
        protected List<Note> doInBackground(Void... params) {
            List<Note> notes = new ArrayList<>();
            try {
                notes = mNoteControler.getAllData();
                for (int i = 0; i < notes.size(); i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    publishProgress(notes.get(i));
                }

            } catch (Exception ex) {
                Log.e("", ex.toString());
            }
            return notes;
        }
    }
}
