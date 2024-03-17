package com.jvn.musilog.data;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;
import com.jvn.musilog.BuildConfig;
import com.jvn.musilog.util.ApiProvider;
import com.neovisionaries.i18n.CountryCode;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.List;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

/**
 * Class for music track metadata.
 *
 * @author Poleon Banouvong
 * @since 2024-03-16
 */
public class TrackMetadata {
  /** The title of the music track. */
  private final String title;

  /** The list of artists for the music track. */
  private final String artistLine;

  /** The cover art URL for the music track. */
  private final String coverArtUrl;

  /** Private default constructor. Use {@link TrackMetadata#fromMusicSource} instead. */
  private TrackMetadata() {
    this.title = "";
    this.artistLine = "";
    this.coverArtUrl = "";
  }

  /** Private qualified constructor. Use {@link TrackMetadata#fromMusicSource} instead. */
  private TrackMetadata(String title, String artistLine, String coverArtUrl) {
    this.title = title;
    this.artistLine = artistLine;
    this.coverArtUrl = coverArtUrl;
  }

  /**
   * Creates {@link TrackMetadata} from a Spotify music track. If there's a problem retrieving the
   * metadata, a {@link TrackMetadata} with no information will be returned.
   *
   * @param sourceId The Spotify track ID
   * @return A {@link TrackMetadata} describing the music track
   */
  private static TrackMetadata fromSpotify(String sourceId) {
    SpotifyApi api = ApiProvider.getSpotifyApi();

    if (api == null) {
      return new TrackMetadata();
    }

    try {
      GetTrackRequest trackRequest = api.getTrack(sourceId).market(CountryCode.US).build();

      Track trackResponse = trackRequest.execute();
      AlbumSimplified album = trackResponse.getAlbum();
      Image[] albumCoverArtUrls = album.getImages();

      // create the artist line
      StringBuilder artistLine = new StringBuilder();
      ArtistSimplified[] artists = trackResponse.getArtists();

      for (int i = 0; i < artists.length; i++) {
        artistLine.append(artists[i].getName());

        if (i != (artists.length - 1)) {
          artistLine.append(", ");
        }
      }

      return new TrackMetadata(
          trackResponse.getName(), artistLine.toString(), albumCoverArtUrls[0].getUrl());
    } catch (IOException | SpotifyWebApiException | ParseException except) {
      return new TrackMetadata();
    }
  }

  /**
   * Creates {@link TrackMetadata} from a YouTube music track. Note that this method doesn't verify
   * that the provided video ID is in fact for a music track. If there's a problem retrieving the
   * metadata, a {@link TrackMetadata} with no information will be returned.
   *
   * @param sourceId The YouTube track ID
   * @return A {@link TrackMetadata} describing the music track
   */
  private static TrackMetadata fromYouTube(String sourceId) {
    YouTube api = ApiProvider.getYoutubeApi();

    if (api == null) {
      return new TrackMetadata();
    }

    List<String> videoParts = List.of("snippet");
    List<String> videoIds = List.of(sourceId);

    try {
      // get video info
      YouTube.Videos.List videoRequest = api.videos().list(videoParts);

      VideoListResponse videosResponse =
          videoRequest.setId(videoIds).setKey(BuildConfig.YOUTUBE_API_KEY).execute();

      List<Video> videos = videosResponse.getItems();

      if (videos.isEmpty()) {
        return new TrackMetadata();
      } else {
        Video video = videos.get(0);
        VideoSnippet videoSnippet = video.getSnippet();
        Thumbnail highResolutionThumbnail = videoSnippet.getThumbnails().getHigh();

        return new TrackMetadata(
            videoSnippet.getTitle(),
            videoSnippet.getChannelTitle(),
            highResolutionThumbnail.getUrl());
      }
    } catch (IOException
        | NullPointerException except) { // many of the API calls can return null values
      return new TrackMetadata();
    }
  }

  /**
   * Creates {@link TrackMetadata} from a music source and ID. If the music source is {@link
   * MusicSource#Unknown}, or there's a problem retrieving the metadata, a {@link TrackMetadata}
   * with no information will be returned.
   *
   * @param source The {@link MusicSource} to get the metadata from
   * @param sourceId The music track ID
   * @return A {@link TrackMetadata} describing the music track
   */
  public static TrackMetadata fromMusicSource(MusicSource source, String sourceId) {
    switch (source) {
      case Spotify:
        return fromSpotify(sourceId);
      case YouTube:
        return fromYouTube(sourceId);
      default:
        return new TrackMetadata();
    }
  }

  /**
   * @return The title for the music track
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return The list of artists for the music track
   */
  public String getArtistLine() {
    return artistLine;
  }

  /**
   * @return The cover art URL for the music track
   */
  public String getCoverArtUrl() {
    return coverArtUrl;
  }
}
