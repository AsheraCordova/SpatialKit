# SpatialKit

Project which adds support for compass and maplibre maps.

## Installation
To install the plugin use:

```
cordova plugin add https://github.com/AsheraCordova/SpatialKit.git
```

## Important Links
https://asheracordova.github.io/

https://asheracordova.github.io/doc/help-doc.html

https://asheracordova.github.io/doc/org/maplibre/android/maps/MapView.html

https://asheracordova.github.io/doc/com/ashera/spatialkit/Compass.html

## Widgets
* com.ashera.spatialkit.Compass
* org.maplibre.android.maps.MapView

### Compass Custom Methods/Attributes

Name                    | Description
-------------           | -------------
targetLatitude          | Latitude to which user needs to navigate using compass.
targetLongitude         | Longitude to which user needs to navigate using compass.
onApproachingDestination| Event which is trigerred when user is about to reach destimation i.e place represented by targetLatitude and targetLongitude.
onDestinationReached    | Event which is trigerred when user has reached destimation i.e place represented by targetLatitude and targetLongitude.
directionId             | The id of the view which points to the direction in which the user needs to travel to get to the place represented by targetLatitude and targetLongitude.
direction2Id            | The alternative id of the view which points to the direction in which the user needs to travel to get to the place represented by targetLatitude and targetLongitude.
headingId 	 	 	 	 	 	 	| The id of the view which shows compass heading.
heading2Id              | The alternative id of the view which shows compass heading.
distanceTextId          | The view which displays to the user the distance from the current location to destination.
distanceFormat          | Formatter which will be used to display distance
distanceUnit            | Miles or kilometers

### MapView Custom Methods/Attributes
Name                    | Description
-------------           | -------------
addMarker 	 	          | Add a marker to map. id, latitude and longitude is mandatory. 	 	 	 	 	 	 	 	 	 	 	 	 	 	 
removeMarker            | Remove marker by id.
showsUserLocation       | Show current user location.
showMarkerForTarget     | Show marker for destination location.
targetLocationMarkerIcon | Custom icon for destination location marker.
targetLocationMarkerSnippet | Description for destination location marker. 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 	 
targetLocationMarkerTitle | Title for destination location marker. 
