package by.equestriadev.nikishin_rostislav.fragment;

import android.support.v7.widget.GridLayoutManager;

import java.util.ArrayList;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.GridAppAdapter;
import by.equestriadev.nikishin_rostislav.adapter.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AppLauncherFragment extends AppFragment{

    @Override
    protected void InitRecyclerView() {
        mAdapter = new GridAppAdapter(this.getContext(), new ArrayList<App>());
        initAdapter(mAdapter);

        appGrid.setAdapter(mAdapter);
        updateAppList();
    }

    @Override
    protected void initDecorators() {
        final int spanCount;
        if (mPrefs.getString(getString(R.string.layout_key), getString(R.string.default_layout)).
                equals(getResources().getStringArray(R.array.layout_options_values)[0])) {
            spanCount = getResources().getInteger(R.integer.compact);
        } else {
            spanCount = getResources().getInteger(R.integer.standart);
        }
        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.launcher_offset);
        mLayoutManager = new GridLayoutManager(this.getContext(), spanCount);
        mDecorator = new AppGridDecorator(spacingInPixels);
        decorateGrid(appGrid);
    }
}