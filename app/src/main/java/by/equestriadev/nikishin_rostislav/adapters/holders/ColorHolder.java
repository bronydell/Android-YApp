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

public class ColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ItemClickListener mClickListener;
    private ItemClickListener mOnLongClickListener;

    @BindView(R.id.color_icon)
    SquareView iconView;

    @BindView(R.id.color_text)
    TextView appNameTextView;

    public ColorHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setOnClickListiner(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public void setOnLongClickListiner(ItemClickListener itemClickListener){
        this.mOnLongClickListener = itemClickListener;
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

    @Override
    public boolean onLongClick(View view) {
        if (mOnLongClickListener != null) mOnLongClickListener.onItemClick(view, getAdapterPosition());
        return true;
    }
}