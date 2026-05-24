// start - imports

	
import CommandAttr from '../../widget/CommandAttr';
import IWidget from '../../widget/IWidget';
import ILayoutParam from '../../widget/ILayoutParam';
import {plainToClass, Type, Exclude, Expose, Transform} from "class-transformer";
import {Gravity} from '../../widget/TypeConstants';
import {ITranform, TransformerFactory} from '../../widget/TransformerFactory';
import {Event} from '../../app/Event';
import {MotionEvent} from '../../app/MotionEvent';
import {DragEvent} from '../../app/DragEvent';
import {KeyEvent} from '../../app/KeyEvent';
import { ScopedObject } from '../../app/ScopedObject';
import { Mixin, decorate } from 'ts-mixer';

















// end - imports
import {ViewImpl} from './ViewImpl';

export class Marker {
	latitude?: number;
	longitude?: number;
	id?: string;
	title?: string;	
	snippet?: string;	
	icon?: string;	
	constructor(id?:string, latitude?: number, longitude?: number, title?: string, snippet?: string, icon?: string);
	constructor();
	constructor(id?:string, latitude?: number, longitude?: number, title?: string, snippet?: string, icon?: string) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
		this.snippet = snippet;
		this.icon = icon;
		this.id = id;
	}

}
export abstract class MapViewImpl<T> extends ViewImpl<T>{
	//start - body
	static initialize() {
    }	
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "maplibre_uiCompassGravity" }))
	maplibre_uiCompassGravity!:CommandAttr<Gravity[]>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "maplibre_uiCompass" }))
	maplibre_uiCompass!:CommandAttr<boolean>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "maplibre_cameraBearing" }))
	maplibre_cameraBearing!:CommandAttr<number>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "maplibre_cameraTargetLat" }))
	maplibre_cameraTargetLat!:CommandAttr<number>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "maplibre_cameraTargetLng" }))
	maplibre_cameraTargetLng!:CommandAttr<number>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "maplibre_cameraZoom" }))
	maplibre_cameraZoom!:CommandAttr<number>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "showMarkerForTarget" }))
	showMarkerForTarget!:CommandAttr<boolean>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "targetLocationMarkerTitle" }))
	targetLocationMarkerTitle!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "targetLocationMarkerSnippet" }))
	targetLocationMarkerSnippet!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "targetLocationMarkerIcon" }))
	targetLocationMarkerIcon!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "showsUserLocation" }))
	showsUserLocation!:CommandAttr<boolean>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "addMarker" }))
	addMarker_!:CommandAttr<Marker|Marker[]>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "removeMarker" }))
	removeMarker_!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "onMapClick" }))
	onMapClick!:CommandAttr<string>| undefined;

	@decorate(Exclude())
	protected thisPointer: T;	
	protected abstract getThisPointer(): T;
	reset() : T {	
		super.reset();
		this.maplibre_uiCompassGravity = undefined;
		this.maplibre_uiCompass = undefined;
		this.maplibre_cameraBearing = undefined;
		this.maplibre_cameraTargetLat = undefined;
		this.maplibre_cameraTargetLng = undefined;
		this.maplibre_cameraZoom = undefined;
		this.showMarkerForTarget = undefined;
		this.targetLocationMarkerTitle = undefined;
		this.targetLocationMarkerSnippet = undefined;
		this.targetLocationMarkerIcon = undefined;
		this.showsUserLocation = undefined;
		this.addMarker_ = undefined;
		this.removeMarker_ = undefined;
		this.onMapClick = undefined;
		return this.thisPointer;
	}
	constructor(id: string, path: string[], event:  string) {
		super(id, path, event);
		this.thisPointer = this.getThisPointer();
	}
	

	public setMaplibre_uiCompassGravity(...value : Gravity[]) : T {
		this.resetIfRequired();
		if (this.maplibre_uiCompassGravity == null || this.maplibre_uiCompassGravity == undefined) {
			this.maplibre_uiCompassGravity = new CommandAttr<Gravity[]>();
		}
		
		this.maplibre_uiCompassGravity.setSetter(true);
		this.maplibre_uiCompassGravity.setValue(value);
		this.orderSet++;
		this.maplibre_uiCompassGravity.setOrderSet(this.orderSet);
this.maplibre_uiCompassGravity.setTransformer('gravity');		return this.thisPointer;
	}
		

	public setMaplibre_uiCompass(value : boolean) : T {
		this.resetIfRequired();
		if (this.maplibre_uiCompass == null || this.maplibre_uiCompass == undefined) {
			this.maplibre_uiCompass = new CommandAttr<boolean>();
		}
		
		this.maplibre_uiCompass.setSetter(true);
		this.maplibre_uiCompass.setValue(value);
		this.orderSet++;
		this.maplibre_uiCompass.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setMaplibre_cameraBearing(value : number) : T {
		this.resetIfRequired();
		if (this.maplibre_cameraBearing == null || this.maplibre_cameraBearing == undefined) {
			this.maplibre_cameraBearing = new CommandAttr<number>();
		}
		
		this.maplibre_cameraBearing.setSetter(true);
		this.maplibre_cameraBearing.setValue(value);
		this.orderSet++;
		this.maplibre_cameraBearing.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setMaplibre_cameraTargetLat(value : number) : T {
		this.resetIfRequired();
		if (this.maplibre_cameraTargetLat == null || this.maplibre_cameraTargetLat == undefined) {
			this.maplibre_cameraTargetLat = new CommandAttr<number>();
		}
		
		this.maplibre_cameraTargetLat.setSetter(true);
		this.maplibre_cameraTargetLat.setValue(value);
		this.orderSet++;
		this.maplibre_cameraTargetLat.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setMaplibre_cameraTargetLng(value : number) : T {
		this.resetIfRequired();
		if (this.maplibre_cameraTargetLng == null || this.maplibre_cameraTargetLng == undefined) {
			this.maplibre_cameraTargetLng = new CommandAttr<number>();
		}
		
		this.maplibre_cameraTargetLng.setSetter(true);
		this.maplibre_cameraTargetLng.setValue(value);
		this.orderSet++;
		this.maplibre_cameraTargetLng.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setMaplibre_cameraZoom(value : number) : T {
		this.resetIfRequired();
		if (this.maplibre_cameraZoom == null || this.maplibre_cameraZoom == undefined) {
			this.maplibre_cameraZoom = new CommandAttr<number>();
		}
		
		this.maplibre_cameraZoom.setSetter(true);
		this.maplibre_cameraZoom.setValue(value);
		this.orderSet++;
		this.maplibre_cameraZoom.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setShowMarkerForTarget(value : boolean) : T {
		this.resetIfRequired();
		if (this.showMarkerForTarget == null || this.showMarkerForTarget == undefined) {
			this.showMarkerForTarget = new CommandAttr<boolean>();
		}
		
		this.showMarkerForTarget.setSetter(true);
		this.showMarkerForTarget.setValue(value);
		this.orderSet++;
		this.showMarkerForTarget.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setTargetLocationMarkerTitle(value : string) : T {
		this.resetIfRequired();
		if (this.targetLocationMarkerTitle == null || this.targetLocationMarkerTitle == undefined) {
			this.targetLocationMarkerTitle = new CommandAttr<string>();
		}
		
		this.targetLocationMarkerTitle.setSetter(true);
		this.targetLocationMarkerTitle.setValue(value);
		this.orderSet++;
		this.targetLocationMarkerTitle.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setTargetLocationMarkerSnippet(value : string) : T {
		this.resetIfRequired();
		if (this.targetLocationMarkerSnippet == null || this.targetLocationMarkerSnippet == undefined) {
			this.targetLocationMarkerSnippet = new CommandAttr<string>();
		}
		
		this.targetLocationMarkerSnippet.setSetter(true);
		this.targetLocationMarkerSnippet.setValue(value);
		this.orderSet++;
		this.targetLocationMarkerSnippet.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setTargetLocationMarkerIcon(value : string) : T {
		this.resetIfRequired();
		if (this.targetLocationMarkerIcon == null || this.targetLocationMarkerIcon == undefined) {
			this.targetLocationMarkerIcon = new CommandAttr<string>();
		}
		
		this.targetLocationMarkerIcon.setSetter(true);
		this.targetLocationMarkerIcon.setValue(value);
		this.orderSet++;
		this.targetLocationMarkerIcon.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setShowsUserLocation(value : boolean) : T {
		this.resetIfRequired();
		if (this.showsUserLocation == null || this.showsUserLocation == undefined) {
			this.showsUserLocation = new CommandAttr<boolean>();
		}
		
		this.showsUserLocation.setSetter(true);
		this.showsUserLocation.setValue(value);
		this.orderSet++;
		this.showsUserLocation.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public addMarker(id : string,
latitude : number,
longitude : number,
title? : string,
snippet? : string,
icon? : string) : T {
		this.resetIfRequired();
		if (this.addMarker_ == null || this.addMarker_ == undefined) {
			this.addMarker_ = new CommandAttr<Marker>();
		}
		
		let wrapper:Marker = new Marker();
		wrapper.id = id;
		wrapper.latitude = latitude;
		wrapper.longitude = longitude;
		wrapper.title = title;
		wrapper.snippet = snippet;
		wrapper.icon = icon;
		this.addMarker_.setSetter(true);
		this.addMarker_.setValue(wrapper);	
		this.orderSet++;
		this.addMarker_.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		
	public addMarkerWithMarker(...arr: Marker[]) : T {
		this.resetIfRequired();
		if (this.addMarker_ == null || this.addMarker_ == undefined) {
			this.addMarker_ = new CommandAttr<Marker[]>();
		}
		
		this.addMarker_.setSetter(true);
		this.addMarker_.setValue(arr);	
		this.orderSet++;
		this.addMarker_.setOrderSet(this.orderSet);
		return this.thisPointer;
	}

	public removeMarker(value : string) : T {
		this.resetIfRequired();
		if (this.removeMarker_ == null || this.removeMarker_ == undefined) {
			this.removeMarker_ = new CommandAttr<string>();
		}
		
		this.removeMarker_.setSetter(true);
		this.removeMarker_.setValue(value);
		this.orderSet++;
		this.removeMarker_.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setOnMapClick(value : string) : T {
		this.resetIfRequired();
		if (this.onMapClick == null || this.onMapClick == undefined) {
			this.onMapClick = new CommandAttr<string>();
		}
		
		this.onMapClick.setSetter(true);
		this.onMapClick.setValue(value);
		this.orderSet++;
		this.onMapClick.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		
	//end - body

}
	
//start - staticinit

export class MapView extends MapViewImpl<MapView> implements IWidget{
    getThisPointer(): MapView {
        return this;
    }
    
   	public getClass() {
		return MapView;
	}
	
   	constructor(id: string, path: string[], event: string) {
		super(id, path, event);	
	}
}

MapViewImpl.initialize();
export interface OnMapClickEvent extends Event{
        //point:LatLng;


}

//end - staticinit
