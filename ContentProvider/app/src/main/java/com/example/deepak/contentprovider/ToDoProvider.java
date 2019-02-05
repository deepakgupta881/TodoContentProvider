package com.example.deepak.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class ToDoProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.deepak.contentprovider";

    public static final String PATH_TODO_LIST = "TODO_LIST";
    public static final String PATH_TODO_PLACE = "TODO_LIST_FROM_PLACE";
    public static final String PATH_TODO_COUNT = "TODOS_COUNT";

    public static final Uri CONTENT_URI_1 = Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_LIST);
    public static final Uri CONTENT_URI_2 = Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_PLACE);
    public static final Uri CONTENT_URI_3 = Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_COUNT);


    public static final String MIME_TYPE_1 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.com.example.deepak.contentprovider.todos";
    public static final String MIME_TYPE_2 = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + "vnd.com.example.deepak.contentprovider.place";
    public static final String MIME_TYPE_3 = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + "vnd.com.example.deepak.contentprovider.todocount";


    public static final int TODOS_LIST = 1;
    public static final int TODOS_FROM_SPECIFIC_PLACE = 2;
    public static final int TODOS_COUNT = 3;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, PATH_TODO_LIST, 1);
        MATCHER.addURI(AUTHORITY, PATH_TODO_PLACE, 2);
        MATCHER.addURI(AUTHORITY, PATH_TODO_COUNT, 3);
    }

    private ToDoListDBAdapter toDoListDBAdapter;

    @Override
    public boolean onCreate() {
        toDoListDBAdapter = ToDoListDBAdapter.getToDoListDBAdapterInstance(getContext());
        return false;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                cursor = toDoListDBAdapter.getCursorsForAllToDos();
                break;
            case TODOS_FROM_SPECIFIC_PLACE:
                cursor = toDoListDBAdapter.getCursorForSpecificPlace(selectionArgs[0]);
                break;
            case TODOS_COUNT:
                cursor = toDoListDBAdapter.getCount();
                break;
        }

        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                return MIME_TYPE_1;
            case TODOS_FROM_SPECIFIC_PLACE:
                return MIME_TYPE_2;
            case TODOS_COUNT:
                return MIME_TYPE_3;
        }


        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri uri1 = null;
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                uri1 = insertToDo(uri, values);
                break;
        }
        return uri1;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int del = -1;
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                del = delete(selection, selectionArgs);
                break;
        }
        return del;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int update = 0;
        switch (MATCHER.match(uri)) {
            case TODOS_LIST:
                update = toDoListDBAdapter.update(values, selection, selectionArgs);
                break;
        }
        return update;
    }


    private Uri insertToDo(Uri uri, ContentValues contentValues) {
        long id = toDoListDBAdapter.insert(contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("content://" + AUTHORITY + "/" + PATH_TODO_LIST + "/" + id);
    }

    private int delete(String whereClause, String[] whereValues) {
        return toDoListDBAdapter.delete(whereClause, whereValues);
    }

}
