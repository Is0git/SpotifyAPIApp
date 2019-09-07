package com.android.spotifyapp.ui.adapters.Artist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.spotifyapp.R;
import com.android.spotifyapp.data.network.model.byId.ArtistsAlbum;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {
    private ArtistsAlbum artistsAlbum;
    private View view;

    @NonNull
    @Override
    public AlbumAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_album_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.MyViewHolder holder, int position) {
        if(artistsAlbum.getItems() != null) {
            if(artistsAlbum.getItems().get(position).getArtists() != null) {
                Picasso.with(view.getContext())
                        .load(artistsAlbum.getItems().get(position).getImages().get(0).getUrl())
                        .fit()
                        .into(holder.artist_album_image);
            }
            holder.songs_number_text.setText(String.valueOf(artistsAlbum.getItems().get(position).getTotal_tracks()));
            holder.release_date_text.setText(artistsAlbum.getItems().get(position).getRelease_date());
            holder.artist_number_text.setText(String.valueOf(artistsAlbum.getItems().get(position).getArtists().size()));
            holder.album_image_type_text.setText(artistsAlbum.getItems().get(position).getAlbum_type());
            holder.album_name.setText(artistsAlbum.getItems().get(position).getName());
            holder.release_date_precision.setText(artistsAlbum.getItems().get(position).getRelease_date_precision().toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        if(artistsAlbum != null) {
            return artistsAlbum.getItems().size();
        }
        return 0;
    }

    public void setArtistsAlbum(ArtistsAlbum artistsAlbum) {
        this.artistsAlbum = artistsAlbum;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.release_date_precision) TextView release_date_precision;
        @BindView(R.id.artist_album_image) ImageView artist_album_image;
        @BindView(R.id.songs_number_text) TextView songs_number_text;
        @BindView(R.id.release_date_text) TextView release_date_text;
        @BindView(R.id.artist_number_text) TextView artist_number_text;
        @BindView(R.id.album_image_type) TextView album_image_type_text;
        @BindView(R.id.artist_album_name_text) TextView album_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, view);
        }
    }
}
