package my.tech.moviebucket.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import my.tech.moviebucket.DetailActivity;
import my.tech.moviebucket.R;
import my.tech.moviebucket.api.ApiClient;
import my.tech.moviebucket.entity.Movie;

public class ReleaseAdapter extends RecyclerView.Adapter<ReleaseAdapter.RealeaseViewHolder> {

    private ArrayList<Movie> releaseData;
    private Context context;

    public ReleaseAdapter(ArrayList<Movie> releaseData, Context context) {
        this.releaseData = releaseData;
        this.context = context;
    }

    @NonNull
    @Override
    public RealeaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_release, parent, false);

        return new RealeaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RealeaseViewHolder holder, int position) {
        holder.bind(releaseData.get(position));

        holder.itemView.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View v, int position) {
                Intent moveToDetail = new Intent(holder.itemView.getContext(), DetailActivity.class);
                moveToDetail.putExtra(DetailActivity.EXTRA_DATA, releaseData.get(position));
                holder.itemView.getContext().startActivity(moveToDetail);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return releaseData.size();
    }

    public class RealeaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgBackdrop;
        private TextView movieTitle, tvShowTitle;

        public RealeaseViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBackdrop = itemView.findViewById(R.id.image_backdrop);
            movieTitle = itemView.findViewById(R.id.title_movie);
            tvShowTitle = itemView.findViewById(R.id.title_tv_show);
        }

        public void bind(Movie movie) {
            Glide.with(itemView.getContext())
                    .load(ApiClient.IMAGE_URL + movie.getBackdropPath())
                    .placeholder(R.color.blue_active)
                    .error(R.color.bg_black)
                    .into(imgBackdrop);

            tvShowTitle.setText(movie.getOriginalName());
            movieTitle.setText(movie.getTitle());
        }
    }
}
