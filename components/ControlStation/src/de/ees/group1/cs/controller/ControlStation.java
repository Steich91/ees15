package de.ees.group1.cs.controller;


import java.util.ListIterator;

import de.ees.group1.bt.BT_manager;
import de.ees.group1.com.IControlStation;
import de.ees.group1.cs.gui.IConnectionController;
import de.ees.group1.cs.gui.IOrderController;
import de.ees.group1.cs.gui.MainWindow;
import de.ees.group1.model.*;

public class ControlStation implements IOrderController, IControlStation, IConnectionController{

	private ProductionOrder currentOrder;
	private ProductionStep currentStep;
	private int currentStepNumber;
	private OrderList list;
	private int statusNXT;
	private BT_manager btManager;
	private WorkingStationAll workingStation;
	private MainWindow mainWindow;
	private boolean isInWaitingPosition;
	
	
	public ControlStation(MainWindow mainWindow){
		this.mainWindow=mainWindow;
		btManager=new BT_manager();
		btManager.register(this);
		//Erzeugt die vier Arbeitsstationen
		workingStation=new WorkingStationAll();
		for (int i = 0; i < 4; i++) {
			new WorkingStation(btManager, i+1, workingStation);
		}
		//Erzeugt OrderList
		list=new OrderList();
		/*btManager.connectWithDevice("00:16:53:05:65:FD");
		//Test
		type=WorkstationType.DRILL;
		currentStep=new ProductionStep(type, 1,5);
		currentOrder.add(currentStep);
		type=WorkstationType.LATHE;
		currentStep=new ProductionStep(type, 3,2);
		currentOrder.add(1,currentStep);
		workingStation.workstationQualityUpdatedAction(1, 1);
		workingStation.workstationQualityUpdatedAction(2, 1);
		workingStation.workstationQualityUpdatedAction(3, 3);
		workingStation.workstationQualityUpdatedAction(4, 3);
		workingStation.workstationTypeUpdatedAction(1, WorkstationType.DRILL);
		workingStation.workstationTypeUpdatedAction(2, WorkstationType.DRILL);
		workingStation.workstationTypeUpdatedAction(3, WorkstationType.LATHE);
		workingStation.workstationTypeUpdatedAction(4, WorkstationType.LATHE);
	*/
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
		list.add(order);
		mainWindow.updateOrderList(list);
		reachedParkingPositionInd(currentStepNumber);
	}

	
	public void orderRemovedAction(int orderID) {
		ListIterator<ProductionOrder> iterator=list.listIterator();
		while(iterator.hasNext()){
			ProductionOrder temp=iterator.next();
			if (temp.getId()==orderID){
				list.remove(temp);
			}
		}
		mainWindow.updateOrderList(list);
	}


	public int getNextOrderId() {
		return currentStepNumber++;
	}

	
	public void moveOrderDown(int orderID) {
		int i=0;
		ListIterator<ProductionOrder> iterator=list.listIterator();
		while(iterator.hasNext()&(i==0)){
			ProductionOrder temp=iterator.next();
			if (temp.getId()==orderID){
				list.remove(temp);
				int index =iterator.nextIndex();
				list.add(index, temp);
				i=1;
			}
		}
		mainWindow.updateOrderList(list);
	}

	
	public void moveOrderUp(int orderID) {
		int i=0;
		ListIterator<ProductionOrder> iterator=list.listIterator();
		while(iterator.hasNext()&(i==0)){
			ProductionOrder temp=iterator.next();
			if (temp.getId()==orderID){
				list.remove(temp);
				int index =iterator.nextIndex();
				index=index-2;
				list.add(index, temp);
				i=1;
			}
		}
		mainWindow.updateOrderList(list);
	}

	
	public void orderUpdatedAction(ProductionOrder tmp) {
		int i=tmp.getId();
		list.remove(i);
		list.setProductionOrder(tmp);
		mainWindow.updateOrderList(list);
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
			if(list.isEmpty()==false){
			currentOrder=list.getFirstOrder();
			btManager.transmitProductionOrder(currentOrder);
			isInWaitingPosition=false;
			}
			else{
				isInWaitingPosition=true;
				currentStepNumber=nextWorkingStep;
			}
		}
		else{
			
		}
	}

	public void connectBT(String MAC) {
		btManager.connectWithDevice(MAC);
	}
}
