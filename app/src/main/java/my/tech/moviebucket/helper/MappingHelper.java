package my.tech.moviebucket.helper;

import android.database.Cursor;

import java.util.ArrayList;

import my.tech.moviebucket.db.DatabaseContract;
import my.tech.moviebucket.entity.Movie;

import static android.provider.BaseColumns._ID;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.BACKDROP;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.DESCRIPTION;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.POSTER;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.RATING;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.RELEASE;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.TITLE;

public class MappingHelper {

    public static ArrayList<Movie> movieCursorToObject(Cursor cursor) {
        ArrayList<Movie> moviesList = new ArrayList<>();
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(RATING))));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));

                moviesList.add(movie);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return moviesList;
    }

    public static ArrayList<Movie> tvCursorToObject(Cursor tvCursor) {
        ArrayList<Movie> tvList = new ArrayList<>();
        tvCursor.moveToFirst();
        Movie movie;
        if (tvCursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(Integer.parseInt(tvCursor.getString(tvCursor.getColumnIndexOrThrow(_ID))));
                movie.setTitle(tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.TITLE)));
                movie.setOverview(tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.DESCRIPTION)));
                movie.setVoteAverage(Double.parseDouble(tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.RATING))));
                movie.setBackdropPath(tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.BACKDROP)));
                movie.setPosterPath(tvCursor.getString(tvCursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavoriteColumns.POSTER)));

                tvList.add(movie);
                tvCursor.moveToNext();

            } while (!tvCursor.isAfterLast());
        }
        tvCursor.close();
        return tvList;
    }
}
