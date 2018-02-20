package by.equestriadev.nikishin_rostislav.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;

/**
 * Created by Rostislav on 04.02.2018.
 */

public class AppHolder extends ListenableHolder{

    @BindView(R.id.app_icon)
    ImageView iconView;

    @BindView(R.id.app_fav)
    ImageView favView;

    @BindView(R.id.app_text)
    TextView appNameTextView;

    public AppHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public ImageView getIconSquareView() {
        return iconView;
    }

    public TextView getAppNameTextView() {
        return appNameTextView;
    }

    public ImageView getFavImageView() {
        return favView;
    }


}