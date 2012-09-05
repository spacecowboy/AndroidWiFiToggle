package com.nononsenseapps.wifitoggle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class Notifier extends Service {

	public static final String ENABLED_KEY = "enabled";

	private static final String TOGGLE_WIFI = "TOGGLE_WIFI";
	//private static final String NOTIFY_ON = "NOTIFY_ON";
	private static final String NOTIFY_OFF = "NOTIFY_OFF";
	private static final int NOTIFICATION_ID = 01;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (TOGGLE_WIFI.equals(intent.getAction())) {
			toggleWifi();
		} else if (NOTIFY_OFF.equals(intent.getAction())) {
			cancelNotification();
		} else {
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			if (prefs.getBoolean(ENABLED_KEY, true)) {
				displayNotification();
			} else {
				cancelNotification();
			}
		}

		return START_NOT_STICKY;
	}

	private void toggleWifi() {
		final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifi.setWifiEnabled(!wifi.isWifiEnabled());
	}

	private void displayNotification() {
		final Intent toggleIntent = new Intent(getApplicationContext(),
				Notifier.class);
		toggleIntent.setAction(TOGGLE_WIFI);
		final PendingIntent pendingToggleIntent = PendingIntent.getService(
				getApplicationContext(), 0, toggleIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		final Intent settingsIntent = new Intent(Intent.ACTION_MAIN, null);
		settingsIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final ComponentName cn = new ComponentName("com.android.settings",
				"com.android.settings.wifi.WifiSettings");
		settingsIntent.setComponent(cn);
		settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		final PendingIntent pendingSettingsIntent = PendingIntent.getActivity(
				getApplicationContext(), 0, settingsIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		final Notification noti = new Notification.Builder(
				getApplicationContext())
				.setContentTitle(getText(R.string.app_name))
				.setContentIntent(pendingToggleIntent)
				.addAction(R.drawable.action_settings_dark,
						getText(R.string.wifi_settings), pendingSettingsIntent)
				.setSmallIcon(R.drawable.device_access_network_wifi_dark)
				.setAutoCancel(false).setOngoing(true).setContentInfo("")
				.setWhen(0).setPriority(Notification.PRIORITY_MIN).build();

		final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, noti);
	}

	private void cancelNotification() {
		final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}
}
