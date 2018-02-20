package by.equestriadev.nikishin_rostislav.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;

/**
 * Created by Bronydell on 2/2/18.
 */

public class ListAppHolder extends ListenableHolder {

    @BindView(R.id.app_icon)
    ImageView appIconCircledImageView;
    @BindView(R.id.fav_img)
    ImageView favoriteImageView;

    @BindView(R.id.app_name)
    TextView appNameTextView;
    @BindView(R.id.app_package)
    TextView appPackageTextView;


    public ListAppHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getAppImageView() {
        return appIconCircledImageView;
    }

    public TextView getAppNameTextView() {
        return appNameTextView;
    }

    public ImageView getFavoriteImageView() {
        return favoriteImageView;
    }

    public TextView getAppPackageTextView() {
        return appPackageTextView;
    }


}
