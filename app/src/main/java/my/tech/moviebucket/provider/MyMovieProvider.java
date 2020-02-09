package my.tech.moviebucket.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import my.tech.moviebucket.db.MovieFavoriteHelper;
import my.tech.moviebucket.db.TvShowFavoriteHelper;

import static my.tech.moviebucket.db.DatabaseContract.AUTHORITY;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.CONTENT_MOVIE;
import static my.tech.moviebucket.db.DatabaseContract.MovieFavoriteColumns.TABLE_MOVIE;
import static my.tech.moviebucket.db.DatabaseContract.TvShowFavoriteColumns.CONTENT_TV;
import static my.tech.moviebucket.db.DatabaseContract.TvShowFavoriteColumns.TABLE_TV;

public class MyMovieProvider extends ContentProvider {

    private static final int FAVORITE_MOVIE = 1;
    private static final int FAVORITE_MOVIE_ID = 2;
    private static final int FAVORITE_TV = 3;
    private static final int FAVORITE_TV_ID = 4;
    private MovieFavoriteHelper movieFavoriteHelper;
    private TvShowFavoriteHelper tvShowFavoriteHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, FAVORITE_MOVIE);
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE + " /#", FAVORITE_MOVIE_ID);

        sUriMatcher.addURI(AUTHORITY, TABLE_TV, FAVORITE_TV);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV + " /#", FAVORITE_TV_ID);
    }

    public MyMovieProvider() {
    }

    @Override
    public boolean onCreate() {
        movieFavoriteHelper = MovieFavoriteHelper.getInstance(getContext());
        movieFavoriteHelper.open();
        tvShowFavoriteHelper = TvShowFavoriteHelper.getInstance(getContext());
        tvShowFavoriteHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIE:
                cursor = movieFavoriteHelper.queryProvider();
                break;
            case FAVORITE_MOVIE_ID:
                cursor = movieFavoriteHelper.queryById(uri.getLastPathSegment());
                break;
            case FAVORITE_TV:
                cursor = tvShowFavoriteHelper.queryProvider();
                break;
            case FAVORITE_TV_ID:
                cursor = tvShowFavoriteHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        long added;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIE:
                added = movieFavoriteHelper.insertProvider(values);
                getContext().getContentResolver().notifyChange(CONTENT_MOVIE, null);
                _uri = Uri.parse(CONTENT_MOVIE + "/" + added);
                break;
            case FAVORITE_TV:
                added = tvShowFavoriteHelper.insertProvider(values);
                getContext().getContentResolver().notifyChange(CONTENT_TV, null);
                _uri = Uri.parse(CONTENT_TV + "/" + added);
                break;
            default:
                added = 0;
                break;
        }

        return _uri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIE_ID:
                updated = movieFavoriteHelper.update(uri.getLastPathSegment(), values);
                getContext().getContentResolver().notifyChange(CONTENT_MOVIE, null);
                break;
            case FAVORITE_TV_ID:
                updated = tvShowFavoriteHelper.update(uri.getLastPathSegment(), values);
                getContext().getContentResolver().notifyChange(CONTENT_TV, null);
                break;
            default:
                updated = 0;
                break;
        }

        return updated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_MOVIE_ID:
                deleted = movieFavoriteHelper.deleteById(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_MOVIE, null);

                break;
            case FAVORITE_TV_ID:
                deleted = tvShowFavoriteHelper.deleteById(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(CONTENT_TV, null);
                break;
            default:
                deleted = 0;
                break;
        }

        return deleted;
    }
}
