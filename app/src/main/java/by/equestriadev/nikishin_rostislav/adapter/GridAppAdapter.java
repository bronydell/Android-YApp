package by.equestriadev.nikishin_rostislav.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.holders.AppHolder;
import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class GridAppAdapter extends AppListAdapter{

    public GridAppAdapter(Context context, List<App> appList) {
        super(context, appList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.app_row, parent, false);
        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder baseHolder, int position) {
        AppHolder holder = (AppHolder)baseHolder;
        App application = mAppList.get(position);
        holder.getAppNameTextView().setText(application.
                getApplicationInfo().getAppname());
        holder.getIconSquareView().setImageDrawable((application.
                getApplicationInfo().getIcon()));
        if(!application.getStatistics().isFavorite())
            holder.getFavImageView().setVisibility(View.GONE);
        else
            holder.getFavImageView().setVisibility(View.VISIBLE);
        holder.setOnItemClickListener(mClickListener);
        holder.setOnLongClickListener(mOnLongClickListener);
    }
}
