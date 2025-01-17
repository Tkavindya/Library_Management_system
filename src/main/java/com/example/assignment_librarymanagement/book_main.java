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

public class book_main extends AppCompatActivity {

    private DBHandler dbHandler;
    private RecyclerView bookContainer;
    private List<Book> bookList;
    private BookAdapter bookAdapter;

    private Button addBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(book_main.this);
        if (isRecordsTableEmpty()) {
            addFirstBooks();
            Toast.makeText(book_main.this, "Loading initial records...", Toast.LENGTH_LONG).show();
        }
        loadBooks();

        addBook = findViewById(R.id.insert_btn);

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(book_main.this, AddActivity.class);
                intent.putExtra("mode", "book");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }

    private void addFirstBooks() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTimestamp = sdf.format(new Date(currentTimeMillis));

        dbHandler.addRecord("Running in the Family", "Michael Ondaatje", "Running in the Family is a fictionalized memoir, written in post-modern style involving aspects of magic realism, by Michael Ondaatje. It deals with his return to his native island of Sri Lanka, also called Ceylon, in the late 1970s. It also deals with his family");
        dbHandler.addRecord("The Seven Moons of Maali Almeida", "Shehan Karunatilaka", "The Seven Moons of Maali Almeida is a 2022 novel by Sri Lankan author Shehan Karunatilaka. It won the 2022 Booker Prize, the announcement being made at a ceremony at the Roundhouse in London on 17 October 2022");
        dbHandler.addRecord("What Lies Between Us", "Nayomi Munaweera", "A young girl grows up carefree in the midst of her loving family in a sprawling old house in Sri Lanka. Her childhood is like any other until a cherished friendship is seen to have monstrous undertones and the consequences spell both the end of her childhood and that of her home. Ostracized by an unforgiving community, the girl and her mother seek safety by immigrating to America. Years later, when she falls in love with Daniel, it appears she has found her happily ever after.");
        dbHandler.addRecord("On Sal Mal Lane", "Ru Freeman", "Sri Lanka, 1979. The Herath family has just moved to Sal Mal Lane, a quiet street disturbed only by the cries of the children whose triumphs and tragedies sustain the families that live there. As the neighbors adapt to the newcomers in different ways, the children fill their days with cricket matches, romantic crushes, and small rivalries.");
        dbHandler.addRecord("The Road to Peradeniya", "Sir Ivor Jennings", "Autobiography of Sir William Ivor Jennings, 1903-1965, British constitutional lawyer and educationalist.");
    }

    private void loadBooks() {

        bookContainer = findViewById(R.id.book_container);
        dbHandler = new DBHandler(this);
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookContainer.setLayoutManager(layoutManager);
        bookContainer.setAdapter(bookAdapter);

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String[] projection = {"rec_id", "title", "author", "description"};
        Cursor cursor = db.query("records", projection, null, null, null, null, "rec_id DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int rec_id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("rec_id")));
                String id = String.valueOf(rec_id);
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                Book book = new Book(id, title, author, desc);
                bookList.add(book);
            } while (cursor.moveToNext());

            cursor.close();
            bookAdapter.notifyDataSetChanged();
        }

        db.close();

    }

    public boolean isRecordsTableEmpty() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM records", null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }
        return true; // Assume empty if cursor is null
    }
}

