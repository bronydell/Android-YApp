package by.equestriadev.nikishin_rostislav.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;
import by.equestriadev.nikishin_rostislav.adapters.holders.SetupHolder;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class SetupAdapter extends RecyclerView.Adapter<SetupHolder> {

    private List<String> mTitlesList = new ArrayList<>();
    private List<String> mDescriptionList = new ArrayList<>();
    private List<String> mValuesList = new ArrayList<>();

    private SetupHolder mLastChecked = null;
    private int mSelected = -1;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ItemClickListener mOnLongClickListener;

    public SetupAdapter(Context context, List<String> titles, List<String> values,
                        List<String> descriptions) {
        this.mInflater = LayoutInflater.from(context);
        this.mTitlesList = titles;
        this.mValuesList = values;
        this.mDescriptionList = descriptions;
    }

    @Override
    public SetupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.setup_row, parent, false);
        return new SetupHolder(view);
    }

    @Override
    public void onBindViewHolder(SetupHolder holder, int position) {
        holder.getTitleTextView().setText(mTitlesList.get(position));
        if(mDescriptionList != null && mDescriptionList.size() > position)
            holder.getDescriptionTextView().setText(mDescriptionList.get(position));
        holder.getRadioButton().setChecked(mSelected == position);
        holder.setOnClickListiner(mClickListener);
        holder.setOnLongClickListiner(mOnLongClickListener);
    }

    @Override
    public int getItemCount() {
        return mTitlesList.size();
    }

    public int getSelectedIndex(){
        return mSelected;
    }

    public void setSelectedIndex(int selectedIndex){
        this.mSelected = selectedIndex;
    }

    public String getItemObject(int position){
        return mValuesList.get(position);
    }

    public void setOnItemClickListener(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setOnLongItemClickListener(ItemClickListener mClickListener) {
        this.mOnLongClickListener = mClickListener;
    }
}