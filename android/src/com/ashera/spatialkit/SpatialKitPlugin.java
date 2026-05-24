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

import com.ashera.widget.WidgetFactory;

public class SpatialKitPlugin  {
    public static void initPlugin() {
    	//start - widgets
        com.ashera.widget.WidgetFactory.registerAttributableFor("View", new com.ashera.spatialkit.CompassViewImpl());
        WidgetFactory.register(new com.ashera.spatialkit.MapViewImpl());
        //end - widgets
    	com.ashera.core.FragmentManagerFactory.registerManager("compass", new CompassManager());
    	com.ashera.core.FragmentManagerFactory.registerManager("map", new MapManager());
    }
}
