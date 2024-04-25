package com.jvn.musilog.data;

import static org.junit.Assert.*;

import com.google.firebase.Timestamp;

import org.junit.Test;

/**
 * Rating unit tests.
 *
 * @author Poleon Banouvong
 * @since 2024-04-09
 */
public class RatingTest {
  /** Tests that rating values are returned as expected. */
  @Test
  public void testRating() {
    Rating rating1 = new Rating();
    Rating rating2 = new Rating((float) 3.5);

    assertNull(rating1.getRating());
    assertNotNull(rating2.getRating());
    assertEquals(3.5, rating2.getRating(), 1e-5);
  }

  /** Tests that timestamp values are returned as expected. */
  @Test
  public void testTimestamp() {
    Rating rating1 = new Rating();
    Rating rating2 = new Rating((float) 1.5);

    Timestamp timestamp1 = rating1.getTimestamp();
    Timestamp timestamp2 = rating2.getTimestamp();

    assertNull(timestamp1);
    assertNotNull(timestamp2);
  }
}
