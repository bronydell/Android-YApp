package by.equestriadev.nikishin_rostislav.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.MainActivity;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.PageAdapter;
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(getClass().getName(), position + " viewpager position");
                Activity activity = getActivity();
                ((MainActivity)activity).updateNav(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(getArguments() != null) {
            viewPager.setCurrentItem(getArguments().getInt("start", 0));
        }
        return v;
    }

    public void changePage(int page){
        if(viewPager != null) {
            Log.d(getClass().getName(), "Changing page on " + page);
            viewPager.setCurrentItem(page);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }
}