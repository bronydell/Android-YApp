package by.equestriadev.nikishin_rostislav.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.views.SquareView;

/**
 * Created by Rostislav on 30.01.2018.
 */

public class AppHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener mClickListener;

    @BindView(R.id.app_icon)
    SquareView iconView;

    @BindView(R.id.color_text)
    TextView appNameTextView;

    public AppHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void setOnClickListiner(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }


    public SquareView getIconSquareView() {
        return iconView;
    }

    public TextView getAppNameTextView() {
        return appNameTextView;
    }

    @Override
    public void onClick(View view) {
        if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
    }
}