package dnd.combat;

import dnd.data.DamageType;
import maths.dice.DiceGroup;
import maths.dice.Die;

public class DamageDiceGroup extends DiceGroup {
	protected int damageBonus;
	protected DamageType damageType;
	
	public DamageDiceGroup(int numDice, int dieSize, DamageType damageType) {
		super(numDice, dieSize);
		this.damageType = damageType;
	}
	
	@Override
	public int getSum() {
		return super.getSum() + damageBonus;
	}
	
	public int getDamageBonus() {
		return damageBonus;
	}
	
	public void addDamageBonus(int bonus) {
		damageBonus += bonus;
	}
	
	public DamageType getDamageType() {
		return damageType;
	}
	
	public void setDamageType(DamageType damageType) {
		this.damageType = damageType;
	}
	
	public void addDamageDie(Die d) {
		dice.add(d);
	}
	
	public boolean grantMagic() {
		switch (damageType) {
		case BLUDGEONING:
			damageType = DamageType.BLUDGEONING_MAGICAL;
			return true;
		case BLUDGEONING_SILVERED:
			damageType = DamageType.BLUDGEONING_MAGICAL_SILVERED;
			return true;
		case PIERCING:
			damageType = DamageType.PIERCING_MAGICAL;
			return true;
		case PIERCING_SILVERED:
			damageType = DamageType.PIERCING_MAGICAL_SILVERED;
			return true;
		case SLASHING:
			damageType = DamageType.SLASHING_MAGICAL;
			return true;
		case SLASHING_SILVERED:
			damageType = DamageType.SLASHING_MAGICAL_SILVERED;
			return true;
		default:
			System.out.println("ERR: damage type cannot be made magical.");
			return false;
		}
	}
	
	public boolean revokeMagic() {
		switch (damageType) {
		case BLUDGEONING_MAGICAL:
			damageType = DamageType.BLUDGEONING;
			return true;
		case BLUDGEONING_MAGICAL_SILVERED:
			damageType = DamageType.BLUDGEONING_SILVERED;
			return true;
		case PIERCING_MAGICAL:
			damageType = DamageType.PIERCING;
			return true;
		case PIERCING_MAGICAL_SILVERED:
			damageType = DamageType.PIERCING_SILVERED;
			return true;
		case SLASHING_MAGICAL:
			damageType = DamageType.SLASHING;
			return true;
		case SLASHING_MAGICAL_SILVERED:
			damageType = DamageType.SLASHING_SILVERED;
			return true;
		default:
			System.out.println("ERR: cannot revoke magic from damage type.");
			return false;
		}
	}
	
	public boolean grantSilvered() {
		switch (damageType) {
		case BLUDGEONING:
			damageType = DamageType.BLUDGEONING_SILVERED;
			return true;
		case BLUDGEONING_MAGICAL:
			damageType = DamageType.BLUDGEONING_MAGICAL_SILVERED;
			return true;
		case PIERCING:
			damageType = DamageType.PIERCING_SILVERED;
			return true;
		case PIERCING_MAGICAL:
			damageType = DamageType.PIERCING_MAGICAL_SILVERED;
			return true;
		case SLASHING:
			damageType = DamageType.SLASHING_SILVERED;
			return true;
		case SLASHING_MAGICAL:
			damageType = DamageType.SLASHING_MAGICAL_SILVERED;
			return true;
		default:
			System.out.println("ERR: damage type cannot be made silvered.");
			return false;
		}
	}
	
	public boolean revokeSilvered() {
		switch (damageType) {
		case BLUDGEONING_SILVERED:
			damageType = DamageType.BLUDGEONING;
			return true;
		case BLUDGEONING_MAGICAL_SILVERED:
			damageType = DamageType.BLUDGEONING_MAGICAL;
			return true;
		case PIERCING_SILVERED:
			damageType = DamageType.PIERCING;
			return true;
		case PIERCING_MAGICAL_SILVERED:
			damageType = DamageType.PIERCING_MAGICAL;
			return true;
		case SLASHING_SILVERED:
			damageType = DamageType.SLASHING;
			return true;
		case SLASHING_MAGICAL_SILVERED:
			damageType = DamageType.SLASHING_MAGICAL;
			return true;
		default:
			System.out.println("ERR: cannot revoke silvered from damage type.");
			return false;
		}
	}
	
}
