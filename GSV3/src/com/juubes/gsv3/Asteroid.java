package com.juubes.gsv3;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public class Asteroid {
	private final Vector location;
	private final Vector velocity;
	private final double mass;
	private boolean colliding;

	
	public Asteroid(Vector location, Vector velocity, double mass) {
		this.location = location;
		this.velocity = velocity;
		this.mass = mass;
		this.colliding = false;
	}

	public Asteroid(double x, double y, double mass) {
		this(new BasicVector(new double[] { x, y }), new BasicVector(2), mass);
	}

	/**
	 * Basicly, make a copy
	 */
	public Asteroid(Asteroid asteroid) {
		this(asteroid.location, asteroid.velocity, asteroid.mass);
	}

	public double getMass() {
		return mass;
	}

	public Vector getLocation() {
		return location;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public double getWidth() {
		return 2 * Math.sqrt(mass) * Game.RADIUS_MULTIPLIER;
	}

	public double getHeight() {
		return 2 * Math.sqrt(mass) * Game.RADIUS_MULTIPLIER;
	}

	public void setLocation(Vector vector) {
		int i = 0;
		for (double d : vector) {
			this.location.set(i++, d);
		}
	}

	public void setVelocity(Vector vector) {
		int i = 0;
		for (double d : vector) {
			this.velocity.set(i++, d);
		}
	}
	
	public void setColliding(boolean colliding) {
		this.colliding = colliding;
	}
	
	public boolean isColliding() {
		return colliding;
	}
	
}
