package com.example.tictactoe;

import static com.example.tictactoe.R.*;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private LinearLayout player1Layout, player2Layout;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    private TextView player1TV, player2TV, player1pointTV,player2pointTV, tieTV;
    
    // winning combinations
    private final List<int[]> combinationsList = new ArrayList<>();
    
    // player unique ID
    private String playerUniqueId = "0";
    
    // getting firebase database reference from URL
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tictactoe-ce6a0-default-rtdb.firebaseio.com/");
    
    // true when opponent will  be found to play the game
    private boolean opponentFound = false;
    
    // opponent unique ID
    private String opponentUniqueId = "0";
    
    // values must be matching or waiting
    // when a player creates a new connection/room and he's waiting for another player to join then the value will be waiting
    private String status = "matching";
    
    // player turn
    private String playerTurn = "";
    
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
    
        player1pointTV = findViewById(id.player1pointTV);
        player2pointTV = findViewById(id.player2pointTV);
    
        tieTV = findViewById(id.tieTV);
        
        
        // getting PlayerName from PlayerName.class file
        final String getPlayerName = getIntent().getStringExtra("playerName");
        
        
        // generating winning combinations
        // horizontal wins
        combinationsList.add(new int[]{0,1,2});
        combinationsList.add(new int[]{3,4,5});
        combinationsList.add(new int[]{6,7,8});
        
        // vertical wins
        combinationsList.add(new int[]{0,3,6});
        combinationsList.add(new int[]{1,4,7});
        combinationsList.add(new int[]{2,5,8});
    
        // diagonal wins
        combinationsList.add(new int[]{0,4,8});
        combinationsList.add(new int[]{2,4,6});
    
        // showing progress dialog while waiting for opponent
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Waiting for Opponent");
        progressDialog.show();
        
        
        // generate player unique id, Player will be identified by this id
        playerUniqueId = String.valueOf(System.currentTimeMillis());
        
        // setting the player name to the TextView
        player1TV.setText(getPlayerName);
        
        databaseReference.child("connections").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // check if opponent found or not, if not then look for the opponent
                if (opponentFound)
                {
                    
                    // checking if there are other players in the firebase realtime database
                    if (snapshot.hasChildren())
                    {
                        // checking all connections if other users are also waiting for a user to play the match
                        for (DataSnapshot connections : snapshot.getChildren())
                        {
                            // getting connection unique ID
                            long conId = Long.parseLong(connections.getKey());
                            
                            // 2 players are required to start the game
                            // if getPlayersCount is 1 it means that there is another player waiting for an opponent to play the game
                            // else if getPlayersCount is 2 it means this connection was established between the 2 players
                            int getPlayersCount = (int)connections.getChildrenCount();
                            
                            // after creating a new connection waiting for the other player to join
                            if (status.equals("waiting"))
                            {
                                // if getPlayersCount is 2 means other player joined the match
                                if (getPlayersCount == 2)
                                {
                                    playerTurn = playerUniqueId;
                                    applyPlayerTurn(playerTurn);
                                }
                            }
                        }
                    }
                }
                
                // if there is no connection available in the firebase database then create a new connection
                // it is like creating a new room and waiting for another player to join the room
                else
                {
                    // generating unique ID for the connection
                    String connectionUniqueId = String.valueOf(System.currentTimeMillis());
                    
                    // adding first player to the connection and waiting for another player to connect and play
                    snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                    
                    status = "waiting";
                }
            }
    
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
        
            }
        });
    }
    
    private void applyPlayerTurn(String playerUniqueId2)
    {
        if (playerUniqueId2.equals(playerUniqueId))
        {
            // check backgrounds again
            player1Layout.setBackgroundResource(drawable.transparent_background);
            player2Layout.setBackgroundResource(drawable.transparent_background);
        }
        else
        {
            // check backgrounds again
            player2Layout.setBackgroundResource(drawable.transparent_background);
            player1Layout.setBackgroundResource(drawable.transparent_background);
        }
    }
    
}