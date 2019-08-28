package com.android.spotifyapp.ui.adapters.Home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.spotifyapp.R;
import com.android.spotifyapp.data.network.model.RecentlyPlayed;
import com.android.spotifyapp.data.network.model.UserTopTracks;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.android.spotifyapp.utils.TAGS.TAG4;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    android.view.View view;
    UserTopTracks userTopTracks;

    public SliderAdapter() {
        userTopTracks = new UserTopTracks();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        if(userTopTracks.getItems() != null) {
                    try {
                        viewHolder.slider_title.setText(userTopTracks.getItems().get(position).getName());
                        Picasso.with(view.getContext()).load(userTopTracks.getItems().get(position).getAlbum().getImages().get(0).getUrl()).fit().into(viewHolder.slider_image, new Callback() {
                            @Override
                            public void onSuccess() {
                                com.android.spotifyapp.utils.ProgressBar.progressBarUnvisible(viewHolder.progressBar);
                            }

                            @Override
                            public void onError() {
                                com.android.spotifyapp.utils.ProgressBar.progressBarUnvisible(viewHolder.progressBar);
                            }
                        });
                    } catch (Exception e) {
                        Log.d(TAG4, "onBindViewHolder: " + e.getMessage());
                    }
            }
        }


    @Override
    public int getCount() {
        if(userTopTracks.getItems() != null) {
            return userTopTracks.getItems().size();
        }
        return 0;
    }
    public void UpdateData(UserTopTracks userTopTracks) {
        this.userTopTracks = userTopTracks;
        notifyDataSetChanged();
    }
    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        TextView slider_title;
        ImageView slider_image;
        ProgressBar progressBar;

        public SliderAdapterVH(android.view.View itemView) {
            super(itemView);
            slider_title = itemView.findViewById(R.id.slider_title);
            slider_image = itemView.findViewById(R.id.slider_image);
            progressBar = itemView.findViewById(R.id.progressBar_slider_bg);
        }
    }
}
