package com.jvn.musilog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jvn.musilog.data.Track;

import java.util.ArrayList;

/**
 * This Activity helps navigate "activity_playlist_editor" layout and also adds entries for
 * RecyclerView
 *
 * @author alexleyva
 * @since 2024-04-15
 */
public class PlaylistEditor extends AppCompatActivity {

  /** Button to go back to User page */
  Button backButton;

  /** Button to go to Add Song page */
  Button addButton;

  /**
   * Creates layout for RecyclerView and "activity_playlist_editor" and creates a new instance of
   * playlistTrack array
   *
   * @param savedInstanceState
   */
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Opens "activity_playlist_editor" page
    setContentView(R.layout.activity_playlist_editor);
    RecyclerView playlistRV = findViewById(R.id.playlist_editor);
    /** Creates new instance of our ArrayList */
    ArrayList<Track> playlistTrack = new ArrayList<Track>();

    // Practice entries for the playlist
    playlistTrack.add(
        Track.fromUrl("https://open.spotify.com/track/3skn2lauGk7Dx6bVIt5DVj?si=c723e889e5254fc3"));
    playlistTrack.add(Track.fromUrl("https://www.youtube.com/watch?v=Cd_THG241GA"));

    /** Initializing our adapter class and passing our arraylist to it */
    EditablePlaylistAdapter editablePlaylistAdapter =
        new EditablePlaylistAdapter(this, playlistTrack, true);

    /** Setting a layout manager for the recycler view */
    LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    // These two lines are setting layout manager and adapter to our recycler view
    playlistRV.setLayoutManager(linearLayoutManager);
    playlistRV.setAdapter(editablePlaylistAdapter);

    /** Onclick which runs "AddNewSong" class */
    addButton = (Button) findViewById(R.id.addPlaylist_Button);
    addButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(PlaylistEditor.this, AddNewSong.class);
            startActivity(intent);
          }
        });

    /** Onclick which runs "UserActivity" class */
    backButton = (Button) findViewById(R.id.goBack_Button);
    backButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(PlaylistEditor.this, UserActivity.class);
            startActivity(intent);
          }
        });
  }
}
