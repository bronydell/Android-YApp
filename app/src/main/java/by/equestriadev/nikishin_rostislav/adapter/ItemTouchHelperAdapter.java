package by.equestriadev.nikishin_rostislav.adapter;

import by.equestriadev.nikishin_rostislav.adapter.holders.ShortcutHolder;

/**
 * Created by Rostislav on 19.02.2018.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(ShortcutHolder from, ShortcutHolder to);
    void onItemFinishedMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
