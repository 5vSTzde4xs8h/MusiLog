package com.jvn.musilog.util;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

/**
 * AppAvailability instrumented tests.
 *
 * @author Poleon Banouvong
 * @since 2024-04-11
 */
public class AppAvailabilityTest {
  /** The application context used to query app availability. */
  private final Context appContext = InstrumentationRegistry.getInstrumentation().getContext();

  /** Tests that a set of apps should be available. */
  @Test
  public void testAvailableApp() {
    boolean isAndroidAvailable = AppAvailability.isAppAvailable(appContext, "android");
    
    boolean isSettingsAvailable =
        AppAvailability.isAppAvailable(appContext, "com.android.settings");

    assertTrue(isAndroidAvailable);
    assertTrue(isSettingsAvailable);
  }

  /** Tests that a set of apps should not be available. */
  @Test
  public void testUnavailableApp() {
    boolean isNonsenseAvailable = AppAvailability.isAppAvailable(appContext, "nonsense.org");
    boolean isAndroidBadAvailable = AppAvailability.isAppAvailable(appContext, "android.badId");

    assertFalse(isNonsenseAvailable);
    assertFalse(isAndroidBadAvailable);
  }
}
