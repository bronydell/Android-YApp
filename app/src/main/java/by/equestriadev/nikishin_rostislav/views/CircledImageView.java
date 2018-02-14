package by.equestriadev.nikishin_rostislav.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by Rostislav on 25.01.2018.
 */

public class CircledImageView  extends android.support.v7.widget.AppCompatImageView {

    public boolean mCircled;
    public boolean mBackgroundCircled;

    public CircledImageView(Context context) {
        super(context);
    }

    public CircledImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircledImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void ResetEverything(){
        mCircled = false;
        mBackgroundCircled = false;

    }
    /**
     * In case the bitmap is manually changed, we make sure to
     * circle it on the next onDraw
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        ResetEverything();
        super.setImageBitmap(bm);
    }

    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        ResetEverything();
        super.setBackgroundDrawable(drawable);
    }

    /**
     * In case the bitmap is manually changed, we make sure to
     * circle it on the next onDraw
     */
    @Override
    public void setImageResource(int resId) {
        ResetEverything();
        super.setImageResource(resId);
    }

    /**
     * In case the bitmap is manually changed, we make sure to
     * circle it on the next onDraw
     */
    @Override
    public void setImageDrawable(Drawable drawable) {
        ResetEverything();
        super.setImageDrawable(drawable);
    }

    /**
     * We want to make sure that the ImageView has the same height and width
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int diw = drawable.getIntrinsicWidth();
            if (diw > 0) {
                int height = width * drawable.getIntrinsicHeight() / diw;
                setMeasuredDimension(width, height);
            } else
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Let's circle the image
        if ( !mCircled && getDrawable() != null) {
            Bitmap bitmap = ProcessDrawable(getDrawable());
            if(bitmap != null) {
                setImageBitmap(bitmap);
            }
            mCircled = true;
        }
        if ( !mBackgroundCircled && getBackground() != null) {
            Bitmap bitmap = ProcessDrawable(getBackground());
            if(bitmap != null) {
                setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
            }
            mBackgroundCircled = true;
        }
        super.onDraw(canvas);
    }

    private Bitmap ProcessDrawable(Drawable d){
        try {
            Bitmap bitmap = drawableToBitmap(d);
            return getCircleBitmap(bitmap);
        } catch (IllegalArgumentException ex) {

            return null;
        }
    }

    /**
     * Method used to circle a bitmap.
     *
     * @param bitmap The bitmap to circle
     * @return The circled bitmap
     */
    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());

        Bitmap output = Bitmap.createBitmap(size,
                size, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        BitmapShader shader;
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        RectF rect = new RectF(0, 0 ,size,size);
        int radius = size/2;
        canvas.drawRoundRect(rect, radius,radius, paint);
        return output;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}