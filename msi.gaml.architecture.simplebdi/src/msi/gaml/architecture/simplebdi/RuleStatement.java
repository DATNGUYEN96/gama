/*********************************************************************************************
 * 
 *
 * 'RuleStatement.java', in plugin 'msi.gaml.architecture.simplebdi', is part of the source code of the GAMA modeling
 * and simulation platform. (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/

package msi.gaml.architecture.simplebdi;

import java.util.List;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.precompiler.GamlAnnotations.doc;
import msi.gama.precompiler.GamlAnnotations.example;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.IConcept;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;
import msi.gaml.statements.AbstractStatement;
import msi.gaml.types.IType;

@symbol (
		name = RuleStatement.RULE,
		kind = ISymbolKind.SINGLE_STATEMENT,
		with_sequence = false,
		concept = { IConcept.BDI })
@inside (
		kinds = { ISymbolKind.SPECIES, ISymbolKind.MODEL })
@facets (
		value = { @facet (
				name = RuleStatement.BELIEF,
				type = PredicateType.id,
				optional = true,
				doc = @doc ("The mandatory belief")),
				@facet (
						name = RuleStatement.DESIRE,
						type = PredicateType.id,
						optional = true,
						doc = @doc ("The mandatory desire")),
				@facet (
						name = RuleStatement.EMOTION,
						type = EmotionType.id,
						optional = true,
						doc = @doc ("The mandatory emotion")),
				@facet (
						name = RuleStatement.UNCERTAINTY,
						type = PredicateType.id,
						optional = true,
						doc = @doc ("The mandatory uncertainty")),
				@facet (
						name = RuleStatement.DESIRES,
						type = IType.LIST,
						of = PredicateType.id,
						optional = true,
						doc = @doc ("The mandatory desires")),
				@facet (
						name = RuleStatement.BELIEFS,
						type = IType.LIST,
						of = PredicateType.id,
						optional = true,
						doc = @doc ("The mandatory beliefs")),
				@facet (
						name = RuleStatement.EMOTIONS,
						type = IType.LIST,
						of = EmotionType.id,
						optional = true,
						doc = @doc ("The mandatory emotions")),
				@facet (
						name = RuleStatement.UNCERTAINTIES,
						type = IType.LIST,
						of = PredicateType.id,
						optional = true,
						doc = @doc ("The mandatory uncertainties")),
				@facet (
						name = RuleStatement.NEW_DESIRE,
						type = PredicateType.id,
						optional = true,
						doc = @doc ("The desire that will be added")),
				@facet (
						name = RuleStatement.NEW_BELIEF,
						type = PredicateType.id,
						optional = true,
						doc = @doc ("The belief that will be added")),
				@facet (
						name = RuleStatement.NEW_EMOTION,
						type = EmotionType.id,
						optional = true,
						doc = @doc ("The emotion that will be added")),
				@facet (
						name = RuleStatement.NEW_UNCERTAINTY,
						type = PredicateType.id,
						optional = true,
						doc = @doc ("The uncertainty that will be added")),
				@facet (
						name = RuleStatement.NEW_DESIRES,
						type = IType.LIST,
						of = PredicateType.id,
						optional = true,
						doc = @doc ("The desire that will be added")),
				@facet (
						name = RuleStatement.NEW_BELIEFS,
						type = IType.LIST,
						of = PredicateType.id,
						optional = true,
						doc = @doc ("The belief that will be added")),
				@facet (
						name = RuleStatement.NEW_EMOTIONS,
						type = IType.LIST,
						of = EmotionType.id,
						optional = true,
						doc = @doc ("The emotion that will be added")),
				@facet (
						name = RuleStatement.NEW_UNCERTAINTIES,
						type = IType.LIST,
						of = PredicateType.id,
						optional = true,
						doc = @doc ("The uncertainty that will be added")),
				@facet (
						name = RuleStatement.REMOVE_BELIEFS,
						type = IType.LIST,
						of = PredicateType.id,
						optional = true,
						doc = @doc ("The belief that will be removed")),
				@facet (
						name = RuleStatement.REMOVE_DESIRES,
						type = IType.LIST,
						of = PredicateType.id,
						optional = true,
						doc = @doc ("The desire that will be removed")),
				@facet (
						name = RuleStatement.REMOVE_EMOTIONS,
						type = IType.LIST,
						of = EmotionType.id,
						optional = true,
						doc = @doc ("The emotion that will be removed")),
				@facet (
						name = RuleStatement.REMOVE_UNCERTAINTIES,
						type = IType.LIST,
						of = PredicateType.id,
						optional = true,
						doc = @doc ("The uncertainty that will be removed")),
				@facet (
						name = RuleStatement.REMOVE_BELIEF,
						type = PredicateType.id,
						optional = true,
						doc = @doc ("The belief that will be removed")),
				@facet (
						name = RuleStatement.REMOVE_DESIRE,
						type = PredicateType.id,
						optional = true,
						doc = @doc ("The desire that will be removed")),
				@facet (
						name = RuleStatement.REMOVE_INTENTION,
						type = PredicateType.id,
						optional = true,
						doc = @doc ("The intention that will be removed")),
				@facet (
						name = RuleStatement.REMOVE_EMOTION,
						type = EmotionType.id,
						optional = true,
						doc = @doc ("The emotion that will be removed")),
				@facet (
						name = RuleStatement.REMOVE_UNCERTAINTY,
						type = PredicateType.id,
						optional = true,
						doc = @doc ("The uncertainty that will be removed")),
				@facet (
						name = IKeyword.WHEN,
						type = IType.BOOL,
						optional = true,
						doc = @doc (" ")),
				@facet (
						name = RuleStatement.THRESHOLD,
						type = IType.FLOAT,
						optional = true,
						doc = @doc ("Threshold linked to the emotion.")),
				@facet (
						name = IKeyword.PARALLEL,
						type = { IType.BOOL, IType.INT },
						optional = true,
						doc = @doc ("setting this facet to 'true' will allow 'perceive' to use concurrency with a parallel_bdi architecture; setting it to an integer will set the threshold under which they will be run sequentially (the default is initially 20, but can be fixed in the preferences). This facet is true by default.")),
				@facet (
						name = RuleStatement.STRENGTH,
						type = { IType.FLOAT, IType.INT },
						optional = true,
						doc = @doc ("The stregth of the mental state created")),
				@facet (
						name = "lifetime",
						type = IType.INT,
						optional = true,
						doc = @doc ("the lifetime value of the mental state created")),
				@facet (
						name = IKeyword.NAME,
						type = IType.ID,
						optional = true,
						doc = @doc ("The name of the rule")) },
		omissible = IKeyword.NAME)
@doc (
		value = "enables to add a desire or a belief or to remove a belief, a desire or an intention if the agent gets the belief or/and desire or/and condition mentioned.",
		examples = {
				@example ("rule belief: new_predicate(\"test\") when: flip(0.5) new_desire: new_predicate(\"test\")") })
public class RuleStatement extends AbstractStatement {

	public static final String RULE = "rule";
	public static final String BELIEF = "belief";
	public static final String DESIRE = "desire";
	public static final String EMOTION = "emotion";
	public static final String UNCERTAINTY = "uncertainty";
	public static final String RULES = "rules";
	public static final String BELIEFS = "beliefs";
	public static final String DESIRES = "desires";
	public static final String EMOTIONS = "emotions";
	public static final String UNCERTAINTIES = "uncertainties";
	public static final String NEW_DESIRE = "new_desire";
	public static final String NEW_BELIEF = "new_belief";
	public static final String NEW_EMOTION = "new_emotion";
	public static final String NEW_UNCERTAINTY = "new_uncertainty";
	public static final String REMOVE_BELIEF = "remove_belief";
	public static final String REMOVE_DESIRE = "remove_desire";
	public static final String REMOVE_INTENTION = "remove_intention";
	public static final String REMOVE_EMOTION = "remove_emotion";
	public static final String REMOVE_UNCERTAINTY = "remove_uncertainty";
	public static final String NEW_DESIRES = "new_desires";
	public static final String NEW_BELIEFS = "new_beliefs";
	public static final String NEW_EMOTIONS = "new_emotions";
	public static final String NEW_UNCERTAINTIES = "new_uncertainties";
	public static final String REMOVE_BELIEFS = "remove_beliefs";
	public static final String REMOVE_DESIRES = "remove_desires";
	public static final String REMOVE_EMOTIONS = "remove_emotions";
	public static final String REMOVE_UNCERTAINTIES = "remove_uncertainties";
	public static final String STRENGTH = "strength";
	public static final String THRESHOLD = "threshold";

	final IExpression when;
	final IExpression parallel;
	final IExpression belief;
	final IExpression desire;
	final IExpression emotion;
	final IExpression uncertainty;
	final IExpression beliefs;
	final IExpression desires;
	final IExpression emotions;
	final IExpression uncertainties;
	final IExpression newBelief;
	final IExpression newDesire;
	final IExpression newEmotion;
	final IExpression newUncertainty;
	final IExpression removeBelief;
	final IExpression removeDesire;
	final IExpression removeIntention;
	final IExpression removeEmotion;
	final IExpression removeUncertainty;
	final IExpression newBeliefs;
	final IExpression newDesires;
	final IExpression newEmotions;
	final IExpression newUncertainties;
	final IExpression removeBeliefs;
	final IExpression removeDesires;
	final IExpression removeEmotions;
	final IExpression removeUncertainties;
	final IExpression strength;
	final IExpression threshold;
	final IExpression lifetime;

	public RuleStatement(final IDescription desc) {
		super(desc);
		when = getFacet(IKeyword.WHEN);
		belief = getFacet(RuleStatement.BELIEF);
		desire = getFacet(RuleStatement.DESIRE);
		emotion = getFacet(RuleStatement.EMOTION);
		uncertainty = getFacet(RuleStatement.UNCERTAINTY);
		beliefs = getFacet(RuleStatement.BELIEFS);
		desires = getFacet(RuleStatement.DESIRES);
		emotions = getFacet(RuleStatement.EMOTIONS);
		uncertainties = getFacet(RuleStatement.UNCERTAINTIES);
		newBelief = getFacet(RuleStatement.NEW_BELIEF);
		newDesire = getFacet(RuleStatement.NEW_DESIRE);
		newEmotion = getFacet(RuleStatement.NEW_EMOTION);
		newUncertainty = getFacet(RuleStatement.NEW_UNCERTAINTY);
		removeBelief = getFacet(RuleStatement.REMOVE_BELIEF);
		removeDesire = getFacet(RuleStatement.REMOVE_DESIRE);
		removeIntention = getFacet(RuleStatement.REMOVE_INTENTION);
		removeEmotion = getFacet(RuleStatement.REMOVE_EMOTION);
		removeUncertainty = getFacet(RuleStatement.REMOVE_UNCERTAINTY);
		newBeliefs = getFacet(RuleStatement.NEW_BELIEFS);
		newDesires = getFacet(RuleStatement.NEW_DESIRES);
		newEmotions = getFacet(RuleStatement.NEW_EMOTIONS);
		newUncertainties = getFacet(RuleStatement.NEW_UNCERTAINTIES);
		removeBeliefs = getFacet(RuleStatement.REMOVE_BELIEFS);
		removeDesires = getFacet(RuleStatement.REMOVE_DESIRES);
		removeEmotions = getFacet(RuleStatement.REMOVE_EMOTIONS);
		removeUncertainties = getFacet(RuleStatement.REMOVE_UNCERTAINTIES);
		strength = getFacet(RuleStatement.STRENGTH);
		threshold = getFacet(RuleStatement.THRESHOLD);
		lifetime = getFacet("lifetime");
		parallel = getFacet(IKeyword.PARALLEL);
	}

	@SuppressWarnings ("unchecked")
	@Override
	protected Object privateExecuteIn(final IScope scope) throws GamaRuntimeException {
		if (newBelief == null && newDesire == null && newEmotion == null && newUncertainty == null
				&& removeBelief == null && removeDesire == null && removeIntention == null && removeEmotion == null
				&& removeUncertainty == null && newBeliefs == null && newDesires == null && newEmotions == null
				&& newUncertainties == null && removeBeliefs == null && removeDesires == null && removeEmotions == null
				&& removeUncertainties == null)
			return null;
		if (when == null || Cast.asBool(scope, when.value(scope))) {
			final MentalState tempBelief = new MentalState("Belief");
			if (belief != null) {
				tempBelief.setPredicate((Predicate) belief.value(scope));
			}
			if (belief == null || SimpleBdiArchitecture.hasBelief(scope, tempBelief)) {
				final MentalState tempDesire = new MentalState("Desire");
				if (desire != null) {
					tempDesire.setPredicate((Predicate) desire.value(scope));
				}
				if (desire == null || SimpleBdiArchitecture.hasDesire(scope, tempDesire)) {
					final MentalState tempUncertainty = new MentalState("Uncertainty");
					if (uncertainty != null) {
						tempUncertainty.setPredicate((Predicate) uncertainty.value(scope));
					}
					if (uncertainty == null || SimpleBdiArchitecture.hasUncertainty(scope, tempUncertainty)) {
						if (emotion == null
								|| SimpleBdiArchitecture.hasEmotion(scope, (Emotion) emotion.value(scope))) {
							if (beliefs == null || hasBeliefs(scope, (List<Predicate>) beliefs.value(scope))) {
								if (desires == null || hasDesires(scope, (List<Predicate>) desires.value(scope))) {
									if (uncertainties == null
											|| hasUncertainties(scope, (List<Predicate>) uncertainties.value(scope))) {
										if (emotions == null
												|| hasEmotions(scope, (List<Emotion>) emotions.value(scope))) {

											if (threshold == null || emotion != null && threshold != null
													&& SimpleBdiArchitecture.getEmotion(scope,
															(Emotion) emotion
																	.value(scope)).intensity >= (Double) threshold
																			.value(scope)) {
												if (newDesire != null) {
													final Predicate newDes = (Predicate) newDesire.value(scope);
													final MentalState tempNewDesire = new MentalState("Desire", newDes);
													if (strength != null) {
														tempNewDesire.setStrength(
																Cast.asFloat(scope, strength.value(scope)));
													}
													if (lifetime != null) {
														tempNewDesire
																.setLifeTime(Cast.asInt(scope, lifetime.value(scope)));
													}
													SimpleBdiArchitecture.addDesire(scope, null, tempNewDesire);
												}
												if (newBelief != null) {
													final Predicate newBel = (Predicate) newBelief.value(scope);
													final MentalState tempNewBelief = new MentalState("Belief", newBel);
													if (strength != null) {
														tempNewBelief.setStrength(
																Cast.asFloat(scope, strength.value(scope)));
													}
													if (lifetime != null) {
														tempNewBelief
																.setLifeTime(Cast.asInt(scope, lifetime.value(scope)));
													}
													SimpleBdiArchitecture.addBelief(scope, tempNewBelief);
												}
												if (newEmotion != null) {
													final Emotion newEmo = (Emotion) newEmotion.value(scope);
													SimpleBdiArchitecture.addEmotion(scope, newEmo);
												}
												if (newUncertainty != null) {
													final Predicate newUncert = (Predicate) newUncertainty.value(scope);
													final MentalState tempNewUncertainty =
															new MentalState("Uncertainty", newUncert);
													if (strength != null) {
														tempNewUncertainty.setStrength(
																Cast.asFloat(scope, strength.value(scope)));
													}
													if (lifetime != null) {
														tempNewUncertainty
																.setLifeTime(Cast.asInt(scope, lifetime.value(scope)));
													}
													SimpleBdiArchitecture.addUncertainty(scope, tempNewUncertainty);
												}
												if (removeBelief != null) {
													final Predicate removBel = (Predicate) removeBelief.value(scope);
													final MentalState tempRemoveBelief =
															new MentalState("Belief", removBel);
													SimpleBdiArchitecture.removeBelief(scope, tempRemoveBelief);
												}
												if (removeDesire != null) {
													final Predicate removeDes = (Predicate) removeDesire.value(scope);
													final MentalState tempRemoveDesire =
															new MentalState("Desire", removeDes);
													SimpleBdiArchitecture.removeDesire(scope, tempRemoveDesire);
												}
												if (removeIntention != null) {
													final Predicate removeInt =
															(Predicate) removeIntention.value(scope);
													final MentalState tempRemoveIntention =
															new MentalState("Intention", removeInt);
													SimpleBdiArchitecture.removeIntention(scope, tempRemoveIntention);
												}
												if (removeEmotion != null) {
													final Emotion removeEmo = (Emotion) removeEmotion.value(scope);
													SimpleBdiArchitecture.removeEmotion(scope, removeEmo);
												}
												if (removeUncertainty != null) {
													final Predicate removUncert =
															(Predicate) removeUncertainty.value(scope);
													final MentalState tempRemoveUncertainty =
															new MentalState("Uncertainty", removUncert);
													SimpleBdiArchitecture.removeUncertainty(scope,
															tempRemoveUncertainty);
												}

												if (newDesires != null) {
													final List<Predicate> newDess =
															(List<Predicate>) newDesires.value(scope);
													for (final Predicate newDes : newDess) {
														final MentalState tempDesires =
																new MentalState("Desire", newDes);
														if (strength != null) {
															tempDesires.setStrength(
																	Cast.asFloat(scope, strength.value(scope)));
														}
														if (lifetime != null) {
															tempDesires.setLifeTime(
																	Cast.asInt(scope, lifetime.value(scope)));
														}
														SimpleBdiArchitecture.addDesire(scope, null, tempDesires);
													}
												}
												if (newBeliefs != null) {
													final List<Predicate> newBels =
															(List<Predicate>) newBeliefs.value(scope);
													for (final Predicate newBel : newBels) {
														final MentalState tempBeliefs =
																new MentalState("Belief", newBel);
														if (strength != null) {
															tempBeliefs.setStrength(
																	Cast.asFloat(scope, strength.value(scope)));
														}
														if (lifetime != null) {
															tempBeliefs.setLifeTime(
																	Cast.asInt(scope, lifetime.value(scope)));
														}
														SimpleBdiArchitecture.addBelief(scope, tempBeliefs);
													}
												}
												if (newEmotions != null) {
													final List<Emotion> newEmos =
															(List<Emotion>) newEmotions.value(scope);
													for (final Emotion newEmo : newEmos)
														SimpleBdiArchitecture.addEmotion(scope, newEmo);
												}
												if (newUncertainties != null) {
													final List<Predicate> newUncerts =
															(List<Predicate>) newUncertainties.value(scope);
													for (final Predicate newUncert : newUncerts) {
														final MentalState tempUncertainties =
																new MentalState("Uncertainty", newUncert);
														if (strength != null) {
															tempUncertainties.setStrength(
																	Cast.asFloat(scope, strength.value(scope)));
														}
														if (lifetime != null) {
															tempUncertainties.setLifeTime(
																	Cast.asInt(scope, lifetime.value(scope)));
														}
														SimpleBdiArchitecture.addUncertainty(scope, tempUncertainties);
													}
												}
												if (removeBeliefs != null) {
													final List<Predicate> removBels =
															(List<Predicate>) removeBeliefs.value(scope);
													for (final Predicate removBel : removBels) {
														final MentalState tempRemoveBeliefs =
																new MentalState("Belief", removBel);
														SimpleBdiArchitecture.removeBelief(scope, tempRemoveBeliefs);
													}
												}
												if (removeDesires != null) {
													final List<Predicate> removeDess =
															(List<Predicate>) removeDesires.value(scope);
													for (final Predicate removeDes : removeDess) {
														final MentalState tempRemoveDesires =
																new MentalState("Desire", removeDes);
														SimpleBdiArchitecture.removeDesire(scope, tempRemoveDesires);
													}
												}
												if (removeEmotions != null) {
													final List<Emotion> removeEmos =
															(List<Emotion>) removeEmotions.value(scope);
													for (final Emotion removeEmo : removeEmos)
														SimpleBdiArchitecture.removeEmotion(scope, removeEmo);
												}
												if (removeUncertainties != null) {
													final List<Predicate> removUncerts =
															(List<Predicate>) removeUncertainties.value(scope);
													for (final Predicate removUncert : removUncerts) {
														final MentalState tempRemoveUncertainties =
																new MentalState("Uncertainty", removUncert);
														SimpleBdiArchitecture.removeUncertainty(scope,
																tempRemoveUncertainties);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	private boolean hasBeliefs(final IScope scope, final List<Predicate> predicates) {
		for (final Predicate p : predicates) {
			final MentalState temp = new MentalState("Belief", p);
			if (!SimpleBdiArchitecture.hasBelief(scope, temp))
				return false;
		}
		return true;
	}

	private boolean hasDesires(final IScope scope, final List<Predicate> predicates) {
		for (final Predicate p : predicates) {
			final MentalState temp = new MentalState("Desire", p);
			if (!SimpleBdiArchitecture.hasDesire(scope, temp))
				return false;
		}
		return true;
	}

	private boolean hasUncertainties(final IScope scope, final List<Predicate> predicates) {
		for (final Predicate p : predicates) {
			final MentalState temp = new MentalState("Uncertainty", p);
			if (!SimpleBdiArchitecture.hasUncertainty(scope, temp))
				return false;
		}
		return true;
	}

	private boolean hasEmotions(final IScope scope, final List<Emotion> emotions) {
		for (final Emotion p : emotions) {
			if (!SimpleBdiArchitecture.hasEmotion(scope, p))
				return false;
		}
		return true;
	}

	public IExpression getParallel() {
		return parallel;
	}

}
