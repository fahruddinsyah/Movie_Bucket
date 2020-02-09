package my.tech.moviebucket.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import my.tech.moviebucket.entity.Movie;

import static android.provider.BaseColumns._ID;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.BACKDROP;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.DESCRIPTION;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.POSTER;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.RATING;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.RELEASE;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.TABLE_MOVIE;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.TITLE;

public class MovieFavoriteHelper {

    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DatabaseHelper databaseHelper;
    private static MovieFavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieFavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static MovieFavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieFavoriteHelper(context);
                }
            }
        }

        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC"
        );
    }

    public Cursor queryById(String id) {
        return database.query(
                DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null
        );
    }

    public long insert(Movie movie) {
        ContentValues valuesMovie = new ContentValues();
        valuesMovie.put(_ID, movie.getId());
        valuesMovie.put(TITLE, movie.getTitle());
        valuesMovie.put(DESCRIPTION, movie.getOverview());
        valuesMovie.put(RATING, movie.getVoteAverage());
        valuesMovie.put(RELEASE, movie.getReleaseDate());
        valuesMovie.put(BACKDROP, movie.getBackdropPath());
        valuesMovie.put(POSTER, movie.getPosterPath());
        return database.insert(DATABASE_TABLE, null, valuesMovie);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

    public boolean checkMovie(String id) {
        database = databaseHelper.getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_MOVIE + " WHERE " + _ID + " =?";
        Cursor cursor = database.rawQuery(selectString, new String[]{id});
        boolean checkMovie = false;
        if (cursor.moveToFirst()) {
            checkMovie = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
            Log.d(TAG, String.format("%d records found", count));
        }
        cursor.close();
        return checkMovie;
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }
}


















