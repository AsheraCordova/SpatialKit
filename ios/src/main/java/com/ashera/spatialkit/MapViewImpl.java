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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import r.android.content.Context;



import r.android.os.Build;
import r.android.view.View;
import r.android.text.*;

import com.ashera.core.IFragment;
import com.ashera.converter.*;

import r.android.annotation.SuppressLint;

import com.ashera.layout.*;
import com.ashera.plugin.*;
import com.ashera.widget.bus.*;
import com.ashera.widget.*;
import com.ashera.widget.bus.Event.*;
import com.ashera.widget.IWidgetLifeCycleListener.EventId;
import com.ashera.widget.IWidgetLifeCycleListener.EventId.*;

/*-[
#include "java/lang/Integer.h"
#include "java/lang/Float.h"
#include "java/lang/Boolean.h"
#include <UIKit/UIKit.h>
#include "HasLifeCycleDecorators.h"
]-*/

import com.google.j2objc.annotations.Property;

import static com.ashera.widget.IWidget.*;
//end - imports
/*-[
#include "ASUIView.h"
#import <MapLibre/MapLibre.h>
#include "CustomPointAnnotation.h"
]-*/
@SuppressLint("NewApi")
public class MapViewImpl extends BaseWidget {
	//start - body
	public final static String LOCAL_NAME = "org.maplibre.android.maps.MapView"; 
	public final static String GROUP_NAME = "org.maplibre.android.maps.MapView";

