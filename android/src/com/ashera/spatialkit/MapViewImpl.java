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

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.*;
import androidx.core.view.*;
import android.view.*;
import android.graphics.drawable.*;


import android.os.Build;
import android.view.View;
import android.text.*;

import com.ashera.core.IFragment;
import com.ashera.converter.*;

import android.annotation.SuppressLint;

import com.ashera.layout.*;
import com.ashera.plugin.*;
import com.ashera.widget.bus.*;
import com.ashera.widget.*;
import com.ashera.widget.bus.Event.*;
import com.ashera.widget.IWidgetLifeCycleListener.EventId;
import com.ashera.widget.IWidgetLifeCycleListener.EventId.*;


import static com.ashera.widget.IWidget.*;
//end - imports

import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMap;
@SuppressLint("NewApi")
public class MapViewImpl extends BaseWidget implements org.maplibre.android.location.permissions.PermissionsListener {
	//start - body
	public final static String LOCAL_NAME = "org.maplibre.android.maps.MapView"; 
	public final static String GROUP_NAME = "org.maplibre.android.maps.MapView";

	protected org.maplibre.android.maps.MapView mapView;
	
	
	@Override
	public void loadAttributes(String attributeName) {
		ViewImpl.register(attributeName);


		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiZoomGestures").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiTiltGestures").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiScrollGestures").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiRotateGestures").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiHorizontalScrollGestures").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiDoubleTapGestures").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiCompassGravity").withType("gravity").withOrder(-1));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiCompassFadeFacingNorth").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_uiCompass").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraBearing").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraPitchMax").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraPitchMin").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraTargetLat").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraTargetLng").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraTilt").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraZoom").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraZoomMax").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("maplibre_cameraZoomMin").withType("float"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("showMarkerForTarget").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("targetLocationMarkerTitle").withType("resourcestring"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("targetLocationMarkerSnippet").withType("resourcestring"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("targetLocationMarkerIcon").withType("drawable"));
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

		
	public class MapViewExt extends org.maplibre.android.maps.MapView implements ILifeCycleDecorator, com.ashera.widget.IMaxDimension{
		private MeasureEvent measureFinished = new MeasureEvent();
		private OnLayoutEvent onLayoutEvent = new OnLayoutEvent();
		
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

		public MapViewExt(Context context, android.util.AttributeSet attrs, int defStyleAttr) {
	        super(context, attrs, defStyleAttr);
	    }

		public MapViewExt(Context context) {
			super(context);
			
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
			
			ViewImpl.nativeMakeFrame(asNativeWidget(), l, t, r, b);
			
			replayBufferedEvents();
			
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
		public void onDraw(Canvas canvas) {
			Runnable runnable = () -> super.onDraw(canvas);
			executeMethodListeners("onDraw", runnable, canvas);
		}

		@Override
		public void draw(Canvas canvas) {
			Runnable runnable = () -> super.draw(canvas);
			executeMethodListeners("draw", runnable, canvas);
		}

		@SuppressLint("WrongCall")
		@Override
		public void execute(String method, Object... args) {
			switch (method) {
				case "onDraw":
					setOnMethodCalled(true);
					super.onDraw((Canvas) args[0]);
					break;

				case "draw":
					setOnMethodCalled(true);
					super.draw((Canvas) args[0]);
					break;

				default:
					break;
			}
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
        
    	public void setState0(float value) {
    		ViewImpl.setState(MapViewImpl.this, 0, value);
    	}
    	public void setState0(int value) {
    		ViewImpl.setState(MapViewImpl.this, 0, value);
    	}
    	public void setState0(double value) {
    		ViewImpl.setState(MapViewImpl.this, 0, value);
    	}
    	
    	public void setState0(Float value) {
    		ViewImpl.setState(MapViewImpl.this, 0, value);
    	}
    	public void setState0(Integer value) {
    		ViewImpl.setState(MapViewImpl.this, 0, value);
    	}
    	public void setState0(Double value) {
    		ViewImpl.setState(MapViewImpl.this, 0, value);
    	}
    	public void setState0(Object value) {
    		ViewImpl.setState(MapViewImpl.this, 0, value);
    	}
    	public void setState1(float value) {
    		ViewImpl.setState(MapViewImpl.this, 1, value);
    	}
    	public void setState1(int value) {
    		ViewImpl.setState(MapViewImpl.this, 1, value);
    	}
    	public void setState1(double value) {
    		ViewImpl.setState(MapViewImpl.this, 1, value);
    	}
    	
    	public void setState1(Float value) {
    		ViewImpl.setState(MapViewImpl.this, 1, value);
    	}
    	public void setState1(Integer value) {
    		ViewImpl.setState(MapViewImpl.this, 1, value);
    	}
    	public void setState1(Double value) {
    		ViewImpl.setState(MapViewImpl.this, 1, value);
    	}
    	public void setState1(Object value) {
    		ViewImpl.setState(MapViewImpl.this, 1, value);
    	}
    	public void setState2(float value) {
    		ViewImpl.setState(MapViewImpl.this, 2, value);
    	}
    	public void setState2(int value) {
    		ViewImpl.setState(MapViewImpl.this, 2, value);
    	}
    	public void setState2(double value) {
    		ViewImpl.setState(MapViewImpl.this, 2, value);
    	}
    	
    	public void setState2(Float value) {
    		ViewImpl.setState(MapViewImpl.this, 2, value);
    	}
    	public void setState2(Integer value) {
    		ViewImpl.setState(MapViewImpl.this, 2, value);
    	}
    	public void setState2(Double value) {
    		ViewImpl.setState(MapViewImpl.this, 2, value);
    	}
    	public void setState2(Object value) {
    		ViewImpl.setState(MapViewImpl.this, 2, value);
    	}
    	public void setState3(float value) {
    		ViewImpl.setState(MapViewImpl.this, 3, value);
    	}
    	public void setState3(int value) {
    		ViewImpl.setState(MapViewImpl.this, 3, value);
    	}
    	public void setState3(double value) {
    		ViewImpl.setState(MapViewImpl.this, 3, value);
    	}
    	
    	public void setState3(Float value) {
    		ViewImpl.setState(MapViewImpl.this, 3, value);
    	}
    	public void setState3(Integer value) {
    		ViewImpl.setState(MapViewImpl.this, 3, value);
    	}
    	public void setState3(Double value) {
    		ViewImpl.setState(MapViewImpl.this, 3, value);
    	}
    	public void setState3(Object value) {
    		ViewImpl.setState(MapViewImpl.this, 3, value);
    	}
    	public void setState4(float value) {
    		ViewImpl.setState(MapViewImpl.this, 4, value);
    	}
    	public void setState4(int value) {
    		ViewImpl.setState(MapViewImpl.this, 4, value);
    	}
    	public void setState4(double value) {
    		ViewImpl.setState(MapViewImpl.this, 4, value);
    	}
    	
    	public void setState4(Float value) {
    		ViewImpl.setState(MapViewImpl.this, 4, value);
    	}
    	public void setState4(Integer value) {
    		ViewImpl.setState(MapViewImpl.this, 4, value);
    	}
    	public void setState4(Double value) {
    		ViewImpl.setState(MapViewImpl.this, 4, value);
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
Context context = (Context) fragment.getRootActivity();
	Object systemStyle = params.get("systemStyle");
	Object systemAndroidAttrStyle = params.get("systemAndroidAttrStyle");
	
	if (systemStyle == null && systemAndroidAttrStyle == null) {
		org.maplibre.android.MapLibre.getInstance(context);mapView = new MapViewExt(context);
	} else {
		int defStyleAttr = 0;
		int defStyleRes = 0;
		
		if (systemStyle != null) {
			defStyleRes = context.getResources().getIdentifier((String) systemStyle, "style", context.getPackageName());	
		}
		
		if (systemAndroidAttrStyle != null) {
			defStyleAttr = context.getResources().getIdentifier((String) systemAndroidAttrStyle, "attr", "android");	
		}
		
		if (defStyleRes == 0) {
			org.maplibre.android.MapLibre.getInstance(context);mapView = new MapViewExt(context, null, defStyleAttr);	
		} else {
		}
		
	}

		nativeCreate(params);	
		ViewImpl.registerCommandConveter(this);
	}

	@Override
	@SuppressLint("NewApi")
	public void setAttribute(WidgetAttribute key, String strValue, Object objValue, ILifeCycleDecorator decorator) {
		Object nativeWidget = asNativeWidget();
		ViewImpl.setAttribute(this,  key, strValue, objValue, decorator);
		
		switch (key.getAttributeName()) {
			case "maplibre_uiZoomGestures": {
				


		setUiZoomGestures(objValue);



			}
			break;
			case "maplibre_uiTiltGestures": {
				


		setUiTiltGestures(objValue);



			}
			break;
			case "maplibre_uiScrollGestures": {
				


		setUiScrollGestures(objValue);



			}
			break;
			case "maplibre_uiRotateGestures": {
				


		setUiRotateGestures(objValue);



			}
			break;
			case "maplibre_uiHorizontalScrollGestures": {
				


		setUiHorizontalScrollGestures(objValue);



			}
			break;
			case "maplibre_uiDoubleTapGestures": {
				


		setUiDoubleTapGestures(objValue);



			}
			break;
			case "maplibre_uiCompassGravity": {
				


		setUiCompassGravity(objValue);



			}
			break;
			case "maplibre_uiCompassFadeFacingNorth": {
				


		setUiCompassFadeFacingNorth(objValue);



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
			case "maplibre_cameraPitchMax": {
				


		setCameraPitchMax(objValue);



			}
			break;
			case "maplibre_cameraPitchMin": {
				


		setCameraPitchMin(objValue);



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
			case "maplibre_cameraTilt": {
				


		setCameraTilt(objValue);



			}
			break;
			case "maplibre_cameraZoom": {
				


		setCameraZoom(objValue);



			}
			break;
			case "maplibre_cameraZoomMax": {
				


		setCameraZoomMax(objValue);



			}
			break;
			case "maplibre_cameraZoomMin": {
				


		setCameraZoomMin(objValue);



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
		Object icon = quickConvert(data.get("icon"), "drawable");


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
		Object icon = quickConvert(data.get("icon"), "drawable");


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
		return mapView;
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
	        return mapView;
	    }
	@Override
	public void setId(String id){
		if (id != null && !id.equals("")){
			super.setId(id);
			mapView.setId((int) quickConvert(id, "id"));
		}
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
    private org.maplibre.android.maps.MapLibreMap mapLibreMap;
    private void nativeCreate(Map<String, Object> params) {
		fragment.getEventBus().on("onCreateMap", new com.ashera.widget.bus.EventBusHandler("onCreateMap") {
			@Override
			protected void doPerform(Object payload) {
				android.os.Bundle bundle = null;
				
				if (payload != null) {
					bundle = (android.os.Bundle) payload;
				}
				mapView.onCreate(bundle);
				
			}
		});
		
		fragment.getEventBus().on("onResumeMap", new com.ashera.widget.bus.EventBusHandler("onResumeMap") {
			@Override
			protected void doPerform(Object payload) {
				loadMap(false);
			}
		});
		
		fragment.getEventBus().on("onStartMap", new com.ashera.widget.bus.EventBusHandler("onStartMap") {
			@Override
			protected void doPerform(Object payload) {
				mapView.onStart();
			}
		});
		
		fragment.getEventBus().on("onStopMap", new com.ashera.widget.bus.EventBusHandler("onStopMap") {
			@Override
			protected void doPerform(Object payload) {
				mapView.onStop();
			}
		});
		
		fragment.getEventBus().on("onPauseMap", new com.ashera.widget.bus.EventBusHandler("onPauseMap") {
			@Override
			protected void doPerform(Object payload) {
				mapView.onPause();
			}
		});
		
		fragment.getEventBus().on("onDestroyMap", new com.ashera.widget.bus.EventBusHandler("onDestroyMap") {
			@Override
			protected void doPerform(Object payload) {
				mapView.onDestroy();
			}
		});
		
		fragment.getEventBus().on("onSaveInstanceStateMap", new com.ashera.widget.bus.EventBusHandler("onSaveInstanceStateMap") {
			@Override
			protected void doPerform(Object payload) {
				android.os.Bundle bundle = null;
				
				if (payload != null) {
					bundle = (android.os.Bundle) payload;
				}
				mapView.onSaveInstanceState(bundle);
			}
		});
		
		fragment.getEventBus().on("onRequestPermissionsResultMap", new com.ashera.widget.bus.EventBusHandler("onSaveInstanceStateMap") {
			@Override
			protected void doPerform(Object payload) {
				if (permissionsManager != null) {
					permissionsManager.onRequestPermissionsResult((int) ((Object[]) payload)[0], (String[]) ((Object[]) payload)[1], (int[]) ((Object[]) payload)[2]);	
				}
			}
		});
		
		
	}


	private void updateMapUiSettings(org.maplibre.android.maps.MapLibreMap map) {
		if (map != null) {
			if (map.getStyle() == null) {
				map.setStyle("https://tiles.openfreemap.org/styles/liberty", new org.maplibre.android.maps.Style.OnStyleLoaded() {
	
					@Override
					public void onStyleLoaded(org.maplibre.android.maps.Style arg0) {
						updateMapUiSettingsAfterStyleLoad(map);
					}
				});
			} else {
				updateMapUiSettingsAfterStyleLoad(map);
			}
		}
	}
	
	private void updateMapUiSettingsAfterStyleLoad(org.maplibre.android.maps.MapLibreMap map) {
		if (zoomGestUreEnabled != null) {
			map.getUiSettings().setZoomGesturesEnabled(zoomGestUreEnabled);
		}
		if (doubleTapGestures != null) {
			map.getUiSettings().setDoubleTapGesturesEnabled(doubleTapGestures);
		}
		
		if (horizontalScrollGestures != null) {
			map.getUiSettings().setHorizontalScrollGesturesEnabled(horizontalScrollGestures);
		}
		
		if (rotateGestures != null) {
			map.getUiSettings().setRotateGesturesEnabled(rotateGestures);
		}
		
		if (scrollGestures != null) {
			map.getUiSettings().setScrollGesturesEnabled(scrollGestures);
		}
		
		if (tiltGestures != null) {
			map.getUiSettings().setTiltGesturesEnabled(tiltGestures);
		}
		
		if (zoomMin != null) {
			map.setMinZoomPreference(zoomMin);
		}
		
		if (zoomMax != null) {
			map.setMinZoomPreference(zoomMax);
		}
		
		if (pitchMin != null) {
			map.setMinPitchPreference(pitchMin);
		}
		
		if (pitchMax != null) {
			map.setMaxPitchPreference(pitchMax);
		}
		
		if (compassEnabled != null) {
			map.getUiSettings().setCompassEnabled(compassEnabled);
		}
		
		if (compassFadeFacingNorth != null) {
			map.getUiSettings().setCompassFadeFacingNorth(compassFadeFacingNorth);
		}
		
		if (compassGravity != null) {
			map.getUiSettings().setCompassGravity(compassGravity);
		}
		
		if (latitude != null && longitude != null) {
			org.maplibre.android.camera.CameraPosition cameraPosition = new org.maplibre.android.camera.CameraPosition.Builder()
			        .target(new org.maplibre.android.geometry.LatLng(latitude, longitude))
			        .zoom(zoom)
			        .bearing(bearing != null ? bearing : 0)
			        .tilt(tilt  != null ? tilt : 0)
			        .build();
			map.setCameraPosition(cameraPosition);
		}
		if (showMarkerForTarget != null && showMarkerForTarget) {
			addMakerToCache("#targetLocationMarker", latitude, longitude, targetLocationMarkerTitle, targetLocationMarkerSnippet, targetLocationMarkerIcon);
		} else {
			MarkerCache targetMarkerCache = markerCacheMap.get("#targetLocationMarker");
			if (targetMarkerCache != null) {
				targetMarkerCache.changed = true;
				targetMarkerCache.markerOptions = null;
			}
		}
		
		if (markerCacheMap != null && !markerCacheMap.isEmpty()) {
			for (String key : markerCacheMap.keySet()) {
				MarkerCache cache = markerCacheMap.get(key);
				
				if (cache.marker == null) {
					if (cache.markerOptions != null) {
						cache.marker = map.addMarker(cache.markerOptions);
					}
					
				} else {
					if (cache.changed) {
						map.removeMarker(cache.marker);
						cache.marker = null;
						
						if (cache.markerOptions != null) {
							cache.marker = map.addMarker(cache.markerOptions);
						}
					}
				}
				
				cache.changed = false;
			}
			
		}
		
		if (showsUserLocation != null) {
			setUserLocationEnabled(map, map.getStyle(), (android.app.Activity) fragment.getRootActivity(), showsUserLocation);
		}
		
		if (onMapClickListener != null && !addedOnMapClickListener) {
			addedOnMapClickListener = true;
			mapLibreMap.addOnMapClickListener(new MapLibreMap.OnMapClickListener() {
				@Override
				public boolean onMapClick(LatLng latLng) {
					if (onMapClickListener != null) {
						return onMapClickListener.onMapClick(latLng);
					}
					return false;
				}
			});
		}
	}
	private Boolean zoomGestUreEnabled;
	private Boolean doubleTapGestures;
	private Boolean rotateGestures;
	private Boolean horizontalScrollGestures;
	private Boolean scrollGestures;
	private Boolean tiltGestures;

	private Float latitude;
	private Float longitude;
	private Float tilt;
	private Float bearing;
	private Float zoom;
	private Float zoomMax;
	private Float zoomMin;
	
	private Float pitchMin;
	private Float pitchMax;
	
	private Object targetLocationMarkerIcon;
	private String targetLocationMarkerSnippet;
	private String targetLocationMarkerTitle;
	private Boolean showMarkerForTarget;
	
	private Boolean compassEnabled;
	private Integer compassGravity;
	private Boolean compassFadeFacingNorth;
	
	private Map<String, MarkerCache> markerCacheMap;

	private void setUiZoomGestures(Object objValue) {
		zoomGestUreEnabled = (Boolean) objValue;
		updateMapUiSettings(mapLibreMap);
	}
	
	
	private void setUiDoubleTapGestures(Object objValue) {
		doubleTapGestures = (Boolean) objValue;
		updateMapUiSettings(mapLibreMap);		
	}

	private void setUiHorizontalScrollGestures(Object objValue) {
		horizontalScrollGestures = (Boolean) objValue;
		updateMapUiSettings(mapLibreMap);			
	}

	
	private void setUiRotateGestures(Object objValue) {
		rotateGestures = (Boolean) objValue;
		updateMapUiSettings(mapLibreMap);			
	}

	private void setUiScrollGestures(Object objValue) {
		scrollGestures = (Boolean) objValue;
		updateMapUiSettings(mapLibreMap);			
	}

	private void setUiTiltGestures(Object objValue) {
		tiltGestures = (Boolean) objValue;
		updateMapUiSettings(mapLibreMap);			
	}

	private void setCameraZoomMin(Object objValue) {
		zoomMax = (Float) objValue;
		updateMapUiSettings(mapLibreMap);
	}

	private void setCameraZoomMax(Object objValue) {
		zoomMin = (Float) objValue;
		updateMapUiSettings(mapLibreMap);
	}

	private void setCameraZoom(Object objValue) {
		zoom = (Float) objValue;
		updateMapUiSettings(mapLibreMap);
	}

	private void setCameraTargetLat(Object objValue) {
		latitude = (Float) objValue;
		updateMapUiSettings(mapLibreMap);	
	}

	private void setCameraTargetLng(Object objValue) {
		longitude = (Float) objValue;		
		updateMapUiSettings(mapLibreMap);	
	}
	

	private void setCameraBearing(Object objValue) {
		bearing = (Float) objValue;		
		updateMapUiSettings(mapLibreMap);	
	}
	
	private void setCameraTilt(Object objValue) {
		tilt = (Float) objValue;		
		updateMapUiSettings(mapLibreMap);	
	}

	private void setCameraPitchMin(Object objValue) {
		pitchMin = (Float) objValue;		
		updateMapUiSettings(mapLibreMap);	
	}

	private void setCameraPitchMax(Object objValue) {
		pitchMax = (Float) objValue;		
		updateMapUiSettings(mapLibreMap);	
	}

	private void setUiCompass(Object objValue) {
		compassEnabled = (Boolean) objValue;	
		updateMapUiSettings(mapLibreMap);
	}

	private void setUiCompassFadeFacingNorth(Object objValue) {
		compassFadeFacingNorth = (Boolean) objValue;	
		updateMapUiSettings(mapLibreMap);
	}

	private void setUiCompassGravity(Object objValue) {
		compassGravity = (Integer) objValue;	
		updateMapUiSettings(mapLibreMap);
	}

	private void setTargetLocationMarkerIcon(Object objValue) {
		targetLocationMarkerIcon = objValue;	
		updateMapUiSettings(mapLibreMap);
	}

	private void setTargetLocationMarkerSnippet(Object objValue) {
		targetLocationMarkerSnippet = (String) objValue;	
		updateMapUiSettings(mapLibreMap);
	}

	private void setTargetLocationMarkerTitle(Object objValue) {
		targetLocationMarkerTitle = (String) objValue;	
		updateMapUiSettings(mapLibreMap);
	}

	private void showMarkerForTarget(Object objValue) {
		showMarkerForTarget = (Boolean) objValue;	
		updateMapUiSettings(mapLibreMap);
	}
	
	private void addMarker(Object id, Object latitude, Object longitude, Object title, Object snippet, Object icon) {
		addMakerToCache(id, latitude, longitude, title, snippet, icon);
		updateMapUiSettings(mapLibreMap);
	}

	private void addMakerToCache(Object id, Object latitude, Object longitude, Object title, Object snippet,
			Object icon) {
		org.maplibre.android.annotations.MarkerOptions markerOptions = new org.maplibre.android.annotations.MarkerOptions().position(new org.maplibre.android.geometry.LatLng((float) latitude, (float)longitude));

		if (title != null) {
			markerOptions.title((String) title);
		}
		
		if (snippet != null) {
			markerOptions.snippet((String) snippet);
		}
		
		if (icon != null && icon instanceof BitmapDrawable) {
			markerOptions.icon(org.maplibre.android.annotations.IconFactory.getInstance(
					(Context) fragment.getRootActivity()).fromBitmap(((BitmapDrawable)icon).getBitmap()));
		}
		
		if (markerCacheMap == null) {
			markerCacheMap = new java.util.TreeMap<>();
		}
		
		if (!markerCacheMap.containsKey(id)) {
			MarkerCache markerCache = new MarkerCache();
			markerCache.markerOptions = markerOptions;
			markerCacheMap.put((String) id, markerCache);	
		} else {
			MarkerCache markerCache = markerCacheMap.get(id);
			markerCache.markerOptions = markerOptions;
			markerCache.changed = true;
		}
	}
	
	class MarkerCache {
		org.maplibre.android.annotations.MarkerOptions markerOptions;
		org.maplibre.android.annotations.Marker marker;
		boolean changed = true;
	}
	
	private Boolean showsUserLocation;
	private void setShowsUserLocation(Object objValue) {
		this.showsUserLocation = (Boolean) objValue;;
		updateMapUiSettings(mapLibreMap);
	}
	
	private void removeMarker(Object id) {
		MarkerCache cache = markerCacheMap.get(id);
		
		if (cache != null && cache.marker != null) {
			mapLibreMap.removeMarker(cache.marker);
			markerCacheMap.remove(id);
		}
	}
	private org.maplibre.android.location.LocationComponent locationComponent;
	private org.maplibre.android.location.permissions.PermissionsManager permissionsManager;
	private void setUserLocationEnabled(org.maplibre.android.maps.MapLibreMap map, org.maplibre.android.maps.Style style, android.app.Activity context, boolean enabled) {
	    if (enabled) {
	        if (isLocationPermissionGranted()) {
	            locationComponent = map.getLocationComponent();

	            if (!locationComponent.isLocationComponentActivated()) {
	            	org.maplibre.android.location.LocationComponentOptions locationComponentOptions = org.maplibre.android.location.LocationComponentOptions.builder(context)
			            .pulseEnabled(true)
			            .pulseColor(android.graphics.Color.RED)
			            .foregroundTintColor(android.graphics.Color.BLACK)
			            .build();
	                locationComponent.activateLocationComponent(
	                    org.maplibre.android.location.LocationComponentActivationOptions.builder(context, style).
	                    	locationComponentOptions(locationComponentOptions).
	                    	build()
	                );
	            }

	            locationComponent.setLocationComponentEnabled(true);

	            // Optional (customize behavior)
	            locationComponent.setCameraMode(org.maplibre.android.location.modes.CameraMode.TRACKING);
	            locationComponent.setRenderMode(org.maplibre.android.location.modes.RenderMode.COMPASS);
	        } else {
	        	permissionsManager = new org.maplibre.android.location.permissions.PermissionsManager(this);
	            permissionsManager.requestLocationPermissions(context);
	        }
	    } else {
	        if (locationComponent != null) {
	            locationComponent.setLocationComponentEnabled(false);
	        }
	    }
	}

	@Override
	public void onExplanationNeeded(List<String> ex) {
		Toast.makeText((Context) fragment.getRootActivity(), "You need to accept location permissions.",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPermissionResult(boolean granted) {
		 if (granted) {
			 loadMap(true);
         } 
	}



	private void loadMap(boolean force) {
		if (force || showsUserLocation == null || !showsUserLocation || isLocationPermissionGranted()) {
			mapView.getMapAsync (new org.maplibre.android.maps.OnMapReadyCallback() {
				@Override
				public void onMapReady(org.maplibre.android.maps.MapLibreMap map) {
					mapLibreMap = map;
					updateMapUiSettings(map);
				}
				
			});
			mapView.onResume();
		}
	}

	private boolean isLocationPermissionGranted() {
		return org.maplibre.android.location.permissions.PermissionsManager.areLocationPermissionsGranted((Context) fragment.getRootActivity());
	}
	
	public static void addEventInfo(Map<String, Object> obj, org.maplibre.android.geometry.LatLng point) {
		obj.put("latitude", point.getLatitude());
		obj.put("longitude", point.getLongitude());
	}

	private MapLibreMap.OnMapClickListener onMapClickListener;
	private boolean addedOnMapClickListener;
	private void setOnMapClickListener(String strValue, Object objValue) {
		if (objValue instanceof String) {
			onMapClickListener = new OnMapClickListener(this, strValue);
		} else {
			onMapClickListener = (MapLibreMap.OnMapClickListener) objValue;
		}
		
		updateMapUiSettings(mapLibreMap);
	}
}
