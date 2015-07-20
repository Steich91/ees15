import de.ees.group1.com.IWorkStation;
import de.ees.group1.model.ProductionStep;


public class Test_WorkStation implements IWorkStation{

	@Override
	public void giveCurrentStep(ProductionStep step) {
		
		System.out.println("Aktueller Arbeitsschritt: ");
		System.out.println("	Typ: " + step.getType());
		System.out.println("	Qualität: " + step.getMinQualityLevel());
		System.out.println("	Zeit: " + step.getWorkTimeSeconds());
		
	}

	@Override
	public void giveAcknowledgement(boolean answer) {
		
		if(answer){
			System.out.println("Antwort ist Ja");
		}else{
			System.out.println("Antwort ist Nein");
		}
		
	}

}
