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
/*-[
#import <CoreLocation/CoreLocation.h>
]-*/
public class CompassManager implements com.ashera.core.IFragmentManager {
    private IFragment fragment;

    private float heading, oldHeading;
    private float longitude, latitude, altitude;
    private float magneticDeclination, trueHeading, oldTrueHeading;

    private boolean stopListeners;

    // Native CLLocationManager
    private Object locationManager;

    @Override
    public void onCreate(IFragment fragment, Object... args) {
        this.fragment = fragment;

        heading = oldHeading = longitude = latitude = altitude = magneticDeclination = trueHeading = 0;
    }

    @Override
    public void onResume(IFragment fragment) {
        if (!stopListeners) {
            startNative();
        }
    }

    @Override
    public void onPause(IFragment fragment) {
        removeListeners();
    }

    private void removeListeners() {
        if (!stopListeners) {
            stopNative();
        }
    }

    private void removeAndStopListeners() {
        removeListeners();
        stopListeners = true;
    }

    // Start iOS sensors
    private native void startNative() /*-[
        self->locationManager_ = [[CLLocationManager alloc] init];
        CLLocationManager *manager = (CLLocationManager *)self->locationManager_;

        manager.delegate = (id<CLLocationManagerDelegate>)self;
		manager.headingFilter = 2; //Reduces noise significantly
        [manager requestWhenInUseAuthorization];
        manager.desiredAccuracy = kCLLocationAccuracyBest;

        [manager startUpdatingLocation];
        [manager startUpdatingHeading];
    ]-*/;

    // Stop iOS sensors
    private native void stopNative() /*-[
        CLLocationManager *manager = (CLLocationManager *)self->locationManager_;
        if (manager) {
            [manager stopUpdatingHeading];
            [manager stopUpdatingLocation];
        }
    ]-*/;

    // Called from iOS
    private void onHeadingUpdate(double magnetic,
                                 double trueHeadingVal,
                                 double accuracy,
                                 double lat,
                                 double lng,
                                 double alt) {

        oldHeading = heading;
        
        if (trueHeadingVal == -1 && lat != 0 && lng != 0) {
        	magneticDeclination = CompassHelper.calculateMagneticDeclination(lat, lng, alt);
        	trueHeadingVal = magneticDeclination + heading;
        }
        trueHeadingVal = smoothHeading(trueHeadingVal, oldTrueHeading);
        
        oldTrueHeading = trueHeading;

        heading = (float) magnetic;
        trueHeading = (float) trueHeadingVal;

        latitude = (float) lat;
        longitude = (float) lng;
        altitude = (float) alt;

        publishResult();
    }
    private double adjustForWrap(double newHeading, double currentHeading) {
    	double diff = newHeading - currentHeading;

        if (diff > 180) {
            newHeading -= 360;
        } else if (diff < -180) {
            newHeading += 360;
        }

        return newHeading;
    }
    private double smoothHeading(double newHeading, double currentHeading) {
        newHeading = adjustForWrap(newHeading, currentHeading);

        double diff = Math.abs(newHeading - currentHeading);
        if (diff > 180) diff = 360 - diff;

        float alpha;

        if (diff > 30) {
            alpha = 0.5f;   // fast response
        } else if (diff > 10) {
            alpha = 0.25f;
        } else {
            alpha = 0.08f;  // heavy smoothing
        }

        double smoothed = currentHeading + alpha * (newHeading - currentHeading);

        if (smoothed < 0) smoothed += 360;
        if (smoothed >= 360) smoothed -= 360;

        currentHeading = smoothed;
        return smoothed;
    }

    private void onError() {
        // optional logging
    }

    private void publishResult() {
        Map<String, Float> compassmap = new java.util.HashMap<>();
        compassmap.put("heading", heading);        
        compassmap.put("trueHeading", trueHeading);
        compassmap.put("oldHeading", oldHeading);
        compassmap.put("oldTrueHeading", oldTrueHeading);
        compassmap.put("latitude", latitude);
        compassmap.put("longitude", longitude);
        compassmap.put("altitude", altitude); // fixed
        compassmap.put("magneticDeclination", magneticDeclination);

        if (fragment == null || fragment.getEventBus() == null) {
        	stopNative();
        } else {
        	fragment.getEventBus().notifyObservers("compass", compassmap);
        }
    }

    /*-[
    // Location updates
    - (void)locationManager:(CLLocationManager *)manager
           didUpdateLocations:(NSArray<CLLocation *> *)locations
    {
        CLLocation *loc = [locations lastObject];
        if (loc) {
            double lat = loc.coordinate.latitude;
            double lng = loc.coordinate.longitude;
            double alt = loc.altitude;

            objc_setAssociatedObject(self, "lat", @(lat), OBJC_ASSOCIATION_RETAIN_NONATOMIC);
            objc_setAssociatedObject(self, "lng", @(lng), OBJC_ASSOCIATION_RETAIN_NONATOMIC);
            objc_setAssociatedObject(self, "alt", @(alt), OBJC_ASSOCIATION_RETAIN_NONATOMIC);
        }
    }

    // Heading updates
    - (void)locationManager:(CLLocationManager *)manager
           didUpdateHeading:(CLHeading *)heading
    {
        double magnetic = heading.magneticHeading;
        double trueHeadingVal = heading.trueHeading;
        double accuracy = heading.headingAccuracy;

        NSNumber *latNum = objc_getAssociatedObject(self, "lat");
        NSNumber *lngNum = objc_getAssociatedObject(self, "lng");
        NSNumber *altNum = objc_getAssociatedObject(self, "alt");

        double lat = latNum ? [latNum doubleValue] : 0;
        double lng = lngNum ? [lngNum doubleValue] : 0;
        double alt = altNum ? [altNum doubleValue] : 0;

        [self onHeadingUpdateWithDouble:magnetic
                            withDouble:trueHeadingVal
                            withDouble:accuracy
                            withDouble:lat
                            withDouble:lng
                            withDouble:alt];
    }

    - (void)locationManager:(CLLocationManager *)manager
           didFailWithError:(NSError *)error
    {
        [self onError];
    }

    - (BOOL)locationManagerShouldDisplayHeadingCalibration:(CLLocationManager *)manager
    {
        return YES;
    }
    ]-*/

    @Override
    public void onDestroy(IFragment fragment) {
        stopNative();
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
        }
    }

	@Override
	public void onAttach(IFragment fragment) {
	}

	@Override
	public void onDetach(IFragment fragment) {
	}

	@Override
	public void onRequestPermissionsResult(IFragment fragment, int requestCode, String[] permissions,
			int[] grantResults) {
		
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
}