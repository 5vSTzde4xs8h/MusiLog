package com.jvn.musilog;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Context;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.jvn.musilog.data.TrackMetadata;
import com.jvn.musilog.R;
import com.jvn.musilog.data.MusicSource;
import com.jvn.musilog.data.Track;

/** This activity is the adapter for the recyclerView
 * @author alexleyva
 * @since 2024-04-18
 *  */
// Credit to Poleon for helping create this class

/** This adapter class is an extenstion of "PlayListTrackVH" class
 *
 *  */
public class EditablePlaylistAdapter extends RecyclerView.Adapter<PlaylistTrackVH> {
    /** The activity the adapter is running under */
  private final AppCompatActivity activity;
    /** Our ArrayList based on the "Track" class */
  private final ArrayList<Track> PlaylistTrack;
    /** Boolean to show the play button */
  private final boolean displayPlayButton;

    /** Creates a new Playlist Adapter
     * @param activity
     * @param PlaylistTrack
     * @param displayPlayButton
     *  */
  public EditablePlaylistAdapter(
      AppCompatActivity activity, ArrayList<Track> PlaylistTrack, boolean displayPlayButton) {
    // this.context = context;
    this.activity = activity;
    this.PlaylistTrack = PlaylistTrack;
    this.displayPlayButton = displayPlayButton;
  }

    /** Views layout of "activity_entry"
     * @param parent
     * @param viewType
     *  */
  @NonNull
  @Override
  public PlaylistTrackVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // to inflate the layout for each item of recycler view
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_entry, parent, false);
    return new PlaylistTrackVH(activity, view);
  }

    /** Binds values retrived from "TrackMetadata" class
     * @param holder
     * @param position
     *  */
  @Override
  public void onBindViewHolder(@NonNull PlaylistTrackVH holder, int position) {
    // gets position of PlaylistTrack array
    Track track = PlaylistTrack.get(position);

    // UI update handler
    Handler updateUIHandler = new Handler(Looper.getMainLooper());
    // Thread for network calls to be able to get info from our links
    Thread metadataThread =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                TrackMetadata trackMetadata =
                    TrackMetadata.fromMusicSource(track.getSource(), track.getSourceId());

                updateUIHandler.post(
                    new Runnable() {
                      @Override
                      public void run() {
                        TrackMetadata thisMetaData;
                        boolean hidePlayButtons = false;
                        if (trackMetadata == null) {
                          thisMetaData = new TrackMetadata("(Unknown", "(Unknown)", null);
                          hidePlayButtons = true;
                        } else {
                          thisMetaData = trackMetadata;
                        }

                        if (displayPlayButton && !hidePlayButtons) {
                          holder.setButton_Type(track.getSource(), track.getSourceId());
                        }
                        holder.setArtists_Name(thisMetaData.getArtistLine());
                        holder.setSong_Name(thisMetaData.getTitle());
                        holder.setSong_Pic(thisMetaData.getCoverArtUrl());
                        //deletes entry when button is pressed
                        holder.deleteButton.setOnClickListener(
                            new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                Log.e("Pushed", "Delete Button has been pressed");
                                PlaylistTrack.remove(position);
                                notifyDataSetChanged();
                              }
                            });
                      }
                    });
              }
            });
    metadataThread.start();
  }

    /** @return PlaylistTrack's size */
  @Override
  public int getItemCount() {
    // this method is used for showing number of card items in recycler view
    return PlaylistTrack.size();
  }
}
