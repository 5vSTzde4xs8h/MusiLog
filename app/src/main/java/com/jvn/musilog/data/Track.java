package com.jvn.musilog.data;

import com.google.firebase.firestore.PropertyName;

import java.util.regex.*;

/**
 * Class for music track data. Meant to be used as a custom object in Firestore.
 *
 * @author Poleon Banouvong
 * @since 2024-03-17
 */
public class Track {
  /** The corresponding Firestore document field name for the {@link Track#source} member. */
  private static final String SOURCE_FIELD = "source";

  /** The corresponding Firestore document field name for the {@link Track#sourceId} member. */
  private static final String SOURCE_ID_FIELD = "sourceId";

  /**
   * Regex pattern for a YouTube video ID. Google doesn't specify the format of these IDs, but <a
   * href="https://webapps.stackexchange.com/questions/54443/format-for-id-of-youtube-video#answer-101153">reverse-engineering</a>
   * suggests that the format is modified Base64.
   */
  private static final String youTubeIdRegex = "[0-9A-Za-z-_]+";

  /**
   * Regex {@link Pattern} for a Spotify track URL. The format of the ID is Base62, as described <a
   * href="https://developer.spotify.com/documentation/web-api/concepts/spotify-uris-ids">in
   * Spotify's documentation</a>.
   */
  private static final Pattern spotifyTrackUrlPattern =
      Pattern.compile("^(https?://)?open\\.spotify\\.com/track/([0-9A-Za-z]+)");

  /** Regex {@link Pattern} for a YouTube video URL. */
  private static final Pattern youTubeVideoUrlPattern =
      Pattern.compile(
          "^(https?://)?((www|music)\\.)?youtube\\.com/watch.*[?&]v=(" + youTubeIdRegex + ")");

  /** Regex {@link Pattern} for a YouTube video short URL. */
  private static final Pattern youTubeVideoShortUrlPattern =
      Pattern.compile("^(https?://)?youtu\\.be/(" + youTubeIdRegex + ")");

  /** The source this music track comes from. */
  private MusicSource source;

  /** The ID of the music track, relative to its source. */
  private String sourceId;

  /** Default constructor. Required to be public to be used as a custom object in Firestore. */
  public Track() {}

  /**
   * Qualified {@link Track} constructor.
   *
   * @param source The {@link MusicSource} the track is coming from
   * @param sourceId The source ID of the track
   */
  public Track(MusicSource source, String sourceId) {
    this.source = source;
    this.sourceId = sourceId;
  }

  /**
   * Constructs a {@link Track} from a URL.
   *
   * @param url The URL to construct the {@link Track} from
   * @return A {@link Track} with data derived from the URL
   */
  public static Track fromUrl(String url) {
    MusicSource source;
    String sourceId;

    Matcher spotifyTrackUrlMatcher = spotifyTrackUrlPattern.matcher(url);
    Matcher youtubeVideoUrlMatcher = youTubeVideoUrlPattern.matcher(url);
    Matcher youtubeVideoShortUrlMatcher = youTubeVideoShortUrlPattern.matcher(url);

    if (spotifyTrackUrlMatcher.find()) {
      source = MusicSource.Spotify;
      sourceId = spotifyTrackUrlMatcher.group(2);
    } else if (youtubeVideoUrlMatcher.find()) {
      source = MusicSource.YouTube;
      sourceId = youtubeVideoUrlMatcher.group(4);
    } else if (youtubeVideoShortUrlMatcher.find()) {
      source = MusicSource.YouTube;
      sourceId = youtubeVideoShortUrlMatcher.group(2);
    } else {
      source = MusicSource.Unknown;
      sourceId = "";
    }

    return new Track(source, sourceId);
  }

  /**
   * @return The {@link MusicSource} for this music track
   */
  @PropertyName(SOURCE_FIELD)
  public MusicSource getSource() {
    return source;
  }

  /**
   * @return The source ID, relative to the music source, for this music track
   */
  @PropertyName(SOURCE_ID_FIELD)
  public String getSourceId() {
    return sourceId;
  }
}
