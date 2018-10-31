package com.example.wiki.assign6;

import android.arch.core.executor.TaskExecutor;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.List;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private ListView listView_contacts;
    private TextView textView_contacts;
    private final static int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private int CONTACTS_COUNT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        listView_contacts = findViewById(R.id.lv_contacts);
        textView_contacts = findViewById(R.id.contacts_count);
        readContacts();
    }

    private void readContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            List<String> contacts = getContactNames();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
            listView_contacts.setAdapter(adapter);
            textView_contacts.setText(""+CONTACTS_COUNT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                readContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private List<String> getContactNames() {
        List<String> contacts = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Get the contacts name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(name);
                CONTACTS_COUNT++;
            } while (cursor.moveToNext());
        }
        cursor.close();

        return contacts;
    }
}

