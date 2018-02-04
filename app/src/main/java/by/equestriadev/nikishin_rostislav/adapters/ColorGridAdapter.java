package by.equestriadev.nikishin_rostislav.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.holders.ColorHolder;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;

/**
 * Created by Rostislav on 30.01.2018.
 */

public class ColorGridAdapter extends RecyclerView.Adapter<ColorHolder> {

    private List<Integer> colorList = new ArrayList<>();

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemClickListener mOnLongClickListener;

    public ColorGridAdapter(Context context, List<Integer> colorList) {
        this.mInflater = LayoutInflater.from(context);
        this.colorList = colorList;
    }

    public void AddItem(Integer color, int position){
        colorList.add(position, color);
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.launcher_row, parent, false);
        return new ColorHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorHolder holder, int position) {
        holder.getAppNameTextView().setText(String.format("#%06X", (0xFFFFFF & colorList.get(position))));
        holder.getIconSquareView().setBackgroundColor(colorList.get(position));
        holder.setOnClickListiner(mClickListener);
        holder.setOnLongClickListiner(mOnLongClickListener);
    }

    public void removeAt(int position){
        colorList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, colorList.size());
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

    public void setOnLongItemClickListener(ItemClickListener mClickListener) {
        this.mOnLongClickListener = mClickListener;
    }
}
