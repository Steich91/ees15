package de.ees.group1.cs.controller;


import de.ees.group1.cs.gui.IWorkstationController;
import de.ees.group1.cs.gui.MainWindow;
import de.ees.group1.model.WorkstationType;


public  class WorkingStationAll  implements IWorkstationController{
	private MainWindow mainWindow;
	private WorkingStation workStation[]=new WorkingStation[4];

	
	public void setWorkingStation(WorkingStation workStation){
		int i=workStation.getId();
		this.workStation[i]=workStation;
	}
	public void workstationTypeUpdatedAction(int id, WorkstationType type) {
		workStation[id].setType(type);
		mainWindow.updateWorkstationState();
		
	}

	
	public void workstationQualityUpdatedAction(int id, int quality) {
		workStation[id].setMaxQualityLevel(quality);
		mainWindow.updateWorkstationState();
		
	}
	
}
