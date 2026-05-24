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

public class MapManager implements com.ashera.core.IFragmentManager {


    @Override
	public void onCreate(com.ashera.core.IFragment fragment, Object... args) {
    	fragment.getEventBus().notifyObservers("onCreateMap", args != null && args.length > 0 ? args[0] : null);
    }

	@Override
	public void onResume(com.ashera.core.IFragment fragment) {
		fragment.getEventBus().notifyObservers("onResumeMap", null);
    }

    @Override
	public void onPause(com.ashera.core.IFragment fragment) {
    	fragment.getEventBus().notifyObservers("onPauseMap", null);
	}
    @Override
	public void onRequestPermissionsResult(IFragment fragment, int requestCode, String[] permissions,
			int[] grantResults) {
    	fragment.getEventBus().notifyObservers("onRequestPermissionsResultMap", new Object [] { requestCode, permissions, grantResults });
    }

	@Override
	public void onAttach(IFragment fragment) {
	}

	@Override
	public void onDetach(IFragment fragment) {

	}

	@Override
	public void onDestroy(IFragment fragment) {
		fragment.getEventBus().notifyObservers("onDestroyMap", null);
	}

	@Override
	public IFragmentManager newInstance() {
		return new MapManager();
	}

	@Override
	public void sendEvent(String action, Map<String, String> extraData) {
		
	}

	@Override
	public void onStart(IFragment fragment) {
		fragment.getEventBus().notifyObservers("onStartMap", null);
	}

	@Override
	public void onStop(IFragment fragment) {
		fragment.getEventBus().notifyObservers("onStopMap", null);
	}

	@Override
	public void onSaveInstanceState(IFragment fragment, Object... args) {
		fragment.getEventBus().notifyObservers("onSaveInstanceStateMap", args != null && args.length > 0 ? args[0] : null);		
	}

}
