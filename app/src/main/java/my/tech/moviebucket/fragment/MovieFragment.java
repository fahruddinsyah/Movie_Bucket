package my.tech.moviebucket.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import my.tech.moviebucket.R;
import my.tech.moviebucket.adapter.DiscoverAdapter;
import my.tech.moviebucket.adapter.ReleaseAdapter;
import my.tech.moviebucket.api.ApiClient;
import my.tech.moviebucket.api.GetService;
import my.tech.moviebucket.entity.Movie;
import my.tech.moviebucket.entity.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private RecyclerView rvRelease, rvDiscover;
    private GetService service;

    private Date date = new Date();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private final String DATE = dateFormat.format(date);

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        rvRelease = view.findViewById(R.id.rv_release_movie);
        rvDiscover = view.findViewById(R.id.rv_discover_movie);

        rvRelease.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvRelease.setHasFixedSize(true);
        rvDiscover.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDiscover.setHasFixedSize(true);

        service = ApiClient.getClient().create(GetService.class);
        getDataDiscover();
        getDataRelease();

        return view;
    }

    private void getDataDiscover() {
        Call<MovieResponse> call = service.getUpComingMovie(ApiClient.API_KEY, ApiClient.LANGUAGE);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                ArrayList<Movie> moviesDiscover = response.body().getResults();
                rvDiscover.setAdapter(new DiscoverAdapter(moviesDiscover, getContext()));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to Connect Internet", Toast.LENGTH_SHORT);
            }
        });
    }

    private void getDataRelease() {
        Call<MovieResponse> call = service.getReleaseMovies(ApiClient.API_KEY, DATE, DATE);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                ArrayList<Movie> moviesRelease = response.body().getResults();
                rvRelease.setAdapter(new ReleaseAdapter(moviesRelease, getContext()));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to Connect Internet", Toast.LENGTH_SHORT);

            }
        });
    }

}
