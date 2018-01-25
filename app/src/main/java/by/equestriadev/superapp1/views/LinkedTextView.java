package by.equestriadev.superapp1.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import by.equestriadev.superapp1.R;

/**
 * Created by Rostislav on 25.01.2018.
 */

public class LinkedTextView extends android.support.v7.widget.AppCompatTextView  implements View.OnClickListener{

    private String urlText;

    public LinkedTextView(Context context) {
        super(context);
        init();
    }

    public LinkedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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

    public LinkedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setOnClickListener(this);
    }

    public String getUrl() {
        return urlText;
    }

    public void setUrl(String urlText) {
        if (!urlText.startsWith("http://") && !urlText.startsWith("https://"))
            urlText = "http://" + urlText;
        this.urlText = urlText;
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
