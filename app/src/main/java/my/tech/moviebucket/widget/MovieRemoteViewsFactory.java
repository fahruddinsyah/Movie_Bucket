package my.tech.moviebucket.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import my.tech.moviebucket.R;
import my.tech.moviebucket.api.ApiClient;
import my.tech.moviebucket.entity.Movie;

public class MovieRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final ArrayList<String> mWidgetItems = new ArrayList<>();
    private WidgetHelper helper;
    private final Context mContext;

    public MovieRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
        helper = WidgetHelper.getInstance(mContext);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long indentityToken = Binder.clearCallingIdentity();
        helper.open();
        ArrayList<Movie> movies = helper.getAllMovies();
        ArrayList<Movie> tvShow = helper.getAllTVShows();
        for (Movie movie : movies) {
            mWidgetItems.add(movie.getPosterPath());
        }
        for (Movie movie : tvShow) {
            mWidgetItems.add(movie.getPosterPath());
        }

        Binder.restoreCallingIdentity(indentityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(ApiClient.IMAGE_URL + mWidgetItems.get(position))
                    .error(R.color.bg_black)
                    .submit()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.imageView, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(MovieBucketWidget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, intent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
