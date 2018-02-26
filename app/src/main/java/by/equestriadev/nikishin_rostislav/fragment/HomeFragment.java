package by.equestriadev.nikishin_rostislav.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.AppUtils;
import by.equestriadev.nikishin_rostislav.MainActivity;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.PageAdapter;
import by.equestriadev.nikishin_rostislav.model.ShortcutType;
import by.equestriadev.nikishin_rostislav.persistence.AppDatabase;
import by.equestriadev.nikishin_rostislav.persistence.entity.Shortcut;

/**
 * Created by Rostislav on 20.02.2018.
 */

public class HomeFragment extends Fragment {

    @BindView(R.id.gridPager)
    ViewPager viewPager;

    private PageAdapter mPageAdapter;
    public static HomeFragment newInstance(int startPosition) {
        HomeFragment fragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putInt("start", startPosition);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_layout, container, false);
        ButterKnife.bind(this, v);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (prefs.getBoolean("firstBoot", true)) {
                    prefs.edit()
                            .putBoolean("firstBoot", false)
                            .apply();
                    AppUtils appUtils = new AppUtils(getContext(), AppDatabase.getDatabase(getContext()));
                    Shortcut shortcut = new Shortcut(0, "https://bronydell.xyz", "My site!",
                            ShortcutType.URL);
                    appUtils.addShortcut(shortcut, 10000);
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initViewPager();
                            }
                        });
                }
            }
        }).start();

        return v;
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeGridFragment.newInstance());
        fragments.add(AppGridFragment.newInstance());
        fragments.add(AppListFragment.newInstance());
        fragments.add(SettingsFragment.newInstance());
        mPageAdapter = new PageAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(mPageAdapter);
        updatePosition(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updatePosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(getArguments() != null) {
            updatePosition(getArguments().getInt("start", 0));
        }
    }

    public void updatePosition(int position) {
        if (getActivity() != null) {
            MainActivity activity = ((MainActivity) getActivity());
            activity.updateNav(position);
            if (activity.getSupportActionBar() != null) {
                String title = "None?";
                switch (position) {
                    case 0:
                        title = getString(R.string.nav_home);
                        break;
                    case 1:
                        title = getString(R.string.nav_launcher);
                        break;
                    case 2:
                        title = getString(R.string.nav_list);
                        break;
                    case 3:
                        title = getString(R.string.nav_settings);
                        break;
                }
                activity.getSupportActionBar().setTitle(title);
            }
        }
    }

    public void changePage(int page){
        if(viewPager != null) {
            viewPager.setCurrentItem(page);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }
}