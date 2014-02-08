package br.ufes.inf.nemo.ontouml2text.stringGenerator;

import br.ufes.inf.nemo.ontouml2text.stringGenerator.patterns.*;
import br.ufes.inf.nemo.ontouml2text.stringGenerator.patterns.binaryPatterns.*;
import br.ufes.inf.nemo.ontouml2text.stringGenerator.patterns.naryPatterns.*;

/*
 * SPECIFIC DESCRIPTIONS
 * The specific descriptions are related with a specific relationship,
 * which is determined by the linked pair of types, i.e, a generalization
 * relationship between a kind and a role represents a description Pattern
 * that results in a specific description 
 * */

/*
 * GENERAL DESCRIPTION
 * The general description performs the link between the specific
 * descriptions, inserting the integration patterns, once a description
 * of a category encompass many patterns, these must be linked to ensure
 * a clear description
 * */

public class PortugueseLanguageAdaptor extends LanguageAdaptor {
	
	public PortugueseLanguageAdaptor(PortugueseDictionary dictionary){
		super(dictionary);
	}
	
	@Override
	protected String insertMultiplicity(PatternCategory target){	
		if(target.getMinMultiplicity() == 1 && target.getMaxMultiplicity() == 1){ // (1,1)
			return insertIndefiniteArticle(target.getLabel());
		}else if(target.getMinMultiplicity() == 1 && target.getMaxMultiplicity() == -1){ // (1,*)
			return insertIndefiniteArticle(target.getLabel()) + "ou mais ";	
		}else if(target.getMinMultiplicity() == 0 && target.getMaxMultiplicity() == 1){ // (0,1)
			return insertIndefiniteArticle(target.getLabel());	
		}else if(target.getMinMultiplicity() == 0 && target.getMaxMultiplicity() == -1){ // (0,*)
			if(dictionary.isMale(target.getLabel()))
				return "v�rios ";
			else
				return "v�rias ";
		}else if(target.getMinMultiplicity() == 2 && target.getMaxMultiplicity() == -1){ // (2,*)
			if(dictionary.isMale(target.getLabel()))
				return "dois ou mais ";
			else
				return "duas ou mais ";
		}	
		
		return "$INDEF$ ";
	}
	
	@Override
	protected String insertIndefiniteArticle(String label){
		if(dictionary.isMale(label))
			return "um ";
		else
			return "uma ";
	}

	@Override
	protected String processTopPattern(DescriptionPattern pattern,
			DescriptionPattern previousPattern,
			String parcialDescription) {
		
		return parcialDescription += pattern.getDescribedCategory().getUserDescription();
	}

	@Override
	protected String processHomogeneousGeneralizationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {
		
		// Generating specific description
		parcialDescription += " � " + 
				insertListing((NaryPattern)pattern, true, "e");
		
		return parcialDescription;
	}

	@Override
	protected String processRigidHeterogeneousGeneralizationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {	
		
		// Integration
		if(previousIsGeneralization(previousPattern) || 
				previousPattern instanceof OrdinaryMediationRevPattern) parcialDescription += " e";
		
		// Generating specific description
		parcialDescription += " � uma categoria de " + 
				insertTarget(((BinaryPattern)pattern).getTargetCategory(), false);
		
		return parcialDescription;
	}

	@Override
	protected String processAntiRigidHeterogeneousGeneralizationIdPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {
		
		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " e";

		// Generating specific description
		parcialDescription += " � um papel que " + 
				insertTarget(((BinaryPattern)pattern).getTargetCategory(), true) +
				" pode desempenhar";
					
		return parcialDescription;
	}

	@Override
	protected String processAntiRigidHeterogeneousGeneralizationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {
		
		// Integration
		if(previousIsGeneralization(previousPattern) || 
				previousPattern instanceof OrdinaryMediationRevPattern) parcialDescription += ", como tamb�m ";

		// Generating specific description
		parcialDescription += " � " + 
				insertListing((NaryPattern)pattern, true, "e");
					
		return parcialDescription;
	}

	@Override
	protected String processPhaseDescriptionPattern(DescriptionPattern pattern,
			DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " e";
		
		// Generating specific description
		parcialDescription += " � " + 
				insertTarget(((BinaryPattern)pattern).getTargetCategory(), true);
					
		return parcialDescription;
	}

	@Override
	protected String processCharacterizationAssociationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += ", que �";
		else if(previousPattern instanceof PhasePattern) parcialDescription += ". �";
		
