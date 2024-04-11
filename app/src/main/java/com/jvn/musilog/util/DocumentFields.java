package com.jvn.musilog.util;

/**
 * A set of classes containing Firestore document field names. Should be used when linking custom
 * object variables to their document fields, as well as when reading and writing documents.
 *
 * @author Poleon Banouvong
 * @since 2024-04-09
 */
public final class DocumentFields {
  /** Private default constructor to prevent object creation. */
  private DocumentFields() {}

  /** Refers to the User object and its corresponding document fields. */
  public static class User {
    /** Refers to the user's display name. */
    public static final String DISPLAY_NAME_FIELD = "displayName";

    /** Refers to the user's e-mail. */
    public static final String EMAIL_FIELD = "email";

    /** Refers to the user's playlist. */
    public static final String PLAYLIST_FIELD = "playlist";

    /** Refers to the user's playlist description. */
    public static final String PLAYLIST_DESCRIPTION_FIELD = "playlistDescription";
  }

  /** Refers to the Track object and its corresponding document fields. */
  public static class Track {
    /** Refers to the track's music source. */
    public static final String SOURCE_FIELD = "source";

    /** Refers to the track's music source ID. */
    public static final String SOURCE_ID_FIELD = "sourceId";
  }

  /** Refers to the Rating object and its corresponding document fields. */
  public static class Rating {
    /** Refers to the rating's rating value. */
    public static final String RATING_FIELD = "rating";

    /** Refers to the rating's timestamp. */
    public static final String TIMESTAMP_FIELD = "timestamp";
  }
}
