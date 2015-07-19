package de.ees.group1.cs.controller;

import de.ees.group1.bt.BT_manager;
import de.ees.group1.model.ProductionStep;
import de.ees.group1.model.WorkstationType;

public class WorkingStation extends WorkingStationAll{
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
	
	public WorkingStation(BT_manager btManager, int id){
		this.btManager=btManager;
		this.btManager.register(this);
		setId(id);
		setMaxQualityLevel(1);
		setStatus(-1);
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
	 * setzt das maximal von der Arbeitsstation zu bewältigende Qualitätsniveau
	 */
	public void setMaxQualityLevel(int maxQualityLevel){
		this.maxQualityLevel=maxQualityLevel;
	}
	
	/*
	 * Gibt den aktuellen Status der Arbeitsstation zurück
	 */
	public int getStatus(){
		return status;
	}
	
	/*
	 * Gibt den Status der Arbeitsstation zurück. -1...default, 0...bereit, 1...in Betrieb, 2...defekt 
	 */
	public void setStatus(int status){
		this.status=status;
	}
	
	/*
	 * Gibt den aktuellen Arbeitsschritt zurück
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
		if ((status==1) & (maxQualityWS>=currentStep.getMinQualityLevel())){
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


	public void workstationTypeUpdatedAction(int id, WorkstationType type) {
	
	
	}


	public void workstationQualityUpdatedAction(int id, int quality) {
	
	
	}
	

	
}
