package com.dndsuite.maths;

public class Vector {

	public static final Vector UNIT_X = new Vector(1, 0, 0);
	public static final Vector UNIT_Y = new Vector(0, 1, 0);
	public static final Vector UNIT_Z = new Vector(0, 0, 1);

	private double x;
	private double y;
	private double z;

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}

	public Vector sub(Vector other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}

	public double dot(Vector other) {
		return x * other.x + y * other.y + z * other.z;
	}

	public Vector cross(Vector other) {
		/*
		 * x y z this.x this.y this.z other.x other.y other.z
		 */
		double x = (this.y * other.z) - (this.z * other.y);
		double y = (this.z * other.x) - (this.x * other.z);
		double z = (this.x * other.y) - (this.y * other.x);
		return new Vector(x, y, z);
	}

	public double mag() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public Vector scale(double scalar) {
		return new Vector(x * scalar, y * scalar, z * scalar);
	}

	public Vector unit() {
		return scale(1.0 / mag());
	}

	public Vector proj(Vector base) {
		return base.scale(dot(base) / base.dot(base));
	}

	public double calculateAngleDiff(Vector other) {
		return Math.acos(dot(other) / (mag() * other.mag()));
	}

	public boolean equalTo(Vector other) {
		return x == other.x && y == other.y && z == other.z;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double z() {
		return z;
	}

	@Override
	public String toString() {
		return "<" + x + ", " + y + ", " + z + ">";
	}

}
