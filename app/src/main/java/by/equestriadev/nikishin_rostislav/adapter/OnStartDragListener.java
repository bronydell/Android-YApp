package by.equestriadev.nikishin_rostislav.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Rostislav on 19.02.2018.
 */

public interface OnStartDragListener {
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
    void onEndDrag(int fromPosition, int toPosition);
}
