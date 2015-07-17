package de.ees.group1.cs.controller;

import de.ees.group1.com.IControlStation;
import de.ees.group1.cs.gui.IOrderController;
import de.ees.group1.model.ProductionOrder;

public class ControlStation implements IOrderController, IControlStation {

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
