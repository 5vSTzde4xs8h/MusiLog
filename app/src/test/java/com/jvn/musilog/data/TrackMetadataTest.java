package com.jvn.musilog.data;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@link TrackMetadata} test class.
 *
 * @author Poleon Banouvong
 * @since 2024-03-16
 */
public class TrackMetadataTest {
  /**
   * Tests that metadata can be retrieved for a Spotify music track. This test depends on external
   * factors such as network and Spotify service availability, and assumes that all such factors do
   * not fail.
   */
  @Test
  public void testFromSpotify() {
    // https://open.spotify.com/track/2tmuRm2ROZ0vHEhutI2Hlp
    TrackMetadata spotifyMetadata =
        TrackMetadata.fromMusicSource(MusicSource.Spotify, "2tmuRm2ROZ0vHEhutI2Hlp");

    assertEquals("Mane Mane Psychotropic", spotifyMetadata.getTitle());
    assertEquals("Kairikibear", spotifyMetadata.getArtistLine());
    assertNotEquals("", spotifyMetadata.getCoverArtUrl());
  }

  /**
   * Tests that metadata cannot be retrieved for a bad Spotify music track ID. This test depends on
   * external factors such as network and Spotify service availability, and assumes that all such
   * factors do not fail.
   */
  @Test
  public void testFromSpotifyBadId() {
    TrackMetadata spotifyMetadata = TrackMetadata.fromMusicSource(MusicSource.Spotify, "badId");

    assertEquals("", spotifyMetadata.getTitle());
    assertEquals("", spotifyMetadata.getArtistLine());
    assertEquals("", spotifyMetadata.getCoverArtUrl());
  }

  /**
   * Tests that metadata can be retrieved for a YouTube music track. This test depends on external
   * factors such as network and YouTube service availability, and assumes that all such factors do
   * not fail.
   */
  @Test
  public void testFromYouTube() {
    // https://www.youtube.com/watch?v=Dun11cIEo9s
    TrackMetadata youTubeMetadata =
        TrackMetadata.fromMusicSource(MusicSource.YouTube, "Dun11cIEo9s");

    assertEquals(
        "Neru - potatoになっていく (Becoming Potatoes) feat. Kagamine Rin & Kagamine Len",
        youTubeMetadata.getTitle());

    assertEquals("Neru OFFICIAL", youTubeMetadata.getArtistLine());
    assertNotEquals("", youTubeMetadata.getCoverArtUrl());
  }

  /**
   * Tests that metadata cannot be retrieved for a bad YouTube music track ID. This test depends on
   * external factors such as network and YouTube service availability, and assumes that all such
   * factors do not fail.
   */
  @Test
  public void testFromYouTubeBadId() {
    TrackMetadata youTubeMetadata = TrackMetadata.fromMusicSource(MusicSource.YouTube, "badId");

    assertEquals("", youTubeMetadata.getTitle());
    assertEquals("", youTubeMetadata.getArtistLine());
    assertEquals("", youTubeMetadata.getCoverArtUrl());
  }

  /**
   * Tests that attempting to retrieve metadata from an unknown source will return an empty {@link
   * TrackMetadata}.
   */
  @Test
  public void testFromUnknown() {
    TrackMetadata unknownMetadata = TrackMetadata.fromMusicSource(MusicSource.Unknown, "");

    assertEquals("", unknownMetadata.getTitle());
    assertEquals("", unknownMetadata.getArtistLine());
    assertEquals("", unknownMetadata.getCoverArtUrl());
  }
}
