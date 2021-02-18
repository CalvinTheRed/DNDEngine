package dnd.combat;

import dnd.data.DamageType;
import maths.dice.DiceGroup;
import maths.dice.Die;

public class DamageDiceGroup extends DiceGroup {
	
	public static final int NORMAL      = 0;
	public static final int RESISTED    = 1;
	public static final int ENHANCED    = 2;
	public static final int NEUTRALIZED = 3;
	public static final int NO_EFFECT   = 4;
	
	protected DamageType damageType;
	protected int damageBonus;
	protected int resistances;
	protected int immunities;
	protected int vulnerabilities;
	
	public DamageDiceGroup(int numDice, int dieSize, DamageType damageType) {
		super(numDice, dieSize);
		this.damageType = damageType;
		resistances     = 0;
		immunities      = 0;
		vulnerabilities = 0;
	}
	
	@Override
	public DamageDiceGroup clone() {
		DamageDiceGroup group = new DamageDiceGroup(0, 0, damageType);
		group.addDamageBonus(damageBonus);
		for (Die d : dice) {
			group.addDamageDie(d.clone());
		}
		return group;
	}
	
	@Override
	public int getSum() {
		return Math.max(1, super.getSum() + damageBonus);
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
	
	public void grantResistance() {
		resistances++;
	}
	
	public void grantImmunity() {
		immunities++;
	}
	
	public void grantVulnerability() {
		vulnerabilities++;
	}
	
	public int getEffectiveness() {
		if (immunities > 0) {
			return NO_EFFECT;
		}
		if (resistances > 0 && vulnerabilities > 0) {
			return NEUTRALIZED;
		}
		if (resistances > 0) {
			return RESISTED;
		}
		if (vulnerabilities > 0) {
			return ENHANCED;
		}
		return NORMAL;
	}
	
	public boolean grantMagic() {
		if (damageType == DamageType.BLUDGEONING) {
			damageType = DamageType.BLUDGEONING_MAGICAL;
		}
		else if (damageType == DamageType.BLUDGEONING_SILVERED) {
			damageType = DamageType.BLUDGEONING_MAGICAL_SILVERED;
		}
		else if (damageType == DamageType.PIERCING) {
			damageType = DamageType.PIERCING_MAGICAL;
		}
		else if (damageType == DamageType.PIERCING_SILVERED) {
			damageType = DamageType.PIERCING_MAGICAL_SILVERED;
		}
		else if (damageType == DamageType.SLASHING) {
			damageType = DamageType.SLASHING_MAGICAL;
		}
		else if (damageType == DamageType.SLASHING_SILVERED) {
			damageType = DamageType.SLASHING_MAGICAL_SILVERED;
		}
		else {
			System.out.println("ERR: damage type " + damageType + " cannot be made magical");
			return false;
		}
		return true;
	}
	
	public boolean revokeMagic() {
		if (damageType == DamageType.BLUDGEONING_MAGICAL) {
			damageType = DamageType.BLUDGEONING;
		}
		else if (damageType == DamageType.BLUDGEONING_MAGICAL_SILVERED) {
			damageType = DamageType.BLUDGEONING_SILVERED;
		}
		else if (damageType == DamageType.PIERCING_MAGICAL) {
			damageType = DamageType.PIERCING;
		}
		else if (damageType == DamageType.PIERCING_MAGICAL_SILVERED) {
			damageType = DamageType.PIERCING_SILVERED;
		}
		else if (damageType == DamageType.SLASHING_MAGICAL) {
			damageType = DamageType.SLASHING;
		}
		else if (damageType == DamageType.SLASHING_MAGICAL_SILVERED) {
			damageType = DamageType.SLASHING_SILVERED;
		}
		else {
			System.out.println("ERR: damage type " + damageType + " cannot be made non-magical");
			return false;
		}
		return true;
	}
	
	public boolean grantSilvered() {
		if (damageType == DamageType.BLUDGEONING) {
			damageType = DamageType.BLUDGEONING_SILVERED;
		}
		else if (damageType == DamageType.BLUDGEONING_MAGICAL) {
			damageType = DamageType.BLUDGEONING_MAGICAL_SILVERED;
		}
		else if (damageType == DamageType.PIERCING) {
			damageType = DamageType.PIERCING_SILVERED;
		}
		else if (damageType == DamageType.PIERCING_MAGICAL) {
			damageType = DamageType.PIERCING_MAGICAL_SILVERED;
		}
		else if (damageType == DamageType.SLASHING) {
			damageType = DamageType.SLASHING_SILVERED;
		}
		else if (damageType == DamageType.SLASHING_MAGICAL) {
			damageType = DamageType.SLASHING_MAGICAL_SILVERED;
		}
		else {
			System.out.println("ERR: damage type " + damageType + " cannot be made silvered");
			return false;
		}
		return true;
	}
	
	public boolean revokeSilvered() {
		if (damageType == DamageType.BLUDGEONING_SILVERED) {
			damageType = DamageType.BLUDGEONING;
		}
		else if (damageType == DamageType.BLUDGEONING_MAGICAL_SILVERED) {
			damageType = DamageType.BLUDGEONING_MAGICAL;
		}
		else if (damageType == DamageType.PIERCING_SILVERED) {
			damageType = DamageType.PIERCING;
		}
		else if (damageType == DamageType.PIERCING_MAGICAL_SILVERED) {
			damageType = DamageType.PIERCING_MAGICAL;
		}
		else if (damageType == DamageType.SLASHING_SILVERED) {
			damageType = DamageType.SLASHING;
		}
		else if (damageType == DamageType.SLASHING_MAGICAL_SILVERED) {
			damageType = DamageType.SLASHING_MAGICAL;
		}
		else {
			System.out.println("ERR: damage type " + damageType + " cannot be made non-silvered");
			return false;
		}
		return true;
	}
	
}
