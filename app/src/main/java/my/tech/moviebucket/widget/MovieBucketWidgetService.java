package my.tech.moviebucket.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MovieBucketWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MovieRemoteViewsFactory(this.getApplicationContext());
    }
}
