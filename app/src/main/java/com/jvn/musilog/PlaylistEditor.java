package com.jvn.musilog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import java.util.LinkedList;
import java.util.List;

public class PlaylistEditor extends AppCompatActivity{
    //temp entries for the playlist
    String []data = {"New Entry","New Entry", "New Entry"};
    int counter = 0;
    Button backButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_editor);

        List<String> entries = new LinkedList<>();
        entries.add("New Entry");

        RecyclerView recyclerView = findViewById(R.id.playlist_editor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Playlist_Adapter adapter = new Playlist_Adapter(entries);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.addPlaylist_Button).setOnClickListener(view -> {
           entries.add(data[counter%3]);
           counter++;
           adapter.notifyItemInserted(entries.size()-1);
        });

        backButton = (Button)findViewById(R.id.goBack_Button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistEditor.this, UserActivity.class);
                startActivity(intent);
            }
        });

    }

}
