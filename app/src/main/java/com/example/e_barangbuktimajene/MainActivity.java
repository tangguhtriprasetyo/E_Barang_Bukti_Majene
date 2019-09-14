package com.example.e_barangbuktimajene;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // Root Database Name for Firebase Database.
    public static final String Database_Path_Process = "On_Process_Item";
    public static final String Database_Path_Taken = "Taken_Item";
    public static final String Database_Path_Return = "Return_Item";

    CardView process, returned, taken, add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        process = findViewById(R.id.card_view1);
        taken = findViewById(R.id.card_view2);
        returned = findViewById(R.id.card_view3);
        add = findViewById(R.id.card_view4);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadImageActivity.class);
                startActivity(intent);
            }
        });

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, DisplayStatusProcess.class);
                startActivity(intent);

            }
        });

        taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, DisplayStatusTaken.class);
                startActivity(intent);

            }
        });

        returned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, DisplayStatusReturned.class);
                startActivity(intent);

            }
        });


    }
}
