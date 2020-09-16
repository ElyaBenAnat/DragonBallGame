package com.dragonball.demo.location;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationManager {

	private static LocationManager instance = null;
	private FusedLocationProviderClient fusedLocationClient;

	private LocationManager(Context context) {
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
	}

	public static LocationManager getInstance(Context context) {
		if (instance == null)
			instance = new LocationManager(context);
		return instance;
	}

	public void getLocation(final LocationListener listener) {
		fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
			@Override
			public void onSuccess(android.location.Location location) {
				if (listener != null && location != null) {
					listener.onLocationReceived(location);
				}
			}
		});
	}
}
