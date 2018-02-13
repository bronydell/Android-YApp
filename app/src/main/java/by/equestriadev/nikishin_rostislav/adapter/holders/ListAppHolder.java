package by.equestriadev.nikishin_rostislav.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.views.CircledImageView;

/**
 * Created by Bronydell on 2/2/18.
 */

public class ListAppHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.app_icon)
    ImageView appIconCircledImageView;
    @BindView(R.id.fav_img)
    ImageView favoriteImageView;

    @BindView(R.id.app_name)
    TextView appNameTextView;
    @BindView(R.id.app_package)
    TextView appPackageTextView;

    private ItemClickListener mClickListener;
    private ItemClickListener mOnLongClickListener;

    public ListAppHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setOnLongClickListener(ItemClickListener itemClickListener) {
        this.mOnLongClickListener = itemClickListener;
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

    @Override
    public void onClick(View view) {
        if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        if (mOnLongClickListener != null)
            mOnLongClickListener.onItemClick(view, getAdapterPosition());
        return true;
    }
}
