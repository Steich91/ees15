package de.ees.group1.bt;

import java.io.IOException;
import java.util.ArrayList;

import javax.bluetooth.RemoteDevice;

import de.ees.group1.com.IComProvider;
import de.ees.group1.com.IControlStation;
import de.ees.group1.com.IWorkStation;
import de.ees.group1.model.Ack_Telegram;
import de.ees.group1.model.Order_Telegram;
import de.ees.group1.model.ProductionOrder;
import de.ees.group1.model.ProductionStep;
import de.ees.group1.model.Telegramm;

public class BT_manager implements IComProvider{
	
	private BT_device localDev = null;
	private RemoteDevice remoteDev = null;
	private IControlStation controlStation = null;
	private ArrayList<IWorkStation> workStation = new ArrayList<IWorkStation>();
	
	public BT_manager(){
		
		this.localDev = new BT_device();
		
	}
	
	public void connectWithDevice(String mac){
		
		try{
			
			//NXT 0016530565FD
			localDev.setSearchMAC(mac);
			
			if(localDev.searchRemoteDevice()){
				
				System.out.println("Fahrzeug gefunden");
				
				if(localDev.startService()){
				
					System.out.println(" Verbindung hergestellt");
				
				}
				
			}else{
				
				System.out.println("Fahrzeug nicht gefunden");
				
			}
		
		}catch(IOException BluetoothStateException){
			
			System.out.println(BluetoothStateException.getMessage());
			
		};
		
	}
	
	public void disconnect(){
		
		try{
			
			this.localDev.close();
			System.out.println("Verbindung beendet");
			
		}catch(IOException e){
			
			System.out.println(e.getMessage());
			
		}
		
	}
	
	public void register(IControlStation cs){
		
		this.controlStation = cs;
		
	}
	
	public void register(IWorkStation ws){
		
		this.workStation.add(ws);
		
	}
	
	public void getMessage(){
		
		try{
			Telegramm tele = this.localDev.receiveMessage();
			int type = tele.getType();
			if(tele.getDestination() == 0){
				switch(type){
				case 0: {
					this.controlStation.giveAcknowledgement(tele.getDataBool());
					break;
				}
				case 3: {
					this.controlStation.transmitActualState(tele.getDataInt());
					break;
				}
				case 4: {
					this.controlStation.reachedParkingPositionInd(tele.getDataInt());
					break;
				}
				default: {
					System.out.println("Falsch addressiertes Telegramm: ");
					System.out.println(tele.transform());
				}
				}
			}else{
				switch(type){
				case 0: {
					this.workStation.get(tele.getDestination()-1).giveAcknowledgement(tele.getDataBool());
					break;
				}
				case 2: {
					ProductionStep currentStep = tele.getDataStep();
					System.out.println("	"+currentStep.getType());
					System.out.println("	"+currentStep.getMinQualityLevel());
					System.out.println("	"+currentStep.getWorkTimeSeconds());
					this.workStation.get(tele.getDestination()-1).giveCurrentStep(tele.getDataStep());
					break;
				}
				default: {
					System.out.println("Falsch addressiertes Telegramm: ");
					System.out.println(tele.transform());
					}
				}
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
	}
	
	public void transmitProductionOrder(ProductionOrder order){

		Telegramm tele = new Order_Telegram(16,0,order);
		
		if(this.localDev.sendMessage(tele)){
			
			System.out.println("Datenübertragung erfolgreich");
			
		} else{
			
			System.out.println("Datenübertragung fehlgeschlagen");
						
		}
		
	}
	
	public void transmitYes(){
		
		Telegramm tele = new Ack_Telegram(16,0,true);
		
		if(this.localDev.sendMessage(tele)){
			
			System.out.println("Datenübertragung erfolgreich");
			
		} else{
			
			System.out.println("Datenübertragung fehlgeschlagen");
						
		}
		
	}
	
	public void transmitNo(){
		
		Telegramm tele = new Ack_Telegram(16,0,false);
		
		if(this.localDev.sendMessage(tele)){
			
			System.out.println("Datenübertragung erfolgreich");
			
		} else{
			
			System.out.println("Datenübertragung fehlgeschlagen");
						
		}
		
	}
	
	public void transmitFinishedStep(boolean done){
		
		Telegramm tele = new Ack_Telegram(16,0,done);
		
		if(this.localDev.sendMessage(tele)){
			
			System.out.println("Datenübertragung erfolgreich");
			
		} else{
			
			System.out.println("Datenübertragung fehlgeschlagen");
						
		}
		
	}

}
