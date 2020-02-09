package my.tech.moviebucket.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import my.tech.moviebucket.R;
import my.tech.moviebucket.adapter.SearchAdapter;
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
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchView searchMovie;
    private RecyclerView rvSearch;

    private GetService service;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchMovie = view.findViewById(R.id.search_movie);
        rvSearch = view.findViewById(R.id.rv_search);

        service = ApiClient.getClient().create(GetService.class);
        searchMovie.setOnQueryTextListener(this);
        searchMovie.setQueryHint(getString(R.string.search));

        setupList();

        return view;
    }

    private void setupList() {
        rvSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearch.setHasFixedSize(true);
    }

    private void loadMovie() {
        String input_Movie = searchMovie.getQuery().toString();

        Call<MovieResponse> call = service.getItemSearch(input_Movie);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                ArrayList<Movie> setDataSearch = response.body().getResults();
                rvSearch.setAdapter(new SearchAdapter(setDataSearch, getContext()));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to Connect Internet", Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadMovie();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
