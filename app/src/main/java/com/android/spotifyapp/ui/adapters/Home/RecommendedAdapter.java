package com.android.spotifyapp.ui.adapters.Home;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spotifyapp.R;
import com.android.spotifyapp.data.network.model.Recommendations;
import com.android.spotifyapp.utils.ProgressBar;
import com.squareup.picasso.Picasso;

import static com.android.spotifyapp.utils.TAGS.TAG2;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.MyViewHolder> {
    private Recommendations recommendations;
    private View view;

    public RecommendedAdapter() {
      recommendations = new Recommendations();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.artist.setText(recommendations.getMtracks().get(position).getMartists().get(0).getName().toUpperCase());
            if(recommendations.getMtracks().get(position).getArtist() != null && recommendations.getMtracks().get(position).getArtist().getImages().size() > 0) {
                Picasso.with(view.getContext())
                        .load(recommendations.getMtracks().get(position).getArtist().getImages().get(0).getUrl())
                        .fit()
                        .into(holder.artist_image);
                holder.followers.setText(view.getContext().getString(R.string.followers, recommendations.getMtracks().get(position).getArtist().getFollowers().getTotal()));
            }
    }
    public void UpdateData(Recommendations recommendations) {
        this.recommendations = recommendations;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(recommendations.getMtracks() != null) {
            return recommendations.getMtracks().size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView artist;
        ImageView artist_image;
        TextView followers;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            artist = itemView.findViewById(R.id.artist_name2);
            artist_image = itemView.findViewById(R.id.artist_image);
            followers = itemView.findViewById(R.id.followers);

        }
    }
}
