package com.example.sqlrecupe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;


class MyDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_TABLE_NAME = "mydatabase";
    private static final String PKEY = "pkey";
    private static final String COL1 = "col1";

    MyDatabase(Context context) {
        super(context, DATABASE_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String DATABASE_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                PKEY + " INTEGER PRIMARY KEY," +
                COL1 + " TEXT);";
        db.execSQL(DATABASE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertData(String s)
    {
        Log.i("JFL"," Insert in database");
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(COL1, s);
        db.insertOrThrow(DATABASE_TABLE_NAME,null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public Cursor query (boolean distinct, String table, String[] columns,
                         String selection, String[] selectionArgs, String groupBy,
                         String having, String orderBy, String limit){return null;}

    @SuppressLint("Range")
    public void readData(ArrayList<String> Names,ArrayAdapter arrayAdapter)
    {
        Log.i("JFL", "Reading database...");
        String select = new String("SELECT * from " + DATABASE_TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        Log.i("JFL", "Number of entries: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int i = 0;
            do {
                Log.i("JFL", "Reading: " + cursor.getString(cursor.getColumnIndex(COL1)));
            Names.add(cursor.getString(cursor.getColumnIndex(COL1))) ;


            } while (cursor.moveToNext());
        }

    }


}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyDatabase mydb = new MyDatabase(this);
        Button button = findViewById(R.id.button);
        ListView listView = (ListView)findViewById(R.id.listView);
        ArrayList<String> Names = new ArrayList<String>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,Names);
        listView.setAdapter(arrayAdapter);
       // TextView text=findViewById(R.id.textView2);
        EditText input1 = findViewById(R.id.editTextTextPersonName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Names.clear();
                String W = input1.getText().toString();
                if(TextUtils.isEmpty(W))
                {
                input1.setError("The Name cannot be empty !");
                }
                else {
                    mydb.insertData(W);
                    mydb.readData(Names,arrayAdapter);
                    listView.setAdapter(arrayAdapter);
                }


            }




        });
    }
}
