import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.ees.group1.bt.BT_manager;
import de.ees.group1.model.ProductionOrder;
import de.ees.group1.model.ProductionStep;
import de.ees.group1.model.WorkstationType;


public class Test_Main {

	public static void main(String[] args){
		
		boolean another_round = true;
		boolean fortfahren = true;
		String eingabe = "";
		Test_ControlStation cs = new Test_ControlStation();
		Test_WorkStation ws1 = new Test_WorkStation();
		Test_WorkStation ws2 = new Test_WorkStation();
		Test_WorkStation ws3 = new Test_WorkStation();
		Test_WorkStation ws4 = new Test_WorkStation();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		BT_manager man = new BT_manager();
		ProductionOrder order = new ProductionOrder(1);
		ProductionStep step1 = new ProductionStep();
		ProductionStep step2 = new ProductionStep();
		ProductionStep step3 = new ProductionStep();
		ProductionStep step4 = new ProductionStep();
		
		man.register(cs);
		man.register(ws1);
		man.register(ws2);
		man.register(ws3);
		man.register(ws4);
		
		step1.setType(WorkstationType.DRILL);
		step1.setWorkTimeSeconds(2);
		step1.setMinQualityLevel(1);
		order.add(step1);
		
		step2.setType(WorkstationType.LATHE);
		step2.setWorkTimeSeconds(5);
		step2.setMinQualityLevel(3);
		order.add(step2);
		
		step3.setType(WorkstationType.LATHE);
		step3.setWorkTimeSeconds(4);
		step3.setMinQualityLevel(1);
		order.add(step3);
		
		step4.setType(WorkstationType.DRILL);
		step4.setWorkTimeSeconds(10);
		step4.setMinQualityLevel(2);
		order.add(step4);
		
		while(another_round){
		
			System.out.println("Starte Testumgebung...");
			System.out.println("Beginnen?");
			try {
				eingabe = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Verbinde...");
			man.connectWithDevice("00:16:53:05:65:FD");
			System.out.println("Übertrage Auftrag...");
			man.transmitProductionOrder(order);
			System.out.println("Erwarte Acknowledgement...");
			man.getMessage();
			while(!cs.getFinished() && fortfahren){
				System.out.println("Warte auf Stationseinfahrt...");
				man.getMessage();
				System.out.println("Ist die Arbeitsstation geeignet?");
				eingabe = "";
				try {
					eingabe = reader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(eingabe.equals("j")){
					System.out.println("Übertrage Ja...");
					man.transmitYes();
					System.out.println("Erwarte Status...");
					man.getMessage();
					System.out.println("Erwarte Einfahrt...");
					man.getMessage();
					System.out.println("Bearbeite...");
					man.transmitFinishedStep(true);
				}else if(eingabe.equals("z")){
					fortfahren = false;
				}else{
					System.out.println("Übertrage Nein...");
					man.transmitNo();
				}
			}
			System.out.println("Neustart?");
			try {
				eingabe = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(!eingabe.equals("j")){
				another_round = false;
			}else{
				man.disconnect();
				fortfahren = true;
			}
			
		}
		System.out.println("Beende Testumgebung");
		man.disconnect();
		System.out.println("Auf Wiedersehen");
	
	}
	
}
