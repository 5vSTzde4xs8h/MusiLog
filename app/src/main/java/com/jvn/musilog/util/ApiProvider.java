package com.jvn.musilog.util;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.jvn.musilog.BuildConfig;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.security.GeneralSecurityException;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;

/**
 * Utility class for providing music metadata APIs.
 *
 * @author Poleon Banouvong
 * @since 2024-03-16
 */
public class ApiProvider {
  /** The timestamp at which the Spotify API needs to refreshed. */
  private static long nextSpotifyRefreshTime = 0;

  /** Spotify API singleton */
  private static SpotifyApi spotifyApi = null;

  /** YouTube API singleton */
  private static YouTube youtubeApi = null;

  /** Private default constructor */
  private ApiProvider() {}

  /** Creates the Spotify API singleton. */
  private static void makeSpotifyApi() {
    // create the API object
    SpotifyApi newSpotifyApi =
        new SpotifyApi.Builder()
            .setClientId(BuildConfig.SPOTIFY_CLIENT_ID)
            .setClientSecret(BuildConfig.SPOTIFY_CLIENT_SECRET)
            .build();

    try {
      // authenticate the client
      ClientCredentials clientCredentials = newSpotifyApi.clientCredentials().build().execute();
      newSpotifyApi.setAccessToken(clientCredentials.getAccessToken());

      spotifyApi = newSpotifyApi;

      nextSpotifyRefreshTime =
          System.currentTimeMillis() + (clientCredentials.getExpiresIn() * 1000);
    } catch (IOException | SpotifyWebApiException | ParseException except) {
      spotifyApi = null;
      nextSpotifyRefreshTime = 0;
    }
  }

  /** Creates the YouTube API singleton. */
  private static void makeYouTubeApi() {
    try {
      youtubeApi =
          new YouTube.Builder(
                  GoogleNetHttpTransport.newTrustedTransport(),
                  GsonFactory.getDefaultInstance(),

                  // we don't need to do any initialisation, but this is required
                  new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {}
                  })
              .setApplicationName("com.jvn.musilog")
              .build();
    } catch (IOException | GeneralSecurityException except) {
      youtubeApi = null;
    }
  }

  /**
   * Returns the Spotify API singleton. If the singleton is {@code null}, or its authorisation has
   * expired, a new API singleton will be created and returned.<br>
   * <br>
   * This singleton should <b>not</b> be cached, as its authorisation may expire between API
   * requests.
   *
   * @return The Spotify API, which may be {@code null}
   */
  public static SpotifyApi getSpotifyApi() {
    long now = System.currentTimeMillis();

    if ((spotifyApi == null) || (now >= nextSpotifyRefreshTime)) {
      makeSpotifyApi();
    }

    return spotifyApi;
  }

  /**
   * Returns the YouTube API singleton. If the singleton is {@code null}, a new API singleton will
   * be created and returned.
   *
   * @return The YouTube API, which may be {@code null}
   */
  public static YouTube getYoutubeApi() {
    if (youtubeApi == null) {
      makeYouTubeApi();
    }

    return youtubeApi;
  }
}
