package msi.gaml.architecture.simplebdi;

import msi.gama.common.interfaces.IValue;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.getter;
import msi.gama.precompiler.GamlAnnotations.variable;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.types.IType;
import msi.gaml.types.Types;

@vars ({ @variable (
		name = "name",
		type = IType.STRING,
		doc = @doc ("The name of this BDI plan")),
//		@var (
//				name = SimpleBdiPlanStatement.INTENTION,
//				type = IType.NONE,
//				doc = @doc ("A string representing the current intention of this BDI plan")),
//		@var (
//				name = SimpleBdiArchitecture.FINISHEDWHEN,
//				type = IType.STRING),
//		@var (
//				name = SimpleBdiArchitecture.INSTANTANEAOUS,
//				type = IType.STRING)
		/*
		 * @var(name = "value", type = IType.NONE),
		 * 
		 * @var(name = "parameters", type = IType.MAP),
		 */
		// @var(name = "values", type = IType.MAP), @var(name = "priority", type
		// = IType.FLOAT),
		// @var(name = "date", type = IType.FLOAT), @var(name = "subintentions",
		// type = IType.LIST),
		// @var(name = "on_hold_until", type = IType.NONE)
})
//classe définissant le type sanction, sur le plan de la définition des types plans ou des normes
public class Sanction implements IValue{

	private SanctionStatement sanctionStatement;
	
	@getter ("name")
	public String getName() {
		return this.sanctionStatement.getName();
	}
	
	public SanctionStatement getSanctionStatement() {
		return this.sanctionStatement;
	}
	
	public Sanction(){
		super();
	}
	
	public Sanction(final SanctionStatement statement) {
		super();
		this.sanctionStatement = statement;
	}
	
	public boolean equals(final Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		final Sanction other = (Sanction) obj;
		if (sanctionStatement == null) {
			if (other.sanctionStatement != null) { return false; }
		} else if (!sanctionStatement.equals(other.sanctionStatement)) { return false; }
		return true;
	}
	
	@Override
	public String serialize(boolean includingBuiltIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IType<?> getType() {
		return Types.get(SanctionType.id);
//		return null;
	}

	@Override
	public String stringValue(IScope scope) throws GamaRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue copy(IScope scope) throws GamaRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

}
