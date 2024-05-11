package com.example.assignment_librarymanagement;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class users_main extends AppCompatActivity {

    private DBHandler dbHandler;
    private RecyclerView userContainer;
    private List<Users> userList;
    private UserAdapter userAdapter;

    private Button addMem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        dbHandler = new DBHandler(users_main.this);
        if (isMembersTableEmpty()) {
            addFirstMembers();
            Toast.makeText(users_main.this, "Loading initial members...", Toast.LENGTH_LONG).show();
        }
        loadUsers();

        addMem = findViewById(R.id.user_insert_btn);

        addMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(users_main.this, AddActivity.class);
                intent.putExtra("mode", "member");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    private void addFirstMembers() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTimestamp = sdf.format(new Date(currentTimeMillis));

        dbHandler.addMembers("Amal Perera", formattedTimestamp);
        dbHandler.addMembers("Kasun Kalhara", formattedTimestamp);
        dbHandler.addMembers("Amandi Sithara", formattedTimestamp);
        dbHandler.addMembers("Gangani Gamage", formattedTimestamp);
        dbHandler.addMembers("Dimantha Ashan", formattedTimestamp);
    }

    private void loadUsers() {
        userContainer = findViewById(R.id.user_container);
        dbHandler = new DBHandler(this);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        userContainer.setLayoutManager(layoutManager);
        userContainer.setAdapter(userAdapter);

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String[] projection = {"mem_id", "name", "date"};
        Cursor cursor = db.query("members", projection, null, null, null, null, "mem_id DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int rec_id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("mem_id")));
                String id = String.valueOf(rec_id);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                Users user = new Users(id, name, date);
                userList.add(user);
            } while (cursor.moveToNext());

            cursor.close();
            userAdapter.notifyDataSetChanged();
        }

        db.close();
    }

    public boolean isMembersTableEmpty() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM members", null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }
        return true; // Assume empty if cursor is null
    }
}
