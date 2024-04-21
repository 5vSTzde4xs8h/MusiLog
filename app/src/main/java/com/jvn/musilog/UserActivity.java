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


// UserActivtiy class is the homepage of MusiLog
public class UserActivity extends AppCompatActivity {

    //Created a button variable
    Button editorButton;

    //Opens the "activity_user" page
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        // Sets editorButton as EditPlaylist_Button
        editorButton=(Button)findViewById(R.id.EditPlaylist_Button);

        //When button is clicked, the app will switch from "UserActivity" page to "PlaylistEditor" page
        editorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(UserActivity.this,PlaylistEditor.class);
                startActivity(intent);
        }

        });

    }



}
