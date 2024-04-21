package com.jvn.musilog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

// This class is the logic for the playlist
public class PlaylistEditor extends AppCompatActivity{
    //temp entries for the playlist
    String []data = {"New Entry","New Entry", "New Entry"};
    //initialized integer counter
    int counter = 0;
    //initialized button backButton
    Button backButton;

    // This runs at the start of "activity_playlist_editor" page
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Opens "activity_playlist_editor" page
        setContentView(R.layout.activity_playlist_editor);

        //Created a Linked List called "entries"
        List<String> entries = new LinkedList<>();
        //The first entry to show when opening "activity_playlist_editor" page
        entries.add("New Entry");

        //Sets the recycler view called "playlist_editor" as "recyclerView"
        RecyclerView recyclerView = findViewById(R.id.playlist_editor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //creates a new instance of "EditablePlaylistAdapter" class called adapter
        EditablePlaylistAdapter adapter = new EditablePlaylistAdapter(entries);
        recyclerView.setAdapter(adapter);

        //On click listener that creates a new entry when the "addPlaylist_Button" is pressed
        findViewById(R.id.addPlaylist_Button).setOnClickListener(view -> {
           entries.add(data[counter%3]);
           counter++;
           adapter.notifyItemInserted(entries.size()-1);
           
        });

        //Sets "goBack_Button" to "backButton"
        backButton = (Button)findViewById(R.id.goBack_Button);

        //On click listener that starts the "UserActivity" Class which would return the app from
        // "activity_playlist_editor" page to "activity_user" page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistEditor.this, UserActivity.class);
                startActivity(intent);
            }
        });



    }

}
