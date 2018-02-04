package by.equestriadev.nikishin_rostislav.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import by.equestriadev.nikishin_rostislav.R;

/**
 * Created by Rostislav on 01.02.2018.
 */

public class LinkedRelativeLayout extends RelativeLayout implements View.OnClickListener {

    private String urlText;

    public LinkedRelativeLayout(Context context) {
        super(context);
        init();
    }

    public LinkedRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LinkedRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(){
        setOnClickListener(this);
    }

    private void init(Context context, AttributeSet attrs){

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LinkedTextView,
                0, 0);

        try {
            urlText = a.getString(R.styleable.LinkedTextView_url);
        } finally {
            a.recycle();
        }
        init();
    }

    @Override
    public void onClick(View v) {
        if(this.urlText != null) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(this.urlText));
            this.getContext().startActivity(i);
        }
    }
}
