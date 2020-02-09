package my.tech.moviebucket.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import my.tech.moviebucket.DetailActivity;
import my.tech.moviebucket.R;
import my.tech.moviebucket.api.ApiClient;
import my.tech.moviebucket.entity.Movie;

public class DiscoverTvAdapter extends RecyclerView.Adapter<DiscoverTvAdapter.DiscoverViewModel> {

    private ArrayList<Movie> movies;
    private Context context;


    public DiscoverTvAdapter(ArrayList<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    public void setDataTv(ArrayList<Movie> data) {

        this.movies.clear();
        this.movies.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiscoverViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover, parent, false);

        return new DiscoverViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DiscoverViewModel holder, int position) {
        holder.bind(movies.get(position));

        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View v, int position) {
                Intent moveToDetail = new Intent(holder.itemView.getContext(), DetailActivity.class);
                moveToDetail.putExtra(DetailActivity.EXTRA_DATA, movies.get(position));
                holder.itemView.getContext().startActivity(moveToDetail);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class DiscoverViewModel extends RecyclerView.ViewHolder {

        private ImageView imgPoster;

        public DiscoverViewModel(@NonNull View itemView) {
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
