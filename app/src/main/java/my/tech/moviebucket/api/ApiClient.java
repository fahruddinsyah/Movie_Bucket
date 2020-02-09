package my.tech.moviebucket.api;

import my.tech.moviebucket.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String API_KEY = BuildConfig.TMDB_API_KEY;
    public static final String BASE_URL = "https://api.themoviedb.org/";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";
    public static String LANGUAGE = "en-US";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
