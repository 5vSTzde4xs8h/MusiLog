package com.jvn.musilog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


// This class adds the recycle view "activity_entry" to "activity_playlist
public class EditablePlaylistAdapter extends RecyclerView.Adapter<playlistVH> {
    public EditablePlaylistAdapter(List<String> entries) {
        this.entries = entries;
    }

    List<String> entries;

    @NonNull
    @Override
    public playlistVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_entry,parent,false);
    return new playlistVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull playlistVH holder, int position) {
        holder.textView.setText(entries.get(position));

    }

    @Override
    public int getItemCount() {
        return entries.size();
    }







}

// Playlist Viewholder class
class playlistVH extends RecyclerView.ViewHolder{
    TextView textView;
    private EditablePlaylistAdapter adapter;

    public playlistVH(@NonNull View itemView){
        super(itemView);

        textView = itemView.findViewById(R.id.Entry);
        itemView.findViewById(R.id.delete_Button).setOnClickListener(view -> {
            adapter.entries.remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
        })
        ;
    }

    public playlistVH linkAdapter (EditablePlaylistAdapter adapter){
        this.adapter = adapter;
        return this;
    }

}
