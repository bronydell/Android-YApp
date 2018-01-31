package by.equestriadev.nikishin_rostislav.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.holders.AppHolder;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;

/**
 * Created by Rostislav on 30.01.2018.
 */

public class AppGridAdapter extends RecyclerView.Adapter<AppHolder> {

    private List<Integer> colorList = new ArrayList<>();

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public AppGridAdapter(Context context, List<Integer> colorList) {
        this.mInflater = LayoutInflater.from(context);
        this.colorList = colorList;
    }


    @Override
    public AppHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.launcher_row, parent, false);
        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(AppHolder holder, int position) {
        holder.getAppNameTextView().setText(String.format("#%06X", (0xFFFFFF & colorList.get(position))));
        holder.getIconSquareView().setBackgroundColor(colorList.get(position));
        holder.setOnClickListiner(mClickListener);
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public Integer getItemObject(int position){
        return colorList.get(position);
    }

    public void setOnItemClickListener(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }
}
