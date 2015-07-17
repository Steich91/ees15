package de.ees.group1.cs.controller;

import de.ees.group1.bt.BT_manager;
import de.ees.group1.com.IControlStation;
import de.ees.group1.cont.WorkStation;
import de.ees.group1.cs.gui.IOrderController;
import de.ees.group1.model.OrderList;
import de.ees.group1.model.ProductionOrder;
import de.ees.group1.model.ProductionStep;


public class ControlStation implements IOrderController, IControlStation {

	private ProductionOrder currentOrder;
	private ProductionStep currentStep;
	private int currentStepNumber;
	private OrderList list;
	private int statusNXT;
	private BT_manager btManager;
	private IControlStation cs;
	ß5
	public ControlStation(){
		btManager=new BT_manager();
		btManager.register(cs);
		//Erzeugt die vier Arbeitsstationen
		for (int i = 0; i < 4; i++) {
			new WorkStation(btManager, i+1);
		}
		//Erzeugt OrderList
		list=new OrderList();
	}
	
	/*
	 * übergibt den gerade an den NXT übermittleten Auftrag an die ControlStation
	 */	
	public void setCurrentOrder(){
		currentOrder=list.getFirstOrder();
		currentStepNumber=0;
	}
	/*
	 * Fügt der Liste mit den ProductionOrder einen neuen Auftrag zu.
	 */
	public void addProductionOrder(ProductionOrder order){
		list.setProductionOrder(order);
	}
	
	public int getStatusNXT(){
		return statusNXT;
	}
	
	public void evaluateStatusNXT(){
		int status=getStatusNXT();
		if ((0<status) & (status<=22)){
			switch (status){
			case 1: case 5: case 9: case 13: case 17:
				//setWorkStation(1);
				break;
			case 2: case 6: case 10: case 14: case 18:
				//setWorkStation(2);
				break;
			case 3: case 7: case 11: case 15: case 19:
				//setWorkStation(3);
				break;
			case 4: case 8: case 12: case 16: case 20:
				//setWorkStation(4);
				break;
			case 21:
				//neuen Auftrag anstoßen
				break;
			case 22:
				//Meldung alles kaputt
				break;
			}
		}
		if ((0<status) & (status<=20)){
			switch (status){
			case 1: case 2: case 3: case 4: 
				//action to "Einfahrt");
				break;
			case 5: case 6: case 7: case 8: 
				//action to "Weiterfahrt";
				break;
			case 9: case 10: case 11: case 12: 
				//arbeit beginnen;
				break;
			case 13: case 14: case 15: case 16: 
				//Arbeit beendet;
				break;
			case 17: case 18: case 19: case 20: 
				//arbeit konnte nicht durchgeführt werden;
				break;
			}
		}
	}
	
	@Override
	public void orderCreatedAction(ProductionOrder order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderRemovedAction(int orderID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reachedParkingPositionInd(int orderID, int nextWorkingStep) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNextOrderId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void moveOrderUp(int orderID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveOrderDown(int orderID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void orderUpdatedAction(ProductionOrder tmp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activeOrderCanceledAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reachedParkingPosistionInd(int nextWorkingStep) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveAcknowledgement(boolean answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void transmitActualState(int state) {
		// TODO Auto-generated method stub
		
	}

}
