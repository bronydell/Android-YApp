package by.equestriadev.nikishin_rostislav.fragment;

import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.ListAppAdapter;
import by.equestriadev.nikishin_rostislav.adapter.decorators.AppGridDecorator;
import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class ListOfAppsFragment extends AppFragment{

    @Override
    protected void InitRecyclerView() {
        mAdapter = new ListAppAdapter(this.getContext(), new ArrayList<App>());
        initAdapter(mAdapter);

        appGrid.setAdapter(mAdapter);
        updateAppList();
    }

    @Override
    protected void initDecorators() {
        final int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.launcher_offset);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mDecorator = new AppGridDecorator(spacingInPixels);
        decorateGrid(appGrid);
    }
}