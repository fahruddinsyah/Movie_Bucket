package my.tech.moviebucket.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.tech.moviebucket.R;
import my.tech.moviebucket.adapter.DiscoverTvAdapter;
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
public class TvShowFragment extends Fragment {

    private RecyclerView rvOnAir, rvDiscover;
    private GetService service;


    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);

        rvOnAir = view.findViewById(R.id.rv_release_movie);
        rvDiscover = view.findViewById(R.id.rv_discover_movie);

        rvOnAir.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvOnAir.setHasFixedSize(true);
        rvDiscover.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvDiscover.setHasFixedSize(true);

        service = ApiClient.getClient().create(GetService.class);
        getDataDiscover();
        getDataOnAir();

        return view;
    }


    private void getDataOnAir() {
        Call<MovieResponse> call = service.getOnAirTvShow(ApiClient.API_KEY, ApiClient.LANGUAGE);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                ArrayList<Movie> tvShowOnAir = response.body().getResults();
                rvOnAir.setAdapter(new ReleaseAdapter(tvShowOnAir, getContext()));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to Connect Internet", Toast.LENGTH_SHORT);
            }
        });
    }

    private void getDataDiscover() {
        Call<MovieResponse> call = service.getDiscoverTvShow(ApiClient.API_KEY, ApiClient.LANGUAGE);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                ArrayList<Movie> tvShowDiscover = response.body().getResults();
                rvDiscover.setAdapter(new DiscoverTvAdapter(tvShowDiscover, getContext()));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to Connect Internet", Toast.LENGTH_SHORT);

            }
        });
    }

}
