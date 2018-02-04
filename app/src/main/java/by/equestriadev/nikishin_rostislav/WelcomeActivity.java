package by.equestriadev.nikishin_rostislav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @OnClick(R.id.goButton)
    public void goButton(View view) {
        goToLauncher();
    }

    private void goToLauncher() {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("visited", true);
        edit.apply();
        Intent launcherIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(launcherIntent);
        finish();
    }
}
