package br.ufes.inf.nemo.antipattern.wizard.homofunc;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import br.ufes.inf.nemo.antipattern.homofunc.HomoFuncOccurrence;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

public class HomoFuncFirstPage extends HomoFuncPage {
	
	public Button btnYes;
	public Button btnNo;
	
	public HomoFuncFirstPage(HomoFuncOccurrence homoFunc) 
	{
		super(homoFunc);		
		//setDescription("Whole: <"+getStereotype(homoFunc.getWhole())+"> "+homoFunc.getWhole().getName());
	}
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label lblTheWholeType = new Label(container, SWT.WRAP);
		lblTheWholeType.setBounds(10, 10, 554, 101);
		lblTheWholeType.setText("The whole type "+homoFunc.getWhole().getName()+" represents a functional complex. The embedded semantics is that the whole has an heterogeneous internal structure, " +
			"in which its parts contribute differently for the function of the whole. \r\n\r\nThe model only captures one type of part: "+homoFunc.getPartEnd().getType().getName()+". Do you mean that all parts have " +
			"the same function (or play the same role) regarding the whole?");
		
		btnYes = new Button(container, SWT.RADIO);
		btnYes.setBounds(10, 117, 554, 16);
		btnYes.setText("Yes (Collection)");
		
		btnNo = new Button(container, SWT.RADIO);
		btnNo.setBounds(10, 139, 554, 16);
		btnNo.setText("No (Functional Complex)");		
	}
	
	@Override
	public IWizardPage getNextPage() 
	{	
		if(btnYes.getSelection()){
			
		}
		if(btnNo.getSelection()){
			return ((HomoFuncWizard)getWizard()).getSecondPage();
		}
		return super.getNextPage();
	}
	
	
}
