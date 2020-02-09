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
import static my.tech.moviebucket.db.DatabaseContract.TvShowFavoriteColumns.TABLE_TV;

public class TvShowFavoriteHelper {

    private static String DATABASE_TABLE = TABLE_TV;
    private static DatabaseHelper databaseHelper;
    private static TvShowFavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    private TvShowFavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static TvShowFavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvShowFavoriteHelper(context);
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
        ContentValues valuesTv = new ContentValues();
        valuesTv.put(_ID, movie.getId());
        valuesTv.put(DatabaseContract.TvShowFavoriteColumns.TITLE, movie.getOriginalName());
        valuesTv.put(DatabaseContract.TvShowFavoriteColumns.DESCRIPTION, movie.getOverview());
        valuesTv.put(DatabaseContract.TvShowFavoriteColumns.RATING, movie.getVoteAverage());
        valuesTv.put(DatabaseContract.TvShowFavoriteColumns.BACKDROP, movie.getBackdropPath());
        valuesTv.put(DatabaseContract.TvShowFavoriteColumns.POSTER, movie.getPosterPath());
        return database.insert(DATABASE_TABLE, null, valuesTv);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

    public boolean checkMovie(String id) {
        database = databaseHelper.getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_TV + " WHERE " + _ID + " =?";
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

















