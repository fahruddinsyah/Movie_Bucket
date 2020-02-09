package my.tech.moviebucket.widget;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import my.tech.moviebucket.db.DatabaseContract;
import my.tech.moviebucket.db.DatabaseHelper;
import my.tech.moviebucket.entity.Movie;

import static android.provider.BaseColumns._ID;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.BACKDROP;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.DESCRIPTION;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.POSTER;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.RATING;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.RELEASE;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.TITLE;

public class WidgetHelper {

    private static final String MOVIE_TABLE = DatabaseContract.MovieFavoriteColumns.TABLE_MOVIE;
    private static final String TV_TABLE = DatabaseContract.TvShowFavoriteColumns.TABLE_TV;

    private static DatabaseHelper databaseHelper;
    private static WidgetHelper INSTANCE;

    private static SQLiteDatabase database;

    private WidgetHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static WidgetHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WidgetHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(MOVIE_TABLE, null, null, null, null, null, null, null);
        Movie movie;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(RATING))));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<Movie> getAllTVShows() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor tvCursor = database.query(TV_TABLE, null, null, null, null, null, null, null);
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
                arrayList.add(movie);
                tvCursor.moveToNext();
            } while (!tvCursor.isAfterLast());
        }
        tvCursor.close();
        return arrayList;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }
}
