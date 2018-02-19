package by.equestriadev.nikishin_rostislav.adapter.holders;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import by.equestriadev.nikishin_rostislav.adapter.ItemTouchHelperAdapter;

/**
 * Created by Rostislav on 19.02.2018.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    public static final float ALPHA_FULL = 1.0f;

    private boolean mOrderChanged = false;

    private int mFrom;
    private int mTo;

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager
        int dragFlags = 0;
        if (viewHolder instanceof ShortcutHolder && ((ShortcutHolder) viewHolder).
                getShortcutIcon().getVisibility() == View.VISIBLE)
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        mOrderChanged = true;
        mFrom = source.getAdapterPosition();
        mTo = target.getAdapterPosition();
        mAdapter.onItemMove(mFrom, mTo);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        // Notify the adapter of the dismissal
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if(!mOrderChanged){
                if (viewHolder instanceof ShortcutHolder) {
                    ShortcutHolder itemViewHolder = (ShortcutHolder) viewHolder;
                    itemViewHolder.onItemSelected();
                }
            }
            else{
                mOrderChanged = false;
            }

        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            ShortcutHolder itemViewHolder = (ShortcutHolder) viewHolder;
            itemViewHolder.onItemClear();
            if(mOrderChanged) {
                mAdapter.onItemFinishedMove(mFrom, mTo);
            }
        }
    }
}
