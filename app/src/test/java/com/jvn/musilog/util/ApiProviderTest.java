package com.jvn.musilog.util;

import static org.junit.Assert.*;

import com.google.api.services.youtube.YouTube;

import org.junit.Test;

import se.michaelthelin.spotify.SpotifyApi;

/**
 * ApiProvider unit tests.
 *
 * @author Poleon Banouvong
 * @since 2024-04-11
 */
public class ApiProviderTest {
  /** Checks that the Spotify API can be retrieved. */
  @Test
  public void testSpotify() {
    SpotifyApi spotify = ApiProvider.getSpotifyApi();
    assertNotNull(spotify);
  }

  /** Checks that the YouTube API can be retrieved. */
  @Test
  public void testYouTube() {
    YouTube youTube = ApiProvider.getYoutubeApi();
    assertNotNull(youTube);
  }
}
