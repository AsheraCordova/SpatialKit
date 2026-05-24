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
export abstract class CompassViewImpl<T> extends ViewImpl<T>{
	//start - body
	static initialize() {
    }	
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "headingId" }))
	headingId!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "heading2Id" }))
	heading2Id!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "directionId" }))
	directionId!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "direction2Id" }))
	direction2Id!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "distanceTextId" }))
	distanceTextId!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "targetLatitude" }))
	targetLatitude!:CommandAttr<number>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "targetLongitude" }))
	targetLongitude!:CommandAttr<number>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "distanceFormat" }))
	distanceFormat!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "distanceUnit" }))
	distanceUnit!:CommandAttr<string>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "onDestinationReached" }))
	onDestinationReached!:CommandAttr<number>| undefined;
	@decorate(Type(() => CommandAttr))
	@decorate(Expose({ name: "onApproachingDestination" }))
	onApproachingDestination!:CommandAttr<number>| undefined;

	@decorate(Exclude())
	protected thisPointer: T;	
	protected abstract getThisPointer(): T;
	reset() : T {	
		super.reset();
		this.headingId = undefined;
		this.heading2Id = undefined;
		this.directionId = undefined;
		this.direction2Id = undefined;
		this.distanceTextId = undefined;
		this.targetLatitude = undefined;
		this.targetLongitude = undefined;
		this.distanceFormat = undefined;
		this.distanceUnit = undefined;
		this.onDestinationReached = undefined;
		this.onApproachingDestination = undefined;
		return this.thisPointer;
	}
	constructor(id: string, path: string[], event:  string) {
		super(id, path, event);
		this.thisPointer = this.getThisPointer();
	}
	

	public setHeadingId(value : string) : T {
		this.resetIfRequired();
		if (this.headingId == null || this.headingId == undefined) {
			this.headingId = new CommandAttr<string>();
		}
		
		this.headingId.setSetter(true);
		this.headingId.setValue(value);
		this.orderSet++;
		this.headingId.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setHeading2Id(value : string) : T {
		this.resetIfRequired();
		if (this.heading2Id == null || this.heading2Id == undefined) {
			this.heading2Id = new CommandAttr<string>();
		}
		
		this.heading2Id.setSetter(true);
		this.heading2Id.setValue(value);
		this.orderSet++;
		this.heading2Id.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setDirectionId(value : string) : T {
		this.resetIfRequired();
		if (this.directionId == null || this.directionId == undefined) {
			this.directionId = new CommandAttr<string>();
		}
		
		this.directionId.setSetter(true);
		this.directionId.setValue(value);
		this.orderSet++;
		this.directionId.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setDirection2Id(value : string) : T {
		this.resetIfRequired();
		if (this.direction2Id == null || this.direction2Id == undefined) {
			this.direction2Id = new CommandAttr<string>();
		}
		
		this.direction2Id.setSetter(true);
		this.direction2Id.setValue(value);
		this.orderSet++;
		this.direction2Id.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setDistanceTextId(value : string) : T {
		this.resetIfRequired();
		if (this.distanceTextId == null || this.distanceTextId == undefined) {
			this.distanceTextId = new CommandAttr<string>();
		}
		
		this.distanceTextId.setSetter(true);
		this.distanceTextId.setValue(value);
		this.orderSet++;
		this.distanceTextId.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setTargetLatitude(value : number) : T {
		this.resetIfRequired();
		if (this.targetLatitude == null || this.targetLatitude == undefined) {
			this.targetLatitude = new CommandAttr<number>();
		}
		
		this.targetLatitude.setSetter(true);
		this.targetLatitude.setValue(value);
		this.orderSet++;
		this.targetLatitude.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setTargetLongitude(value : number) : T {
		this.resetIfRequired();
		if (this.targetLongitude == null || this.targetLongitude == undefined) {
			this.targetLongitude = new CommandAttr<number>();
		}
		
		this.targetLongitude.setSetter(true);
		this.targetLongitude.setValue(value);
		this.orderSet++;
		this.targetLongitude.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setDistanceFormat(value : string) : T {
		this.resetIfRequired();
		if (this.distanceFormat == null || this.distanceFormat == undefined) {
			this.distanceFormat = new CommandAttr<string>();
		}
		
		this.distanceFormat.setSetter(true);
		this.distanceFormat.setValue(value);
		this.orderSet++;
		this.distanceFormat.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setDistanceUnit(value : string) : T {
		this.resetIfRequired();
		if (this.distanceUnit == null || this.distanceUnit == undefined) {
			this.distanceUnit = new CommandAttr<string>();
		}
		
		this.distanceUnit.setSetter(true);
		this.distanceUnit.setValue(value);
		this.orderSet++;
		this.distanceUnit.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setOnDestinationReached(value : number) : T {
		this.resetIfRequired();
		if (this.onDestinationReached == null || this.onDestinationReached == undefined) {
			this.onDestinationReached = new CommandAttr<number>();
		}
		
		this.onDestinationReached.setSetter(true);
		this.onDestinationReached.setValue(value);
		this.orderSet++;
		this.onDestinationReached.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		

	public setOnApproachingDestination(value : number) : T {
		this.resetIfRequired();
		if (this.onApproachingDestination == null || this.onApproachingDestination == undefined) {
			this.onApproachingDestination = new CommandAttr<number>();
		}
		
		this.onApproachingDestination.setSetter(true);
		this.onApproachingDestination.setValue(value);
		this.orderSet++;
		this.onApproachingDestination.setOrderSet(this.orderSet);
		return this.thisPointer;
	}
		
	//end - body

}
	
//start - staticinit

export class CompassView extends CompassViewImpl<CompassView> implements IWidget{
    getThisPointer(): CompassView {
        return this;
    }
    
   	public getClass() {
		return CompassView;
	}
	
   	constructor(id: string, path: string[], event: string) {
		super(id, path, event);	
	}
}

CompassViewImpl.initialize();

//end - staticinit
