package com.jvn.musilog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jvn.musilog.data.MusicSource;
import com.jvn.musilog.data.Track;
import com.jvn.musilog.data.User;
import com.jvn.musilog.util.DocumentFields;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * This Activity helps navigate "activity_playlist_editor" layout and also adds entries for
 * RecyclerView
 *
 * @author alexleyva
 * @author Poleon Banouvong
 * @since 2024-04-15
 */
public class PlaylistEditor extends AppCompatActivity {
  /** Class tag for identifying output in Logcat. */
  private static final String TAG = "PlaylistEditor";

  /** The Firestore instance. */
  private FirebaseFirestore firestore;

  /** The Firebase Auth instance. */
  private FirebaseAuth firebaseAuth;

  /** Button to go back to User page */
  private Button backButton;

  /** Button to go to Add Song page */
  private Button addButton;

  /** Button to add a new song to the user's playlist. */
  private Button linkToPlaylistButton;

  /** The EditText for the song URL to be added. */
  private EditText songLink;

  /** The RecyclerView for the user's playlist. */
  private RecyclerView playlist;

  /** The adapter for an editable playlist. */
  private EditablePlaylistAdapter editablePlaylistAdapter;

  /** The Firestore document for the current user's data. */
  DocumentReference userDocument;

  /** The current working playlist. */
  LinkedHashSet<Track> workingPlaylist = null;

  /** The user's playlist as an array. */
  ArrayList<Track> playlistArray = new ArrayList<Track>();

  /**
   * Shows a snackbar message
   *
   * @param message The resource ID of the message string
   */
  private void showSnackbar(int message) {
    Snackbar.make(findViewById(R.id.main), message, BaseTransientBottomBar.LENGTH_LONG).show();
  }

  /**
   * Creates layout for RecyclerView and "activity_playlist_editor" and creates a new instance of
   * playlistTrack array
   *
   * @param savedInstanceState If the activity is being re-initialized after previously being shut
   *     down then this Bundle contains the data it most recently supplied in {@link
   *     #onSaveInstanceState}. <b><i>Note: Otherwise it is null.</i></b>
   */
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_playlist_editor);

    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });

    // get Firebase instances
    firestore = FirebaseFirestore.getInstance();
    firebaseAuth = FirebaseAuth.getInstance();

    // bind views
    addButton = findViewById(R.id.addPlaylist_Button);
    backButton = findViewById(R.id.goBack_Button);
    songLink = findViewById(R.id.song_Link);
    linkToPlaylistButton = findViewById(R.id.addLinkToPlaylist_Button);
    playlist = findViewById(R.id.playlist_editor);

    editablePlaylistAdapter = new EditablePlaylistAdapter(this, playlistArray, true);
    playlist.setLayoutManager(new LinearLayoutManager(this));
    playlist.setAdapter(editablePlaylistAdapter);

    // get user document
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    if (currentUser == null) {
      Log.e(TAG, "User is not logged-in");
      finish();
      return;
    }

    userDocument = firestore.collection("users").document(currentUser.getUid());
  }

  @Override
  protected void onStart() {
    super.onStart();
    workingPlaylist = null;

    // directs to an activity on how to add a song
    addButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(PlaylistEditor.this, AddNewSong.class);
            startActivity(intent);
          }
        });

    // go to UserActivity
    backButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(PlaylistEditor.this, UserActivity.class);
            startActivity(intent);
          }
        });

    // add a new song to the user's playlist
    linkToPlaylistButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            // we can't add songs if we don't have the current playlist
            if (workingPlaylist == null) {
              return;
            }

            String songUrl = songLink.getText().toString().trim();

            if (songUrl.isEmpty()) {
              showSnackbar(R.string.playlist_editor_empty_url);
              return;
            }

            Track newTrack = Track.fromUrl(songUrl);

            if (newTrack.getSource() == MusicSource.Unknown) {
              return;
            }

            boolean songAdded = workingPlaylist.add(newTrack);

            if (!songAdded) {
              showSnackbar(R.string.playlist_add_duplicate);
              return;
            }

            // update the user's data
            int playlistInsertIndex = playlistArray.size();
            playlistArray.add(newTrack);

            userDocument
                .update(DocumentFields.User.PLAYLIST_FIELD, playlistArray)
                .addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void unused) {
                        showSnackbar(R.string.playlist_add_success);
                        editablePlaylistAdapter.notifyItemInserted(playlistInsertIndex);
                      }
                    })
                .addOnFailureListener(
                    new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        showSnackbar(R.string.playlist_add_error);
                      }
                    });
          }
        });

    // get user playlist data
    userDocument
        .get()
        .addOnSuccessListener(
            new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                // check if the document could be cast to User
                if (user == null) {
                  finish();
                  Log.e(TAG, "Unable to cast User object; check the document fields");
                  return;
                }

                ArrayList<Track> currentPlaylist = user.getPlaylist();

                // retrieve the user's current playlist
                if (currentPlaylist == null) {
                  workingPlaylist = new LinkedHashSet<Track>();
                } else {
                  workingPlaylist = new LinkedHashSet<Track>(currentPlaylist);
                }

                playlistArray.clear();
                playlistArray.addAll(workingPlaylist);
                editablePlaylistAdapter.notifyDataSetChanged();
              }
            })
        .addOnFailureListener(
            new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
                finish();
              }
            });
  }
}
