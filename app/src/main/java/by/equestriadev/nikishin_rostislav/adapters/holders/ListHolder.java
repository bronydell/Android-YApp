package by.equestriadev.nikishin_rostislav.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.views.CircledSquareView;

/**
 * Created by Bronydell on 2/2/18.
 */

public class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.color_icon)
    CircledSquareView colorView;
    @BindView(R.id.color_text)
    TextView colorNameTextView;
    @BindView(R.id.another_text)
    TextView secondColorTextView;
    private ItemClickListener mClickListener;
    private ItemClickListener mOnLongClickListener;

    public ListHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setOnClickListiner(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setOnLongClickListiner(ItemClickListener itemClickListener) {
        this.mOnLongClickListener = itemClickListener;
    }


    public CircledSquareView getColorSquareView() {
        return colorView;
    }

    public TextView getColorTextView() {
        return colorNameTextView;
    }

    public TextView getSecondColorTextView() {
        return secondColorTextView;
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
