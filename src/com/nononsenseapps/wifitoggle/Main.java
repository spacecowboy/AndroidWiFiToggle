package com.nononsenseapps.wifitoggle;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
import android.support.v4.app.NavUtils;

public class Main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		ToggleButton onButton = (ToggleButton) findViewById(R.id.toggleButton1);
		onButton.setChecked(prefs.getBoolean(Notifier.ENABLED_KEY, true));
		onButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				prefs.edit().putBoolean(Notifier.ENABLED_KEY, isChecked)
						.commit();

				startService(new Intent(getApplicationContext(), Notifier.class));
			}
		});

		startService(new Intent(this, Notifier.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
