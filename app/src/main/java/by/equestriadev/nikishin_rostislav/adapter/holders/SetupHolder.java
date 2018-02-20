package by.equestriadev.nikishin_rostislav.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class SetupHolder extends ListenableHolder {

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

    public RadioButton getRadioButton() {
        return radioButton;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getDescriptionTextView() {
        return descriptionTextView;
    }

}