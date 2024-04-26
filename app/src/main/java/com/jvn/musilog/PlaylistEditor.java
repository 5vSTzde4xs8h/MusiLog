package com.jvn.musilog;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jvn.musilog.data.MusicSource;
import com.jvn.musilog.data.Track;
import java.util.ArrayList;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateField;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

  Button linkToPlaylistButton;

  EditText songLink;

  String newLink;

  // Firebase String to search userId
  private String userID;

  /**
   * Creates layout for RecyclerView and "activity_playlist_editor" and creates a new instance of
   * playlistTrack array
   *
   * @param savedInstanceState
   */
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
/**
    // Created instance of firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference userPlaylist = db.collection("users").document(userID);
    ///////////

*/
    // Opens "activity_playlist_editor" page
    setContentView(R.layout.activity_playlist_editor);
    RecyclerView playlistRV = findViewById(R.id.playlist_editor);
    /** Creates new instance of our ArrayList */
    // Practice entries for the playlist
    ArrayList<Track> playlistTrack = new ArrayList<Track>();
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
    /** Onclick, takes link provided by the user and updates it to both the firebase and the playlist*/
    songLink = (EditText) findViewById(R.id.song_Link);
    linkToPlaylistButton = (Button) findViewById(R.id.addLinkToPlaylist_Button);
    linkToPlaylistButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Log.v("Song Link", songLink.getText().toString());
            newLink = songLink.getText().toString();
            playlistTrack.add(Track.fromUrl(newLink));

            /**

            // Temp firebase update//////////
            userPlaylist
                .update("source", playlistTrack)
                .addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                      }
                    })
                .addOnFailureListener(
                    new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                      }
                    });

            userPlaylist
                .update("sourceId", playlistTrack)
                .addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                      }
                    })
                .addOnFailureListener(
                    new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                      }
                    });
            /////////////////////////////////////////
*/

          }
        });
  }
}
