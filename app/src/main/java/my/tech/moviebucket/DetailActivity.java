package my.tech.moviebucket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import my.tech.moviebucket.api.ApiClient;
import my.tech.moviebucket.db.MovieFavoriteHelper;
import my.tech.moviebucket.db.TvShowFavoriteHelper;
import my.tech.moviebucket.entity.Movie;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgBackdrop, imgPoster;
    private TextView titleMovie, titleShow, descMovie, releaseMovie;
    private RatingBar ratingMovie;
    private Button btnFavorite;

    private Movie movie;
    private int position;
    private boolean isFavorite = false;
    private MovieFavoriteHelper movieFavoriteHelper;
    private TvShowFavoriteHelper tvShowFavoriteHelper;

    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_POSITION = "extra_position";
    private static final int RESULT_VALUE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgBackdrop = findViewById(R.id.img_Backdrop);
        imgPoster = findViewById(R.id.img_Poster);
        titleMovie = findViewById(R.id.title_movie);
        titleShow = findViewById(R.id.title_tv_show);
        descMovie = findViewById(R.id.desc_movie);
        releaseMovie = findViewById(R.id.release_movie);
        ratingMovie = findViewById(R.id.rating_movie);
        btnFavorite = findViewById(R.id.btn_Favorite);

        movieFavoriteHelper = MovieFavoriteHelper.getInstance(getApplicationContext());
        tvShowFavoriteHelper = TvShowFavoriteHelper.getInstance(getApplicationContext());
        movie = getIntent().getParcelableExtra(EXTRA_DATA);

        String idMovie = Integer.toString(movie.getId());
        String btnTitle = getString(R.string.unfavorite_mov);

        if (movieFavoriteHelper.checkMovie(idMovie) || tvShowFavoriteHelper.checkMovie(idMovie)) {
            btnFavorite.setText(btnTitle);
        }

        setDetailMovie();

        btnFavorite.setOnClickListener(this);
    }

    private void setDetailMovie() {
        movie = getIntent().getParcelableExtra(EXTRA_DATA);
        if (movie != null) {
            Glide.with(this)
                    .load(ApiClient.IMAGE_URL + movie.getBackdropPath())
                    .placeholder(R.color.blue_active)
                    .error(R.color.bg_black)
                    .into(imgBackdrop);

            Glide.with(this)
                    .load(ApiClient.IMAGE_URL + movie.getPosterPath())
                    .placeholder(R.color.blue_active)
                    .error(R.color.bg_black)
                    .into(imgPoster);

            titleMovie.setText(movie.getTitle());
            titleShow.setText(movie.getOriginalName());
            descMovie.setText(movie.getOverview());
            releaseMovie.setText(movie.getReleaseDate());
            ratingMovie.setRating((float) (movie.getVoteAverage() / 2));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Favorite) {
            Movie movie = getIntent().getParcelableExtra(EXTRA_DATA);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATA, movie);
            intent.putExtra(EXTRA_POSITION, position);

            if (!isFavorite) {

                long resultMovie = movieFavoriteHelper.insert(movie);
                long resultTv = tvShowFavoriteHelper.insert(movie);

                if (resultMovie > RESULT_VALUE || resultTv > RESULT_VALUE) {
                    movie.setId((int) resultMovie);
                    movie.setId((int) resultTv);

                    btnFavorite.setText(getString(R.string.unfavorite_mov));
                    Toast.makeText(this, getString(R.string.succes_add_favorite), Toast.LENGTH_SHORT).show();
                } else {
                    long removeMovie = movieFavoriteHelper.deleteById(String.valueOf(movie.getId()));
                    long removeTv = tvShowFavoriteHelper.deleteById(String.valueOf(movie.getId()));

                    if (removeMovie > RESULT_VALUE || removeTv > RESULT_VALUE) {
                        intent.putExtra(EXTRA_POSITION, position);
                        Toast.makeText(this, getString(R.string.succes_unfavorite), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }

    }
}
