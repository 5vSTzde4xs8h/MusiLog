package com.jvn.musilog;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
import com.jvn.musilog.data.Rating;
import com.jvn.musilog.data.Track;
import com.jvn.musilog.data.User;
import com.jvn.musilog.util.DocumentFields;
import com.jvn.musilog.view.PlaylistAdapter;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * The activity for viewing a user's page.
 *
 * <p>To initialise this activity, the intent must contain the user's ID in the extra data key
 * {@code userId}. See {@link Intent#putExtra(String, String)}.
 *
 * @author Poleon Banouvong
 * @since 2024-04-05
 */
public class OtherUserActivity extends AppCompatActivity {
  /** Class tag for identifying output in Logcat. */
  private static final String TAG = "OtherUserActivity";

  /** The Firebase Auth instance. */
  private FirebaseAuth firebaseAuth;

  /** The Firestore query for the user's data. */
  private DocumentReference userDocument;

  /** The Firestore query for the currently-authenticated user's rating of the user's playlist. */
  private DocumentReference thisUserRatingDocument;

  /**
   * The Firestore query for the number of ratings and the average rating of the user's playlist.
   */
  private AggregateQuery ratingQuery;

  /** The TextView representing the description of the user's playlist. */
  private TextView playlistDescriptionView;

  /** The TextView representing the average rating info of the user's playlist. */
  private TextView ratingTextView;

  /** The RecyclerView containing the list of track in the user's playlist. */
  private RecyclerView playlistView;

  /**
   * The MaterialRatingBar representing the currently-authenticated user's rating of the user's
   * playlist.
   */
  private MaterialRatingBar thisUserRatingBar;

  /** The ID of the user whose data will be retrieved. */
  private String userId;

  /** The current rating of the currently-authenticated user's rating of the user's playlist. */
  private float lastRating;

  /** Listener to be invoked when a Firestore query necessary for the activity fails. */
  private final OnFailureListener criticalQueryFailureListener =
      new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.e(TAG, e.toString());
          finish();
        }
      };

  /**
   * Populates the playlist view.
   *
   * @param playlist The list of tracks to populate the playlist View with
   */
  private void setupPlaylistView(ArrayList<Track> playlist) {
    if (playlist == null) {
      playlist = new ArrayList<Track>();
    }

    PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, playlist, true);
    playlistView.setLayoutManager(new LinearLayoutManager(this));
    playlistView.setAdapter(playlistAdapter);
  }

  /** Queries Firestore for rating information and updates the rating text view. */
  private void updateRatingText() {
    ratingQuery
        .get(AggregateSource.SERVER)
        .addOnSuccessListener(
            new OnSuccessListener<AggregateQuerySnapshot>() {
              @Override
              public void onSuccess(AggregateQuerySnapshot aggregateQuerySnapshot) {
                long numRatings = aggregateQuerySnapshot.get(AggregateField.count());

                Double averageRating =
                    aggregateQuerySnapshot.get(
                        AggregateField.average(DocumentFields.Rating.RATING_FIELD));

                if (averageRating == null) {
                  averageRating = 0.0;
                }

                String ratingString = getString(R.string.average_rating, averageRating, numRatings);
                ratingTextView.setText(ratingString);
              }
            })
        .addOnFailureListener(criticalQueryFailureListener);
  }

  /** Initialises the rating bar and its functionality. */
  private void setupRatingBar() {
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    if (currentUser == null) {
      Log.e(TAG, "Could not retrieve user when setting up rating bar");
      finish();
      return;
    }

    if (currentUser.getUid().equals(userId)) {
      // the user should not be able to rate their own playlist
      thisUserRatingBar.setVisibility(View.GONE);
    } else {
      // listen for rating bar changes
      thisUserRatingBar.setOnRatingBarChangeListener(
          new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
              if ((!fromUser) || (ratingBar != thisUserRatingBar)) {
                return;
              }

              Task<Void> operation;

              if (rating == 0) {
                // 0 is the same as no rating
                operation = thisUserRatingDocument.delete();
              } else {
                operation = thisUserRatingDocument.set(new Rating(rating));
              }

              operation
                  .addOnSuccessListener(
                      new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                          lastRating = rating;
                          updateRatingText();
                        }
                      })
                  .addOnFailureListener(
                      new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                          // reset the rating on the bar to the previous value
                          thisUserRatingBar.setRating(lastRating);

                          Snackbar.make(
                                  findViewById(R.id.main),
                                  R.string.rating_set_error,
                                  BaseTransientBottomBar.LENGTH_LONG)
                              .show();
                        }
                      });
            }
          });
    }
  }

  /**
   * Initialise the activity and UI bindings.
   *
   * @param savedInstanceState If the activity is being re-initialized after previously being shut
   *     down then this Bundle contains the data it most recently supplied in {@link
   *     #onSaveInstanceState}. <b><i>Note: Otherwise it is null.</i></b>
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_other_user);

    ViewCompat.setOnApplyWindowInsetsListener(
        findViewById(R.id.main),
        (v, insets) -> {
          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
          return insets;
        });

    // bind views
    playlistDescriptionView = findViewById(R.id.playlistDescription);
    ratingTextView = findViewById(R.id.ratingText);
    playlistView = findViewById(R.id.playlist);
    thisUserRatingBar = findViewById(R.id.userRating);

    // make the playlist description scrollable
    playlistDescriptionView.setMovementMethod(new ScrollingMovementMethod());

    // get the user ID
    Bundle extraData = getIntent().getExtras();

    if (extraData == null) {
      Log.e(TAG, "Intent did not contain userId");
      finish();
      return;
    }

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    firebaseAuth = FirebaseAuth.getInstance();

    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    userId = extraData.getString("userId");

    if ((userId == null) || (currentUser == null)) {
      finish();
      return;
    }

    userDocument = firestore.collection("users").document(userId);

    CollectionReference userRatingCollection = userDocument.collection("ratings");
    thisUserRatingDocument = userRatingCollection.document(currentUser.getUid());

    // the number of ratings and the average rating of the user's playlist
    // https://firebase.google.com/docs/firestore/query-data/aggregation-queries
    ratingQuery =
        userRatingCollection.aggregate(
            AggregateField.count(), AggregateField.average(DocumentFields.Rating.RATING_FIELD));
  }

  /** Retrieve the user's data and set up UI events. */
  @Override
  protected void onStart() {
    super.onStart();

    // setup rating bar
    setupRatingBar();

    // get the playlist rating information
    updateRatingText();

    // get the user's data
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

                playlistDescriptionView.setText(user.getPlaylistDescription());
                setupPlaylistView(user.getPlaylist());
              }
            })
        .addOnFailureListener(criticalQueryFailureListener);

    // get the current user's rating for the playlist
    thisUserRatingDocument
        .get()
        .addOnSuccessListener(
            new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                float rating;

                if (!documentSnapshot.exists()) {
                  rating = 0;
                } else {
                  Double ratingDouble =
                      documentSnapshot.getDouble(DocumentFields.Rating.RATING_FIELD);

                  if (ratingDouble == null) {
                    rating = 0;
                  } else {
                    rating = ratingDouble.floatValue();
                  }
                }

                lastRating = rating;
                thisUserRatingBar.setRating(lastRating);
              }
            })
        .addOnFailureListener(criticalQueryFailureListener);
  }
}
