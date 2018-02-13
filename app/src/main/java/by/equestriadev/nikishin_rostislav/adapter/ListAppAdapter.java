package by.equestriadev.nikishin_rostislav.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.holders.ListAppHolder;
import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class ListAppAdapter extends AppListAdapter{


    public ListAppAdapter(Context context, List<App> appList) {
        super(context, appList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.applist_row, parent, false);
        return new ListAppHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder baseHolder, int position) {
        ListAppHolder holder = (ListAppHolder)baseHolder;
        App application = mAppList.get(position);
        holder.getAppNameTextView().setText(application.
                getResolveInfo().loadLabel(mPackageManager));
        holder.getAppPackageTextView().setText(application.
                getResolveInfo().activityInfo.packageName);
        holder.getAppImageView().setImageDrawable((application.
                getResolveInfo().loadIcon(mPackageManager)));
        if(!application.getStatistics().isFavorite())
            holder.getFavoriteImageView().setVisibility(View.GONE);
        else
            holder.getFavoriteImageView().setVisibility(View.VISIBLE);
        holder.setOnItemClickListener(mClickListener);
        holder.setOnLongClickListener(mOnLongClickListener);
    }
}