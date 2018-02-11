package by.equestriadev.nikishin_rostislav.fragments.setup;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yandex.metrica.YandexMetrica;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.R;
import by.equestriadev.nikishin_rostislav.adapters.SetupAdapter;
import by.equestriadev.nikishin_rostislav.adapters.holders.ItemClickListener;

/**
 * Created by Rostislav on 05.02.2018.
 */

public class SetupFragment extends Fragment {

    @BindView(R.id.theme_list)
    RecyclerView mThemeRecyclerView;

    private SetupAdapter mSetupAdapter;
    private SharedPreferences mSharedPrefs;
    private String mSettingKey;
    private String mDefault;
    private int mLabelArrayID;
    private int mValueArrayID;
    private int mDescriptionArrayID;

    public static SetupFragment newInstance(String key,
                                            int labels_id,
                                            int values_id,
                                            int description_id,
                                            String def) {
        SetupFragment fragment = new SetupFragment();

        Bundle args = new Bundle();
        args.putString("key", key);
        args.putString("default", def);
        args.putInt("label", labels_id);
        args.putInt("values", values_id);
        args.putInt("description", description_id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome_setup, container, false);
        ButterKnife.bind(this, v);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currentSetting = mSharedPrefs.getString(mSettingKey, mDefault);
        List<String> titles = Arrays.asList(getContext().getResources().getStringArray(mLabelArrayID));
        List<String> values = Arrays.asList(getContext().getResources().getStringArray(mValueArrayID));
        List<String> descriptions = mDescriptionArrayID == -1 ?
                null: Arrays.asList(getContext().getResources().getStringArray(mDescriptionArrayID));
        mSetupAdapter = new SetupAdapter(getContext(),
                titles,
                values,
                descriptions
                );
        mSetupAdapter.setSelectedIndex(values.indexOf(currentSetting));
        mSetupAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                editor.putString(mSettingKey, mSetupAdapter.getItemObject(position));
                mSetupAdapter.setSelectedIndex(position);
                editor.apply();
                mSetupAdapter.notifyDataSetChanged();
                if(mSettingKey.equals(getString(R.string.theme_key))) {
                    YandexMetrica.reportEvent("Theme has changed");
                    getActivity().recreate();
                }
            }
        });
        if(getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT)
            mThemeRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        else
            mThemeRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(),
                    LinearLayoutManager.HORIZONTAL, false));

        mThemeRecyclerView.setAdapter(mSetupAdapter);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingKey = getArguments().getString("key");
        mDefault = getArguments().getString("default");
        mLabelArrayID = getArguments().getInt("label");
        mValueArrayID = getArguments().getInt("values");
        mDescriptionArrayID = getArguments().getInt("description");
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(false);
    }
}
