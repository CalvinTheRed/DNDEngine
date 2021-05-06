package com.dndsuite.dnd.combat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dndsuite.dnd.combat.DamageDiceGroup;
import com.dndsuite.dnd.data.DamageType;
import com.dndsuite.maths.dice.Die;

public class DamageDiceGroupTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Sanity check")
	void test1() {
		int numDice = 4;
		int dieSize = 1;
		DamageType type = DamageType.ACID;
		DamageDiceGroup damageDice = new DamageDiceGroup(numDice, dieSize, type);
		LinkedList<Die> list = damageDice.getDice();
		assertEquals(list.size(), numDice);
		for (Die d : list) {
			assertEquals(d.getSize(), dieSize);
		}
		assertEquals(damageDice.getDamageType(), type);
		assertEquals(damageDice.getEffectiveness(), DamageDiceGroup.NORMAL);
	}

	@Test
	@DisplayName("Damage type support")
	void test2() {
		DamageType type;
		DamageDiceGroup damageDice;
		
		type = DamageType.ACID;
		damageDice = new DamageDiceGroup(0, 1, type);
		assertEquals(damageDice.getDamageType(), type);
		
		type = DamageType.BLUDGEONING;
		damageDice.setDamageType(type);
		assertEquals(damageDice.getDamageType(), type);
	}

	@Test
	@DisplayName("Granting magical damage")
	void test3() {
		DamageDiceGroup damageDice;
		damageDice = new DamageDiceGroup(0, 1, DamageType.BLUDGEONING);
		damageDice.grantMagic();
		assertEquals(damageDice.getDamageType(), DamageType.BLUDGEONING_MAGICAL);
		damageDice = new DamageDiceGroup(0, 1, DamageType.BLUDGEONING_SILVERED);
		damageDice.grantMagic();
		assertEquals(damageDice.getDamageType(), DamageType.BLUDGEONING_MAGICAL_SILVERED);
		damageDice = new DamageDiceGroup(0, 1, DamageType.PIERCING);
		damageDice.grantMagic();
		assertEquals(damageDice.getDamageType(), DamageType.PIERCING_MAGICAL);
		damageDice = new DamageDiceGroup(0, 1, DamageType.PIERCING_SILVERED);
		damageDice.grantMagic();
		assertEquals(damageDice.getDamageType(), DamageType.PIERCING_MAGICAL_SILVERED);
		damageDice = new DamageDiceGroup(0, 1, DamageType.SLASHING);
		damageDice.grantMagic();
		assertEquals(damageDice.getDamageType(), DamageType.SLASHING_MAGICAL);
		damageDice = new DamageDiceGroup(0, 1, DamageType.SLASHING_SILVERED);
		damageDice.grantMagic();
		assertEquals(damageDice.getDamageType(), DamageType.SLASHING_MAGICAL_SILVERED);
	}

	@Test
	@DisplayName("Revoking magical damage")
	void test4() {
		DamageDiceGroup damageDice;
		damageDice = new DamageDiceGroup(0, 1, DamageType.BLUDGEONING_MAGICAL);
		damageDice.revokeMagic();
		assertEquals(damageDice.getDamageType(), DamageType.BLUDGEONING);
		damageDice = new DamageDiceGroup(0, 1, DamageType.BLUDGEONING_MAGICAL_SILVERED);
		damageDice.revokeMagic();
		assertEquals(damageDice.getDamageType(), DamageType.BLUDGEONING_SILVERED);
		damageDice = new DamageDiceGroup(0, 1, DamageType.PIERCING_MAGICAL);
		damageDice.revokeMagic();
		assertEquals(damageDice.getDamageType(), DamageType.PIERCING);
		damageDice = new DamageDiceGroup(0, 1, DamageType.PIERCING_MAGICAL_SILVERED);
		damageDice.revokeMagic();
		assertEquals(damageDice.getDamageType(), DamageType.PIERCING_SILVERED);
		damageDice = new DamageDiceGroup(0, 1, DamageType.SLASHING_MAGICAL);
		damageDice.revokeMagic();
		assertEquals(damageDice.getDamageType(), DamageType.SLASHING);
		damageDice = new DamageDiceGroup(0, 1, DamageType.SLASHING_MAGICAL_SILVERED);
		damageDice.revokeMagic();
		assertEquals(damageDice.getDamageType(), DamageType.SLASHING_SILVERED);
	}

	@Test
	@DisplayName("Granting silver damage")
	void test5() {
		DamageDiceGroup damageDice;
		damageDice = new DamageDiceGroup(0, 1, DamageType.BLUDGEONING);
		damageDice.grantSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.BLUDGEONING_SILVERED);
		damageDice = new DamageDiceGroup(0, 1, DamageType.BLUDGEONING_MAGICAL);
		damageDice.grantSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.BLUDGEONING_MAGICAL_SILVERED);
		damageDice = new DamageDiceGroup(0, 1, DamageType.PIERCING);
		damageDice.grantSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.PIERCING_SILVERED);
		damageDice = new DamageDiceGroup(0, 1, DamageType.PIERCING_MAGICAL);
		damageDice.grantSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.PIERCING_MAGICAL_SILVERED);
		damageDice = new DamageDiceGroup(0, 1, DamageType.SLASHING);
		damageDice.grantSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.SLASHING_SILVERED);
		damageDice = new DamageDiceGroup(0, 1, DamageType.SLASHING_MAGICAL);
		damageDice.grantSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.SLASHING_MAGICAL_SILVERED);
	}

	@Test
	@DisplayName("Revoking silver damage")
	void test6() {
		DamageDiceGroup damageDice;
		damageDice = new DamageDiceGroup(0, 1, DamageType.BLUDGEONING_SILVERED);
		damageDice.revokeSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.BLUDGEONING);
		damageDice = new DamageDiceGroup(0, 1, DamageType.BLUDGEONING_MAGICAL_SILVERED);
		damageDice.revokeSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.BLUDGEONING_MAGICAL);
		damageDice = new DamageDiceGroup(0, 1, DamageType.PIERCING_SILVERED);
		damageDice.revokeSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.PIERCING);
		damageDice = new DamageDiceGroup(0, 1, DamageType.PIERCING_MAGICAL_SILVERED);
		damageDice.revokeSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.PIERCING_MAGICAL);
		damageDice = new DamageDiceGroup(0, 1, DamageType.SLASHING_SILVERED);
		damageDice.revokeSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.SLASHING);
		damageDice = new DamageDiceGroup(0, 1, DamageType.SLASHING_MAGICAL_SILVERED);
		damageDice.revokeSilvered();
		assertEquals(damageDice.getDamageType(), DamageType.SLASHING_MAGICAL);
	}

	@Test
	@DisplayName("Damage effectiveness")
	void test7() {
		DamageDiceGroup damageDice;
		// no modifiers
		damageDice = new DamageDiceGroup(0, 1, null);
		assertEquals(damageDice.getEffectiveness(), DamageDiceGroup.NORMAL);
		// only resistance
		damageDice = new DamageDiceGroup(0, 1, null);
		damageDice.grantResistance();
		assertEquals(damageDice.getEffectiveness(), DamageDiceGroup.RESISTED);
		// only vulnerability
		damageDice = new DamageDiceGroup(0, 1, null);
		damageDice.grantVulnerability();
		assertEquals(damageDice.getEffectiveness(), DamageDiceGroup.ENHANCED);
		// only immunity
		damageDice = new DamageDiceGroup(0, 1, null);
		damageDice.grantImmunity();
		assertEquals(damageDice.getEffectiveness(), DamageDiceGroup.NO_EFFECT);
		// resistance and vulnerability
		damageDice = new DamageDiceGroup(0, 1, null);
		damageDice.grantResistance();
		damageDice.grantVulnerability();
		assertEquals(damageDice.getEffectiveness(), DamageDiceGroup.NEUTRALIZED);
		// resistance and immunity
		damageDice = new DamageDiceGroup(0, 1, null);
		damageDice.grantResistance();
		damageDice.grantImmunity();
		assertEquals(damageDice.getEffectiveness(), DamageDiceGroup.NO_EFFECT);
		// vulnerability and immunity
		damageDice = new DamageDiceGroup(0, 1, null);
		damageDice.grantVulnerability();
		damageDice.grantImmunity();
		assertEquals(damageDice.getEffectiveness(), DamageDiceGroup.NO_EFFECT);
		// resistance, vulnerability, and immunity
		damageDice = new DamageDiceGroup(0, 1, null);
		damageDice.grantResistance();
		damageDice.grantVulnerability();
		damageDice.grantImmunity();
		assertEquals(damageDice.getEffectiveness(), DamageDiceGroup.NO_EFFECT);
	}

	@Test
	@DisplayName("Damage cloning")
	void test8() {
		DamageDiceGroup damageDice1 = new DamageDiceGroup(4, 4, null);
		damageDice1.roll();
		DamageDiceGroup damageDice2 = damageDice1.clone();
		assertNotEquals(damageDice1, damageDice2);
		assertNotEquals(damageDice1.getDice(), damageDice2.getDice());
		assertEquals(damageDice1.getDice().size(), damageDice2.getDice().size());
		assertEquals(damageDice1.getSum(), damageDice2.getSum());
		for (int i = 0; i < damageDice1.getDice().size(); i++) {
			assertEquals(damageDice1.getDice().get(i).getSize(), damageDice2.getDice().get(i).getSize());
			assertNotEquals(damageDice1.getDice().get(i), damageDice2.getDice().get(i));
		}
	}

	@Test
	@DisplayName("DamageDiceGroup getSum override works")
	void test9() {
		// This test meant to verify damage is always 1 or more
		int numDice = 4;
		DamageDiceGroup damageDice = new DamageDiceGroup(numDice, 1, null);
		damageDice.roll();
		assertEquals(damageDice.getSum(), numDice);
		damageDice.addBonus(-numDice * 2);
		assertEquals(damageDice.getSum(), 1);
	}

}
