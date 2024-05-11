// Corrected menuactivity.java
package com.example.assignment_librarymanagement;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class menuactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuactivity);

        BottomNavigation();
    }

    private void BottomNavigation() {
        LinearLayout btnbooks = findViewById(R.id.btnbooks);
        btnbooks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(menuactivity.this, book_main.class));
            }
        });
    }


}
