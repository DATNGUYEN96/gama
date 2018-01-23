/*********************************************************************************************
 *
 * 'SelfExpression.java, in plugin msi.gama.core, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package msi.gaml.expressions;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlProperties;
import msi.gama.runtime.IScope;
import msi.gama.util.ICollector;
import msi.gaml.descriptions.IDescription;
import msi.gaml.descriptions.VariableDescription;
import msi.gaml.types.IType;

public class SuperExpression extends VariableExpression {

	protected SuperExpression(final IType<?> type) {
		super(IKeyword.SUPER, type, true, null);
	}

	@Override
	public Object value(final IScope scope) {
		return scope.getAgent();
	}

	@Override
	public String getTitle() {
		return "pseudo-variable super of type " + getType().getTitle();
	}

	@Override
	public String getDocumentation() {
		return "Represents the current agent, instance of species " + type.getTitle()
				+ ", indicating a redirection to the parent species in case of calling an action";
	}

	@Override
	public void setVal(final IScope scope, final Object v, final boolean create) {}

	/**
	 * Method collectPlugins()
	 * 
	 * @see msi.gama.common.interfaces.IGamlDescription#collectPlugins(java.util.Set)
	 */
	@Override
	public void collectMetaInformation(final GamlProperties meta) {}

	@Override
	public void collectUsedVarsOf(final IDescription species, final ICollector<VariableDescription> result) {}

}
