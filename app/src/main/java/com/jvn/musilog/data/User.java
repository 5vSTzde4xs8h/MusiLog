package com.jvn.musilog.data;

import static com.jvn.musilog.util.DocumentFields.User.*;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.function.Predicate;

/**
 * Class for user information. Meant to be used as a custom object in Firestore.
 *
 * @author Poleon Banouvong
 * @since 2024-03-29
 */
@IgnoreExtraProperties
public class User {
  /**
   * The list of music tracks the user has added to their playlist. This list must not contain any
   * duplicate tracks, tracks whose sources are {@link MusicSource#Unknown Unknown}, or {@code null}
   * tracks.<br>
   * <br>
   * The data type is a {@link java.util.List List} because collections, such as {@link
   * LinkedHashSet}, are not serialisable by Firestore.
   */
  private ArrayList<Track> playlist;

  /** The description of the user's playlist. */
  private String playlistDescription;

  /** The display name of the user. */
  private String displayName;

  /** Default constructor. Required to be public to be used as a custom object in Firestore. */
  public User() {}

  /**
   * Qualified constructor. If the playlist contains either {@code null} tracks or tracks whose
   * source is {@link MusicSource#Unknown Unknown}, those tracks will be removed. Additionally, if
   * the playlist contains duplicate tracks, all but one of the duplicate tracks will be removed.
   *
   * @param displayName The display name of the user
   * @param playlist The list of {@link Track}s in the user's playlist
   * @param playlistDescription The description of the user's playlist
   */
  public User(
      String displayName,
      ArrayList<Track> playlist,
      String playlistDescription) {
    if (playlist != null) {
      // converting the list to a LinkedHashSet removes duplicate tracks
      LinkedHashSet<Track> checkedPlaylist = new LinkedHashSet<Track>(playlist);

      // remove tracks that have an Unknown source or are null
      checkedPlaylist.removeIf(
          new Predicate<Track>() {
            @Override
            public boolean test(Track track) {
              return (track == null) || (track.getSource() == MusicSource.Unknown);
            }
          });

      this.playlist = new ArrayList<Track>(checkedPlaylist);
    }

    this.displayName = displayName;
    this.playlistDescription = playlistDescription;
  }

  /**
   * @return The user's playlist, which will not contain duplicate tracks (tracks with the same
   *     source and source ID) or {@code null} tracks, and which may be {@code null}
   */
  @PropertyName(PLAYLIST_FIELD)
  public ArrayList<Track> getPlaylist() {
    if (playlist == null) {
      return null;
    }

    return new ArrayList<Track>(playlist);
  }

  /**
   * @return The user's playlist description
   */
  @PropertyName(PLAYLIST_DESCRIPTION_FIELD)
  public String getPlaylistDescription() {
    return playlistDescription;
  }

  /**
   * @return The user account's display name
   */
  @PropertyName(DISPLAY_NAME_FIELD)
  public String getDisplayName() {
    return displayName;
  }
}
