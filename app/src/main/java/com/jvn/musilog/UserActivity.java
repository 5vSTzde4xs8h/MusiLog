package com.jvn.musilog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.meta.When;

/**
 * The Activity for User's homepage
 * @author Alex Leyva
 * @since 2024-04-06
 */

/** UserActivtiy class is the homepage of MusiLog*/
public class UserActivity extends AppCompatActivity {

  /** The Button that will navigate user from this activity(User's Homepage) to Playlist Editor */
  Button editorButton;

  /** Initializes(or creates) the the activity and UI bindings to help navigate around the app
   * @param savedInstanceState stores a bit of data to recreate UI reinitialized
   * */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_user);

    /** Sets editorButton as EditPlaylist_Button from activity_playlist_editor.xml */
    editorButton = (Button) findViewById(R.id.EditPlaylist_Button);

    /** When button is clicked, the app will switch from "UserActivity" page to "PlaylistEditor"
     *  page */
    editorButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(UserActivity.this, PlaylistEditor.class);
            startActivity(intent);

          }
        });
  }
}
