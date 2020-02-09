package my.tech.myfavoriteapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import my.tech.myfavoriteapp.R;
import my.tech.myfavoriteapp.api.ApiClient;
import my.tech.myfavoriteapp.entity.Movie;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> {

    private ArrayList<Movie> movies;
    private Context context;

    public DiscoverAdapter(ArrayList<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    public void setData(ArrayList<Movie> data) {

        this.movies.clear();
        this.movies.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover, parent, false);

        return new DiscoverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DiscoverViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class DiscoverViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgPoster;

        public DiscoverViewHolder(View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.item_discover_picture);

        }

        public void bind(Movie movie) {
            Glide.with(itemView.getContext())
                    .load(ApiClient.IMAGE_URL + movie.getPosterPath())
                    .placeholder(R.color.blue_active)
                    .error(R.color.bg_black)
                    .into(imgPoster);
        }
    }
}
