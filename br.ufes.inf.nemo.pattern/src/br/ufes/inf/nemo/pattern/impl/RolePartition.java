package br.ufes.inf.nemo.pattern.impl;

import java.util.Arrays;

import RefOntoUML.Classifier;
import RefOntoUML.Collective;
import RefOntoUML.Kind;
import RefOntoUML.Phase;
import RefOntoUML.Quantity;
import RefOntoUML.Role;
import RefOntoUML.SubKind;
import RefOntoUML.parser.OntoUMLParser;
import br.ufes.inf.nemo.assistant.util.UtilAssistant;
import br.ufes.inf.nemo.common.ontoumlfixer.Fix;

public class RolePartition extends AbstractPattern{
	private Classifier c;
	public RolePartition(OntoUMLParser parser, Classifier c, double x, double y) {
		super(parser, x, y, "/resource/RolePartition.png", "Role Partition");
		this.c = c;
	}

	public RolePartition(OntoUMLParser parser, double x, double y) {
		super(parser, x, y, "/resource/RolePartition.png", "Role Partition");
	}
	
	@Override
	public void runPattern() {
		dym.addHashTree(fillouthashTree(Arrays.asList(new Class[]{Kind.class, Collective.class, Quantity.class, SubKind.class, Phase.class, Role.class})));

		dym.addTableLine("general", "General", new String[] {"Kind","Collective", "Quantity", "Subkind", "Phase", "Role"});

		if(c != null){
			dym.addTableRigidLine("specific", UtilAssistant.getStringRepresentationClass(c), new String[] {"Role"});
		}else{
			dym.addTableLine("specific", "Specific 1", new String[] {"Role"});
		}
		dym.addTableLine("specific", "Specific 2", new String[] {"Role"});

		dym.setInitialItemCount(3);

		dym.setAddLineButtonAction("specific", "Specific N", new String[] {"Role"});

		isPartitionPattern(Arrays.asList(new Class[]{Kind.class, Collective.class, Quantity.class, SubKind.class, Phase.class, Role.class}), Arrays.asList(new Class[]{Role.class}));

		dm.open();
	}

	@Override
	public Fix getSpecificFix(){
		getPartitionFix();
		return fix;
	}

}
