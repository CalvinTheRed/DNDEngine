package com.dndsuite.core;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dndsuite.core.json.JSONLoader;
import com.dndsuite.core.json.parsers.Subevent;
import com.dndsuite.core.json.parsers.subevents.AbilityScoreCalculation;
import com.dndsuite.core.json.parsers.subevents.DamageCalculation;
import com.dndsuite.exceptions.SubeventMismatchException;
import com.dndsuite.exceptions.UUIDDoesNotExistException;
import com.dndsuite.exceptions.UUIDNotAssignedException;
import com.dndsuite.maths.Vector;
import com.dndsuite.maths.dice.DamageDiceGroup;

public class GameObject extends JSONLoader implements UUIDTableElement {

	ArrayList<EventGroup> queuedEvents;

	public GameObject(JSONObject json) {
		super(json);
		queuedEvents = new ArrayList<EventGroup>();

		UUIDTable.addToTable(this);
		VirtualBoard.addGameObject(this);
	}

	@SuppressWarnings("unchecked")
	public GameObject(String file, Vector pos, Vector rot) {
		super("gameobjects/" + file);
		queuedEvents = new ArrayList<EventGroup>();

		JSONArray position = new JSONArray();
		position.add(pos.x());
		position.add(pos.y());
		position.add(pos.z());
		json.put("pos", position);

		JSONArray rotation = new JSONArray();
		rotation.add(rot.x());
		rotation.add(rot.y());
		rotation.add(rot.z());
		json.put("rot", rotation);

		UUIDTable.addToTable(this);
		VirtualBoard.addGameObject(this);
	}

	@Override
	public long getUUID() throws UUIDNotAssignedException {
		if (json.containsKey("uuid")) {
			return (long) json.get("uuid");
		}
		throw new UUIDNotAssignedException(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void assignUUID(long uuid) {
		json.put("uuid", uuid);
	}

	public Vector getPos() {
		JSONArray pos = (JSONArray) json.get("pos");
		return new Vector((double) pos.get(0), (double) pos.get(1), (double) pos.get(2));
	}

	public Vector getRot() {
		JSONArray rot = (JSONArray) json.get("rot");
		return new Vector((double) rot.get(0), (double) rot.get(1), (double) rot.get(2));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void parseResourceData() {
		JSONArray effectNames = (JSONArray) json.remove("effects");
		JSONArray effectUUIDs = new JSONArray();
		while (effectNames.size() > 0) {
			// TODO: ensure source/target UUIDs are provided here
			String effectName = (String) effectNames.remove(0);
			Effect e = new Effect(effectName, this, this);
			UUIDTable.addToTable(e);
			try {
				effectUUIDs.add(e.getUUID());
			} catch (UUIDNotAssignedException ex) {
				ex.printStackTrace();
			}

		}
		json.put("effects", effectUUIDs);

		JSONArray taskNames = (JSONArray) json.remove("tasks");
		JSONArray taskUUIDs = new JSONArray();
		while (taskNames.size() > 0) {
			String taskName = (String) taskNames.remove(0);
			Task t = new Task(taskName);
			UUIDTable.addToTable(t);
			try {
				taskUUIDs.add(t.getUUID());
			} catch (UUIDNotAssignedException ex) {
				ex.printStackTrace();
			}
		}
		json.put("tasks", taskUUIDs);
	}

	public boolean processSubevent(Subevent s) {
		JSONArray effects = (JSONArray) json.get("effects");
		boolean changed = false;
		for (int i = 0; i < effects.size(); i++) {
			long uuid = (long) effects.get(i);
			try {
				Effect effect = (Effect) UUIDTable.get(uuid);
				changed |= effect.processSubevent(s);
			} catch (UUIDDoesNotExistException ex) {
				ex.printStackTrace();
			}
		}
		return changed;
	}

	public void invokeTask(long uuid) {
		// TODO: dedicate a subevent to collecting all Tasks available to this
		try {
			UUIDTableElement element = UUIDTable.get(uuid);
			if (element instanceof Task) {
				Task t = (Task) element;
				t.invoke(this);
			}
		} catch (UUIDDoesNotExistException e) {
			e.printStackTrace();
			return;
		}
	}

	public void invokeQueuedEvent(Event e, Vector start, Vector end) {
		EventGroup container = null;
		for (EventGroup group : queuedEvents) {
			if (group.contains(e)) {
				container = group;
				e.invoke(start, end, this);
				break;
			}
		}

		if (container != null) {
			queuedEvents.remove(container);
		}
	}

	public void queueEventGroup(EventGroup group) {
		queuedEvents.add(group);
	}

	public void clearQueuedEvents() {
		queuedEvents.clear();
	}

	@SuppressWarnings("unchecked")
	public long getAbilityModifier(String ability) {
		JSONObject ascJson = new JSONObject();
		ascJson.put("subevent", "ability_score_calculation");
		ascJson.put("ability", ability);
		AbilityScoreCalculation asc = new AbilityScoreCalculation();
		try {
			asc.parse(ascJson, null, this, this);
		} catch (SubeventMismatchException ex) {
			ex.printStackTrace();
		}

		long abilityScoreBuffer = asc.get() - 10;
		if (abilityScoreBuffer < 0) {
			abilityScoreBuffer--;
		}
		return abilityScoreBuffer / 2;
	}

	public void takeDamage(DamageCalculation damage) {
		for (DamageDiceGroup group : damage.getDamageDice()) {
			takeDamage(group.getDamageValue());
		}
	}

	@SuppressWarnings("unchecked")
	private void takeDamage(long damage) {
		JSONObject health = (JSONObject) json.remove("health");
		long base = (long) health.get("base");
		long max = (long) health.get("max");
		long tmp = (long) health.get("tmp");
		long current = (long) health.get("current");

		if (tmp > 0) {
			tmp -= damage;
			if (tmp < 0) {
				damage = -tmp;
				tmp = 0;
			}
			health.put(tmp, tmp);
		}

		current -= damage;
		if (current < 0) {
			current = 0;
			// TODO: death or death saves here
			// instant-kill check would happen here based on magnitude of current
		}
		health.put("current", current);

		json.put("health", health);
	}

	public JSONObject getHealth() {
		return (JSONObject) json.get("health");
	}

	public long getProficiencyBonus() {
		if (json.containsKey("level")) {
			long level = (long) json.get("level");
			return 2L + (level - 1L) / 4L;
		}
		return 0L;
	}

}
