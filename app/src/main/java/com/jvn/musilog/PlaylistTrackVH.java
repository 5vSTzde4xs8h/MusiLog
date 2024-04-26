package com.jvn.musilog;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
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
 * This class is for storing data for User's Playlist
 *
 * @author alexleyva
 * @since 2024-04-18
 */
// Credit to Poleon for helping in creating this class

public class PlaylistTrackVH extends RecyclerView.ViewHolder {
  /** String for base link of Spotify website without the sourceID */
  private static final String Spotify_Track_Url = "https://open.spotify.com/track/";

  /** String for base link for Youtube website without the sourceID */
  private static final String Youtube_Track_Url = "https://www.youtube.com/watch?v=";

  /** String for base link for Youtube Music website without the sourceID */
  private static final String Youtube_Music_Track_Url = "https://music.youtube.com/watch?v=";

  /** The Activity the adapter is running under. */
  private final AppCompatActivity activity;

  /** TextView of the artist(s) name */
  private final TextView artists_Name;

  /** TextView of the song name */
  private final TextView song_Name;

  /** ImageView of the the song's cover */
  private final ImageView song_Pic;

  /**
   * ImageButton to the link and the image of the type of source the link came from (ex: Youtube,
   * Spotify)
   */
  private final ImageButton button_Type;

  /** Button to delete an entry of the playlist */
  Button deleteButton;

  /**
   * Sets attributes to parts of a playlist entry via findViewById
   *
   * @param activity
   * @param itemView
   */
  public PlaylistTrackVH(AppCompatActivity activity, @NonNull View itemView) {
    super(itemView);
    this.activity = activity;
    artists_Name = itemView.findViewById(R.id.artists_name);
    song_Name = itemView.findViewById(R.id.song_name);
    song_Pic = itemView.findViewById(R.id.song_img);
    button_Type = itemView.findViewById(R.id.source_button);
    deleteButton = itemView.findViewById(R.id.delete_button);
  }

  /**
   * Updates the artist's name via Text
   *
   * @param artistLine
   */

  public void setArtists_Name(String artistLine) {
    artists_Name.setText(artistLine);
  }

  /**
   * Updates song's name via Text
   *
   * @param title
   */
  public void setSong_Name(String title) {
    song_Name.setText(title);
  }

  /**
   * Updates song's cover pic
   *
   * @param coverArtUrl
   */
  public void setSong_Pic(String coverArtUrl) {
    if (activity.isDestroyed()) {
      return;
    }
    /** Updates song's pic else it defaults to "error_icon" */
    Glide.with(activity).load(coverArtUrl).error(R.drawable.error_icon).into(song_Pic);
  }

  /**
   * Sets the look of the logo of the button depending on the source of the link
   *
   * @param source
   * @param sourceID
   */
  public void setButton_Type(MusicSource source, String sourceID) {
    if (activity.isDestroyed()) {
      return;
    }

    if ((source == null) || (sourceID == null)) {
      button_Type.setVisibility(View.GONE);
      return;
    }

    String buttonBrand;
    String buttonLink;
    int buttonVersion;
    int buttonLook;

    // Switch that determines the button's logo, source, description based on Source of link
    switch (source) {
      case Spotify:
        buttonLink = Spotify_Track_Url + sourceID;
        buttonVersion = R.color.spotify_green;
        buttonLook = R.drawable.spotify_icon_light;

        buttonBrand = activity.getString(R.string.track_play_button_description, "Spotify");
        break;
      case YouTube:
        buttonVersion = R.color.youtube_red;
        buttonLook = R.drawable.youtube_icon_light;

        buttonBrand = activity.getString(R.string.track_play_button_description, "Youtube");

        if (AppAvailability.isYouTubeMusicAppAvailable(activity.getApplicationContext())) {
          buttonLink = Youtube_Music_Track_Url + sourceID;
        } else {
          buttonLink = Youtube_Track_Url + sourceID;
        }

        break;
      case Unknown:
      default:
        buttonBrand = "";
        buttonLink = null;
        buttonVersion = R.color.black;
        buttonLook = R.drawable.error_icon;
    }
    if (buttonLink == null) {
      return;
    }
    button_Type.setContentDescription(buttonBrand);
    button_Type.setBackgroundColor(
        activity.getResources().getColor(buttonVersion, activity.getTheme()));
    Glide.with(activity)
        .load(buttonLook)
        .error(R.drawable.error_icon)
        .fitCenter()
        .into(button_Type);

    // On click, button will open to song's webpage
    button_Type.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(buttonLink));
            activity.startActivity(intent);
          }
        });
    button_Type.setVisibility(View.VISIBLE);
  }
}
