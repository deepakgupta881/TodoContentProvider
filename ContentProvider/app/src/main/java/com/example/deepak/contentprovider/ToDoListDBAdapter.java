package com.example.deepak.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ToDoListDBAdapter {


    private static final String TAG = ToDoListDBAdapter.class.getSimpleName();

    public static final String DB_NAME = "todolist.db";
    public static final int DB_VERSION = 2;

    public static final String TABLE_TODO = "table_todo";
    public static final String COLUMN_TODO_ID = "task_id";
    public static final String COLUMN_TODO = "todo";
    public static final String COLUMN_PLACE = "place";

    //create table table_todo(task_id integer primary key, todo text not null);

    public static String CREATE_TABLE_TODO = "CREATE TABLE " + TABLE_TODO + "(" + COLUMN_TODO_ID + " INTEGER PRIMARY KEY, " + COLUMN_TODO + " TEXT NOT NULL, " +
            COLUMN_PLACE + " TEXT )";

    private Context context;
    private SQLiteDatabase sqLliteDatabase;
    private static ToDoListDBAdapter toDoListDBAdapterInstance;


    private ToDoListDBAdapter(Context context) {
        this.context = context;
        sqLliteDatabase = new ToDoListDBHelper(this.context, DB_NAME, null, DB_VERSION).getWritableDatabase();
    }

    public static ToDoListDBAdapter getToDoListDBAdapterInstance(Context context) {
        if (toDoListDBAdapterInstance == null) {
            toDoListDBAdapterInstance = new ToDoListDBAdapter(context);
        }
        return toDoListDBAdapterInstance;
    }

    //Will be used in the content provider
    public Cursor getCursorsForAllToDos() {
        Cursor cursor = sqLliteDatabase.query(TABLE_TODO, new String[]{COLUMN_TODO_ID, COLUMN_TODO, COLUMN_PLACE}, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor getCursorForSpecificPlace(String place) {
        //SELECT task_id,todo FROM table_todo WHERE place LIKE '%desk%'
        Cursor cursor = sqLliteDatabase.query(TABLE_TODO, new String[]{COLUMN_TODO_ID, COLUMN_TODO}, COLUMN_PLACE + " LIKE '%" + place + "%'", null, null, null, null, null);
        return cursor;
    }

    public Cursor getCount() {
        Cursor cursor = sqLliteDatabase.rawQuery("SELECT COUNT(*) FROM " + TABLE_TODO, null);
        return cursor;
    } //insert,delete,modify,query methods

    public boolean insert(String toDoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TODO, toDoItem);

        return sqLliteDatabase.insert(TABLE_TODO, null, contentValues) > 0;
    }

    public boolean insert(String toDoItem, String place) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TODO, toDoItem);
        contentValues.put(COLUMN_PLACE, place);

        return sqLliteDatabase.insert(TABLE_TODO, null, contentValues) > 0;
    } //Will be used in the content provider

    public long insert(ContentValues contentValues) {
        return sqLliteDatabase.insert(TABLE_TODO, null, contentValues);
    }



    //Will be used by the provider
    public int delete(String whereClause, String[] whereValues) {
        return sqLliteDatabase.delete(TABLE_TODO, whereClause, whereValues);
    }

    public boolean delete(int taskId) {
        return sqLliteDatabase.delete(TABLE_TODO, COLUMN_TODO_ID + " = " + taskId, null) > 0;
    }

    public boolean modify(int taskId, String newToDoItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TODO, newToDoItem);

        return sqLliteDatabase.update(TABLE_TODO, contentValues, COLUMN_TODO_ID + " = " + taskId, null) > 0;
    }

    //Will be used in the content provider
    public int update(ContentValues contentValues, String s, String[] strings) {
        return sqLliteDatabase.update(TABLE_TODO, contentValues, s, strings);
    }

    public List<Todo> getAllToDos() {
        List<Todo> toDoList = new ArrayList<Todo>();

        Cursor cursor = sqLliteDatabase.query(TABLE_TODO, new String[]{COLUMN_TODO_ID, COLUMN_TODO, COLUMN_PLACE}, null, null, null, null, null, null);

        if (cursor != null & cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Todo toDo = new Todo(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
                toDoList.add(toDo);

            }
        }
        cursor.close();
        return toDoList;
    }

    private static class ToDoListDBHelper extends SQLiteOpenHelper {

        public ToDoListDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
            Log.i(TAG, "Inside onConfigure");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_TODO);
            Log.i(TAG, "Inside onCreate");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
