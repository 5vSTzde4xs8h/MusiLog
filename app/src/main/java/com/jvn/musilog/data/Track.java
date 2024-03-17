package com.jvn.musilog.data;

import java.util.regex.*;

/**
 * Class for music track data.
 *
 * @author Poleon Banouvong
 * @since 2024-03-17
 */
public class Track {
  /** Regex pattern for a YouTube video ID. */
  private static final String youTubeIdRegex = "[0-9A-Za-z-_]+";

  /** Regex {@link Pattern} for a Spotify track URL. */
  private static final Pattern spotifyTrackUrlPattern =
      Pattern.compile("^(https?://)?open\\.spotify\\.com/track/([0-9A-Za-z]+)");

  /** Regex {@link Pattern} for a YouTube video URL. */
  private static final Pattern youTubeVideoUrlPattern =
      Pattern.compile("^(https?://)?((www|music)\\.)?youtube\\.com/watch.*[?&]v=(" + youTubeIdRegex + ")");

  /** Regex {@link Pattern} for a YouTube video short URL. */
  private static final Pattern youTubeVideoShortUrlPattern =
      Pattern.compile("^(https?://)?youtu\\.be/(" + youTubeIdRegex + ")");

  /** The source this music track comes from. */
  private MusicSource source;

  /** The ID of the music track, relative to its source. */
  private String sourceId;

  /** The metadata of the music track. */
  private TrackMetadata metadata;

  /** Private default constructor. Use {@link Track#fromUrl} instead. */
  private Track() {}

  /** Private qualified constructor. Use {@link Track#fromUrl} instead. */
  private Track(MusicSource source, String sourceId, TrackMetadata metadata) {
    this.source = source;
    this.sourceId = sourceId;
    this.metadata = metadata;
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

    TrackMetadata metadata = TrackMetadata.fromMusicSource(source, sourceId);
    return new Track(source, sourceId, metadata);
  }

  /**
   * @return The {@link MusicSource} for this music track
   */
  public MusicSource getSource() {
    return source;
  }

  /**
   * @return The source ID, relative to the music source, for this music track
   */
  public String getSourceId() {
    return sourceId;
  }

  /**
   * @return The metadata for this music track
   */
  public TrackMetadata getMetadata() {
    return metadata;
  }
}
