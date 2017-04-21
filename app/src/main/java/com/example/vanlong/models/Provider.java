package com.example.vanlong.models;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by vanlong on 4/18/2017.
 */

public class Provider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.android.provider.Notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/notes");
    public static final String DATABASE_NAME = "notes.db";
    public static final String DATABASE_TABLE = "notes";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DATE = "date";

    static final int NOTES = 1;
    static final int NOTE_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "notes", NOTES);
        uriMatcher.addURI(PROVIDER_NAME, "notes/#", NOTE_ID);
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_CREATE =
            "CREATE TABLE " + DATABASE_TABLE + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    TITLE + " TEXT NOT NULL, " +
                    CONTENT + " TEXT NOT NULL," +
                    DATE + " TEXT NOT NULL);";

    private SQLiteDatabase notesDB;

    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            // this for example, shouldn't use in future project
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(sqLiteDatabase);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        notesDB = dbHelper.getWritableDatabase();
        return !(notesDB == null);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        // s = selection
        // strings = selectionArgs
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case NOTES:
                count = notesDB.delete(DATABASE_TABLE, s, strings);
                break;
            case NOTE_ID:
                String id = uri.getPathSegments().get(1);
                count = notesDB.delete(DATABASE_TABLE, _ID + " = " + id + (!(TextUtils.isEmpty(s)) ? " AND (" + s + ")" : ""), strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case NOTES:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME;
            case NOTE_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + PROVIDER_NAME;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long rowId = notesDB.insert(DATABASE_TABLE, "", contentValues);
        if (rowId > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(DATABASE_TABLE);
        if (uriMatcher.match(uri) == NOTE_ID) {
            sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = TITLE;
        }

        Cursor c = sqlBuilder.query(notesDB, projection, selection, selectionArgs, null, null, sortOrder);
        // --- register to watch a content URI for changes ---
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case NOTES:
                count = notesDB.update(DATABASE_TABLE, contentValues, selection, selectionArgs);
                break;
            case NOTE_ID:
                count = notesDB.update(DATABASE_TABLE, contentValues, _ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
