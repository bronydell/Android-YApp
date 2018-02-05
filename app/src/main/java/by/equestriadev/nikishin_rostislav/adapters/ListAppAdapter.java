package by.equestriadev.nikishin_rostislav.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.holders.AppHolder;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;
import by.equestriadev.nikishin_rostislav.adapters.holders.ListAppHolder;
import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class ListAppAdapter extends RecyclerView.Adapter<ListAppHolder> {

    private List<App> appList = new ArrayList<>();
    private PackageManager mPackageManager;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemClickListener mOnLongClickListener;

    public ListAppAdapter(Context context, List<App> appList) {
        this.mInflater = LayoutInflater.from(context);
        this.mPackageManager = context.getPackageManager();
        this.appList = appList;
    }

    public void AddItem(App app, int position){
        appList.add(position, app);
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public ListAppHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.applist_row, parent, false);
        return new ListAppHolder(view);
    }

    @Override
    public void onBindViewHolder(ListAppHolder holder, int position) {
        App application = appList.get(position);
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
        holder.setOnClickListiner(mClickListener);
        holder.setOnLongClickListiner(mOnLongClickListener);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public App getItemObject(int position){
        return appList.get(position);
    }

    public void setAppList(List<App> appList) {
        this.appList = appList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setOnLongItemClickListener(ItemClickListener mClickListener) {
        this.mOnLongClickListener = mClickListener;
    }
}