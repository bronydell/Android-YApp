package by.equestriadev.nikishin_rostislav.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.PageAdapter;
import by.equestriadev.nikishin_rostislav.fragment.setup.WelcomeFragment;

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
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(GridFragment.newInstance());
        fragments.add(AppLauncherFragment.newInstance());
        fragments.add(ListOfAppsFragment.newInstance());
        fragments.add(SettingsFragment.newInstance());
        mPageAdapter = new PageAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(mPageAdapter);
        if(getArguments() != null) {
            viewPager.setCurrentItem(getArguments().getInt("start", 0));
        }
        return v;
    }

    public void changePage(int page){
        if(viewPager != null)
            viewPager.setCurrentItem(page);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }
}