package by.equestriadev.nikishin_rostislav.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Rostislav on 15.02.2018.
 */

public abstract class ListenableHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    protected ItemClickListener mClickListener;
    protected ItemClickListener mOnLongClickListener;
    protected View rootView;
    public ListenableHolder(View itemView) {
        super(itemView);
        this.rootView = itemView;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public View getView(){
        return rootView;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setOnLongClickListener(ItemClickListener itemClickListener) {
        this.mOnLongClickListener = itemClickListener;
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
