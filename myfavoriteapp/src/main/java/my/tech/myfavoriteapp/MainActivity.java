package my.tech.myfavoriteapp;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.tech.myfavoriteapp.adapter.DiscoverAdapter;
import my.tech.myfavoriteapp.adapter.DiscoverTvAdapter;
import my.tech.myfavoriteapp.db.DatabaseContract;
import my.tech.myfavoriteapp.entity.Movie;
import my.tech.myfavoriteapp.helper.MappingHelper;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovieFavorite;
    private RecyclerView rvTvFavorite;

    private DiscoverAdapter adapter;
    private DiscoverTvAdapter adapterTv;
    private ArrayList<Movie> dataMovie = new ArrayList<>();
    private ArrayList<Movie> dataTv = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovieFavorite = findViewById(R.id.rv_favorite_movie);
        rvTvFavorite = findViewById(R.id.rv_favorite_tv);

        adapter = new DiscoverAdapter(dataMovie, this);
        adapterTv = new DiscoverTvAdapter(dataTv, this);

        rvMovieFavorite.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTvFavorite.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        rvMovieFavorite.setHasFixedSize(true);
        rvTvFavorite.setHasFixedSize(true);
        rvMovieFavorite.setAdapter(adapter);
        rvTvFavorite.setAdapter(adapterTv);

        Cursor movieCursor = getContentResolver().query(DatabaseContract.MovieFavoriteColumns.CONTENT_MOVIE, null, null, null, null);
        Cursor tvCursor = getContentResolver().query(DatabaseContract.TvShowFavoriteColumns.CONTENT_TV, null, null, null, null);

        if (movieCursor != null || tvCursor != null) {
            dataMovie = MappingHelper.movieCursorToObject(movieCursor);
            adapter.setData(dataMovie);
            dataTv = MappingHelper.tvCursorToObject(tvCursor);
            adapterTv.setDataTv(dataTv);
        }

    }

}
