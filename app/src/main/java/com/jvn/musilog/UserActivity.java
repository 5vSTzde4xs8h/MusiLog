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

public class UserActivity extends AppCompatActivity {
    Button editorButton;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        editorButton=(Button)findViewById(R.id.EditPlaylist_Button);
        editorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(UserActivity.this,PlaylistEditor.class);
                startActivity(intent);
        }

        });

    }



}
