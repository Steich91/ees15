package de.ees.group1.com;

public interface IControlStation {
	
	void reachedParkingPositionInd(int nextWorkingStep);

	void giveAcknowledgement(boolean answer);
	
	void transmitActualState(int state);
	
}
