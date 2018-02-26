package by.equestriadev.nikishin_rostislav.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.ListAppAdapter;
import by.equestriadev.nikishin_rostislav.adapter.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class AppListFragment extends AppFragment {

    public static AppListFragment newInstance() {
        AppListFragment fragment = new AppListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void InitRecyclerView() {
        mAdapter = new ListAppAdapter(this.getContext(), new ArrayList<App>());
        initAdapter(mAdapter);

        appGrid.setAdapter(mAdapter);
        update();
    }

    @Override
    protected void initDecorators() {
        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.launcher_offset);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mDecorator = new AppGridDecorator(spacingInPixels);
        decorateGrid(appGrid);
    }
}