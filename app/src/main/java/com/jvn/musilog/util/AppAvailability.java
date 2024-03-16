package com.jvn.musilog.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Utility class for checking if certain apps are installed on the device.
 *
 * @author Poleon Banouvong
 * @since 2024-03-16
 */
public class AppAvailability {
  /**
   * Checks if an app is installed.
   *
   * @param context The {@link Context} to get the {@link PackageManager} from. This should be the
   *     application context.
   * @param packageId The ID of the app to look up
   * @return A boolean telling if the app is installed
   */
  private static boolean isAppAvailable(Context context, String packageId) {
    PackageManager packageManager = context.getPackageManager();

    try {
      packageManager.getPackageInfo(packageId, 0);
      return true;
    } catch (PackageManager.NameNotFoundException exception) {
      return false;
    }
  }

  /**
   * Checks if the Spotify app is installed.
   *
   * @param context The {@link Context} to get the {@link PackageManager} from. This should be the
   *     application context.
   * @return A boolean telling if the Spotify app is installed
   */
  public static boolean isSpotifyAppAvailable(Context context) {
    return isAppAvailable(context, "com.spotify.music");
  }

  /**
   * Checks if the YouTube app is installed.
   *
   * @param context The {@link Context} to get the {@link PackageManager} from. This should be the
   *     application context.
   * @return A boolean telling if the YouTube app is installed
   */
  public static boolean isYouTubeAppAvailable(Context context) {
    return isAppAvailable(context, "com.google.android.youtube");
  }

  /**
   * Checks if the YouTube Music app is installed.
   *
   * @param context The {@link Context} to get the {@link PackageManager} from. This should be the
   *     application context.
   * @return A boolean telling if the YouTube Music app is installed
   */
  public static boolean isYouTubeMusicAppAvailable(Context context) {
    return isAppAvailable(context, "com.google.android.apps.youtube.music");
  }
}
