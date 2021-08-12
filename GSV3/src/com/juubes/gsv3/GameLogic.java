package com.juubes.gsv3;

import java.util.HashSet;

import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

public class GameLogic {
    private final Game game;
    private HashSet<Asteroid> asteroids;

    public GameLogic(Game game) {
	this.game = game;
	asteroids = new HashSet<>();
    }

    public void prepareGame() {
	for (int i = 0; i < Game.ASTEROID_COUNT; i++) {
	    Vector loc = new BasicVector(
		    new double[] { Math.random() * game.getWidth(), Math.random() * game.getHeight() });

	    // Move to center
	    Vector halfScreen = new BasicVector(new double[] { game.getWidth() / 2, game.getHeight() / 2 });
	    loc = loc.subtract(halfScreen);
	    loc = loc.multiply(0.8);
	    loc = loc.add(halfScreen);

	    BasicVector vel = new BasicVector(new double[] { Math.random() * 20, Math.random() * 20 });
	    asteroids.add(new Asteroid(loc, vel, Math.random() * Game.DEFAULT_MAX_MASS + Game.DEFAULT_MAX_MASS * 0.1));
	}
    }

    public void tick() {
	HashSet<Asteroid> tickedAsteroids = new HashSet<>();
	for (Asteroid asteroid : this.asteroids) {
	    if (asteroid.isColliding())
		continue;
	    Asteroid clone = new Asteroid(asteroid);

	    // Calculate the total gravitational pull from each asteroid
	    Vector totalForce = new BasicVector(2);
	    for (Asteroid colliding : this.asteroids) {
		if (colliding == asteroid)
		    continue;

		// If collides
		Vector dVector /* distance */ = asteroid.getLocation().subtract(colliding.getLocation());
		boolean bordersTouch = dVector.euclideanNorm() < asteroid.getWidth() / 2 + colliding.getWidth() / 2;
		boolean movingTowardsEachother;
		{
		    double distanceBefore = dVector.euclideanNorm();
		    Vector asteroidPosAfter = asteroid.getLocation().add(asteroid.getVelocity());
		    Vector colliderPosAfter = colliding.getLocation().add(colliding.getVelocity());
		    double distanceAfter = asteroidPosAfter.subtract(colliderPosAfter).euclideanNorm();
		    movingTowardsEachother = distanceBefore > distanceAfter;
		}

		if (bordersTouch && movingTowardsEachother) {
		    Vector newLoc = clone.getLocation().add(colliding.getLocation()).divide(2);
		    Vector newVel = clone.getVelocity().add(colliding.getVelocity()).divide(2);
		    Asteroid combined = new Asteroid(newLoc, newVel, clone.getMass() + colliding.getMass());
		    // Combine the asteroids into one
		    // Take masses into account
		    // TODO tp to the right place

		    boolean explode = false;
		    // boolean explode = (asteroid.getMass() > Game.DEFAULT_MAX_MASS ||
		    // colliding.getMass() > Game.DEFAULT_MAX_MASS);
		    if (explode) {

			// Break the two asteroids into 3 pieces flying off
			double angleOffset = Math.random() * Math.PI * 2;
			for (int i = 0; i < Game.EXPLODING_PIECES; i++) {
			    double angle = angleOffset + Math.PI * 2 * (i / Game.EXPLODING_PIECES);

			    double mass = combined.getMass() / Game.EXPLODING_PIECES;
			    Vector loc = combined.getLocation().multiply(Math.random());

			    Vector angleVel = new BasicVector(new double[] { Math.cos(angle), Math.sin(angle) })
				    .multiply(5);
			    Vector vel = combined.getVelocity().divide(Game.EXPLODING_PIECES).add(angleVel);

			    tickedAsteroids.add(new Asteroid(loc, vel, mass));
			}
		    } else {
			tickedAsteroids.add(combined);
		    }
		    colliding.setColliding(true);
		    clone.setColliding(true);
		    continue;
		}

		// Didn't collide
		double angle = Math.atan2(dVector.get(1), dVector.get(0));
		double gravitationalForce = Game.GRAVITATIONAL_CONSTANT
			* ((asteroid.getMass() * colliding.getMass()) / Math.pow(dVector.euclideanNorm(), 1.8));

		Vector fVector = new BasicVector(2);
		fVector.set(0, -Math.cos(angle) * gravitationalForce);
		fVector.set(1, -Math.sin(angle) * gravitationalForce);

		if (!bordersTouch)
		    totalForce = totalForce.add(fVector);
	    }
	    if (clone.isColliding())
		continue;

	    clone.setVelocity(clone.getVelocity().add(totalForce.divide(clone.getMass())));
	    tickedAsteroids.add(clone);
	}

	for (Asteroid asteroid : tickedAsteroids) {
	    Vector loc = asteroid.getLocation().add(asteroid.getVelocity());
	    Vector vel = asteroid.getVelocity();

	    if (Game.BORDERS) {
		// Teleport out of screen asteroids
		if (loc.get(0) < 0) {
		    loc.set(0, 0);
		    vel.set(0, -vel.get(0));
		}
		if (loc.get(0) > game.getWidth()) {
		    loc.set(0, game.getWidth());
		    vel.set(0, -vel.get(0));
		}

		if (loc.get(1) < 0) {
		    loc.set(1, 0);
		    vel.set(1, -vel.get(1));
		}
		if (loc.get(1) > game.getHeight()) {
		    loc.set(1, game.getHeight());
		    vel.set(1, -vel.get(1));
		}
	    }
	    asteroid.setLocation(loc);
	    asteroid.setVelocity(vel);
	}

	this.asteroids = tickedAsteroids;
    }

    public HashSet<Asteroid> getAsteroids() {
	return asteroids;
    }

}
