package my.tech.moviebucket.fragment;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import my.tech.moviebucket.R;
import my.tech.moviebucket.adapter.DiscoverAdapter;
import my.tech.moviebucket.adapter.DiscoverTvAdapter;
import my.tech.moviebucket.db.DatabaseContract;
import my.tech.moviebucket.entity.Movie;
import my.tech.moviebucket.helper.MappingHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements LoadMovieCallback, LoadTvCallback {

    private DiscoverAdapter adapter;
    private DiscoverTvAdapter adapterTv;
    private ArrayList<Movie> dataMovie = new ArrayList<>();
    private ArrayList<Movie> dataTv = new ArrayList<>();

    private final static String STATE_KEY_MOVIE = "STATE_MOVIE";
    private final static String STATE_KEY_TV = "STATE_TV";

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        RecyclerView rvMovieFavorite = view.findViewById(R.id.rv_favorite_movie);
        RecyclerView rvTvFavorite = view.findViewById(R.id.rv_favorite_tv);

        adapter = new DiscoverAdapter(dataMovie, getContext());
        adapterTv = new DiscoverTvAdapter(dataTv, getContext());

        rvMovieFavorite.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvTvFavorite.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvMovieFavorite.setHasFixedSize(true);
        rvTvFavorite.setHasFixedSize(true);
        rvMovieFavorite.setAdapter(adapter);
        rvTvFavorite.setAdapter(adapterTv);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, getContext());
        Objects.requireNonNull(getContext()).getContentResolver().registerContentObserver(DatabaseContract.MovieFavoriteColumns.CONTENT_MOVIE, true, myObserver);
        Objects.requireNonNull(getContext()).getContentResolver().registerContentObserver(DatabaseContract.TvShowFavoriteColumns.CONTENT_TV, true, myObserver);

        if (savedInstanceState == null) {
            //proses ambil data
            new LoadMovieAsync(getContext(), this).execute();
            new LoadTvAsync(getContext(), this).execute();
        } else {
            ArrayList<Movie> listMovie = savedInstanceState.getParcelableArrayList(STATE_KEY_MOVIE);
            if (listMovie != null) {
                dataMovie = listMovie;
                adapter.setData(dataMovie);
            }

            ArrayList<Movie> listTv = savedInstanceState.getParcelableArrayList(STATE_KEY_TV);
            if (listTv != null) {
                dataTv = listTv;
                adapterTv.setDataTv(dataTv);
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_KEY_MOVIE, dataMovie);
        outState.putParcelableArrayList(STATE_KEY_TV, dataTv);
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadMovieAsync(getContext(), this).execute();
        new LoadTvAsync(getContext(), this).execute();
    }

    @Override
    public void preExecute() {
    }

    @Override
    public void postExecute2(ArrayList<Movie> movies) {
        if (movies.size() > 0) {
            dataTv = movies;
            adapterTv.setDataTv(dataTv);
        } else {
            adapterTv.setDataTv(new ArrayList<Movie>());
        }
    }

    @Override
    public void postExecute(ArrayList<Movie> movies) {
        if (movies.size() > 0) {
            dataMovie = movies;
            adapter.setData(movies);
        } else {
            adapter.setData(new ArrayList<Movie>());
        }
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieCallback> weakCallback;

        private LoadMovieAsync(Context context, LoadMovieCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.MovieFavoriteColumns.CONTENT_MOVIE, null, null, null, null);
            return MappingHelper.movieCursorToObject(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    private static class LoadTvAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTvCallback> weakCallback;

        private LoadTvAsync(Context context, LoadTvCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.TvShowFavoriteColumns.CONTENT_TV, null, null, null, null);
            return MappingHelper.tvCursorToObject(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute2(movies);
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        private DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadMovieAsync(context, (LoadMovieCallback) context).execute();
            new LoadTvAsync(context, (LoadTvCallback) context).execute();

        }
    }
}

interface LoadTvCallback {
    void preExecute();

    void postExecute2(ArrayList<Movie> movies);
}

interface LoadMovieCallback {
    void preExecute();

    void postExecute(ArrayList<Movie> movies);
}
