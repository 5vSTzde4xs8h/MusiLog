package com.jvn.musilog;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jvn.musilog.data.TrackMetadata;
import com.jvn.musilog.data.Track;
import com.jvn.musilog.util.DocumentFields;

/**
 * This activity is the adapter for the recyclerView
 *
 * @author alexleyva
 * @author Poleon Banouvong
 * @since 2024-04-18
 */
public class EditablePlaylistAdapter extends RecyclerView.Adapter<PlaylistTrackVH> {
  /** Class tag for identifying output in Logcat. */
  private static final String TAG = "EditablePlaylistAdapter";

  /** The activity the adapter is running under */
  private final AppCompatActivity activity;

  /** Our ArrayList based on the "Track" class */
  private final ArrayList<Track> playlist;

  /** Boolean to show the play button */
  private final boolean displayPlayButton;

  /** The current user's Firestore data document. */
  private final DocumentReference userDocument;

  /**
   * Shows a snackbar message.
   *
   * @param message The resource ID of the message string
   */
  private void showSnackbar(int message) {
    Snackbar.make(
            activity.findViewById(android.R.id.content).getRootView(),
            message,
            BaseTransientBottomBar.LENGTH_LONG)
        .show();
  }

  /**
   * Creates a new Playlist Adapter
   *
   * @param activity
   * @param playlist
   * @param displayPlayButton
   */
  public EditablePlaylistAdapter(
      AppCompatActivity activity, ArrayList<Track> playlist, boolean displayPlayButton) {

    this.activity = activity;
    this.playlist = playlist;
    this.displayPlayButton = displayPlayButton;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    if (currentUser == null) {
      Log.e(TAG, "User is not logged-in");
      userDocument = null;
    } else {
      userDocument = firestore.collection("users").document(currentUser.getUid());
    }
  }

  /**
   * Views layout of "activity_entry"
   *
   * @param parent
   * @param viewType
   */
  @NonNull
  @Override
  public PlaylistTrackVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // to inflate the layout for each item of recycler view
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_entry, parent, false);

    return new PlaylistTrackVH(activity, view);
  }

  /**
   * Binds values retrived from "TrackMetadata" class
   *
   * @param holder
   * @param position
   */
  @Override
  public void onBindViewHolder(@NonNull PlaylistTrackVH holder, int position) {
    // gets position of PlaylistTrack array
    Track track = playlist.get(position);

    // UI update handler
    Handler updateUIHandler = new Handler(Looper.getMainLooper());

    // Thread for network calls to be able to get info from our links
    Thread metadataThread =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                TrackMetadata trackMetadata =
                    TrackMetadata.fromMusicSource(track.getSource(), track.getSourceId());

                updateUIHandler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        TrackMetadata thisMetaData;
                        boolean hidePlayButtons = false;

                        if (trackMetadata == null) {
                          thisMetaData = new TrackMetadata("(Unknown", "(Unknown)", null);
                          hidePlayButtons = true;
                        } else {
                          thisMetaData = trackMetadata;
                        }

                        if (displayPlayButton && !hidePlayButtons) {
                          holder.setButton_Type(track.getSource(), track.getSourceId());
                        }

                        holder.setArtists_Name(thisMetaData.getArtistLine());
                        holder.setSong_Name(thisMetaData.getTitle());
                        holder.setSong_Pic(thisMetaData.getCoverArtUrl());

                        // deletes entry when button is pressed
                        holder.deleteButton.setOnClickListener(
                            new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                if (userDocument == null) {
                                  return;
                                }

                                int currentPosition = holder.getAdapterPosition();
                                Track removedTrack = playlist.remove(currentPosition);

                                userDocument
                                    .update(DocumentFields.User.PLAYLIST_FIELD, playlist)
                                    .addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                          @Override
                                          public void onSuccess(Void unused) {
                                            showSnackbar(R.string.playlist_remove_success);
                                            notifyItemRemoved(currentPosition);
                                          }
                                        })
                                    .addOnFailureListener(
                                        new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                            // replace the removed track
                                            Log.e(TAG, e.toString());
                                            showSnackbar(R.string.playlist_remove_error);
                                            playlist.add(currentPosition, removedTrack);
                                          }
                                        });
                              }
                            });
                      }
                    });
              }
            });

    metadataThread.start();
  }

  /**
   * @return PlaylistTrack's size
   */
  @Override
  public int getItemCount() {
    // this method is used for showing number of card items in recycler view
    return playlist.size();
  }
}
