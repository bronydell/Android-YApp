package by.equestriadev.nikishin_rostislav.adapter.holders;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;

/**
 * Created by Rostislav on 19.02.2018.
 */

public class ShortcutHolder extends ListenableHolder implements ItemTouchHelperViewHolder {

    @BindView(R.id.shortcut_icon)
    ImageView shortcutIcon;

    @BindView(R.id.shortcut_text)
    TextView shortcutText;

    public ShortcutHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public ImageView getShortcutIcon() {
        return shortcutIcon;
    }

    public TextView getShortcutText() {
        return shortcutText;
    }

    @Override
    public void onItemSelected() {
        getView().setBackgroundColor(Color.LTGRAY);
    }

    public void setVisible(int status){
        getShortcutText().setVisibility(status);
        getShortcutIcon().setVisibility(status);
    }

    @Override
    public void onItemClear() {
        getView().setBackgroundColor(0);
    }
}
