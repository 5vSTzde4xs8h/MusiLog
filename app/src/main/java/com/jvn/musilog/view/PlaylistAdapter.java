package com.jvn.musilog.view;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jvn.musilog.R;
import com.jvn.musilog.data.MusicSource;
import com.jvn.musilog.data.Track;
import com.jvn.musilog.data.TrackMetadata;

import java.util.ArrayList;

/**
 * Adapter for a playlist to be used in a RecyclerView.
 *
 * @author Poleon Banouvong
 * @since 2024-04-06
 */
// https://developer.android.com/develop/ui/views/layout/recyclerview
public class PlaylistAdapter extends RecyclerView.Adapter<TrackViewHolder> {
  /** The playlist that will be rendered. */
  private final ArrayList<Track> playlist;

  /** The Activity the adapter is running under. */
  private final AppCompatActivity activity;

  /**
   * Creates a new PlaylistAdapter.
   *
   * @param activity The activity the adapter is running under
   * @param playlist The playlist to render
   */
  public PlaylistAdapter(AppCompatActivity activity, ArrayList<Track> playlist) {
    this.activity = activity;
    this.playlist = playlist;
  }

  /**
   * @return The number of tracks in the playlist
   */
  @Override
  public int getItemCount() {
    return playlist.size();
  }

  /**
   * Creates a new ViewHolder for a playlist item.
   *
   * @param parent The ViewGroup into which the new View will be added after it is bound to an
   *     adapter position.
   * @param viewType The view type of the new View.
   * @return The ViewHolder for the playlist item
   */
  @NonNull
  @Override
  public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);

    return new TrackViewHolder(activity, view);
  }

  /**
   * Binds the playlist track to its corresponding ViewHolder.
   *
   * @param holder The ViewHolder which should be updated to represent the contents of the item at
   *     the given position in the data set.
   * @param position The position of the item within the adapter's data set.
   */
  @Override
  public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
    Track track = playlist.get(position);

    // the handler allows us to dispatch UI updates to the main thread, the only thread where UI
    // updates are allowed
    // https://developer.android.com/develop/background-work/background-tasks/asynchronous/java-threads#use-handlers
    Handler updateUIHandler = new Handler(Looper.getMainLooper());

    // retrieving metadata requires a network call, so we must put it in a separate thread
    Thread metadataThread =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                TrackMetadata metadata =
                    TrackMetadata.fromMusicSource(track.getSource(), track.getSourceId());

                // dispatch UI updates
                updateUIHandler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        TrackMetadata thisMetadata;

                        if (metadata == null) {
                          thisMetadata = new TrackMetadata("(Unknown)", "(Unknown)", null);
                          holder.setupPlayButton(MusicSource.Unknown, null);
                        } else {
                          thisMetadata = metadata;
                          holder.setupPlayButton(track.getSource(), track.getSourceId());
                        }

                        holder.setTitle(thisMetadata.getTitle());
                        holder.setArtistLine(thisMetadata.getArtistLine());
                        holder.setCoverArt(thisMetadata.getCoverArtUrl());
                      }
                    });
              }
            });

    metadataThread.start();
  }
}
