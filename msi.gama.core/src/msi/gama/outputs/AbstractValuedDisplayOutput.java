package msi.gama.outputs;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GAML;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.IExpression;

public abstract class AbstractValuedDisplayOutput extends AbstractDisplayOutput {

	protected String expressionText = "";
	protected IExpression value;
	protected Object lastValue = "";

	public AbstractValuedDisplayOutput(final IDescription desc) {
		super(desc);
		setValue(getFacet(IKeyword.VALUE));
		expressionText = getValue() == null ? "" : getValue().serialize(false);
	}

	public Object getLastValue() {
		return lastValue;
	}

	public IExpression getValue() {
		return value;
	}

	public String getExpressionText() {
		return expressionText == null ? "" : expressionText;
	}

	public boolean setNewExpressionText(final String string) {
		expressionText = string;
		setValue(GAML.compileExpression(string, getScope().getSimulation(), true));
		return getScope().step(this).passed();
	}

	public void setNewExpression(final IExpression expr) throws GamaRuntimeException {
		expressionText = expr == null ? "" : expr.serialize(false);
		setValue(expr);
		getScope().step(this);
	}

	protected void setValue(final IExpression value) {
		this.value = value;
	}

}
