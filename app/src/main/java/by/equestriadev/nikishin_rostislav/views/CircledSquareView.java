package by.equestriadev.nikishin_rostislav.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Bronydell on 2/2/18.
 */

public class CircledSquareView extends SquareView {

    private int color = Color.RED;

    public CircledSquareView(Context context) {
        super(context);
    }

    public CircledSquareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircledSquareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint);
    }
}