	protected @Property Object uiView;
	protected org.maplibre.android.maps.MapView measurableView;		
	
	
	@Override
	public void loadAttributes(String attributeName) {
		ViewImpl.register(attributeName);


		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiCompassGravity").withType("gravity").withOrder(-1));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiCompass").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraBearing").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraTargetLat").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraTargetLng").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraZoom").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("showMarkerForTarget").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("targetLocationMarkerTitle").withType("resourcestring"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("targetLocationMarkerSnippet").withType("resourcestring"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("targetLocationMarkerIcon").withType("string"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("showsUserLocation").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("addMarker").withType("object"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("removeMarker").withType("string"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("onMapClick").withType("string"));
	}
	
	public MapViewImpl() {
		super(GROUP_NAME, LOCAL_NAME);
	}
	public  MapViewImpl(String localname) {
		super(GROUP_NAME, localname);
	}
	public  MapViewImpl(String groupName, String localname) {
		super(groupName, localname);
	}

@com.google.j2objc.annotations.WeakOuter		
	public class MapViewExt extends org.maplibre.android.maps.MapView implements ILifeCycleDecorator, com.ashera.widget.IMaxDimension{
		private MeasureEvent measureFinished = new MeasureEvent();
		private OnLayoutEvent onLayoutEvent = new OnLayoutEvent();
		private List<IWidget> overlays;
		public IWidget getWidget() {
			return MapViewImpl.this;
		}
		private int mMaxWidth = -1;
		private int mMaxHeight = -1;
		@Override
		public void setMaxWidth(int width) {
			mMaxWidth = width;
		}
		@Override
		public void setMaxHeight(int height) {
			mMaxHeight = height;
		}
		@Override
		public int getMaxWidth() {
			return mMaxWidth;
		}
		@Override
		public int getMaxHeight() {
			return mMaxHeight;
		}

		public MapViewExt() {
			super();
			
		}
		
		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

			if(mMaxWidth > 0) {
	        	widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.AT_MOST);
	        }
	        if(mMaxHeight > 0) {
	            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);

	        }

	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			IWidgetLifeCycleListener listener = (IWidgetLifeCycleListener) getListener();
			if (listener != null) {
			    measureFinished.setWidth(getMeasuredWidth());
			    measureFinished.setHeight(getMeasuredHeight());
				listener.eventOccurred(EventId.measureFinished, measureFinished);
			}
		}
		
		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			super.onLayout(changed, l, t, r, b);
			ViewImpl.setDrawableBounds(MapViewImpl.this, l, t, r, b);
			if (!isOverlay()) {
			ViewImpl.nativeMakeFrame(asNativeWidget(), l, t, r, b);
			nativeMakeFrameForChildWidget(l, t, r, b);
			}
			replayBufferedEvents();
	        ViewImpl.redrawDrawables(MapViewImpl.this);
	        overlays = ViewImpl.drawOverlay(MapViewImpl.this, overlays);
			
			IWidgetLifeCycleListener listener = (IWidgetLifeCycleListener) getListener();
			if (listener != null) {
				onLayoutEvent.setB(b);
				onLayoutEvent.setL(l);
				onLayoutEvent.setR(r);
				onLayoutEvent.setT(t);
				onLayoutEvent.setChanged(changed);
				listener.eventOccurred(EventId.onLayout, onLayoutEvent);
			}
			
			if (isInvalidateOnFrameChange() && isInitialised()) {
				MapViewImpl.this.invalidate();
			}
		}	
		
		@Override
		public void execute(String method, Object... canvas) {
			
		}

		public void updateMeasuredDimension(int width, int height) {
			setMeasuredDimension(width, height);
		}


		@Override
		public ILifeCycleDecorator newInstance(IWidget widget) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttribute(WidgetAttribute widgetAttribute,
				String strValue, Object objValue) {
			throw new UnsupportedOperationException();
		}		
		

		@Override
		public List<String> getMethods() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void initialized() {
			throw new UnsupportedOperationException();
		}
		
        @Override
        public Object getAttribute(WidgetAttribute widgetAttribute) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void drawableStateChanged() {
        	super.drawableStateChanged();
        	if (!isWidgetDisposed()) {
        		ViewImpl.drawableStateChanged(MapViewImpl.this);
        	}
        }
        private Map<String, IWidget> templates;
    	@Override
    	public r.android.view.View inflateView(java.lang.String layout) {
    		if (templates == null) {
    			templates = new java.util.HashMap<String, IWidget>();
    		}
    		IWidget template = templates.get(layout);
    		if (template == null) {
    			template = (IWidget) quickConvert(layout, "template");
    			templates.put(layout, template);
    		}
    		
    		IWidget widget = template.loadLazyWidgets(MapViewImpl.this.getParent());
			return (View) widget.asWidget();
    	}   
        
    	@Override
		public void remeasure() {
    		if (getFragment() != null) {
    			getFragment().remeasure();
    		}
		}
    	
        @Override
		public void removeFromParent() {
        	MapViewImpl.this.getParent().remove(MapViewImpl.this);
		}
        @Override
        public void getLocationOnScreen(int[] appScreenLocation) {
        	appScreenLocation[0] = ViewImpl.getLocationXOnScreen(asNativeWidget());
        	appScreenLocation[1] = ViewImpl.getLocationYOnScreen(asNativeWidget());
        }
        @Override
        public void getWindowVisibleDisplayFrame(r.android.graphics.Rect displayFrame){
        	
        	displayFrame.left = ViewImpl.getLocationXOnScreen(asNativeWidget());
        	displayFrame.top = ViewImpl.getLocationYOnScreen(asNativeWidget());
        	displayFrame.right = displayFrame.left + getWidth();
        	displayFrame.bottom = displayFrame.top + getHeight();
        }
        @Override
		public void offsetTopAndBottom(int offset) {
			super.offsetTopAndBottom(offset);
			ViewImpl.nativeMakeFrame(asNativeWidget(), getLeft(), getTop(), getRight(), getBottom());
		}
		@Override
		public void offsetLeftAndRight(int offset) {
			super.offsetLeftAndRight(offset);
			ViewImpl.nativeMakeFrame(asNativeWidget(), getLeft(), getTop(), getRight(), getBottom());
		}
		@Override
		public void setMyAttribute(String name, Object value) {
			if (name.equals("state0")) {
				setState0(value);
				return;
			}
			if (name.equals("state1")) {
				setState1(value);
				return;
			}
			if (name.equals("state2")) {
				setState2(value);
				return;
			}
			if (name.equals("state3")) {
				setState3(value);
				return;
			}
			if (name.equals("state4")) {
				setState4(value);
				return;
			}
			MapViewImpl.this.setAttribute(name, value, !(value instanceof String));
		}
        @Override
        public void setVisibility(int visibility) {
            super.setVisibility(visibility);
            ViewImpl.nativeSetVisibility(asNativeWidget(), visibility != View.VISIBLE);
            
        }
        
    	public void setState0(Object value) {
    		ViewImpl.setState(MapViewImpl.this, 0, value);
    	}
    	public void setState1(Object value) {
    		ViewImpl.setState(MapViewImpl.this, 1, value);
    	}
    	public void setState2(Object value) {
    		ViewImpl.setState(MapViewImpl.this, 2, value);
    	}
    	public void setState3(Object value) {
    		ViewImpl.setState(MapViewImpl.this, 3, value);
    	}
    	public void setState4(Object value) {
    		ViewImpl.setState(MapViewImpl.this, 4, value);
    	}
        	public void state0() {
        		ViewImpl.state(MapViewImpl.this, 0);
        	}
        	public void state1() {
        		ViewImpl.state(MapViewImpl.this, 1);
        	}
        	public void state2() {
        		ViewImpl.state(MapViewImpl.this, 2);
        	}
        	public void state3() {
        		ViewImpl.state(MapViewImpl.this, 3);
        	}
        	public void state4() {
        		ViewImpl.state(MapViewImpl.this, 4);
        	}
                        
        public void stateYes() {
        	ViewImpl.stateYes(MapViewImpl.this);
        	
        }
        
        public void stateNo() {
        	ViewImpl.stateNo(MapViewImpl.this);
        }
     
	
	}	@Override
	public Class getViewClass() {
		return MapViewExt.class;
	}

	@Override
	public IWidget newInstance() {
		return new MapViewImpl(groupName, localName);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void create(IFragment fragment, Map<String, Object> params) {
		super.create(fragment, params);
		measurableView = new MapViewExt();
		nativeCreate(params);	
		ViewImpl.registerCommandConveter(this);
		setWidgetOnNativeClass();
	}
	private native void setWidgetOnNativeClass() /*-[
		((ASUIView*) self.uiView).widget = self;
	]-*/;

	@Override
	@SuppressLint("NewApi")
	public void setAttribute(WidgetAttribute key, String strValue, Object objValue, ILifeCycleDecorator decorator) {
		Object nativeWidget = asNativeWidget();
		ViewImpl.setAttribute(this,  key, strValue, objValue, decorator);
		
		switch (key.getAttributeName()) {
			case "maplibre_uiCompassGravity": {
				


		setUiCompassGravity(objValue);



			}
			break;
			case "maplibre_uiCompass": {
				


		setUiCompass(objValue);



			}
			break;
			case "maplibre_cameraBearing": {
				


		setCameraBearing(objValue);



			}
			break;
			case "maplibre_cameraTargetLat": {
				


		setCameraTargetLat(objValue);



			}
			break;
			case "maplibre_cameraTargetLng": {
				


		setCameraTargetLng(objValue);



			}
			break;
			case "maplibre_cameraZoom": {
				


		setCameraZoom(objValue);



			}
			break;
			case "showMarkerForTarget": {
				


		showMarkerForTarget(objValue);



			}
			break;
			case "targetLocationMarkerTitle": {
				


		setTargetLocationMarkerTitle(objValue);



			}
			break;
			case "targetLocationMarkerSnippet": {
				


		setTargetLocationMarkerSnippet(objValue);



			}
			break;
			case "targetLocationMarkerIcon": {
				


		setTargetLocationMarkerIcon(objValue);



			}
			break;
			case "showsUserLocation": {
				


		setShowsUserLocation(objValue);



			}
			break;
			case "addMarker": {
				
		if (objValue instanceof Map) {
			Map<String, Object> data = ((Map<String, Object>) objValue);
		Object id = quickConvert(data.get("id"), "string");
		Object latitude = quickConvert(data.get("latitude"), "float");
		Object longitude = quickConvert(data.get("longitude"), "float");
		Object title = quickConvert(data.get("title"), "resourcestring");
		Object snippet = quickConvert(data.get("snippet"), "resourcestring");
		Object icon = quickConvert(data.get("icon"), "string");


		 addMarker(id, latitude, longitude, title, snippet, icon);


}
if (objValue instanceof java.util.List) {
	java.util.List<Object> list = (java.util.List<Object>) objValue;
	for (Object object : list) {
		Map<String, Object> data = PluginInvoker.getMap(object);
		Object id = quickConvert(data.get("id"), "string");
		Object latitude = quickConvert(data.get("latitude"), "float");
		Object longitude = quickConvert(data.get("longitude"), "float");
		Object title = quickConvert(data.get("title"), "resourcestring");
		Object snippet = quickConvert(data.get("snippet"), "resourcestring");
		Object icon = quickConvert(data.get("icon"), "string");


		 addMarker(id, latitude, longitude, title, snippet, icon);


	}
}
			}
			break;
			case "removeMarker": {
				


		removeMarker(objValue);



			}
			break;
			case "onMapClick": {
				


		setOnMapClickListener(strValue, objValue);



			}
			break;
		default:
			break;
		}
		
	}
	
	@Override
	@SuppressLint("NewApi")
	public Object getAttribute(WidgetAttribute key, ILifeCycleDecorator decorator) {
		Object nativeWidget = asNativeWidget();
		Object attributeValue = ViewImpl.getAttribute(this, nativeWidget, key, decorator);
		if (attributeValue != null) {
			return attributeValue;
		}
		switch (key.getAttributeName()) {
		}
		
		return null;
	}
	
	@Override
	public Object asWidget() {
		return measurableView;
	}

	

	final static class  MapLibreMap {
		interface OnMapClickListener {
			public boolean onMapClick(LatLng point);
		}
	}
	
	class LatLng {
		private double lat;
		private double lng;
		public LatLng(double lat, double lng) {
			this.lat = lat;
			this.lng = lng;
		}
		public double getLatitude() {
			return lat;
		}
		public double getLongitude() {
			return lng;
		}
		
	}
	private MapLibreMap.OnMapClickListener onMapClickListener;
	private boolean addedOnMapClickListener;
	private void setOnMapClickListener(String strValue, Object objValue) {
		if (objValue instanceof String) {
			onMapClickListener = new OnMapClickListener(this, strValue);
		} else {
			onMapClickListener = (MapLibreMap.OnMapClickListener) objValue;
		}
		
		setOnMapClickListener();
	}
	
	
	public static void addEventInfo(Map<String, Object> obj, LatLng point) {
		obj.put("latitude", point.getLatitude());
		obj.put("longitude", point.getLongitude());
	}
	

	@SuppressLint("NewApi")
private static class OnMapClickListener implements MapLibreMap.OnMapClickListener, com.ashera.widget.IListener{
private IWidget w; private View view; private String strValue; private String action;
public String getAction() {return action;}
public OnMapClickListener(IWidget w, String strValue)  {
this.w = w; this.strValue = strValue;
}
public OnMapClickListener(IWidget w, String strValue, String action)  {
this.w = w; this.strValue = strValue;this.action=action;
}
public boolean onMapClick(LatLng point){
    boolean result = true;
    
	if (action == null || action.equals("onMapClick")) {
		// populate the data from ui to pojo
		w.syncModelFromUiToPojo("onMapClick");
	    java.util.Map<String, Object> obj = getOnMapClickEventObj(point);
	    String commandName =  (String) obj.get(EventExpressionParser.KEY_COMMAND_NAME);
	    
	    // execute command based on command type
	    String commandType = (String)obj.get(EventExpressionParser.KEY_COMMAND_TYPE);
		switch (commandType) {
		case "+":
		    if (EventCommandFactory.hasCommand(commandName)) {
		    	 Object commandResult = EventCommandFactory.getCommand(commandName).executeCommand(w, obj, point);
		    	 if (commandResult != null) {
		    		 result = (boolean) commandResult;
		    	 }
		    }

			break;
		default:
			break;
		}
		
		if (obj.containsKey("refreshUiFromModel")) {
			Object widgets = obj.remove("refreshUiFromModel");
			com.ashera.layout.ViewImpl.refreshUiFromModel(w, widgets, true);
		}
		if (w.getModelUiToPojoEventIds() != null) {
			com.ashera.layout.ViewImpl.refreshUiFromModel(w, w.getModelUiToPojoEventIds(), true);
		}
		if (strValue != null && !strValue.isEmpty() && !strValue.trim().startsWith("+")) {
		    com.ashera.core.IActivity activity = (com.ashera.core.IActivity)w.getFragment().getRootActivity();
		    if (activity != null) {
		    	activity.sendEventMessage(obj);
		    }
		}
	}
    return result;
}//#####

public java.util.Map<String, Object> getOnMapClickEventObj(LatLng point) {
	java.util.Map<String, Object> obj = com.ashera.widget.PluginInvoker.getJSONCompatMap();
    obj.put("action", "action");
    obj.put("eventType", "mapclick");
    obj.put("fragmentId", w.getFragment().getFragmentId());
    obj.put("actionUrl", w.getFragment().getActionUrl());
    obj.put("namespace", w.getFragment().getNamespace());
    
    if (w.getComponentId() != null) {
    	obj.put("componentId", w.getComponentId());
    }
    
    PluginInvoker.putJSONSafeObjectIntoMap(obj, "id", w.getId());
     
        MapViewImpl.addEventInfo(obj, point);
    
    // parse event info into the map
    EventExpressionParser.parseEventExpression(strValue, obj);
    
    // update model data into map
    w.updateModelToEventMap(obj, "onMapClick", (String)obj.get(EventExpressionParser.KEY_EVENT_ARGS));
    return obj;
}
}

	
		@Override
	    public Object asNativeWidget() {
	        return uiView;
	    }
	    public native boolean checkIosVersion(String v) /*-[
			return ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedDescending);
		]-*/;
	@Override
	public void setId(String id){
		if (id != null && !id.equals("")){
			super.setId(id);
			measurableView.setId((int) quickConvert(id, "id"));
		}
	}
	
    @Override
    public void setVisible(boolean b) {
        ((View)asWidget()).setVisibility(b ? View.VISIBLE : View.GONE);
    }
 
    @Override
    public void requestLayout() {
    	if (isInitialised()) {
    		ViewImpl.requestLayout(this, asNativeWidget());
    		
    	}
    }
    
    @Override
    public void invalidate() {
    	if (isInitialised()) {
			ViewImpl.invalidate(this, asNativeWidget());

    	}
    }

	
		//end - body
    
    protected @Property Object mapiView;
    private native void createMLNMapView()/*-[
    	ASUIView* uiView = [ASUIView new];
		uiView.backgroundColor = [UIColor clearColor];
		self->uiView_ = uiView;
    	
    	NSURL *styleURL = [NSURL URLWithString:@"https://tiles.openfreemap.org/styles/liberty"];
		MLNMapView* mapView= [[MLNMapView alloc] initWithFrame:CGRectZero
                                            styleURL:styleURL];
		mapView.delegate = self;
		self->mapiView_ = mapView;
		[uiView addSubview: mapView];
	]-*/;
	private void nativeCreate(Map<String, Object> params) {
		createMLNMapView();
		fragment.addDisposable(new MapViewDelloc());
	}
	

	@com.google.j2objc.annotations.WeakOuter
	private final class MapViewDelloc implements Runnable {
		@Override
		public void run() {
			releaseMLNMapView();
		}
	}
	
    private native void releaseMLNMapView()/*-[
    	MLNMapView* mapView = (MLNMapView*)self->mapiView_;
    	mapView.showsUserLocation = NO;
    	mapView.delegate = nil; 
    	[mapView removeFromSuperview];
		self->mapiView_ = nil;
	]-*/;
	
    private Map<String, Object> markerMap;
	private void addMarker(Object id, Object latitude, Object longitude, Object title, Object snippet, Object icon) {
		removeMarkerById((String) id);
		if (markerMap == null) {
			markerMap = new HashMap<>();
		}
		Object marker = nativeAddMarker((float)latitude, (float)longitude, (String) title, (String)snippet, (String) icon);
		markerMap.put((String) id, marker);
	}
	
	private void removeMarker(Object id) {
		removeMarkerById((String) id);		
	}
	
	public void removeMarkerById(String id) {
		if (markerMap != null && markerMap.containsKey(id)) {
			Object marker = markerMap.get(id);
			if (marker != null) {
				nativeremoveMarker(marker);
				markerMap.remove(id);
			}
		}
	}
	
	private native void nativeremoveMarker(
	        Object marker
	)/*-[
	    MLNMapView* mapView = (MLNMapView*)self->mapiView_;
	    CustomPointAnnotation *ann = (CustomPointAnnotation*) marker;
	    [mapView removeAnnotation:ann];
	]-*/;

	private float lat;
	private float lng;
	private void setCameraTargetLng(Object objValue) {
		lng = (float) objValue;
		nativeSetCameraLatLng(lat, lng);
		
		if (isInitialised()) {
			showMarkerForTarget();
		}
	}
	
	private native void nativeSetCameraLatLng(float lat, float lng)/*-[
		CLLocationCoordinate2D coord = CLLocationCoordinate2DMake(lat, lng);
		MLNMapView* mapView = (MLNMapView*)self->mapiView_; 
    	[mapView setCenterCoordinate:coord animated:NO];
	]-*/;

	private void setCameraTargetLat(Object objValue) {
		lat = (float) objValue;
		nativeSetCameraLatLng(lat, lng);
		
		if (isInitialised()) {
			showMarkerForTarget();
		}
	}
	

	private void setCameraZoom(Object objValue) {
		nativeSetCameraZoom(((Float) objValue).intValue());
	}

	private native void nativeSetCameraZoom(int zoom)/*-[
		MLNMapView* mapView = (MLNMapView*)self->mapiView_;
		[mapView setZoomLevel:zoom animated:NO];
	]-*/;

	
	private Object targetLocationMarkerIcon;
	private String targetLocationMarkerSnippet;
	private String targetLocationMarkerTitle;
	private Boolean showMarkerForTarget;
	
	private void setTargetLocationMarkerIcon(Object objValue) {
		targetLocationMarkerIcon = objValue;
		
		if (isInitialised()) {
			showMarkerForTarget();
		}
	}

	private void setTargetLocationMarkerSnippet(Object objValue) {
		targetLocationMarkerSnippet = (String) objValue;
		
		if (isInitialised()) {
			showMarkerForTarget();
		}
	}

	private void setTargetLocationMarkerTitle(Object objValue) {
		targetLocationMarkerTitle = (String) objValue;
		
		if (isInitialised()) {
			showMarkerForTarget();
		}
	}

	private void showMarkerForTarget(Object objValue) {
		showMarkerForTarget = (boolean) objValue;
		
		if (isInitialised()) {
			showMarkerForTarget();
		}
	}
	


	private void setCameraBearing(Object objValue) {
		nativeSetCameraBearing((float) objValue);
	}
	
	private native void nativeSetCameraBearing(double bearing)/*-[
    	MLNMapView* mapView = (MLNMapView*)self->mapiView_;
    	[mapView setDirection:bearing animated:NO];
	]-*/;

	private native void nativeSetCompassEnabled(boolean enabled)/*-[
	    MLNMapView* mapView = (MLNMapView*)self->mapiView_;
	    mapView.showsCompassView = enabled;
	]-*/;
	
	private void setUiCompass(Object objValue) {
	    boolean enabled = (boolean) objValue;
	    nativeSetCompassEnabled(enabled);
	}

	private void setUiCompassGravity(Object objValue) {
		nativeSetCompassPosition((int) objValue);
	}
	
	private final static int Gravity_TOP = r.android.view.Gravity.TOP;
	private final static int Gravity_BOTTOM = r.android.view.Gravity.BOTTOM;
	private final static int Gravity_LEFT = r.android.view.Gravity.LEFT;
	private final static int Gravity_RIGHT = r.android.view.Gravity.RIGHT;
	private native void nativeSetCompassPosition(int gravity)/*-[
	    MLNMapView* mapView = (MLNMapView*)self->mapiView_;
	
	    MLNOrnamentPosition position = MLNOrnamentPositionTopRight;
	
	    BOOL isTop = (gravity & ASMapViewImpl_Gravity_TOP) == ASMapViewImpl_Gravity_TOP;
	    BOOL isBottom = (gravity & ASMapViewImpl_Gravity_BOTTOM) == ASMapViewImpl_Gravity_BOTTOM;
	    BOOL isLeft = (gravity & ASMapViewImpl_Gravity_LEFT) == ASMapViewImpl_Gravity_LEFT;
	    BOOL isRight = (gravity & ASMapViewImpl_Gravity_RIGHT) == ASMapViewImpl_Gravity_RIGHT;
	
	    if (isTop && isLeft) {
	        position = MLNOrnamentPositionTopLeft;
	    } else if (isTop && isRight) {
	        position = MLNOrnamentPositionTopRight;
	    } else if (isBottom && isLeft) {
	        position = MLNOrnamentPositionBottomLeft;
	    } else if (isBottom && isRight) {
	        position = MLNOrnamentPositionBottomRight;
	    }
	
	    mapView.compassViewPosition = position;
	]-*/;
	/*-[	
	- (void)mapView:(MLNMapView *)mapView didFinishLoadingStyle:(MLNStyle *)style {
	}
	
	]-*/;
	/*-[	
	- (void)mapView:(MLNMapView *)mapView didTapAtCoordinate:(CLLocationCoordinate2D)coordinate {
	    double lat = coordinate.latitude;
	    double lng = coordinate.longitude;
	    [self setOnMapClickListenerWithDouble: lat withDouble: lng];
	}
	]-*/;
	/*-[
	- (BOOL)mapView:(MLNMapView *)mapView annotationCanShowCallout:(id<MLNAnnotation>)annotation {
		if (![annotation isKindOfClass:[CustomPointAnnotation class]]) {
	        return NO;
	    }
	    
	    CustomPointAnnotation *ann = (CustomPointAnnotation *)annotation;
	    return ann.title != nil;
	}
	]-*/;
	
	/*-[
	- (MLNAnnotationView *)mapView:(MLNMapView *)mapView
	            viewForAnnotation:(id<MLNAnnotation>)annotation {

	    if (![annotation isKindOfClass:[CustomPointAnnotation class]]) {
	        return nil;
	    }

	    CustomPointAnnotation *ann = (CustomPointAnnotation *)annotation;

	    if (!ann.imageName) {
	        return nil;
	    }

	    NSString *reuseId = ann.imageName;
	    MLNAnnotationView *view =
	        [mapView dequeueReusableAnnotationViewWithIdentifier:reuseId];

	    UIImage *image = nil;
		id imgObj = [self getImageWithNSString:ann.imageName]; // J2ObjC naming

		if ([imgObj isKindOfClass:[UIImage class]]) {
		    image = (UIImage *)imgObj;
		}

	    if (!image) return nil;

	    CGSize size = image.size;

	    UIImageView *img;

	    if (!view) {
	        view = [[MLNAnnotationView alloc] initWithReuseIdentifier:reuseId];

	        img = [[UIImageView alloc] initWithImage:image];
	        img.tag = 100;

	        img.frame = CGRectMake(0, 0, size.width, size.height);

	        [view addSubview:img];
	        view.frame = img.frame;
	    } else {
	        img = [view viewWithTag:100];
	        img.image = image;
	        img.frame = CGRectMake(0, 0, size.width, size.height);
	        view.frame = img.frame;
	    }

	    view.centerOffset = CGVectorMake(0, -size.height / 2.0);

	    return view;
	}
	]-*/
	
	private Object getImage(String imageName) {
		Object drawable = quickConvert(imageName, "drawable");
		
		if (drawable instanceof r.android.graphics.drawable.Drawable) {
			drawable = ((r.android.graphics.drawable.Drawable) drawable).getDrawable();
		}
		return drawable;
	}
	private native Object nativeAddMarker(
	        double lat,
	        double lon,
	        String title,
	        String snippet,
	        String imageName
	)/*-[
	    MLNMapView* mapView = (MLNMapView*)self->mapiView_;

	    CustomPointAnnotation *ann = [[CustomPointAnnotation alloc] init];
	    ann.coordinate = CLLocationCoordinate2DMake(lat, lon);
	    ann.title = title;
	    ann.subtitle = snippet;
	    ann.imageName = imageName;

	    [mapView addAnnotation:ann];
	    return ann;
	]-*/;
	
	
    @Override
    public void initialized() {
    	super.initialized();
    	
    	showMarkerForTarget();
    }

	public void showMarkerForTarget() {
		removeMarkerById("targetLocationMarker");
		if (showMarkerForTarget != null && showMarkerForTarget) {
			addMarker("targetLocationMarker", lat, lng, targetLocationMarkerTitle, targetLocationMarkerSnippet, targetLocationMarkerIcon);
		}
	}
    
    private void nativeMakeFrameForChildWidget(int l, int t, int r, int b) {
		ViewImpl.updateBounds(mapiView, 0, 0, r-l, b-t);
		
	}
    
	
	private void setShowsUserLocation(Object objValue) {
		nativeShowsUserLocation((boolean) objValue);
	}
	
	private native void nativeShowsUserLocation(boolean enabled)/*-[
    	MLNMapView* mapView = (MLNMapView*)self->mapiView_;
    	mapView.showsUserLocation = enabled;
]-*/;
	
	private void setOnMapClickListener() {
		
	}
	
	private void setOnMapClickListener(double lat, double lng) {
		if (onMapClickListener != null) {
			onMapClickListener.onMapClick(new LatLng(lat, lng));
		}
		
	}

}
