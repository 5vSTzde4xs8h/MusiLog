package com.jvn.musilog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.jvn.musilog.R;
import com.jvn.musilog.data.MusicSource;
import com.jvn.musilog.data.Track;
import com.jvn.musilog.data.TrackMetadata;

/**
 * This class adds functionality to the buttons and takes links that user sends
 *
 * @author alexleyva
 * @since 2024-04-20
 */

public class AddNewSong extends AppCompatActivity {
  /** Button to go back to previous page */
  Button back;

  /** Button that adds the song the user entered via link */
  Button addSong;

  /** Button that opens Youtube's website */
  Button youtubeWeb;

  /** Button that opens Spotify's website */
  Button spotifyWeb;

  /** EditText that is the link the user entered */
  EditText songLink;

  /**
   * Creates "activity_add_link" layout and adds functionality to the buttons and EditTexts
   *
   * @param savedInstanceState
   */
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_add_link);

    /** Sets the id of the buttons */
    back = (Button) findViewById(R.id.back_Button);
    addSong = (Button) findViewById(R.id.link_add_button);
    youtubeWeb = (Button) findViewById(R.id.youtube_link_button);
    spotifyWeb = (Button) findViewById(R.id.spotify_link_button);
    songLink = (EditText) findViewById(R.id.user_song_link);

    /** Onclick will start "PlaylistEditor" activity */
    back.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(AddNewSong.this, PlaylistEditor.class);
            startActivity(intent);
          }
        });

    /** Onclick will take a user's link and send it to the playlist */
    addSong.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // log to test if what user entered in editText could be sent
            Log.v("Song Link", songLink.getText().toString());
          }
        });

    /** Onclick will open Youtube's website */
    youtubeWeb.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Uri uri = Uri.parse("https://www.youtube.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
          }
        });

    /** Onclick will open Spotify's website */
    spotifyWeb.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Uri uri = Uri.parse("https://open.spotify.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
          }
        });
  }
}
