package by.equestriadev.nikishin_rostislav.adapter;

/**
 * Created by Rostislav on 19.02.2018.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemFinishedMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
