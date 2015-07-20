import de.ees.group1.com.IControlStation;


public class Test_ControlStation implements IControlStation {

	private boolean finished;
	
	public Test_ControlStation(){
		
		finished = false;
		
	}

	@Override
	public void reachedParkingPositionInd(int nextWorkingStep) {
		
		System.out.println("Nächster Bearbeitungsschritt ist Nr. " + nextWorkingStep);
		finished = true;
		
	}

	@Override
	public void giveAcknowledgement(boolean answer) {
		
		if(answer){
			System.out.println("Antwort ist Ja");
		}else{
			System.out.println("Antwort ist Nein");
		}
		
	}

	@Override
	public void transmitActualState(int state) {
		
		System.out.println("Aktueller Zustand ist: " + state);
		
	}
	
	public boolean getFinished(){
		
		return finished;
		
	}
	
}
