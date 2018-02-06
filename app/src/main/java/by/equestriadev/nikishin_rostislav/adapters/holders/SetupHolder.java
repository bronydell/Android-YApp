package by.equestriadev.nikishin_rostislav.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.views.SquareView;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class SetupHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private ItemClickListener mClickListener;
    private ItemClickListener mOnLongClickListener;

    @BindView(R.id.radioButton)
    RadioButton radioButton;
    @BindView(R.id.titleTextView)
    TextView titleTextView;


    @BindView(R.id.descriptionText)
    TextView descriptionTextView;

    public SetupHolder(View itemView) {
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

    public RadioButton getRadioButton() {
        return radioButton;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getDescriptionTextView() {
        return descriptionTextView;
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