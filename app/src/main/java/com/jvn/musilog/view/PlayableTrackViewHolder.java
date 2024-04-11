package com.jvn.musilog.view;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jvn.musilog.R;
import com.jvn.musilog.data.MusicSource;
import com.jvn.musilog.util.AppAvailability;

/**
 * The ViewHolder for the view that represents a playable music track.
 *
 * @author Poleon Banouong
 * @since 2024-04-06
 */
public class PlayableTrackViewHolder extends RecyclerView.ViewHolder {
  /** The URL format for Spotify tracks. */
  private static final String SPOTIFY_TRACK_URL = "https://open.spotify.com/track/";

  /** The URL for YouTube tracks. */
  private static final String YOUTUBE_TRACK_URL = "https://www.youtube.com/watch?v=";

  /** The URL for YouTube Music tracks. */
  private static final String YOUTUBE_MUSIC_TRACK_URL = "https://music.youtube.com/watch?v=";

  /** The activity the view holder is running under. */
  private final AppCompatActivity activity;

  /** The TextView for the track's title. */
  private final TextView titleView;

  /** The TextView for the track's artist line. */
  private final TextView artistLineView;

  /** The ImageView for the track's cover art. */
  private final ImageView coverArtView;

  /** The ImageButton that redirects the user to play the track. */
  private final ImageButton playButton;

  /**
   * Creates a new ViewHolder.
   *
   * @param activity The activity the view holder is running under
   * @param itemView The view representing a playlist item
   */
  public PlayableTrackViewHolder(AppCompatActivity activity, @NonNull View itemView) {
    super(itemView);

    this.activity = activity;
    titleView = itemView.findViewById(R.id.trackTitle);
    artistLineView = itemView.findViewById(R.id.trackArtists);
    coverArtView = itemView.findViewById(R.id.coverArt);
    playButton = itemView.findViewById(R.id.playButton);
  }

  /**
   * Updates the title view's text.
   *
   * @param title The track title
   */
  public void setTitle(String title) {
    titleView.setText(title);
  }

  /**
   * Updates artist line text.
   *
   * @param artistLine The track's artist line
   */
  public void setArtistLine(String artistLine) {
    artistLineView.setText(artistLine);
  }

  /**
   * Updates the cover art image.
   *
   * @param coverArtUrl The cover art URL
   */
  public void setCoverArt(String coverArtUrl) {
    // Glide doesn't like it when you call a load on a destroyed activity
    if (activity.isDestroyed()) {
      return;
    }

    Glide.with(activity).load(coverArtUrl).error(R.drawable.error_dark).into(coverArtView);
  }

  /**
   * Sets up the appearance and functionality of the play button.
   *
   * @param source The music source of the track
   * @param sourceId The source ID of the track
   */
  public void setupPlayButton(MusicSource source, String sourceId) {
    // Glide doesn't like it when you call a load on a destroyed activity
    if (activity.isDestroyed()) {
      return;
    }

    if ((source == null) || (sourceId == null)) {
      playButton.setVisibility(View.GONE);
      return;
    }

    String buttonDescription;
    String sourceUrl;
    int buttonColor;
    int buttonIcon;

    // determine the icon and music URL
    switch (source) {
      case Spotify:
        sourceUrl = SPOTIFY_TRACK_URL + sourceId;
        buttonColor = R.color.spotify_green;
        buttonIcon = R.drawable.spotify_icon_light;

        buttonDescription = activity.getString(R.string.track_play_button_description, "Spotify");

        break;
      case YouTube:
        buttonColor = R.color.youtube_red;
        buttonIcon = R.drawable.youtube_icon_light;

        buttonDescription = activity.getString(R.string.track_play_button_description, "YouTube");

        if (AppAvailability.isYouTubeMusicAppAvailable(activity.getApplicationContext())) {
          sourceUrl = YOUTUBE_MUSIC_TRACK_URL + sourceId;
        } else {
          sourceUrl = YOUTUBE_TRACK_URL + sourceId;
        }

        break;
      case Unknown:
      default:
        buttonDescription = "";
        sourceUrl = null;
        buttonColor = R.color.black;
        buttonIcon = R.drawable.error_dark;
    }

    if (sourceUrl == null) {
      return;
    }

    // set up the button
    playButton.setContentDescription(buttonDescription);

    playButton.setBackgroundColor(
        activity.getResources().getColor(buttonColor, activity.getTheme()));

    // load the play button's icon
    Glide.with(activity).load(buttonIcon).error(R.drawable.error_dark).fitCenter().into(playButton);

    playButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(sourceUrl));

            activity.startActivity(intent);
          }
        });

    playButton.setVisibility(View.VISIBLE);
  }
}
