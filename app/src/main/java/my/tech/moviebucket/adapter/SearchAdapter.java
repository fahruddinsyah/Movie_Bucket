package my.tech.moviebucket.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import my.tech.moviebucket.DetailActivity;
import my.tech.moviebucket.R;
import my.tech.moviebucket.api.ApiClient;
import my.tech.moviebucket.entity.Movie;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<Movie> searchData;
    private Context context;

    public SearchAdapter(ArrayList<Movie> searchData, Context context) {
        this.searchData = searchData;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movie, parent, false);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int position) {
        holder.bind(searchData.get(position));

        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View v, int position) {
                Intent moveToDetail = new Intent(holder.itemView.getContext(), DetailActivity.class);
                moveToDetail.putExtra(DetailActivity.EXTRA_DATA, searchData.get(position));
                holder.itemView.getContext().startActivity(moveToDetail);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgPoster;
        private TextView titleMovie, descMovie;
        private RatingBar ratingMovie;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.poster_movie);
            titleMovie = itemView.findViewById(R.id.title_movie);
            descMovie = itemView.findViewById(R.id.desc_movie);
            ratingMovie = itemView.findViewById(R.id.rating_movie);
        }

        public void bind(Movie movie) {

            Glide.with(itemView.getContext())
                    .load(ApiClient.IMAGE_URL + movie.getPosterPath())
                    .placeholder(R.color.blue_active)
                    .error(R.color.bg_black)
                    .into(imgPoster);
            titleMovie.setText(movie.getTitle());
            descMovie.setText(movie.getOverview());
            ratingMovie.setRating((float) (movie.getVoteAverage() / 2));
        }
    }
}
