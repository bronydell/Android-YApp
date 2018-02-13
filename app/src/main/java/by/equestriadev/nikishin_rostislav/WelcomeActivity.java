package by.equestriadev.nikishin_rostislav;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.adapter.PageAdapter;
import by.equestriadev.nikishin_rostislav.fragment.setup.SetupFragment;
import by.equestriadev.nikishin_rostislav.fragment.setup.WelcomeFinishFragment;
import by.equestriadev.nikishin_rostislav.fragment.setup.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tabDots)
    TabLayout mDots;

    @BindView(R.id.pager)
    ViewPager mViewPager;
    PagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String themeName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(getString(R.string.theme_key),
                        getString(R.string.default_theme));
        switch (themeName){
            case "light":
                this.setTheme(R.style.AppTheme);
                break;
            case "dark":
                this.setTheme(R.style.AppTheme_Dark);
                break;
        }
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.welcome_activity_name);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(WelcomeFragment.newInstance());
        fragments.add(SetupFragment.newInstance(getString(R.string.theme_key), R.array.theme_modes,
                R.array.theme_modes_values, -1,
                getString(R.string.default_theme)));
        fragments.add(SetupFragment.newInstance(getString(R.string.layout_key), R.array.layout_options,
                R.array.layout_options_values, R.array.layout_options_description,
                getString(R.string.default_layout)));
        fragments.add(WelcomeFinishFragment.newInstance());
        mAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mDots.setupWithViewPager(mViewPager, true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        YandexMetrica.reportEvent("Configuration has changed in About Activity");
    }

}
