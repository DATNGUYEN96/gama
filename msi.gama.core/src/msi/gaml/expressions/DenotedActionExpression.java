/*********************************************************************************************
 * 
 * 
 * DenotedActionExpression.java', in plugin 'msi.gama.core', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package msi.gaml.expressions;

import msi.gama.runtime.IScope;
import msi.gaml.descriptions.StatementDescription;
import msi.gaml.types.Types;

public class DenotedActionExpression extends VariableExpression {

	StatementDescription description;
	boolean recursiveSerializing;

	public DenotedActionExpression(final StatementDescription action) {
		super(action.getName(), Types.NO_TYPE, true, null);
		this.description = action;
	}

	@Override
	public Object value(final IScope scope) {
		return description;
	}

	@Override
	public String getTitle() {
		return description.getTitle();
	}

	@Override
	public String serialize(final boolean includingBuiltIn) {
		if ( recursiveSerializing ) {
			recursiveSerializing = false;
			return description.getName();
		} else {
			recursiveSerializing = true;
			String result = description.serialize(includingBuiltIn);
			recursiveSerializing = false;
			return result;
		}
	}

	/**
	 * @see msi.gaml.expressions.IExpression#getDocumentation()
	 */
	@Override
	public String getDocumentation() {
		return "This expression denotes  the description of " + getTitle();
	}

	@Override
	public void setVal(final IScope scope, final Object v, final boolean create) {}

}
