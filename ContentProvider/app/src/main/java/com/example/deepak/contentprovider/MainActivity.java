package com.example.deepak.contentprovider;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "ContentProviderDemo";

    private String mSelectionCluse = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = ?";

    private String[] mSelectionArguments = new String[]{"Aditya Gupta"};

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 20;

    private boolean firstTimeLoaded = false;

    private TextView textViewQueryResult;
    private Button buttonLoadData;

    private String[] mColumnProjection = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.HAS_PHONE_NUMBER};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewQueryResult = findViewById(R.id.textView);
        buttonLoadData = findViewById(R.id.button);
//        buttonLoadData.setOnClickListener(this);

/*
  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, mColumnProjection,
                    null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                StringBuilder stringBuilderQueryResult = new StringBuilder("");
                while (cursor.moveToNext()) {
                    stringBuilderQueryResult.append(cursor.getString(0) + " , " + cursor.getString(1) + " , " + cursor.getString(2) + "\n");
                }
                textViewQueryResult.setText(stringBuilderQueryResult.toString());
            } else {
                textViewQueryResult.setText("No Contacts in device");
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, mColumnProjection,
                    null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                StringBuilder stringBuilderQueryResult = new StringBuilder("");
                while (cursor.moveToNext()) {
                    stringBuilderQueryResult.append(cursor.getString(0) + " , " + cursor.getString(1) + " , " + cursor.getString(2) + "\n");
                }
                textViewQueryResult.setText(stringBuilderQueryResult.toString());
            } else {
                textViewQueryResult.setText("No Contacts in device");
            }
        }
*/
    }

    public void onClickButton(View view) {
        getLoaderManager().initLoader(1, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (i == 1) {
            return new CursorLoader(this, ContactsContract.Contacts.CONTENT_URI, mColumnProjection, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder stringBuilderQueryResult = new StringBuilder("");
            while (cursor.moveToNext()) {
                stringBuilderQueryResult.append(cursor.getString(0) + " , " + cursor.getString(1) + " , " + cursor.getString(2) + "\n");
            }
            textViewQueryResult.setText(stringBuilderQueryResult.toString());
        } else {
            textViewQueryResult.setText("No Contacts in device");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

}