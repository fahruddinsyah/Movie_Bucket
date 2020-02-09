package my.tech.moviebucket;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import my.tech.moviebucket.fragment.NavigationFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = new NavigationFragment();
            addFragment(fragment, R.id.contentFrame);
        }

    }

    private void addFragment(Fragment fragment, int contentFrame) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(contentFrame, fragment);
        ft.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
