package com.example.tictactoe;

import static com.example.tictactoe.R.*;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private LinearLayout player1Layout, player2Layout;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    private TextView player1TV, player2TV;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        Toast.makeText(MainActivity.this,"Firebase connection Success",Toast.LENGTH_LONG).show();
        
        player1Layout = findViewById(id.player1Layout);
        player2Layout = findViewById(id.player2Layout);
        
        image1 = findViewById(id.image1);
        image2 = findViewById(id.image2);
        image3 = findViewById(id.image3);
        image4 = findViewById(id.image4);
        image5 = findViewById(id.image5);
        image6 = findViewById(id.image6);
        image7 = findViewById(id.image7);
        image8 = findViewById(id.image8);
        image9 = findViewById(id.image9);
    
        player1TV = findViewById(id.player1TV);
        player2TV = findViewById(id.player2TV);
        
        // getting PlayerName from PlayerName.class file
        final String getPlayerName = getIntent().getStringExtra("playerName");
    }
    
}