		// Generating specific description
		parcialDescription += " caracterizado por ter " + 
				insertTarget(((BinaryPattern)pattern).getTargetCategory(), false);
					
		return parcialDescription;
	}

	@Override
	protected String processCharacterizationAssociationRevPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " e";
		
		// Generating specific description
		parcialDescription += " � uma caracter�stica de " + 
				insertTarget(((BinaryPattern)pattern).getTargetCategory(), true);
					
		return parcialDescription;
	}

	@Override
	protected String processComponentOfPattern(DescriptionPattern pattern,
			DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " e";
		else if(previousPattern instanceof FormalPattern) parcialDescription += " e";
		
		// Generating specific description
		parcialDescription += " comp�e " + 
				insertTarget(((BinaryPattern)pattern).getTargetCategory(), false);
					
		return parcialDescription;
	}

	@Override
	protected String processMemberOfPattern(DescriptionPattern pattern,
			DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " e";
		else if(previousPattern instanceof FormalPattern) parcialDescription += " e";
					
		// Generating specific description
		parcialDescription += " � membro de " + 
				insertTarget(((BinaryPattern)pattern).getTargetCategory(), true);
					
		return parcialDescription;
	}
	
	@Override
	protected String processComponentOfRevPattern(DescriptionPattern pattern,
			DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " e";
		else if(previousPattern instanceof FormalPattern) parcialDescription += ", al�m disso, ";
		
		// Generating specific description
		parcialDescription += " � composto por: " + 
				insertListing((NaryPattern)pattern, true, "e");
					
		return parcialDescription;
	}

	@Override
	protected String processMemberOfRevPattern(DescriptionPattern pattern,
			DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " e";
		else if(previousPattern instanceof FormalPattern) parcialDescription += ", al�m disso, ";
		
		// Generating specific description
		parcialDescription += " tem como membros: " + 
				insertListing((NaryPattern)pattern, true, "e");
					
		return parcialDescription;
	}

	@Override
	protected String processSubcollectiveOfPattern(DescriptionPattern pattern,
			DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " e";
		else if(previousPattern instanceof FormalPattern) parcialDescription += " e";
					
		// Generating specific description
		parcialDescription += " � um subcoletivo de " + 
				insertTarget(((BinaryPattern)pattern).getTargetCategory(), false);
					
		return parcialDescription;
	}

	@Override
	protected String processOrdinaryMediationRevPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousPattern instanceof AntiRigidHeterogeneousGeneralizationIdPattern) parcialDescription += " quando envolvido";
		else if(previousIsGeneralization(previousPattern)) parcialDescription += " e se envolve";
		else if(previousPattern == null) parcialDescription += " se envolve";
		 
		// Generating specific description
		parcialDescription += " em " + 
				insertTarget(((BinaryPattern)pattern).getTargetCategory(), true);
					
		return parcialDescription;
	}

	@Override
	protected String processPhaseDescriptionRevPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " e";
		
		// Generating specific description
		parcialDescription += " tem como fases: " + 
				insertListing((NaryPattern)pattern, false, "e");
					
		return parcialDescription;
	}

	@Override
	protected String processFormalAssociationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousPattern instanceof CharacterizationPattern) parcialDescription += " e";
		if(previousPattern instanceof PhaseDescriptionRevPattern) parcialDescription += "; como tamb�m";
		if(previousIsGeneralization(previousPattern)) parcialDescription += ", o qual";
					
		// Generating specific description			
		parcialDescription += " se associa a " + 
				insertListing((NaryPattern)pattern, true, "e");
		
		return parcialDescription;
	}

	@Override
	protected String processFormalAssociationRevPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousPattern instanceof CharacterizationPattern) parcialDescription += " e";
		else if(previousPattern instanceof PhaseDescriptionRevPattern) parcialDescription += "; como tamb�m";
		else if(previousIsGeneralization(previousPattern)) parcialDescription += ", o qual";
					
		// Generating specific description			
		parcialDescription += " se associa a " + 
				insertListing((NaryPattern)pattern, true, "e");
					
		return parcialDescription;
	}
	
	@Override
	protected String processOptionalFormalAssociationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousPattern instanceof CharacterizationPattern) parcialDescription += " e";
		else if(previousPattern instanceof FormalAssociationPattern || previousPattern instanceof FormalAssociationRevPattern) parcialDescription += ", em adi��o, pode"; 
		else if(previousPattern == null) parcialDescription += " pode";
		
		// Generating specific description
		parcialDescription += " se associar a " + 
				insertListing((NaryPattern)pattern, true, "ou");
					
		return parcialDescription;
	}

	@Override
	protected String processOptionalFormalAssociationRevPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {
		
		// Integration
		if(previousPattern instanceof CharacterizationPattern) parcialDescription += " e";
		else if(previousPattern instanceof FormalAssociationPattern || previousPattern instanceof FormalAssociationRevPattern) parcialDescription += ", em adi��o, pode"; 
		else if(previousPattern == null) parcialDescription += " pode";
		
		// Generating specific description
		parcialDescription += " se associar a " + 
				insertListing((NaryPattern)pattern, true, "ou");
					
		return parcialDescription;
	}

	@Override
	protected String processOrdinaryMediationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " que";
		
		// Generating specific description
		parcialDescription += " envolve " + 
				insertListing((NaryPattern)pattern, true, "e");
					
		return parcialDescription;
	}

	@Override
	protected String processOrdinaryOptionalMediationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsHeterogeneousMediation(previousPattern)) parcialDescription += ", al�m de poder"; 
		else if(previousPattern instanceof ExceptionMediationPattern || previousPattern instanceof ExceptionMediationRevPattern) parcialDescription += " e poder"; 
		else if(previousPattern == null) parcialDescription += " pode";			
		
		// Generating specific description
		parcialDescription += " envolver " + 
				insertListing((NaryPattern)pattern, true, "ou");
					
		return parcialDescription;
	}

	@Override
	protected String processExceptionMediationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " que est�";
		else if(previousPattern instanceof OrdinaryMediationPattern) parcialDescription += ", al�m de estar";
		else if(previousPattern == null) parcialDescription += " est�";
		
		// Generating specific description
		parcialDescription += " relacionado com " + 
				insertListing((NaryPattern)pattern, true, "e");
					
		return parcialDescription;
	}

	@Override
	protected String processExceptionMediationRevPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern)) parcialDescription += " que est�";
		else if(previousPattern instanceof OrdinaryMediationPattern) parcialDescription += ", al�m de estar";
		else if(previousPattern == null) parcialDescription += " est�";
		
		// Generating specific description
		parcialDescription += " relacionado com " + 
				insertListing((NaryPattern)pattern, true, "e");
					
		return parcialDescription;
	}
	
	@Override
	protected String processOptionalExceptionMediationPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsHeterogeneousMediation(previousPattern) || previousPattern instanceof ExceptionMediationPattern) parcialDescription += ", al�m de poder"; 
		else if(previousPattern == null) parcialDescription += " pode";
		
		// Generating specific description
		parcialDescription += " estar relacionado com " + 
				insertListing((NaryPattern)pattern, true, "ou");
					
		return parcialDescription;
	}
	
	@Override
	protected String processOptionalExceptionMediationRevPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsHeterogeneousMediation(previousPattern) || previousPattern instanceof ExceptionMediationPattern) parcialDescription += ", al�m de poder"; 
		else if(previousPattern == null) parcialDescription += " pode";
		
		// Generating specific description
		parcialDescription += " estar relacionado com " + 
				insertListing((NaryPattern)pattern, true, "ou");
					
		return parcialDescription;
	}
	
	@Override
	protected String processAbstractMediationRevPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Generating specific description
		parcialDescription += " � um papel envolvido em " + 
				insertListing((NaryPattern)pattern, true, "e");
					
		return parcialDescription;
	}

	@Override
	protected String processGeneralizationSetRevPattern(
			DescriptionPattern pattern, DescriptionPattern previousPattern,
			String parcialDescription) {

		// Integration
		if(previousIsGeneralization(previousPattern) || 
				previousPattern instanceof OrdinaryMediationRevPattern) parcialDescription += " e";	
		else if(previousIsHeterogeneousMediation(previousPattern) || 
				previousIsOptionalMediation(previousPattern) ||
				previousPattern instanceof FormalPattern) parcialDescription += ";";
		
		// Generating specific description
		parcialDescription += " pode ser dos tipos: " + 
				insertListing((NaryPattern)pattern, false, "e");
					
		return parcialDescription;
	}

	@Override
	protected String processCustomPattern(DescriptionPattern pattern,
			DescriptionPattern previousPattern,
			String parcialDescription) {
		
		
		return parcialDescription;
	}
		
}