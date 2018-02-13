package by.equestriadev.nikishin_rostislav.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import by.equestriadev.nikishin_rostislav.adapter.holders.ItemClickListener;
import by.equestriadev.nikishin_rostislav.model.App;

/**
 * Created by Rostislav on 13.02.2018.
 */

public abstract class AppListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    protected List<App> mAppList = new ArrayList<>();
    protected PackageManager mPackageManager;
    protected LayoutInflater mInflater;
    protected ItemClickListener mClickListener;
    protected ItemClickListener mOnLongClickListener;

    public AppListAdapter(Context context, List<App> appList) {
        this.mInflater = LayoutInflater.from(context);
        this.mPackageManager = context.getPackageManager();
        this.mAppList = appList;
    }

    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mAppList.size();
    }

    public App getItemObject(int position){
        return mAppList.get(position);
    }

    public void setAppList(List<App> appList) {
        this.mAppList = appList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setOnLongItemClickListener(ItemClickListener mClickListener) {
        this.mOnLongClickListener = mClickListener;
    }
}
