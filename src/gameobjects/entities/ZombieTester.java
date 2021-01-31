package gameobjects.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import engine.Manager;
import engine.effects.ViciousMockeryEffect;
import engine.events.Event;
import engine.events.attackrolls.GuidingBolt;

class ZombieTester extends Zombie {

	static Zombie assistant = new Zombie();
	
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
		ViciousMockeryEffect VM = new ViciousMockeryEffect(assistant,this);  
		Manager.addEffect(VM);
		addTarget(assistant);
		GuidingBolt guidingBolt = (GuidingBolt) availableEvents.get(0);
		Manager.processEvent(guidingBolt, assistant);
		assertEquals(-1,guidingBolt.getAdvantageState());
	}
}
