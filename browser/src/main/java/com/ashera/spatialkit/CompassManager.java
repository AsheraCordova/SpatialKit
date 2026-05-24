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

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;

import com.ashera.core.IFragment;
import com.ashera.core.IFragmentManager;

public class CompassManager implements com.ashera.core.IFragmentManager {
	org.teavm.jso.dom.events.EventListener<DeviceOrientationEvent> deviceOrientationListener;
	private boolean stopListeners;
	private float heading, oldHeading, longitude, latitude, altitude, magneticDeclination, trueHeading, oldTrueHeading;
	private IFragment fragment;
	private int locationWatchId;

	@Override
	public void onCreate(IFragment fragment, Object... args) {
		this.fragment = fragment;
		heading = oldHeading = longitude = latitude = altitude = magneticDeclination = trueHeading = 0;
	}

	private void removeListeners() {
		if (!stopListeners) {
			if (deviceOrientationListener != null) {
				org.teavm.jso.browser.Window.current().removeEventListener("deviceorientation",
						deviceOrientationListener);
			}
			if (locationWatchId != 0) {
				clearLocationWatch(locationWatchId);
			}
		}
	}

	private void removeAndStopListeners() {
		removeListeners();
		stopListeners = true;
	}

	@Override
	public void onPause(IFragment fragment) {
		removeListeners();
	}

	@JSBody(params = { "callback" }, script = "if (typeof DeviceOrientationEvent !== 'undefined' && "
			+ "typeof DeviceOrientationEvent.requestPermission === 'function') {"
			+ "  DeviceOrientationEvent.requestPermission().then(function(response) {"
			+ "    if (response === 'granted') { callback(); }" + "  }).catch(function(e) { console.log(e); });"
			+ "} else { callback(); }")
	private static native void requestPermission(Callback callback);

	private void requestPermissionAndStart() {
		requestPermission(() -> {
			addListener();
		});
	}

	@JSBody(params = { "callback" }, script = "if (navigator.geolocation) {"
			+ "  return navigator.geolocation.watchPosition(function(pos) {"
			+ "    callback(pos.coords.latitude, pos.coords.longitude, pos.coords.altitude);" + "  });"
			+ "} else { return -1; }")
	private static native int watchLocation(LocationCallback callback);
	@JSBody(params = { "id" }, script = "if (navigator.geolocation) {" + "  navigator.geolocation.clearWatch(id);"
			+ "}")
	private static native void clearLocationWatch(int id);

	private void startLocation() {
		locationWatchId = watchLocation((lat, lng, alt) -> {
			latitude = (float) lat;
			longitude = (float) lng;
			altitude = (float) alt;
			updateHeading();
		});
	}

	private void addListener() {
		deviceOrientationListener = new org.teavm.jso.dom.events.EventListener<DeviceOrientationEvent>() {
			@Override
			public void handleEvent(DeviceOrientationEvent evt) {
				heading = evt.getAlpha();
				updateHeading();
			}
		};
		org.teavm.jso.browser.Window.current().addEventListener("deviceorientation", deviceOrientationListener);

	}

	@Override
	public void onResume(IFragment fragment) {
		requestPermissionAndStart();
		startLocation();
	}

	private void updateHeading() {
		oldHeading = heading;
		oldTrueHeading = trueHeading;

		if (latitude != 0 && longitude != 0) {
			magneticDeclination = CompassHelper.calculateMagneticDeclination(latitude, longitude, altitude);
			trueHeading = magneticDeclination + heading;
		}
		trueHeading = smoothHeading(trueHeading, oldTrueHeading);

		publishResult();

	}

	private float smoothHeading(float newHeading, float currentHeading) {
		newHeading = adjustForWrap(newHeading, currentHeading);

		float diff = Math.abs(newHeading - currentHeading);
		if (diff > 180)
			diff = 360 - diff;

		float alpha;

		if (diff > 30) {
			alpha = 0.5f; // fast response
		} else if (diff > 10) {
			alpha = 0.25f;
		} else {
			alpha = 0.08f; // heavy smoothing
		}

		float smoothed = currentHeading + alpha * (newHeading - currentHeading);

		if (smoothed < 0)
			smoothed += 360;
		if (smoothed >= 360)
			smoothed -= 360;

		currentHeading = smoothed;
		return smoothed;
	}

	private float adjustForWrap(float newHeading, float currentHeading) {
		float diff = newHeading - currentHeading;

		if (diff > 180) {
			newHeading -= 360;
		} else if (diff < -180) {
			newHeading += 360;
		}

		return newHeading;
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

	@Override
	public void onRequestPermissionsResult(IFragment fragment, int requestCode, String[] permissions,
			int[] grantResults) {

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

	public interface DeviceOrientationEvent extends org.teavm.jso.dom.events.Event {
		@org.teavm.jso.JSProperty
		float getAlpha();

		@org.teavm.jso.JSProperty
		float getGamma();

		@org.teavm.jso.JSProperty
		float getBeta();
	}

	@JSFunctor
	private interface LocationCallback  extends JSObject {
		void onLocation(double lat, double lng, double alt);
	}
	
	@JSFunctor
	private interface Callback  extends JSObject{
		void onUpdate();
	}

	@Override
	public void onSaveInstanceState(IFragment fragment, Object... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(IFragment fragment) {
		
	}

	@Override
	public void onStop(IFragment fragment) {
		
	}


}
