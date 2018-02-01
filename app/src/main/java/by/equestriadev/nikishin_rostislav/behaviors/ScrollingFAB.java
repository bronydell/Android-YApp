package by.equestriadev.nikishin_rostislav.behaviors;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Rostislav on 01.02.2018.
 */

public class ScrollingFAB extends FloatingActionButton.Behavior {

        public ScrollingFAB(Context context, AttributeSet attrs) {
            super();
        }

        public boolean onStartNestedScroll(CoordinatorLayout parent, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
            return true;
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
            if(dependency instanceof RecyclerView)
                return true;

            return false;
        }

        @Override
        public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                                   FloatingActionButton child, View target, int dxConsumed,
                                   int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            // TODO Auto-generated method stub
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                    dxUnconsumed, dyUnconsumed);

            if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
                child.hide();
            } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
                child.show();
            }
        }
}