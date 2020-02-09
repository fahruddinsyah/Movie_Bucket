package my.tech.moviebucket.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmovieapp";
    private static final int DATABASE_VERSION = 1;


    private static final String SQL_CREATE_TABLE_FAVORITE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.MovieFavoriteColumns.TABLE_MOVIE,
            DatabaseContract.MovieFavoriteColumns._ID,
            DatabaseContract.MovieFavoriteColumns.TITLE,
            DatabaseContract.MovieFavoriteColumns.DESCRIPTION,
            DatabaseContract.MovieFavoriteColumns.RATING,
            DatabaseContract.MovieFavoriteColumns.RELEASE,
            DatabaseContract.MovieFavoriteColumns.BACKDROP,
            DatabaseContract.MovieFavoriteColumns.POSTER
    );

    private static final String SQL_CREATE_TABLE_TV = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TvShowFavoriteColumns.TABLE_TV,
            DatabaseContract.TvShowFavoriteColumns._ID,
            DatabaseContract.TvShowFavoriteColumns.TITLE,
            DatabaseContract.TvShowFavoriteColumns.DESCRIPTION,
            DatabaseContract.TvShowFavoriteColumns.RATING,
            DatabaseContract.TvShowFavoriteColumns.BACKDROP,
            DatabaseContract.TvShowFavoriteColumns.POSTER
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
        db.execSQL(SQL_CREATE_TABLE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.MovieFavoriteColumns.TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TvShowFavoriteColumns.TABLE_TV);
        onCreate(db);
    }
}
