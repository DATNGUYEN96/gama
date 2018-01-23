/*********************************************************************************************
 * 
 *
 * 'FocusStatement.java', in plugin 'msi.gaml.architecture.simplebdi', is part of the source code of the 
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/

package msi.gaml.architecture.simplebdi;

import java.util.Map;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.GAMA;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaMap;
import msi.gama.util.GamaMapFactory;
import msi.gama.util.IContainer;
import msi.gama.util.IList;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;
import msi.gaml.statements.AbstractStatement;
import msi.gaml.types.IType;

@symbol(name = FocusStatement.FOCUS, kind = ISymbolKind.SINGLE_STATEMENT, with_sequence = false, concept = {
		IConcept.BDI })
@inside(kinds = { ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_STATEMENT })
@facets(value = {
		@facet(name = IKeyword.NAME, type = IType.ID, optional = true, doc = @doc("the identifier of the focus")),
		@facet(name = FocusStatement.VAR, type = { IType.NONE, IType.LIST,
				IType.CONTAINER }, optional = true, doc = @doc("the variable of the perceived agent you want to add to your beliefs")),
		@facet(name = FocusStatement.EXPRESSION, type = IType.NONE, optional = true, doc = @doc("an expression that will be the value kept in the belief")),
		@facet(name = IKeyword.WHEN, type = IType.BOOL, optional = true, doc = @doc("A boolean value to focus only with a certain condition")),
		@facet(name = FocusStatement.LIFETIME, type = IType.INT, optional = true, doc = @doc("the lifetime value of the created belief")),
		@facet(name = FocusStatement.TRUTH, type = IType.BOOL, optional = true, doc = @doc("the truth value of the created belief")),
		@facet(name = FocusStatement.AGENTCAUSE, type = IType.AGENT, optional = true, doc = @doc("the agentCause value of the created belief (can be nil")),
		@facet (
				name = FocusStatement.BELIEF,
				type = PredicateType.id,
				optional = true,
				doc = @doc ("The predicate to focus on the beliefs of the other agent")),
		@facet (
				name = FocusStatement.DESIRE,
				type = PredicateType.id,
				optional = true,
				doc = @doc ("The predicate to focus on the desires of the other agent")),
		@facet (
				name = FocusStatement.EMOTION,
				type = EmotionType.id,
				optional = true,
				doc = @doc ("The emotion to focus on the emotions of the other agent")),
		@facet (
				name = FocusStatement.UNCERTAINTY,
				type = PredicateType.id,
				optional = true,
				doc = @doc ("The predicate to focus on the uncertainties of the other agent")),
		@facet(
				name = FocusStatement.IDEAL,
				type = PredicateType.id,
				optional = true,
				doc = @doc ("The predicate to focus on the ideals of the other agent")),
		@facet(name = FocusStatement.STRENGTH, type = { IType.FLOAT,
				IType.INT }, optional = true, doc = @doc("The priority of the created predicate")) }, omissible = IKeyword.NAME)
@doc(value = "enables to directly add a belief from the variable of a perceived specie.", examples = {
		@example("focus var:speed /*where speed is a variable from a species that is being perceived*/") })
public class FocusStatement extends AbstractStatement {

	public static final String FOCUS = "focus";
	public static final String STRENGTH = "strength";
	public static final String EXPRESSION = "expression";
	public static final String VAR = "var";
	public static final String BELIEF = "belief";
	public static final String DESIRE = "desire";
	public static final String UNCERTAINTY = "uncertainty";
	public static final String IDEAL = "ideal";
	public static final String EMOTION = "emotion";
	public static final String LIFETIME = "lifetime";
	public static final String TRUTH = "truth";
	public static final String AGENTCAUSE = "agent_cause";

	final IExpression name;
	final IExpression variable;
	final IExpression expression;
	final IExpression belief;
	final IExpression desire;
	final IExpression uncertainty;
	final IExpression ideal;
	final IExpression emotion;
	final IExpression when;
	final IExpression strength;
	final IExpression lifetime;
	final IExpression truth;
	final IExpression agentCause;

