package by.equestriadev.nikishin_rostislav;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yandex.metrica.YandexMetrica;

import butterknife.BindView;
import butterknife.ButterKnife;
import by.equestriadev.nikishin_rostislav.broadcast.SilentPushReceiver;
import by.equestriadev.nikishin_rostislav.fragment.IUpdatable;

public class AboutActivity extends AppCompatActivity implements IUpdatable {

    @BindView(R.id.about_toolbar)
    Toolbar toolbar;

    @BindView(R.id.messageLayout)
    RelativeLayout messageLayout;

    @BindView(R.id.messageText)
    TextView messageText;

    SharedPreferences mPrefs;
    SilentPushReceiver mSilentPushReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String themeName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString(getString(R.string.theme_key),
                getString(R.string.default_theme));
        switch (themeName){
            case "light":
                this.setTheme(R.style.AppTheme);
                break;
            case "dark":
                this.setTheme(R.style.AppTheme_Dark);
                break;
        }
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            YandexMetrica.reportEvent("About activity configuration is portrait");
        } else {
            YandexMetrica.reportEvent("About activity configuration is landscape");
        }
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        initReceiver();
        update();
    }

    private void initReceiver() {
        mSilentPushReceiver = new SilentPushReceiver(this);
        registerReceiver(mSilentPushReceiver,
                new IntentFilter(getPackageName() + ".action.ymp.SILENT_PUSH_RECEIVE"));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mSilentPushReceiver);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update() {
        String message = mPrefs.getString("silent_message", null);
        if (message == null) {
            messageLayout.setVisibility(View.GONE);
        } else {
            messageLayout.setVisibility(View.VISIBLE);
            messageText.setText(message);
        }
    }
}
