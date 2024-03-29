package com.jvn.musilog.data;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Unit tests for the {@link User} class.
 *
 * @author Poleon Banouvong
 * @since 2024-03-29
 */
public class UserTest {
  /** Test {@link Track} with a {@link MusicSource#Spotify Spotify} source. */
  private final Track spotifyTrack = new Track(MusicSource.Spotify, "abcd");

  /** Test {@link Track} with a {@link MusicSource#YouTube YouTube} source. */
  private final Track youTubeTrack = new Track(MusicSource.YouTube, "efgh");

  /** Test {@link Track} with an {@link MusicSource#Unknown Unknown} source. */
  private final Track unknownTrack = new Track(MusicSource.Unknown, "ijkl");

  /** Tests that the qualified {@link User} constructor works as intended. */
  @Test
  public void testQualifiedConstructor() {
    ArrayList<Track> playlist = new ArrayList<>();
    playlist.add(spotifyTrack);
    playlist.add(youTubeTrack);

    User newUser = new User(playlist, "This is a playlist.", (float) 0, 0);

    assertEquals(2, newUser.getPlaylist().size());
    assertEquals("This is a playlist.", newUser.getPlaylistDescription());
    assertEquals(0, (float) newUser.getAveragePlaylistRating(), 0);
    assertEquals(0, (int) newUser.getNumPlaylistRatings());
  }

  /**
   * Tests that playlists cannot contain tracks whose source is {@link MusicSource#Unknown Unknown}.
   */
  @Test
  public void testPlaylistNoUnknowns() {
    Track track4 = new Track(MusicSource.Unknown, "mnop");

    ArrayList<Track> playlist = new ArrayList<Track>();
    playlist.add(spotifyTrack);
    playlist.add(unknownTrack);
    playlist.add(youTubeTrack);
    playlist.add(track4);

    User newUser = new User(playlist, null, null, null);
    assertEquals(2, newUser.getPlaylist().size());
  }

  /**
   * Tests that playlists cannot contain duplicate tracks (tracks with the same source and source
   * ID).
   */
  @Test
  public void testPlaylistNoDuplicates() {
    // test with the same Track object
    ArrayList<Track> playlist1 = new ArrayList<Track>();
    playlist1.add(spotifyTrack);
    playlist1.add(spotifyTrack);
    playlist1.add(spotifyTrack);
    playlist1.add(spotifyTrack);

    // test with different Track objects with the same data
    ArrayList<Track> playlist2 = new ArrayList<Track>();
    playlist2.add(new Track(MusicSource.Spotify, "abcd"));
    playlist2.add(new Track(MusicSource.Spotify, "abcd"));
    playlist2.add(new Track(MusicSource.Spotify, "abcd"));
    playlist2.add(new Track(MusicSource.Spotify, "abcd"));

    User newUser1 = new User(playlist1, null, null, null);
    User newUser2 = new User(playlist2, null, null, null);

    assertEquals(1, newUser1.getPlaylist().size());
    assertEquals(1, newUser2.getPlaylist().size());
  }

  /**
   * Tests that the playlist preserves ordering, since if may be modified when passed to the
   * constructor.
   */
  @Test
  public void testPlaylistOrdering() {
    Track track3 = new Track(MusicSource.Spotify, "mnop");

    ArrayList<Track> playlist = new ArrayList<Track>();
    playlist.add(spotifyTrack);
    playlist.add(youTubeTrack);
    playlist.add(track3);

    User newUser = new User(playlist, null, null, null);
    ArrayList<Track> returnedPlaylist = newUser.getPlaylist();

    assertEquals(spotifyTrack, returnedPlaylist.get(0));
    assertEquals(youTubeTrack, returnedPlaylist.get(1));
    assertEquals(track3, returnedPlaylist.get(2));
  }

  /** Tests that the playlist description is returned properly. */
  @Test
  public void testPlaylistDescription() {
    User newUser = new User(null, "This is a null playlist.", null, null);
    assertEquals("This is a null playlist.", newUser.getPlaylistDescription());
  }

  /** Tests that the average rating is returned properly. */
  @Test
  public void testAverageRating() {
    User newUser = new User(null, null, (float) 1.24, null);
    assertEquals(1.24, newUser.getAveragePlaylistRating(), 0.01);
  }

  /** Tests that the number of ratings is returned properly. */
  @Test
  public void testNumRatings() {
    User newUser = new User(null, null, null, 250);
    assertEquals(250, (int) newUser.getNumPlaylistRatings());
  }
}