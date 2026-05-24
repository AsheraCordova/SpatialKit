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
//start - imports
import java.util.*;

import r.android.content.Context;
import r.android.os.Build;
import r.android.view.View;
import r.android.annotation.SuppressLint;
import r.android.annotation.SuppressLint;

import com.ashera.widget.*;
import com.ashera.converter.*;

/*-[
#include <UIKit/UIKit.h>
#include "ASUIView.h"
]-*/
import com.google.j2objc.annotations.Property;
import androidx.core.view.*;

import static com.ashera.widget.IWidget.*;
//end - imports

import java.text.DecimalFormat;
import r.android.view.animation.Animation;
import r.android.view.animation.RotateAnimation;


/*-[
#import <CoreLocation/CoreLocation.h>
]-*/
public class CompassViewImpl implements com.ashera.widget.IAttributable {
	// start - body
	public final static String LOCAL_NAME = "CompassView"; 
	private IWidget w;
	private java.util.Map<IWidget, IAttributable> instances = new java.util.WeakHashMap<>();
	private CompassViewImpl(IWidget widget) {
		this.w = widget;
	}
	
	public String getLocalName() {
		return LOCAL_NAME;
	}
	
	public CompassViewImpl() {
	}
	
	@Override
	public com.ashera.widget.IAttributable newInstance(IWidget widget) {
		return instances.computeIfAbsent(widget, w -> new CompassViewImpl(w));
	}
	
	
	@SuppressLint("NewApi")
	@Override
	public void loadAttributes(String localName) {

		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("headingId").withType("id"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("heading2Id").withType("id"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("directionId").withType("id"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("direction2Id").withType("id"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("distanceTextId").withType("id"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("targetLatitude").withType("float").withOrder(-1));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("targetLongitude").withType("float").withOrder(-1));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("distanceFormat").withType("resourcestring").withOrder(-1));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("distanceUnit").withType("resourcestring").withOrder(-1));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("onDestinationReached").withType("int").withOrder(-1));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("onApproachingDestination").withType("int").withOrder(-1));
	}

	@SuppressLint("NewApi")
	@Override
	public void setAttribute(WidgetAttribute key, String strValue, Object objValue, ILifeCycleDecorator decorator) {
		View view = (View) w.asWidget();
		Object nativeWidget = w.asNativeWidget();
		switch (key.getAttributeName()) {
		case "headingId": {


		 setHeadingId(strValue, objValue);



			}
			break;
		case "heading2Id": {


		 setHeadin2gId(strValue, objValue);



			}
			break;
		case "directionId": {


		 setDirectionId(strValue, objValue);



			}
			break;
		case "direction2Id": {


		 setDirection2Id(strValue, objValue);



			}
			break;
		case "distanceTextId": {


		 setDistanceTextId(strValue, objValue);



			}
			break;
		case "targetLatitude": {


		 setTargetLatitude(objValue);



			}
			break;
		case "targetLongitude": {


		 setTargetLongitude(objValue);



			}
			break;
		case "distanceFormat": {


		 setDistanceFormat(objValue);



			}
			break;
		case "distanceUnit": {


		 setDistanceUnit(objValue);



			}
			break;
		case "onDestinationReached": {


		 onDestinationReached(objValue);



			}
			break;
		case "onApproachingDestination": {


		 onApproachingDestination(objValue);



			}
			break;
		default:
			break;
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public Object getAttribute(WidgetAttribute key, ILifeCycleDecorator decorator) {
		View view = (View) w.asWidget();
		Object nativeWidget = w.asNativeWidget();
		switch (key.getAttributeName()) {
		}
		return null;
	}
	
	

	private IWidget heading2View;
	private IWidget direction2View;
	private String distanceFormat = "%s";
	private String distanceUnit = "km"; // km/miles
	private float targetLatitude;
	private float targetLongitude;
	

	
	private void setDistanceUnit(Object objValue) {
		this.distanceUnit = (String) objValue;
	}

	private void setDistanceFormat(Object objValue) {
		this.distanceFormat = (String) objValue;
	}

	private void setTargetLongitude(Object objValue) {
		this.targetLongitude = (float) objValue;
	}

	private void setTargetLatitude(Object objValue) {
		this.targetLatitude = (float) objValue;
	}

	private void setDistanceTextId(String strValue, Object objValue) {
		IWidget distanceTextView = w.findWidgetById(strValue);
		displayDistance(distanceTextView);
	}

	private void setDirection2Id(String strValue, Object objValue) {
		this.direction2View = w.findWidgetById(strValue);		
	}

	private void setDirectionId(String strValue, Object objValue) {
		IWidget directionView = w.findWidgetById(strValue);
		directionListener(directionView);
	}

	private void setHeadin2gId(String strValue, Object objValue) {
		this.heading2View = w.findWidgetById(strValue);
	}

	private void setHeadingId(String strValue, Object objValue) {
		IWidget headingView = w.findWidgetById(strValue);
		headingListener(headingView);
	}

	private void displayDistance(IWidget distanceTextView) {
		w.getFragment().getEventBus().on("compass", new com.ashera.widget.bus.EventBusHandler("compass") {
			@Override
			protected void doPerform(Object payload) {
				Map<String, Float> compassmap = (Map<String, Float>) payload;
				float latitude = compassmap.get("latitude");
				float longitude = compassmap.get("longitude");

				if (latitude != 0 && longitude != 0 && targetLatitude != 0 && targetLongitude != 0) {
					float distance = getDistanceFrom(latitude, longitude);
					String formattedDistance = null;
					switch (distanceUnit) {
					case "km":
						if (distance < 1000) {
							if (distance == 0) {
								formattedDistance = "";
							} else {
								formattedDistance = String.format(distanceFormat,
										new DecimalFormat("## M").format(distance));
							}
						} else if (distance < 1000 * 100) {
							formattedDistance = String.format(distanceFormat,
									new DecimalFormat("##.## KM").format(distance / 1000));

						} else {
							formattedDistance = String.format(distanceFormat,
									new DecimalFormat("## KM").format(distance / 1000));
						}
						break;
					case "mile":
						formattedDistance = String.format(distanceFormat,
								new DecimalFormat("## MILE").format(distance / 1609.344));
						break;
					default:
						break;
					}

					distanceTextView.setAttribute("text", formattedDistance, false);
					distanceTextView.requestLayout();
					distanceTextView.getFragment().remeasure();
				}
			}
		});

	}

	private void headingListener(IWidget headingView) {
		if (headingView != null) {
			w.getFragment().getEventBus().on("compass", new com.ashera.widget.bus.EventBusHandler("compass") {
				@Override
				protected void doPerform(Object payload) {
					Map<String, Float> compassmap = (Map<String, Float>) payload;
					float oldTrueHeading = compassmap.get("oldTrueHeading");
					float trueHeading = compassmap.get("trueHeading");
					rotateView(headingView, oldTrueHeading, trueHeading);
	
					if (heading2View != null) {
						rotateView(heading2View, oldTrueHeading, trueHeading);
	
					}
				}
			});
		}
	}

	private void directionListener(IWidget directionView) {
		if (directionView != null) {
			w.getFragment().getEventBus().on("compass", new com.ashera.widget.bus.EventBusHandler("compass") {
				@Override
				protected void doPerform(Object payload) {
					Map<String, Float> compassmap = (Map<String, Float>) payload;
					float oldTrueHeading = compassmap.get("oldTrueHeading");
					float trueHeading = compassmap.get("trueHeading");
					float latitude = compassmap.get("latitude");
					float longitude = compassmap.get("longitude");
	
					if (latitude != 0 && longitude != 0 && targetLatitude != 0 && targetLongitude != 0
							&& trueHeading != 0) {
						float bearing = getBearingTo(latitude, longitude);
						float degree = trueHeading - bearing;
						float olddegree = oldTrueHeading - bearing;
	
						rotateView(directionView, degree, olddegree);
	
						if (direction2View != null) {
							rotateView(direction2View, degree, olddegree);
						}
					}
				}
			});
		}
	}
	
	private void onDestinationReached(Object objValue) {
		onDestination(objValue, "onDestinationReached");		
	}

	private void onDestination(Object objValue, String action) {
		int tolerance = (int) objValue;
		w.getFragment().getEventBus().on("compass", new com.ashera.widget.bus.EventBusHandler("compass") {
			boolean flag = false;
			@Override
			protected void doPerform(Object payload) {
				Map<String, Float> compassmap = (Map<String, Float>) payload;
				float latitude = compassmap.get("latitude");
				float longitude = compassmap.get("longitude");

				if (latitude != 0 && longitude != 0 && targetLatitude != 0 && targetLongitude != 0) {
					float distance = getDistanceFrom(latitude, longitude);
					
					if (!flag && distance < tolerance) {
						Map<String, String> obj = new java.util.HashMap<>();
						obj.put("distance",  new DecimalFormat("##.##").format(distance));
						w.getFragment().sendEvent(action, obj);
						flag = true;
					}
				}
			}
		});
	}
	
	
	private void onApproachingDestination(Object objValue) {
		onDestination(objValue, "onApproachingDestination");
	}
	


	// end - body
	
	private native float getDistanceFrom(float latitude, float longitude) /*-[
	    CLLocation *currentLocation = [[CLLocation alloc] initWithLatitude:latitude
	                                                             longitude:longitude];
	
	    CLLocation *targetLocation = [[CLLocation alloc] initWithLatitude:self->targetLatitude_
	                                                             longitude:self->targetLongitude_];
	
	    CLLocationDistance distance = [currentLocation distanceFromLocation:targetLocation];
	
	    return (float)distance;
	]-*/;
	
	private native float getBearingTo(float latitude, float longitude) /*-[
	    double lat1 = latitude * M_PI / 180.0;
	    double lon1 = longitude * M_PI / 180.0;
	    double lat2 = self->targetLatitude_ * M_PI / 180.0;
	    double lon2 = self->targetLongitude_ * M_PI / 180.0;
	
	    double dLon = lon2 - lon1;
	
	    double y = sin(dLon) * cos(lat2);
	    double x = cos(lat1) * sin(lat2) -
	               sin(lat1) * cos(lat2) * cos(dLon);
	
	    double bearing = atan2(y, x);
	
	    // Convert radians -> degrees
	    bearing = bearing * 180.0 / M_PI;
	
	    // Normalize to 0–360
	    if (bearing < 0) {
	        bearing += 360.0;
	    }
	
	    return (float)bearing;
	]-*/;

//	private void rotateView2(IWidget view, float oldTrueHeading, float trueHeading) {
//		RotateAnimation rotateAnimation = new RotateAnimation(-oldTrueHeading, -trueHeading, Animation.RELATIVE_TO_SELF,
//				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		rotateAnimation.setDuration(200);
//		rotateAnimation.setFillAfter(true);
//		((View) view.asWidget()).startAnimation(rotateAnimation);
//	}
	
	private native void rotateView(IWidget view, float oldTrueHeading, float trueHeading) /*-[
	    UIView *uiView = (UIView *)[view asNativeWidget];
	
	    // Convert degrees -> radians
	    CGFloat fromAngle = -oldTrueHeading * M_PI / 180.0;
	    CGFloat toAngle   = -trueHeading * M_PI / 180.0;
	
	    // Create rotation animation
	    CABasicAnimation *rotation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];
	    rotation.fromValue = @(fromAngle);
	    rotation.toValue = @(toAngle);
	    rotation.duration = 0.2;
	    rotation.fillMode = kCAFillModeForwards;
	    rotation.removedOnCompletion = NO;
	
	    // Apply animation
	    [uiView.layer addAnimation:rotation forKey:@"rotateAnimation"];
	
	    // Important: set final state (otherwise it snaps back later)
	    uiView.layer.transform = CATransform3DMakeRotation(toAngle, 0, 0, 1);
	]-*/;

}
