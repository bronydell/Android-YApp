package by.equestriadev.nikishin_rostislav.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapter.holders.ItemClickListener;
import by.equestriadev.nikishin_rostislav.adapter.holders.ShortcutHolder;
import by.equestriadev.nikishin_rostislav.model.AppShortcut;
import by.equestriadev.nikishin_rostislav.model.ShortcutType;
import by.equestriadev.nikishin_rostislav.persistence.entity.Shortcut;

/**
 * Created by Rostislav on 15.02.2018.
 * TODO: Fix fucking drag&drop
 */

public class HomeGridAdapter extends RecyclerView.Adapter<ShortcutHolder> implements ItemTouchHelperAdapter{

    public static final int ROW_COUNT = 30;
    private Map<Integer, Shortcut> mShortcutList = new HashMap<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemClickListener mOnLongClickListener;
    private ItemClickListener mOnBackgroundLongClickListener;
    private OnStartDragListener mDragStartListener;
    private int longClickDuration = 2000;
    private boolean isLongPress = false;

    public HomeGridAdapter(Context context, Map<Integer, Shortcut> shortcutList, OnStartDragListener dragListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mShortcutList = shortcutList;
        this.mDragStartListener = dragListener;
    }

    @Override
    public ShortcutHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.home_row, parent, false);
        return new ShortcutHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShortcutHolder holder, int position){
        Shortcut shortcut = mShortcutList.get(position);
        if(shortcut == null){
            holder.setVisible(View.INVISIBLE);
            holder.setOnLongClickListener(mOnBackgroundLongClickListener);
        }
        else{
            if(shortcut.getShortcutType() == ShortcutType.APPLICATION){
                AppShortcut appShortcut = (AppShortcut)shortcut;
                holder.setVisible(View.VISIBLE);
                holder.getShortcutIcon().setImageDrawable(appShortcut.getApplication().getIcon());
                holder.getShortcutText().setText(appShortcut.getApplication().getAppname());
            }
            else{
                holder.setVisible(View.VISIBLE);
                holder.getShortcutText().setText(shortcut.getTitle());
                if(shortcut.getShortcutType() == ShortcutType.URL) {
                    holder.getShortcutIcon().setImageDrawable(mContext.getResources().
                            getDrawable(R.drawable.ic_share));
                    holder.getShortcutIcon().setColorFilter(Color.rgb(66, 134, 244));
                }
                if(shortcut.getShortcutType() == ShortcutType.CONTACT) {
                    holder.getShortcutIcon().setImageDrawable(mContext.getResources().
                            getDrawable(R.drawable.ic_contact));
                    holder.getShortcutIcon().setColorFilter(Color.rgb(66, 134, 244));
                }
            }
            holder.setOnLongClickListener(mOnLongClickListener);
            holder.setOnItemClickListener(mClickListener);
            holder.getView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        holder.setHolded(true);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (holder.isHolded()) {
                                    Vibrator vibrator = (Vibrator)mContext.
                                            getSystemService(Context.VIBRATOR_SERVICE);
                                    if(vibrator != null)
                                        vibrator.vibrate(200);
                                    mDragStartListener.onStartDrag(holder);
                                }
                            }
                        }, longClickDuration);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        holder.setHolded(false);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ROW_COUNT;
    }

    public Shortcut getItemObject(int position){
        return mShortcutList.get(position);
    }

    public void setShortcutList(Map<Integer, Shortcut> shortcutList) {
        this.mShortcutList = shortcutList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setOnLongItemClickListener(ItemClickListener clickListener) {
        this.mOnLongClickListener = clickListener;
    }

    public void setOnBackgroundLongItemClickListener(ItemClickListener clickListener) {
        this.mOnBackgroundLongClickListener = clickListener;
    }

    @Override
    public void onItemDismiss(int position) {
        // Empty... For now
    }


    @Override
    public void onItemMove(ShortcutHolder source, ShortcutHolder from, ShortcutHolder to) {
        from.dehighlightView();
        to.highlightView();
    }

    @Override
    public void onItemFinishedMove(int fromPosition, int toPosition) {
        if(mShortcutList.get(fromPosition) != null){
            if(mShortcutList.get(toPosition) != null){
                Shortcut buffer = mShortcutList.get(fromPosition);
                buffer.setPosition(toPosition);
                mShortcutList.get(toPosition).setPosition(fromPosition);
                mShortcutList.put(fromPosition, mShortcutList.get(toPosition));
                mShortcutList.put(toPosition, buffer);
            }
            else{
                mShortcutList.get(fromPosition).setPosition(toPosition);
                mShortcutList.put(toPosition, mShortcutList.get(fromPosition));
                mShortcutList.remove(fromPosition);
            }
        }
        notifyDataSetChanged();
        mDragStartListener.onEndDrag(fromPosition, toPosition);
    }
}