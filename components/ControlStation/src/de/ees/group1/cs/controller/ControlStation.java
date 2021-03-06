package de.ees.group1.cs.controller;

import de.ees.group1.bt.BT_manager;
import de.ees.group1.com.IControlStation;
import de.ees.group1.cs.gui.IOrderController;
import de.ees.group1.cs.gui.MainWindow;
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
	private WorkingStationAll workingStation;
	private MainWindow mainWindow;
	
	public ControlStation(){
		btManager=new BT_manager();
		btManager.register(cs);
		//Erzeugt die vier Arbeitsstationen
		workingStation=new WorkingStationAll();
		for (int i = 0; i < 4; i++) {
			new WorkingStation(btManager, i+1, workingStation);
		}
		//Erzeugt OrderList
		list=new OrderList();
	}
	
	/*
	 * �bergibt den gerade an den NXT �bermittleten Auftrag an die ControlStation
	 */	
	public void setCurrentOrder(){
		currentOrder=list.getFirstOrder();
		currentStepNumber=0;
	}
	/*
	 * F�gt der Liste mit den ProductionOrder einen neuen Auftrag zu.
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
				//neuen Auftrag ansto�en
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
				//arbeit konnte nicht durchgef�hrt werden;
				break;
			}
		}
	}
	
	public void setCurrentStep(ProductionStep step) {
		currentStep=step;		
	}

	
	public ProductionStep getCurrentStep() {
		return currentStep;
	}


	public void orderCreatedAction(ProductionOrder order) {
		int i=order.getId();
		list.add(i, order);
		mainWindow.updateOrderList(list);
		
	}

	
	public void orderRemovedAction(int orderID) {
		list.remove(orderID);
		mainWindow.updateOrderList(list);
	}


	public int getNextOrderId() {
		return currentStepNumber++;
	}

	
	public void moveOrderUp(int orderID) {
		if (orderID>0){
			ProductionOrder temp=list.get(orderID);
			temp.setId(orderID-1);
			list.remove(orderID);
			list.setProductionOrder(temp);
			mainWindow.updateOrderList(list);
		}
		
	}

	
	public void moveOrderDown(int orderID) {
		if (orderID>0){
			ProductionOrder temp=list.get(orderID);
			temp.setId(orderID+1);
			list.remove(orderID);
			list.setProductionOrder(temp);
			mainWindow.updateOrderList(list);
		}
	}

	
	public void orderUpdatedAction(ProductionOrder tmp) {
		int i=tmp.getId();
		list.remove(i);
		list.setProductionOrder(tmp);
		mainWindow.updateOrderList(list);
	}

	//Keine M�glichkeit den aktuell laufenden Auftrag auf dem NXT zu stoppen
	public void activeOrderCanceledAction() {
		
	}
 

	//Auftrag erfolgreich �bertragen, keine Reaktion 
	public void giveAcknowledgement(boolean answer) {
	}

	public void transmitActualState(int state) {
		statusNXT=state;
		evaluateStatusNXT();
		
	}

	public void reachedParkingPositionInd(int nextWorkingStep) {
		if ((nextWorkingStep==currentStepNumber)&(currentOrder.size()>=nextWorkingStep)){
			currentOrder=list.getFirstOrder();
			btManager.transmitProductionOrder(currentOrder);
		}
	}
}
