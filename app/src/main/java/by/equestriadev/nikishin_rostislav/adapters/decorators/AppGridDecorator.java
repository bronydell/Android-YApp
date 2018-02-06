package by.equestriadev.nikishin_rostislav.adapters.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Rostislav on 31.01.2018.
 */

public class AppGridDecorator extends RecyclerView.ItemDecoration {
    private int offset;

    public AppGridDecorator(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = offset;
        outRect.right = offset;
        outRect.bottom = offset;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = offset;
        } else {
            outRect.top = 0;
        }
    }
}