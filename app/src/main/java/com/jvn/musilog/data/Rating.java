package com.jvn.musilog.data;

import static com.jvn.musilog.util.DocumentFields.Rating.*;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

/**
 * Represents a rating of something. Meant to be used as a custom object in Firestore.
 *
 * @author Poleon Banouvong
 * @since 2024-04-07
 */
public class Rating {
  /** The rating. */
  private Float rating;

  /** The timestamp when the rating was made. */
  private Timestamp timestamp;

  /** Default constructor. Required to be public to be used as a Firestore custom object. */
  public Rating() {}

  /**
   * Qualified constructor.
   *
   * @param rating The rating
   */
  public Rating(Float rating) {
    this.rating = rating;
    this.timestamp = Timestamp.now();
  }

  /**
   * @return The rating
   */
  @PropertyName(RATING_FIELD)
  public Float getRating() {
    return rating;
  }

  /**
   * @return The timestamp when the rating was made
   */
  @PropertyName(TIMESTAMP_FIELD)
  @ServerTimestamp
  public Timestamp getTimestamp() {
    return timestamp;
  }
}
