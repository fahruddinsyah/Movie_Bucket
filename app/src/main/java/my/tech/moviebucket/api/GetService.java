package my.tech.moviebucket.api;

import my.tech.moviebucket.BuildConfig;
import my.tech.moviebucket.entity.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetService {

    @GET("3/discover/movie")
    Call<MovieResponse> getReleaseMovies(@Query("api_key") String apiKey,
                                         @Query("primary_release_date.gte") String gteDate,
                                         @Query("primary_release_date.lte") String lteDate);

    @GET("3/discover/tv")
    Call<MovieResponse> getDiscoverTvShow(@Query("api_key") String apiKey,
                                          @Query("language") String language);

    @GET("3/movie/upcoming")
    Call<MovieResponse> getUpComingMovie(@Query("api_key") String apiKey,
                                         @Query("language") String language);

    @GET("3/tv/on_the_air")
    Call<MovieResponse> getOnAirTvShow(@Query("api_key") String apiKey,
                                       @Query("language") String dateGte);

    @GET("3/search/movie?api_key=" + BuildConfig.TMDB_API_KEY)
    Call<MovieResponse> getItemSearch(@Query("query") String nameMovie);

}
