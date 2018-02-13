package by.equestriadev.nikishin_rostislav;

import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yandex.metrica.YandexMetrica;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.about_toolbar)
    Toolbar toolbar;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
