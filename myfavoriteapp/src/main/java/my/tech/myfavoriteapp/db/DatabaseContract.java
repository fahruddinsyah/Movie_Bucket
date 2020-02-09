package my.tech.myfavoriteapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.tech.moviebucket";
    private static final String SCHEME = "content";


    public static final class MovieFavoriteColumns implements BaseColumns {

        public static final String TABLE_MOVIE = "movie";

        public static final String TITLE = "title";
        public static final String DESCRIPTION = "overview";
        public static final String RATING = "vote_average";
        public static final String BACKDROP = "backdrop_path";
        public static final String POSTER = "poster_path";
        public static final String RELEASE = "release_date";

        public static final Uri CONTENT_MOVIE = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build();
    }

    public static final class TvShowFavoriteColumns implements BaseColumns {

        public static final String TABLE_TV = "tv";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "overview";
        public static final String RATING = "vote_average";
        public static final String BACKDROP = "backdrop_path";
        public static final String POSTER = "poster_path";

        public static final Uri CONTENT_TV = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_TV)
                .build();
    }
}
