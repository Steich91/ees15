package de.ees.group1.com;

import de.ees.group1.model.ProductionStep;

public interface IWorkStation {
	
	void giveCurrentStep(ProductionStep step);
	
	void giveAcknowledgement(boolean answer); 
	
	/*
	 * TODO: still to be specified
	 */
}
