package com.android.spotifyapp.ui.adapters.Home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.spotifyapp.R;
import com.android.spotifyapp.data.network.model.RecentlyPlayed;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.android.spotifyapp.utils.ProgressBar;

public class HomeHorizontal extends RecyclerView.Adapter<HomeHorizontal.MyViewHolder> {
    private RecentlyPlayed recentlyPlayed;
    private View view;
    private android.widget.ProgressBar progressBar;
    public HomeHorizontal() {
        recentlyPlayed = new RecentlyPlayed();
    }

    @NonNull
    @Override
    public HomeHorizontal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeHorizontal.MyViewHolder holder, int position) {
            holder.song_name.setText(recentlyPlayed.getMitems().get(position).getTrack().getName());
            holder.artist_name.setText(recentlyPlayed.getMitems().get(position).getTrack().getArtists().get(0).getName());
            ProgressBar.progressBarVisible(holder.progressBar);

        Picasso.with(view.getContext())
                .load(recentlyPlayed.getMitems().get(position).getTrack().getAlbums().getImages().get(0).getUrl())
                .fit()
                .into(holder.album_image, new Callback() {
                    @Override
                    public void onSuccess() {
//                            ProgressBar.progressBarUnvisible(holder.progressBar);
                        Log.d("IMAGE", "onBindViewHolder: END");
                    }

                    @Override
                    public void onError() {
                        ProgressBar.progressBarUnvisible(holder.progressBar);
                    }
                });
    }

    @Override
    public int getItemCount() {
        if(recentlyPlayed.getMitems() != null)
        {
            return recentlyPlayed.getMitems().size();
        }
        return 0;

    }
    public void UpdateData(RecentlyPlayed recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView album_image;
        TextView song_name;
        TextView artist_name;
        android.widget.ProgressBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.album_image);
            song_name = itemView.findViewById(R.id.song_name);
            artist_name = itemView.findViewById(R.id.artist_name);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
