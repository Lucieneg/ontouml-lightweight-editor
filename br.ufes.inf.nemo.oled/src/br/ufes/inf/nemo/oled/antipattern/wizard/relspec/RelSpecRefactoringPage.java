package br.ufes.inf.nemo.oled.antipattern.wizard.relspec;

import java.text.Normalizer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import br.ufes.inf.nemo.antipattern.relspec.RelSpecAntipattern;
import br.ufes.inf.nemo.antipattern.relspec.RelSpecOccurrence;

/**
 * @author Tiago Sales
 * @author John Guerson
 *
 */

public class RelSpecRefactoringPage extends RelSpecPage {
	private Button btnSubsets;
	private Button btnRedefine;
	private Button btnDisjoint;
	private Button btnNothing;
	private Button btnSpecializeRedefine;
	private Button btnDelete;
	private CCombo comboDelete;
	private CCombo comboSpecializeRedefine;
	
	/**
	 * Create the wizard.
	 */
	public RelSpecRefactoringPage(RelSpecOccurrence relSpec) 
	{
		super(relSpec);	
		
		setTitle(RelSpecAntipattern.getAntipatternInfo().acronym+" Refactoring Options");
		setDescription("The follwing options can be used to refactor the model.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		
		Label lblChooseTheAppropriate = new Label(container, SWT.NONE);
		lblChooseTheAppropriate.setText("Choose the appropriate refactoring option:");
		lblChooseTheAppropriate.setBounds(10, 10, 554, 15);
				
		btnSubsets = new Button(container, SWT.RADIO);
		btnSubsets.setText(relSpec.getParser().getStereotype(relSpec.getSpecific())+" subsets "+relSpec.getParser().getStereotype(relSpec.getGeneral()));
		btnSubsets.setBounds(10, 40, 449, 16);
		
		btnRedefine = new Button(container, SWT.RADIO);
		btnRedefine.setText(relSpec.getParser().getStereotype(relSpec.getSpecific())+" redefines "+relSpec.getParser().getStereotype(relSpec.getGeneral()));
		btnRedefine.setBounds(10, 65, 449, 16);
		
		btnDisjoint = new Button(container, SWT.RADIO);
		btnDisjoint.setText(relSpec.getParser().getStereotype(relSpec.getSpecific())+" is disjoint from "+relSpec.getParser().getStereotype(relSpec.getGeneral()));
		btnDisjoint.setBounds(10, 90, 449, 16);
		
		btnNothing = new Button(container, SWT.RADIO);
		btnNothing.setText("Do nothing");
		btnNothing.setBounds(10, 115, 169, 16);
		
		if (relSpec.isVariation4() || relSpec.isVariation5()) {
			btnSpecializeRedefine = new Button(container, SWT.RADIO);
			btnSpecializeRedefine.setText("Specialize ends and redefine");
			btnSpecializeRedefine.setBounds(10, 140, 169, 16);
			
			btnDelete = new Button(container, SWT.RADIO);
			btnDelete.setText("Delete one of the associations");
			btnDelete.setBounds(10, 165, 178, 16);
			
			comboSpecializeRedefine = new CCombo(container, SWT.BORDER);
			comboSpecializeRedefine.setItems(new String[] {	relSpec.getParser().getStringRepresentation(relSpec.getSpecificSource())+" ("+relSpec.getParser().getStringRepresentation(relSpec.getSpecific())+" source end)", 
											relSpec.getParser().getStringRepresentation(relSpec.getSpecificTarget())+" ("+relSpec.getParser().getStringRepresentation(relSpec.getSpecific())+" target end)",
											"Both ends of "+relSpec.getParser().getStringRepresentation(relSpec.getSpecific()), 
											relSpec.getParser().getStringRepresentation(relSpec.getGeneralSource())+" ("+relSpec.getParser().getStringRepresentation(relSpec.getGeneral())+" source end)", 
											relSpec.getParser().getStringRepresentation(relSpec.getGeneralTarget())+" ("+relSpec.getParser().getStringRepresentation(relSpec.getGeneral())+" target end)",
											"Both ends of "+relSpec.getParser().getStringRepresentation(relSpec.getGeneral())});
			
			comboSpecializeRedefine.setBounds(194, 135, 216, 21);
			
			comboDelete = new CCombo(container, SWT.BORDER);
			comboDelete.setItems(new String[] {	relSpec.getParser().getStringRepresentation(relSpec.getGeneral()), 
												relSpec.getParser().getStringRepresentation(relSpec.getSpecific())});
			comboDelete.setBounds(194, 160, 216, 21);
		}
		else {
			btnSpecializeRedefine = null;
			btnDelete = null;
		}
	}

	public static String getStereotype(EObject element)
	{
		String type = element.getClass().toString().replaceAll("class RefOntoUML.impl.","");
	    type = type.replaceAll("Impl","");
	    type = Normalizer.normalize(type, Normalizer.Form.NFD);
	    type = type.replace("Association","");
	    return type;
	}
	
	@Override
	public IWizardPage getNextPage() {
		
		if (btnSubsets.getSelection()){
			// Action =====================
			//TODO: <Specific> subsets <General>
		}
		if (btnRedefine.getSelection()){
			// Action =====================
			//TODO: <Specific> redefines <General>
		}
		if (btnDisjoint.getSelection()){
			// Action =====================
			//TODO: <Specific> is disjoint from <General>
		}
		
		if (btnSpecializeRedefine!=null && btnSpecializeRedefine.getSelection()){
			if (comboSpecializeRedefine.getSelectionIndex()==0 || comboSpecializeRedefine.getSelectionIndex()==2){
				// Action =====================
				//TODO: specialize <GeneralSourceEnd>
				//TODO: <General> redefines <Specific>, context newGeneralSourceSubtype
			}
			if (comboSpecializeRedefine.getSelectionIndex()==1 || comboSpecializeRedefine.getSelectionIndex()==2){
				// Action =====================
				//TODO: specialize <GeneralTargetEnd>
				if(comboSpecializeRedefine.getSelectionIndex()==1){
				//TODO: <General> redefines <Specific>, context newGeneralTargetSubtype
				}
			}
			if (comboSpecializeRedefine.getSelectionIndex()==3 || comboSpecializeRedefine.getSelectionIndex()==5){
				// Action =====================
				//TODO: specialize <SpecificSourceEnd>
				//TODO: <Specific> redefines <General>, context newSpecificSourceSubtype
			}
			if (comboSpecializeRedefine.getSelectionIndex()==4 || comboSpecializeRedefine.getSelectionIndex()==5){
				// Action =====================
				//TODO: specialize <SpecificTargetEnd>
				if(comboSpecializeRedefine.getSelectionIndex()==4){
				//TODO: <Specific> redefines <General>, context newSpecificTargetSubtype
				}
			}
		}
		if (btnDelete!=null && btnDelete.getSelection()){
			if(comboDelete.getSelectionIndex()==0){
				//ACTION
				//TODO: DELETE GENERAL
			}
			if(comboDelete.getSelectionIndex()==1){
				//ACTION
				//TODO: DELETE SPECIFIC
			}
		}
		if (btnNothing.getSelection()){
			//ACTION
		}
		return 	getRelSpecWizard().getFinishing();				
	}
}
