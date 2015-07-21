package de.ees.group1.cs.controller;

import de.ees.group1.bt.BT_manager;
import de.ees.group1.com.IWorkStation;
import de.ees.group1.model.ProductionStep;
import de.ees.group1.model.WorkStation;
import de.ees.group1.model.WorkstationType;

public class WorkingStation extends WorkingStationAll implements IWorkStation{
	public enum Type {
		DRILL,
		LATHE,
	}
	
	private int status;
	private int maxQualityLevel;
	private ProductionStep currentStep;
	private WorkstationType type;
	private BT_manager btManager;
	private int id;
	private WorkingStationAll workingStation;
	

	
	public WorkingStation(BT_manager btManager, int id, WorkingStationAll workingStation){
		this.btManager=btManager;
		this.btManager.register(this);
		setId(id);
		setMaxQualityLevel(1);
		setStatus(1);
		this.workingStation=workingStation;
		this.workingStation.setWorkingStation(this);
	}
	
	public int getMaxQualityLevel(){
		return maxQualityLevel;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	/*
	 * setzt das maximal von der Arbeitsstation zu bew�ltigende Qualit�tsniveau
	 */
	public void setMaxQualityLevel(int maxQualityLevel){
		this.maxQualityLevel=maxQualityLevel;
	}
	
	/*
	 * Gibt den aktuellen Status der Arbeitsstation zur�ck
	 */
	public int getStatus(){
		return status;
	}
	
	/*
	 * Gibt den Status der Arbeitsstation zur�ck. -1...default, 0...bereit, 1...in Betrieb, 2...defekt 
	 */
	public void setStatus(int status){
		this.status=status;
	}
	
	/*
	 * Gibt den aktuellen Arbeitsschritt zur�ck
	 */
	public ProductionStep getStep(){
		return currentStep;
	}
	
	/*
	 * Setzt den aktuellen Arbeitsschritt
	 */
	public void setStep(ProductionStep step){
		currentStep=step;
	}
	
	/*
	 * Setzt den Typ der Arbeitsstation
	 */
	public void setType(WorkstationType type){
		this.type=type;
	}
	
	public void simulateWork(){
		int time=getStep().getWorkTimeSeconds();
		time=time*1000;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (getStatus()==1){
			btManager.transmitFinishedStep(true);
		}
		else{
			btManager.transmitFinishedStep(false);
		}
	}


	public void giveCurrentStep(ProductionStep step) {
		int maxQualityWS=getMaxQualityLevel();
		currentStep=step;
		if ((status==1) && (maxQualityWS>=currentStep.getMinQualityLevel()) && (this.type.equals(step.getType()))){
			btManager.transmitYes();
		}
		else{
			btManager.transmitNo();
		}
	
}

	//NXT hat Arbeitsposition und wartet auf abarbeitung des aktuellen Auftrags
	public void giveAcknowledgement(boolean answer) {
		simulateWork();
	
	}


	
	

	
}
