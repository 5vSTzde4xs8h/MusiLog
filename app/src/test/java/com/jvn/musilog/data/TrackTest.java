package com.jvn.musilog.data;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@link Track} test class.
 *
 * @author Poleon Banouvong
 * @since 2024-03-17
 */
public class TrackTest {
  /** Tests that {@link Track}s for Spotify URLs are correctly generated. */
  @Test
  public void testFromSpotifyUrl() {
    String[] urls = {
      "https://open.spotify.com/track/4uoDLr3XxvDB3xXLiDZz4b",
      "open.spotify.com/track/4uoDLr3XxvDB3xXLiDZz4b?badness=1&cookies=false",
      "http://open.spotify.com/track/1OAp6qN5KmoGUQ2edICKsC?extra=some-extra-stuff",
      "open.spotify.com/track/1OAp6qN5KmoGUQ2edICKsC?extra=some-extra-stuff",
      "https://open.spotify.com/track/283oaRFCEHRY4DreLpwAPW?n=a",
      "open.spotify.com/track/283oaRFCEHRY4DreLpwAPW",
      "http://open.spotify.com/track/28DyuhrsEP2mBlAVSTJPBg?badness=dont-match-me",
      "open.spotify.com/track/28DyuhrsEP2mBlAVSTJPBg?badness=dont-match"
    };

    String[] expectedIds = {
      "4uoDLr3XxvDB3xXLiDZz4b",
      "4uoDLr3XxvDB3xXLiDZz4b",
      "1OAp6qN5KmoGUQ2edICKsC",
      "1OAp6qN5KmoGUQ2edICKsC",
      "283oaRFCEHRY4DreLpwAPW",
      "283oaRFCEHRY4DreLpwAPW",
      "28DyuhrsEP2mBlAVSTJPBg",
      "28DyuhrsEP2mBlAVSTJPBg"
    };

    // check that the test data itself is valid
    if (urls.length != expectedIds.length) {
      fail("Number of test URLs does not match the number of expected IDs");
    }

    for (int i = 0; i < urls.length; i++) {
      Track track = Track.fromUrl(urls[i]);

      assertEquals(MusicSource.Spotify, track.getSource());
      assertEquals(expectedIds[i], track.getSourceId());
      assertNotEquals(null, track.getMetadata());
    }
  }

  /** Tests that {@link Track}s for YouTube URLs are correctly generated. */
  @Test
  public void testFromYouTubeUrl() {
    String[] urls = {
      "https://youtube.com/watch?v=Ey_NHZNYTeE",
      "youtube.com/watch?v=Ey_NHZNYTeE&test=pass",
      "http://www.youtube.com/watch?v=UvUJbhxUKT8",
      "www.youtube.com/watch?test=pass&v=UvUJbhxUKT8&play=true",
      "https://music.youtube.com/watch?v=pUH9vCsvq08&badness=true",
      "music.youtube.com/watch?badness=true&v=pUH9vCsvq08",
    };

    String[] expectedIds = {
      "Ey_NHZNYTeE", "Ey_NHZNYTeE", "UvUJbhxUKT8", "UvUJbhxUKT8", "pUH9vCsvq08", "pUH9vCsvq08"
    };

    // check that the test data itself is valid
    if (urls.length != expectedIds.length) {
      fail("Number of test URLs does not match the number of expected IDs");
    }

    for (int i = 0; i < urls.length; i++) {
      Track track = Track.fromUrl(urls[i]);

      assertEquals(MusicSource.YouTube, track.getSource());
      assertEquals(expectedIds[i], track.getSourceId());
      assertNotEquals(null, track.getMetadata());
    }
  }

  /** Tests that {@link Track}s for YouTube short URLs are correctly generated. */
  @Test
  public void testFromYouTubeShortUrl() {
    String[] urls = {
      "https://youtu.be/Ey_NHZNYTeE",
      "youtu.be/Ey_NHZNYTeE?test=pass",
      "http://youtu.be/UvUJbhxUKT8",
      "youtu.be/UvUJbhxUKT8?test=pass&play=true",
      "https://youtu.be/pUH9vCsvq08&badness=true",
      "youtu.be/pUH9vCsvq08?badness=true",
    };

    String[] expectedIds = {
      "Ey_NHZNYTeE", "Ey_NHZNYTeE", "UvUJbhxUKT8", "UvUJbhxUKT8", "pUH9vCsvq08", "pUH9vCsvq08"
    };

    // check that the test data itself is valid
    if (urls.length != expectedIds.length) {
      fail("Number of test URLs does not match the number of expected IDs");
    }

    for (int i = 0; i < urls.length; i++) {
      Track track = Track.fromUrl(urls[i]);

      assertEquals(MusicSource.YouTube, track.getSource());
      assertEquals(expectedIds[i], track.getSourceId());
      assertNotEquals(null, track.getMetadata());
    }
  }

  /** Tests that {@link Track}s for unknown URLs are correctly generated. */
  @Test
  public void testFromBadUrl() {
    String[] urls = {
        "https://project-mili.bandcamp.com/track/poems-of-a-machine",

        // YouTube URLs must be one of the following domains exactly:
        // youtube.com
        // www.youtube.com
        // music.youtube.com
        // youtu.be
        "https://alpine.youtube.com/watch?v=Ey_NHZNYTeE",
        "unknown.youtube.com/watch?v=Ey_NHZNYTeE&test=pass",
        "https://musicals.youtube.com/watch?v=UvUJbhxUKT8",
        "music.youtu.be/UvUJbhxUKT8&test=pass",
        "https://extra.www.youtube.com/watch?v=pUH9vCsvq08",
        "www.www.youtu.be/pUH9vCsvq08&test=pass",

        // Spotify URLs must be from the domain open.spotify.com exactly
        "https://spotify.com/track/4uoDLr3XxvDB3xXLiDZz4b",
        "www.spotify.com/track/4uoDLr3XxvDB3xXLiDZz4b?badness=1&cookies=false",
        "https://opens.spotify.com/track/4uoDLr3XxvDB3xXLiDZz4b",
        "closes.spotify.com/track/4uoDLr3XxvDB3xXLiDZz4b?badness=1",
    };

    for (int i = 0; i < urls.length; i++) {
      Track track = Track.fromUrl(urls[i]);

      assertEquals(MusicSource.Unknown, track.getSource());
      assertEquals("", track.getSourceId());
      assertNotEquals(null, track.getMetadata());
    }
  }
}
