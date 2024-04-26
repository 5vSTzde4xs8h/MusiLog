package com.jvn.musilog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jvn.musilog.data.Track;
import com.jvn.musilog.data.User;
import com.jvn.musilog.view.PlaylistAdapter;

import java.util.*;

/**
 * The Activity for User's homepage
 *
 * @author Alex Leyva
 * @author Poleon Banouvong
 * @since 2024-04-06
 */
public class UserActivity extends AppCompatActivity {
  /** Class tag for identifying output in Logcat. */
  private static final String TAG = "UserActivity";

  /** The Firestore instance. */
  private FirebaseFirestore firestore;

  /** The Firebase Auth instance. */
  private FirebaseAuth firebaseAuth;

  /** The RecyclerView for viewing the user's playlist. */
  private RecyclerView playlistPreview;

  /** The Button that will navigate user from this activity(User's Homepage) to Playlist Editor */
  Button editorButton;

  /**
   * Initializes (or creates) the the activity and UI bindings to help navigate around the app
   *
   * @param savedInstanceState stores a bit of data to recreate UI reinitialized
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_user);

    firestore = FirebaseFirestore.getInstance();
    firebaseAuth = FirebaseAuth.getInstance();

    // bind views
    editorButton = (Button) findViewById(R.id.EditPlaylist_Button);
    playlistPreview = findViewById(R.id.PlaylistPreview);

    // button to take the user to edit their playlist
    editorButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(UserActivity.this, PlaylistEditor.class);
            startActivity(intent);
          }
        });
  }

  /**
   * Populates the user's playlist view when the activity (re)starts.
   */
  @Override
  protected void onStart() {
    super.onStart();

    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    // user must be authenticated
    if (currentUser == null) {
      Log.e(TAG, "User is not logged-in");
      finish();
      return;
    }

    // get user playlist
    DocumentReference userDocument = firestore.collection("users").document(currentUser.getUid());

    userDocument
        .get()
        .addOnSuccessListener(
            new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                // check if the document could be cast to User
                if (user == null) {
                  Log.e(TAG, "Unable to cast User object; check the document fields");
                  finish();
                  return;
                }

                ArrayList<Track> playlist = user.getPlaylist();

                if (playlist == null) {
                  playlist = new ArrayList<Track>();
                }

                PlaylistAdapter playlistAdapter =
                    new PlaylistAdapter(UserActivity.this, playlist, false);

                playlistPreview.setLayoutManager(new LinearLayoutManager(UserActivity.this));
                playlistPreview.setAdapter(playlistAdapter);
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
