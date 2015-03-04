package br.ufes.inf.nemo.ootos.ocl2owl_swrl.factory.ocl.uml.impl;

import org.eclipse.uml2.uml.internal.impl.NamedElementImpl;

import br.ufes.inf.nemo.ootos.util.MappingProperties;

/**
 * @author Freddy Brasileiro Silva {freddybrasileiro@gmail.com}
 */
public class LoopExpImplFactory extends CallExpImplFactory {
	public OCLExpressionImplFactory bodyFactory;
	
	public LoopExpImplFactory(MappingProperties mappingProperties, NamedElementImpl m_NamedElementImpl){
		super(mappingProperties, m_NamedElementImpl);
	}
}