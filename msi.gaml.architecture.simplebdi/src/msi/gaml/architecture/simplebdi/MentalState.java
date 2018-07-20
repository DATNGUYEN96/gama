package msi.gaml.architecture.simplebdi;

import msi.gama.common.interfaces.IValue;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.precompiler.GamlAnnotations.getter;
import msi.gama.precompiler.GamlAnnotations.variable;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.types.IType;
import msi.gaml.types.Types;

@vars({@variable(name = "modality", type = IType.STRING),
	@variable(name = "predicate", type = PredicateType.id),
	@variable(name = "mental_state", type = MentalStateType.id),
	@variable(name = "emotion", type= EmotionType.id),
	@variable(name = "owner", type = IType.AGENT),
	@variable(name = "strength", type = IType.FLOAT),
	@variable(name = "lifetime", type = IType.INT)})
public class MentalState implements IValue {

	String modality;
	Predicate predicate;
	Double strength;
	int lifetime = -1;
	boolean isUpdated = false;
	MentalState mental;
	Emotion emo;
	IAgent owner;
	
	@getter("modality")
	public String getModality(){
		return modality;
	}
	
	@getter("predicate")
	public Predicate getPredicate(){
		return predicate;
	}
	
	@getter("mental_state")
	public MentalState getMentalState(){
		return mental;
	}
	
	@getter("emotion")
	public Emotion getEmotion(){
		return emo;
	}
	
	@getter("strength")
	public Double getStrength(){
		return strength;
	}
	
	@getter("lifetime")
	public int getLifeTime(){
		return lifetime;
	}
	
	@getter("owner")
	public IAgent getOwner(){
		return owner;
	}
	
	public void setModality(String mod){
		this.modality=mod;
	}
	
	public void setPredicate(Predicate pred){
		this.predicate=pred;
	}
	
	public void setMentalState(MentalState ment){
		this.mental=ment;
	}
	
	public void setEmotion(Emotion em){
		this.emo=em;
	}
	
	public void setStrength(Double stre){
		this.strength=stre;
	}
	
	public void setLifeTime(int life){
		this.lifetime=life;
	}
	
	public void setOwner(IAgent ag){
		this.owner=ag;
	}
	
	public void updateLifetime(){
		if (this.lifetime > 0 && !this.isUpdated) {
			this.lifetime = this.lifetime - 1;
			this.isUpdated = true;
		}
	}
	
	public MentalState(){
		super();
		this.modality="";
		this.predicate=null;
		this.mental=null;
		this.strength=1.0;
		this.owner = null;
		this.emo = null;
	}
	
	public MentalState(String mod){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=null;
		this.strength=1.0;
		this.owner = null;
		this.emo = null;
	}
	
	public MentalState(String mod, Predicate pred){
		super();
		this.modality=mod;
		this.predicate=pred;
		this.mental=null;
		this.strength=1.0;
		this.owner = null;
		this.emo = null;
	}
	
	public MentalState(String mod, MentalState ment){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=ment;
		this.strength=1.0;
		this.owner = null;
		this.emo = null;
	}
	
	public MentalState(String mod, Emotion em){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=null;
		this.strength=1.0;
		this.owner = null;
		this.emo = em;
	}
	
	public MentalState(String mod, Predicate pred, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=pred;
		this.mental=null;
		this.strength=1.0;
		this.owner = ag;
		this.emo = null;
	}
	
	public MentalState(String mod, MentalState ment, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=ment;
		this.strength=1.0;
		this.owner = ag;
		this.emo = null;
	}
	
	public MentalState(String mod, Emotion em, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=null;
		this.strength=1.0;
		this.owner = ag;
		this.emo = em;
	}
	
	public MentalState(String mod, Predicate pred, Double stre){
		super();
		this.modality=mod;
		this.predicate=pred;
		this.mental=null;
		this.strength=stre;
		this.emo = null;
	}
	
	public MentalState(String mod, MentalState ment, Double stre){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=ment;
		this.strength=stre;
		this.emo = null;
	}
	
	public MentalState(String mod, Emotion em, Double stre){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=null;
		this.strength=stre;
		this.emo = em;
	}
	
	public MentalState(String mod, Predicate pred, int life){
		super();
		this.modality=mod;
		this.predicate=pred;
		this.mental=null;
		this.lifetime=life;
		this.strength=1.0;
		this.emo = null;
	}
	
	public MentalState(String mod, MentalState ment, int life){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=ment;
		this.lifetime=life;
		this.strength=1.0;
		this.emo = null;
	}
	
	public MentalState(String mod, Emotion em, int life){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=null;
		this.lifetime=life;
		this.strength=1.0;
		this.emo = em;
	}
	