	public FocusStatement(final IDescription desc) {
		super(desc);
		name = getFacet(IKeyword.NAME);
		variable = getFacet(FocusStatement.VAR);
		expression = getFacet(FocusStatement.EXPRESSION);
		belief = getFacet(FocusStatement.BELIEF);
		desire = getFacet(FocusStatement.DESIRE);
		uncertainty = getFacet(FocusStatement.UNCERTAINTY);
		ideal = getFacet(FocusStatement.IDEAL);
		emotion = getFacet(FocusStatement.EMOTION);
		when = getFacet(IKeyword.WHEN);
		strength = getFacet(FocusStatement.STRENGTH);
		lifetime = getFacet(FocusStatement.LIFETIME);
		truth = getFacet(FocusStatement.TRUTH);
		agentCause = getFacet(FocusStatement.AGENTCAUSE);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		if (when == null || Cast.asBool(scope, when.value(scope))) {
			final IAgent[] stack = scope.getAgentsStack();
			final IAgent mySelfAgent = stack[stack.length - 2];
			IScope scopeMySelf = null;
			if (mySelfAgent != null) {
				scopeMySelf = mySelfAgent.getScope().copy("in FocusStatement");
				scopeMySelf.push(mySelfAgent);
			}
			Predicate tempPred;
			if (variable != null) {
				// Pour la liste, faire un truc générique dans un premier temps
				// avec un nom des variables du genre test_i, sans chercher à
				// récupérer le nom précis des variables.
				if (variable.value(scope) instanceof IContainer) {
					String namePred;
					if (name != null) {
						namePred = (String) name.value(scope);
					} else {
						namePred = variable.getName() + "_" + scope.getAgent().getSpeciesName();
					}
					String nameVarTemp;
					final Map<String, Object> tempValues = new GamaMap<String, Object>(1, null, null);
					final IList<?> variablesTemp = ((IContainer<?, ?>) variable.value(scope)).listValue(scope, null,
							true);
					for (int temp = 0; temp < variablesTemp.length(scope); temp++) {
						final Object temp2 = variablesTemp.get(temp);
						nameVarTemp = "test" + temp;
						tempValues.put(nameVarTemp + "_value", Cast.asInt(scope, temp2));
					}
					tempPred = new Predicate(namePred,
							GamaMapFactory.createWithoutCasting(
									((GamaMap<String, Object>) tempValues).getType().getKeyType(),
									((GamaMap<String, Object>) tempValues).getType().getContentType(), tempValues));
					if (truth != null) {
						tempPred.setIs_True(Cast.asBool(scopeMySelf, truth.value(scopeMySelf)));
					}
					if (agentCause != null) {
						tempPred.setAgentCause((IAgent) agentCause.value(scopeMySelf));
					} else {
						tempPred.setAgentCause(scope.getAgent());
					}
					MentalState tempBelief;
					if (strength != null) {
						tempBelief = new MentalState("Belief",tempPred,(Cast.asFloat(scopeMySelf, strength.value(scopeMySelf))));
					}else{
						tempBelief = new MentalState("Belief",tempPred);
					}
					if (lifetime != null) {
						tempBelief.setLifeTime(Cast.asInt(scopeMySelf, lifetime.value(scopeMySelf)));
					}
					if (!SimpleBdiArchitecture.hasBelief(scopeMySelf, tempBelief)) {
						SimpleBdiArchitecture.addBelief(scopeMySelf, tempBelief);
					}
				} else {
					String namePred;
					if (name != null) {
						namePred = (String) name.value(scope);
					} else {
						namePred = variable.getName() + "_" + scope.getAgent().getSpeciesName();
					}
					final String nameVar = variable.getName();
					final Map<String, Object> tempValues = new GamaMap<String, Object>(1, null, null);
					if (expression != null) {
						tempValues.put(nameVar + "_value", expression.value(scope));
					} else {
						tempValues.put(nameVar + "_value", variable.value(scope));
					}
					tempPred = new Predicate(namePred, tempValues);
					if (truth != null) {
						tempPred.setIs_True(Cast.asBool(scopeMySelf, truth.value(scopeMySelf)));
					}
					if (agentCause != null) {
						tempPred.setAgentCause((IAgent) agentCause.value(scopeMySelf));
					} else {
						tempPred.setAgentCause(scope.getAgent());
					}
					MentalState tempBelief;
					if (strength != null) {
						tempBelief = new MentalState("Belief",tempPred,(Cast.asFloat(scopeMySelf, strength.value(scopeMySelf))));
					}else{
						tempBelief = new MentalState("Belief",tempPred);
					}
					if (lifetime != null) {
						tempBelief.setLifeTime(Cast.asInt(scopeMySelf, lifetime.value(scopeMySelf)));
					}
					if (!SimpleBdiArchitecture.hasBelief(scopeMySelf, tempBelief)) {
						SimpleBdiArchitecture.addBelief(scopeMySelf, tempBelief);
					}
				}
			} else {
				if(belief !=null){
					MentalState temporaryBelief = new MentalState("Belief");
					temporaryBelief.setPredicate((Predicate) belief.value(scope));
					if(SimpleBdiArchitecture.hasBelief(scope, temporaryBelief)){
						MentalState tempBelief = new MentalState("Belief");
						for(MentalState temp : SimpleBdiArchitecture.getBase(scope, "belief_base")){
							if(temp.equals(temporaryBelief)){
								temporaryBelief = temp;
							}
						}
						tempBelief.setMentalState(temporaryBelief);
						if (strength != null) {
							tempBelief.setStrength((Cast.asFloat(scopeMySelf, strength.value(scopeMySelf))));
						}
						if (lifetime != null) {
							tempBelief.setLifeTime(Cast.asInt(scopeMySelf, lifetime.value(scopeMySelf)));
						}
						if (!SimpleBdiArchitecture.hasBelief(scopeMySelf, tempBelief)) {
							SimpleBdiArchitecture.addBelief(scopeMySelf, tempBelief);
						}
					}
				}
				if(desire != null){
					MentalState temporaryBelief = new MentalState("Desire");
					temporaryBelief.setPredicate((Predicate) desire.value(scope));
					if(SimpleBdiArchitecture.hasDesire(scope, temporaryBelief)){
						MentalState tempBelief = new MentalState("Belief");
						for(MentalState temp : SimpleBdiArchitecture.getBase(scope, "desire_base")){
							if(temp.equals(temporaryBelief)){
								temporaryBelief = temp;
							}
						}
						tempBelief.setMentalState(temporaryBelief);
						if (strength != null) {
							tempBelief.setStrength((Cast.asFloat(scopeMySelf, strength.value(scopeMySelf))));
						}
						if (lifetime != null) {
							tempBelief.setLifeTime(Cast.asInt(scopeMySelf, lifetime.value(scopeMySelf)));
						}
						if (!SimpleBdiArchitecture.hasBelief(scopeMySelf, tempBelief)) {
							SimpleBdiArchitecture.addBelief(scopeMySelf, tempBelief);
						}
					}
				}
				if(uncertainty != null){
					MentalState temporaryBelief = new MentalState("Uncertainty");
					temporaryBelief.setPredicate((Predicate) uncertainty.value(scope));
					if(SimpleBdiArchitecture.hasUncertainty(scope, temporaryBelief)){
						MentalState tempBelief = new MentalState("Belief");
						for(MentalState temp : SimpleBdiArchitecture.getBase(scope, "uncertainty_base")){
							if(temp.equals(temporaryBelief)){
								temporaryBelief = temp;
							}
						}
						tempBelief.setMentalState(temporaryBelief);
						if (strength != null) {
							tempBelief.setStrength((Cast.asFloat(scopeMySelf, strength.value(scopeMySelf))));
						}
						if (lifetime != null) {
							tempBelief.setLifeTime(Cast.asInt(scopeMySelf, lifetime.value(scopeMySelf)));
						}
						if (!SimpleBdiArchitecture.hasBelief(scopeMySelf, tempBelief)) {
							SimpleBdiArchitecture.addBelief(scopeMySelf, tempBelief);
						}
					}
				}
				if(ideal != null){
					MentalState temporaryBelief = new MentalState("Ideal");
					temporaryBelief.setPredicate((Predicate) ideal.value(scope));
					if(SimpleBdiArchitecture.hasIdeal(scope, temporaryBelief)){
						MentalState tempBelief = new MentalState("Belief");
						for(MentalState temp : SimpleBdiArchitecture.getBase(scope, "ideal_base")){
							if(temp.equals(temporaryBelief)){
								temporaryBelief = temp;
							}
						}
						tempBelief.setMentalState(temporaryBelief);
						if (strength != null) {
							tempBelief.setStrength((Cast.asFloat(scopeMySelf, strength.value(scopeMySelf))));
						}
						if (lifetime != null) {
							tempBelief.setLifeTime(Cast.asInt(scopeMySelf, lifetime.value(scopeMySelf)));
						}
						if (!SimpleBdiArchitecture.hasBelief(scopeMySelf, tempBelief)) {
							SimpleBdiArchitecture.addBelief(scopeMySelf, tempBelief);
						}
					}
				}
				if(emotion != null){
					Emotion temporaryEmotion = (Emotion) emotion.value(scope);
					if(SimpleBdiArchitecture.hasEmotion(scope, temporaryEmotion)){
						MentalState tempBelief = new MentalState("Belief");
						for(Emotion temp : SimpleBdiArchitecture.getEmotionBase(scope, "emotion_base")){
							if(temp.equals(temporaryEmotion)){
								temporaryEmotion = temp;
							}
						}
						tempBelief.setEmotion(temporaryEmotion);
						if (strength != null) {
							tempBelief.setStrength((Cast.asFloat(scopeMySelf, strength.value(scopeMySelf))));
						}
						if (lifetime != null) {
							tempBelief.setLifeTime(Cast.asInt(scopeMySelf, lifetime.value(scopeMySelf)));
						}
						if (!SimpleBdiArchitecture.hasBelief(scopeMySelf, tempBelief)) {
							SimpleBdiArchitecture.addBelief(scopeMySelf, tempBelief);
						}
					}
				}
				if (expression != null) {
					String namePred;
					if (name != null) {
						namePred = (String) name.value(scope);
					} else {
						namePred = "expression" + "_" + scope.getAgent().getSpeciesName();
					}
					final String nameVar = "expression";
					final Map<String, Object> tempValues = new GamaMap<String, Object>(1, null, null);
					tempValues.put(nameVar + "_value", expression.value(scope));
					tempPred = new Predicate(namePred, tempValues);
					if (truth != null) {
						tempPred.setIs_True(Cast.asBool(scopeMySelf, truth.value(scopeMySelf)));
					}
					if (agentCause != null) {
						tempPred.setAgentCause((IAgent) agentCause.value(scopeMySelf));
					} else {
						tempPred.setAgentCause(scope.getAgent());
					}
					MentalState tempBelief;
					if (strength != null) {
						tempBelief = new MentalState("Belief",tempPred,(Cast.asFloat(scopeMySelf, strength.value(scopeMySelf))));
					}else{
						tempBelief = new MentalState("Belief",tempPred);
					}
					if (lifetime != null) {
						tempBelief.setLifeTime(Cast.asInt(scopeMySelf, lifetime.value(scopeMySelf)));
					}
					if (!SimpleBdiArchitecture.hasBelief(scopeMySelf, tempBelief)) {
						SimpleBdiArchitecture.addBelief(scopeMySelf, tempBelief);
					}
				} 
				if(variable==null && belief == null && desire == null && uncertainty == null && ideal == null && emotion == null && expression == null){
					String namePred = null;
					if (name != null) {
						namePred = (String) name.value(scope);
					}
					final Map<String, Object> tempValues = new GamaMap<String, Object>(1, null, null);
					tempPred = new Predicate(namePred, tempValues);
					if (truth != null) {
						tempPred.setIs_True(Cast.asBool(scopeMySelf, truth.value(scopeMySelf)));
					}
					if (agentCause != null) {
						tempPred.setAgentCause((IAgent) agentCause.value(scopeMySelf));
					} else {
						tempPred.setAgentCause(scope.getAgent());
					}
					MentalState tempBelief;
					if (strength != null) {
						tempBelief = new MentalState("Belief",tempPred,(Cast.asFloat(scopeMySelf, strength.value(scopeMySelf))));
					}else{
						tempBelief = new MentalState("Belief",tempPred);
					}
					if (lifetime != null) {
						tempBelief.setLifeTime(Cast.asInt(scopeMySelf, lifetime.value(scopeMySelf)));
					}
					if (!SimpleBdiArchitecture.hasBelief(scopeMySelf, tempBelief)) {
						SimpleBdiArchitecture.addBelief(scopeMySelf, tempBelief);
					}
				}
			}
			GAMA.releaseScope(scopeMySelf);
		}
		return null;
	}

}