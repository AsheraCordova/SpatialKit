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

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.annotation.SuppressLint;
import androidx.core.view.*;
import android.annotation.SuppressLint;

import com.ashera.widget.*;
import com.ashera.converter.*;
import android.widget.*;
import android.view.*;
import android.graphics.*;
import android.content.res.*;


import static com.ashera.widget.IWidget.*;
//end - imports

import java.text.DecimalFormat;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class CompassViewImpl implements com.ashera.widget.IAttributable {
	//start - body
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
		switch (key.getAttributeName()) {
		}
		return null;
	}
	
	

	// end - body
	//start - copycode
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
	//end - copycode
	

	private void rotateView(IWidget view, float oldTrueHeading, float trueHeading) {
		RotateAnimation rotateAnimation = new RotateAnimation(-oldTrueHeading, -trueHeading, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(200);
		rotateAnimation.setFillAfter(true);
		((View) view.asWidget()).startAnimation(rotateAnimation);
	}

	private float getDistanceFrom(float latitude, float longitude) {
		android.location.Location currentLocation = new android.location.Location("");
		currentLocation.setLatitude(latitude);
		currentLocation.setLongitude(longitude);

		android.location.Location targetLocation = new android.location.Location("");
		targetLocation.setLatitude(targetLatitude);
		targetLocation.setLongitude(targetLongitude);

		float distance = currentLocation.distanceTo(targetLocation);
		return distance;
	}
	

	private float getBearingTo(float latitude, float longitude) {
		android.location.Location currentLocation = new android.location.Location("");
		currentLocation.setLatitude(latitude);
		currentLocation.setLongitude(longitude);

		android.location.Location targetLocation = new android.location.Location("");
		targetLocation.setLatitude(targetLatitude);
		targetLocation.setLongitude(targetLongitude);

		float bearing = currentLocation.bearingTo(targetLocation);
		return bearing;
	}
}
