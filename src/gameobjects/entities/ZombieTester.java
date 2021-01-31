package gameobjects.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import engine.Manager;
import engine.effects.ViciousMockeryEffect;
import engine.events.attackrolls.GuidingBolt;

class ZombieTester extends Zombie {

	static TestDummy dummy = new TestDummy();
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Manager.initialize();
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
	void test_IntelligenceModifier() {
		assertEquals(-3,getAbilityModifier(INT));
	}
	
	@Test
	void test_Disadvantage() {
		GuidingBolt guidingBolt = (GuidingBolt) availableEvents.get(0); // TODO: this is cheating
		ViciousMockeryEffect VM = new ViciousMockeryEffect(dummy,this);
		addTarget(dummy);
		// 0 indicates neither ADVANTAGE nor DISADVANTAGE
		assertEquals(0,guidingBolt.getAdvantageState());
		Manager.addEffect(VM);
		Manager.processEvent(guidingBolt, dummy);
		// -1 indicates DISADVANTAGE
		assertEquals(-1,guidingBolt.getAdvantageState());
		invokeEvent(guidingBolt);
		// 0 indicates neither ADVANTAGE nor DISADVANTAGE
		assertEquals(0,guidingBolt.getAdvantageState());
	}
}