	public MentalState(String mod, Predicate pred, Double stre, int life){
		super();
		this.modality=mod;
		this.predicate=pred;
		this.mental=null;
		this.strength=stre;
		this.lifetime=life;
		this.emo = null;
	}
	
	public MentalState(String mod, MentalState ment, Double stre, int life){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=ment;
		this.strength=stre;
		this.lifetime=life;
		this.emo = null;
	}
	
	public MentalState(String mod, Emotion em, Double stre, int life){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=null;
		this.strength=stre;
		this.lifetime=life;
		this.emo = em;
	}
	
	public MentalState(String mod, Predicate pred, Double stre, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=pred;
		this.mental=null;
		this.strength=stre;
		this.owner = ag;
		this.emo = null;
	}
	
	public MentalState(String mod, MentalState ment, Double stre, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=ment;
		this.strength=stre;
		this.owner = ag;
		this.emo = null;
	}
	
	public MentalState(String mod, Emotion em, Double stre, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=null;
		this.strength=stre;
		this.owner = ag;
		this.emo = em;
	}
	
	public MentalState(String mod, Predicate pred, int life, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=pred;
		this.mental=null;
		this.strength=1.0;
		this.lifetime=life;
		this.owner = ag;
		this.emo = null;
	}
	
	public MentalState(String mod, MentalState ment, int life, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=ment;
		this.strength=1.0;
		this.lifetime=life;
		this.owner = ag;
		this.emo = null;
	}
	
	public MentalState(String mod, Emotion em, int life, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=null;
		this.strength=1.0;
		this.lifetime=life;
		this.owner = ag;
		this.emo = em;
	}
	
	public MentalState(String mod, Predicate pred, Double stre, int life, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=pred;
		this.mental=null;
		this.strength=stre;
		this.lifetime=life;
		this.owner = ag;
		this.emo = null;
	}
	
	public MentalState(String mod, MentalState ment, Double stre, int life, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=ment;
		this.strength=stre;
		this.lifetime=life;
		this.owner = ag;
		this.emo = null;
	}
	
	public MentalState(String mod, Emotion em, Double stre, int life, IAgent ag){
		super();
		this.modality=mod;
		this.predicate=null;
		this.mental=null;
		this.strength=stre;
		this.lifetime=life;
		this.owner = ag;
		this.emo = em;
	}
	
	@Override
	public String toString() {
		return serialize(true);
	}
	
	@Override
	public String serialize(boolean includingBuiltIn) {
		return modality + "(" + (predicate == null ? "" : predicate)+ (mental == null ? "" : mental) + (emo == null ? "" : emo) +","+ (owner == null ? "" : owner) +"," +strength+","+lifetime+")";
	}

	@Override
	public IType<?> getType() {
		return Types.get(MentalStateType.id);
	}

	@Override
	public String stringValue(IScope scope) throws GamaRuntimeException {
		return modality + "(" + (predicate == null ? "" : predicate)+ (mental == null ? "" : mental) + (emo == null ? "" : emo) + ","+ (owner == null ? "" : owner) +"," +strength+","+lifetime+")";
	}

	@Override
	public IValue copy(IScope scope) throws GamaRuntimeException {
		MentalState tempMental = new MentalState(modality);
		tempMental.setLifeTime(lifetime);
		tempMental.setStrength(strength);
		tempMental.setOwner(owner);
		if(predicate!=null){
			tempMental.setPredicate(predicate);
			return tempMental;
		} else if(mental!=null){
			tempMental.setMentalState(mental);
			return tempMental;
		}else if(emo!=null){
			tempMental.setEmotion(emo);
			return tempMental;
		}
		return tempMental;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return result;
	}
	
	public boolean equals(final Object obj){
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MentalState other = (MentalState)obj;
//		if(other.getModality()!=this.modality){return false;}
		if(this.predicate==null && other.getPredicate()!=null){return false;}
		if(this.predicate!=null && other.getPredicate()==null){return false;}
		if(this.predicate!=null && other.getPredicate()!=null){
			if(!other.getPredicate().equals(this.predicate)){return false;}
		}
		if(this.mental==null && other.getMentalState()!=null){return false;}
		if(this.mental!=null && other.getMentalState()==null){return false;}
		if(this.mental!=null && other.getMentalState()!=null){
			if(!other.getMentalState().equals(this.mental)){return false;}
		}
		if(this.emo==null && other.getEmotion()!=null){return false;}
		if(this.emo!=null && other.getEmotion()==null){return false;}
		if(this.emo!=null && other.getEmotion()!=null){
			if(!other.getEmotion().equals(this.emo)){return false;}
		}
		if(this.owner!=null && other.getOwner()!=null){
			if(!other.getOwner().equals(this.owner)){return false;}
		}
//		if(other.getStrength()!=this.strength){return false;}
		return true;
	}

}
