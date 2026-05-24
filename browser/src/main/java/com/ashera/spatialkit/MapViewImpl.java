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


import org.teavm.jso.dom.html.HTMLElement;

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


import static com.ashera.widget.IWidget.*;
//end - imports
@SuppressLint("NewApi")
public class MapViewImpl extends BaseWidget {
	//start - body
	public final static String LOCAL_NAME = "org.maplibre.android.maps.MapView"; 
	public final static String GROUP_NAME = "org.maplibre.android.maps.MapView";

	protected org.teavm.jso.dom.html.HTMLElement hTMLElement;
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
        	appScreenLocation[0] = hTMLElement.getBoundingClientRect().getLeft();
        	appScreenLocation[1] = hTMLElement.getBoundingClientRect().getTop();
        }
        @Override
        public void getWindowVisibleDisplayFrame(r.android.graphics.Rect displayFrame){
        	
        	org.teavm.jso.dom.html.TextRectangle boundingClientRect = hTMLElement.getBoundingClientRect();
			displayFrame.top = boundingClientRect.getTop();
        	displayFrame.left = boundingClientRect.getLeft();
        	displayFrame.bottom = boundingClientRect.getBottom();
        	displayFrame.right = boundingClientRect.getRight();
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
            ((HTMLElement)asNativeWidget()).getStyle().setProperty("display", visibility != View.VISIBLE ? "none" : "block");
            
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
	}

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
	        return hTMLElement;
	    }
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
    private JSMapLibreMap map;
	private Object targetLocationMarkerIcon;
	private String targetLocationMarkerSnippet;
	private String targetLocationMarkerTitle;
	private Boolean showMarkerForTarget;
	private Map<String, Marker> markerMap;
	private void nativeCreate(Map<String, Object> params) {
    	hTMLElement = org.teavm.jso.dom.html.HTMLDocument.current().createElement("div");
    	hTMLElement.getStyle().setProperty("box-sizing", "border-box");
    	
    	map = create(hTMLElement, "https://tiles.openfreemap.org/styles/liberty");
	}
	
	private void setTargetLocationMarkerIcon(Object objValue) {
		targetLocationMarkerIcon = objValue;
		
		if (isInitialised()) {
			showMarkerForTarget();
		}
	}

	private void setTargetLocationMarkerSnippet(Object objValue) {
		targetLocationMarkerSnippet = (String) objValue;
		
		if (isInitialised()) {
			if (markerMap != null) {
				Marker marker = markerMap.get("targetLocationMarker");
				
				if (marker != null) {
					updateTitleAndSnippet(targetLocationMarkerTitle, targetLocationMarkerSnippet, marker);
				}
			}
		}
	}

	private void setTargetLocationMarkerTitle(Object objValue) {
		targetLocationMarkerTitle = (String) objValue;
		
		if (markerMap != null) {
			Marker marker = markerMap.get("targetLocationMarker");
			
			if (marker != null) {
				updateTitleAndSnippet(targetLocationMarkerTitle, targetLocationMarkerSnippet, marker);
			}
		}
	}

	private void showMarkerForTarget(Object objValue) {
		showMarkerForTarget = (boolean) objValue;
		
		if (isInitialised()) {
			showMarkerForTarget();
		}
	}

	private void setCameraZoom(Object objValue) {
		map.setZoom((float) objValue);		
	}

	private float lat;
	private float lng;
	private void setCameraTargetLng(Object objValue) {
		lng = (float) objValue;
		
		if (isInitialised()) {
			showMarkerForTarget();
			map.setCenter(new double [] {lng, lat });
		}
	}

	private void setCameraTargetLat(Object objValue) {
		lat = (float) objValue;
		
		if (isInitialised()) {
			showMarkerForTarget();
			map.setCenter(new double [] {lng, lat });
		}
	}


	private void addMarker(Object id, Object latitude, Object longitude, Object title, Object snippet, Object drawable) {
		Marker marker;
		
		removeMarkerById((String) id);
		if (markerMap == null) {
			markerMap = new HashMap<>();
		}
		if (drawable != null) {
			Object icon = ((r.android.graphics.drawable.Drawable) drawable).getDrawable();
			HTMLElement img = (HTMLElement) org.teavm.jso.browser.Window.current()
			        .getDocument()
			        .createElement("img");

			if (icon instanceof String) {
				if (((String) icon).startsWith("#")) {
					img.removeAttribute("src");
					img.getStyle().setProperty("background-color", (String) icon);				
				} else {
					img.setAttribute("src", (String) icon);
				}
			}
			
			if ("@null".equals(icon)) {
				img.removeAttribute("src");
			}
			marker = createMarkerCustom(img);
		} else {
			 marker = createMarker();
		}
		marker.setLngLat(new double[] {(float)longitude, (float)latitude})
	            .addTo(map);
		
		if (title != null) {
			updateTitleAndSnippet(title, snippet, marker);
		}
		
		markerMap.put((String) id, marker);
	}

	public void updateTitleAndSnippet(Object title, Object snippet, Marker marker) {
		String desc = title != null ? (String) "<b>" + title + "</b>" : "";
		
		if (snippet != null) {
			desc += ((String) title != null ? "<br>" : "") + (String) snippet;
		}
		Popup popup = createPopup(25).setHTML(desc);
		marker.setPopup(popup);
	}
	
    public interface JSMapLibreMap extends org.teavm.jso.JSObject {
    	@org.teavm.jso.JSMethod
        void setZoom(double zoom);
    	
    	@org.teavm.jso.JSMethod
    	void setCenter(double[] lngLat);

    	@org.teavm.jso.JSMethod
		void resize();
    	
    	@org.teavm.jso.JSMethod
        void addControl(org.teavm.jso.JSObject control, String position);

    	@org.teavm.jso.JSMethod
		void setBearing(double bearing);

    	@org.teavm.jso.JSMethod
		void removeControl(org.teavm.jso.JSObject geolocateControl);
    	
    	 @org.teavm.jso.JSMethod("on")
    	 void on(String eventType, MapLoadCallback callback);
    }
    
    @org.teavm.jso.JSFunctor
    public interface MapLoadCallback extends org.teavm.jso.JSObject {
        void onEvent(JSMapMouseEvent e);
    }
    public interface JSMapMouseEvent extends org.teavm.jso.JSObject {
        @org.teavm.jso.JSProperty
        JSLngLat getLngLat();
    }
    public interface JSLngLat extends org.teavm.jso.JSObject {
        @org.teavm.jso.JSProperty
        double getLng();

        @org.teavm.jso.JSProperty
        double getLat();
    }
    
	
	public interface GeolocateControl extends org.teavm.jso.JSObject {
	    // Maps the geolocate.trigger() method
	    @org.teavm.jso.JSMethod("trigger")
	    boolean trigger();
	}
    @org.teavm.jso.JSBody(params = {"container", "style"},
            script = "return new maplibregl.Map({" +
                     "container: container," +
                     "style: style" +
                     "});")
    public static native JSMapLibreMap create(
    		 HTMLElement container,
            String style
    );

    @Override
    public void initialized() {
    	super.initialized();
    	
    	map.setCenter(new double [] {lng, lat });
    	
    	new r.android.os.Handler().post(new Runnable() {
			@Override
			public void run() {
				map.resize();
			}
    	});
    	
    	showMarkerForTarget();
    }

	public void showMarkerForTarget() {
		removeMarkerById("targetLocationMarker");
		if (showMarkerForTarget != null && showMarkerForTarget) {
			addMarker("targetLocationMarker", lat, lng, targetLocationMarkerTitle, targetLocationMarkerSnippet, targetLocationMarkerIcon);
		}
	}

	public void removeMarkerById(String id) {
		if (markerMap != null && markerMap.containsKey(id)) {
			Marker marker = markerMap.get(id);
			if (marker != null) {
				marker.remove();
				markerMap.remove(id);
			}
		}
	}
	
	private void removeMarker(Object id) {
		removeMarkerById((String) id);		
	}
    
    public interface Marker extends org.teavm.jso.JSObject {
    	@org.teavm.jso.JSMethod
    	void remove();
        @org.teavm.jso.JSMethod
        Marker setLngLat(double[] lngLat);

        @org.teavm.jso.JSMethod
        Marker addTo(JSMapLibreMap map);
        
        @org.teavm.jso.JSMethod Marker setPopup(Popup popup);
    }
    
    
    public interface Popup extends org.teavm.jso.JSObject {
        @org.teavm.jso.JSMethod Popup setText(String text);
        @org.teavm.jso.JSMethod Popup setHTML(String html);
    }
    @org.teavm.jso.JSBody(script = "return new maplibregl.Marker();")
    public static native Marker createMarker();
    
    @org.teavm.jso.JSBody(params = {"el"}, script = "return new maplibregl.Marker({element: el});")
    public static native Marker createMarkerCustom(HTMLElement el);
    
    @org.teavm.jso.JSBody(params = {"offset"},
            script = "return new maplibregl.Popup({ offset: offset });")
   public static native Popup createPopup(double offset);
    @org.teavm.jso.JSBody(params = {"showCompass"}, script =
            "return new maplibregl.NavigationControl({" +
            		"showCompass: showCompass" +
            "});")
    public static native org.teavm.jso.JSObject createNavigationControlFull(boolean showCompass);
    
    
    @org.teavm.jso.JSBody(params = {"trackUserLocation", "enableHighAccuracy"}, script =
            "return new maplibregl.GeolocateControl({" +
            		"positionOptions: {"
            		+ "      enableHighAccuracy: enableHighAccuracy"
            		+ "  },"
            		+ "  trackUserLocation: trackUserLocation" +
            "});")
    public static native GeolocateControl creatGeolocateControl(boolean trackUserLocation, boolean enableHighAccuracy);
	
    private org.teavm.jso.JSObject navigationControl; 
	private void setUiCompass(Object objValue) {
		if ((boolean) objValue) {
			navigationControl = createNavigationControlFull(true);
			map.addControl(navigationControl, toMapLibrePosition(gravity));
		} else {
			if (navigationControl != null) {
				map.removeControl(navigationControl);
				navigationControl = null;
			}
		}
	}
	
	private String toMapLibrePosition(int gravity) {
	    boolean left   = (gravity & r.android.view.Gravity.LEFT) == r.android.view.Gravity.LEFT;
	    boolean right  = (gravity & r.android.view.Gravity.RIGHT) == r.android.view.Gravity.RIGHT;
	    boolean top    = (gravity & r.android.view.Gravity.TOP) == r.android.view.Gravity.TOP;
	    boolean bottom = (gravity & r.android.view.Gravity.BOTTOM) == r.android.view.Gravity.BOTTOM;

	    // defaults
	    if (!top && !bottom) top = true;
	    if (!left && !right) left = true;

	    if (top && left) return "top-left";
	    if (top && right) return "top-right";
	    if (bottom && left) return "bottom-left";
	    if (bottom && right) return "bottom-right";

	    return "top-left"; // fallback
	}
	
	private int gravity;
	private void setUiCompassGravity(Object objValue) {
		gravity = (int) objValue;
	}
	
	
	private void setCameraBearing(Object objValue) {
		map.setBearing((float) objValue);
	}

	private GeolocateControl geolocateControl; 
	private void setShowsUserLocation(Object objValue) {
		if ((boolean) objValue) {
			geolocateControl = creatGeolocateControl(true, true);
			map.addControl(geolocateControl, "top-right");			
		} else {
			if (geolocateControl != null) {
				map.removeControl(geolocateControl);
				geolocateControl = null;
			}
		}
		
		map.on("load", (e) -> {
			if (geolocateControl != null) {
				geolocateControl.trigger();
			}
        });
 	}
	
	private void nativeMakeFrameForChildWidget(int l, int t, int r, int b) {
		map.resize();
	}
	
	private void setOnMapClickListener() {
		if (!addedOnMapClickListener) {
			addedOnMapClickListener = true;
		
			map.on("click", (e) -> {
				if (onMapClickListener != null) {
					onMapClickListener.onMapClick(new LatLng(e.getLngLat().getLat(), e.getLngLat().getLng()));
				}
	        });
		}
	}
}

