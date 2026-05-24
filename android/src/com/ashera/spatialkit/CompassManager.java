//start - license
/*
 * Copyright (c) 2025 Ashera Cordova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
//end - license
package com.ashera.spatialkit;

import java.util.Map;

import com.ashera.core.IFragment;
import com.ashera.core.IFragmentManager;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class CompassManager implements SensorEventListener, com.ashera.core.IFragmentManager {
	protected static final int REQUEST_PERMISSION_FINE_LOCATION = 1;

	private SensorManager sensorManager;
	private FusedLocationProviderClient fusedLocationClient;

	private final float[] accelerometerReading = new float[3];
	private final float[] magnetometerReading = new float[3];

	private float heading, oldHeading, longitude, latitude, altitude, magneticDeclination, trueHeading, oldTrueHeading;
	private IFragment fragment;
	private boolean isLocationRetrieved = false;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean stopListeners;

    @Override
	public void onCreate(com.ashera.core.IFragment fragment, Object... args) {
		this.fragment = fragment;
		Context context = ((androidx.fragment.app.Fragment) fragment).requireContext();
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

		if (ContextCompat.checkSelfPermission(context,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			((androidx.fragment.app.Fragment) fragment).requestPermissions(
					new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_PERMISSION_FINE_LOCATION);
		}

		heading = oldHeading = longitude = latitude = altitude = magneticDeclination = trueHeading = 0;
    }

	@Override
	public void onResume(com.ashera.core.IFragment fragment) {
		if (!stopListeners) {
			Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			if (accelerometer != null) {
				sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME,
						SensorManager.SENSOR_DELAY_GAME);
			}
	
			Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
			if (magneticField != null) {
				sensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_GAME,
						SensorManager.SENSOR_DELAY_GAME);
			}
	        requestLocationUpdates();
		}
    }

    private void requestLocationUpdates() {
        Context context = ((Fragment) fragment).requireContext();
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initLocation();
            fusedLocationClient.requestLocationUpdates(
                    this.locationRequest,
                    this.locationCallback,
                    Looper.getMainLooper()
            );
        }
    }

    @Override
	public void onPause(com.ashera.core.IFragment fragment) {
		removeListeners();
	}

	private void removeListeners() {
		if (!stopListeners) {
			sensorManager.unregisterListener(this);
	        if (this.locationCallback != null) {
	            fusedLocationClient.removeLocationUpdates(locationCallback);
	        }
		}
	}
	
	private void removeAndStopListeners() {
		removeListeners();
        stopListeners = true;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// make sensor readings smoother using a low pass filter
			CompassHelper.lowPassFilter(event.values.clone(), accelerometerReading);
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			// make sensor readings smoother using a low pass filter
			CompassHelper.lowPassFilter(event.values.clone(), magnetometerReading);
		}
		
		updateHeading();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private void updateHeading() {
		// oldHeading required for image rotate animation
		oldHeading = heading;
		oldTrueHeading = trueHeading;

		heading = CompassHelper.calculateHeading(accelerometerReading, magnetometerReading);
		heading = CompassHelper.convertRadtoDeg(heading);
		heading = CompassHelper.map180to360(heading);
		
		if (isLocationRetrieved) {
			trueHeading = heading + magneticDeclination;
			if (trueHeading > 360) {
				trueHeading = trueHeading - 360;
			}
		}
		
		publishResult();

	}

	private void publishResult() {
		Map<String, Float> compassmap = new java.util.HashMap<>();
		compassmap.put("heading", heading);
		compassmap.put("trueHeading", trueHeading);
		compassmap.put("oldHeading", oldHeading);
		compassmap.put("oldTrueHeading", oldTrueHeading);
		compassmap.put("latitude", latitude);
		compassmap.put("longitude", longitude);
		compassmap.put("altitude", altitude);
		compassmap.put("magneticDeclination", magneticDeclination);
		
		fragment.getEventBus().notifyObservers("compass", compassmap);
	}

    private void initLocation() {
        if (this.locationRequest == null) {
            this.locationRequest =
                    new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                            .setMinUpdateIntervalMillis(2000)
                            .build();

            this.locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        isLocationRetrieved = true;
                        latitude = (float) location.getLatitude();
                        longitude = (float) location.getLongitude();
                        altitude = (float) location.getAltitude();
                        magneticDeclination = CompassHelper.calculateMagneticDeclination(latitude, longitude, altitude);
                        publishResult();
                    }
                }
            };
        }
    }

    @Override
	public void onRequestPermissionsResult(IFragment fragment, int requestCode, String[] permissions,
			int[] grantResults) {
		if (requestCode == REQUEST_PERMISSION_FINE_LOCATION) {
			// if request is cancelled, the result arrays are empty.
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// permission is granted
				requestLocationUpdates();
			} else {
				// display Toast with error message
				Toast.makeText(((Context) fragment.getRootActivity()), "Location Error", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onAttach(IFragment fragment) {

	}

	@Override
	public void onDetach(IFragment fragment) {

	}

	@Override
	public void onDestroy(IFragment fragment) {

	}

	@Override
	public void onSaveInstanceState(IFragment fragment, Object... args) {
	}

	@Override
	public void onStart(IFragment fragment) {
	}

	@Override
	public void onStop(IFragment fragment) {
	}

	@Override
	public IFragmentManager newInstance() {
		return new CompassManager();
	}

	@Override
	public void sendEvent(String action, Map<String, String> extraData) {
		switch (action) {
		
		case "onDestinationReached":
			removeAndStopListeners();			
			break;

		default:
			break;
		}
	}
}
