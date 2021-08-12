package com.juubes.gsv3;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Renderer {
    private final JFrame frame;
    private final Game game;

    private Canvas canvas;

    public Renderer(Game game) {
	this.game = game;
	this.frame = new JFrame("GSV3 by Juubes");
	frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setUndecorated(true);
	frame.setVisible(false);
    }

    public void createCanvas() {
	Input input = new Input();

	this.canvas = new Canvas();
	this.canvas.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	this.canvas.setBackground(Color.BLACK);
	this.canvas.addKeyListener(input);

	this.frame.add(canvas);
	this.frame.addKeyListener(input);
	this.frame.setVisible(true);

	this.canvas.requestFocus();
    }

    public void render() {
	BufferStrategy bs = canvas.getBufferStrategy();
	if (bs == null) {
	    canvas.createBufferStrategy(2);
	    return;
	}
	Graphics2D g = (Graphics2D) bs.getDrawGraphics();
	// Clear screen
	g.setColor(new Color(0, 0, 0, 5));
	// g.setColor(Color.BLACK);
	g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

	// Draw asteroids
	g.scale(1 / Game.CANVAS_MULTIPLIER, 1 / Game.CANVAS_MULTIPLIER);
	for (Asteroid asteroid : game.getGameLogic().getAsteroids()) {
	    if (!asteroid.isColliding())
		g.setColor(new Color(255, 165, 0, 255));
	    else
		g.setColor(Color.RED);
	    int x = (int) (asteroid.getLocation().get(0) - asteroid.getWidth() / 2);
	    int y = (int) (asteroid.getLocation().get(1) - asteroid.getHeight() / 2);
	    g.fillOval(x, y, (int) asteroid.getWidth(), (int) asteroid.getHeight());
	}

	g.scale(Game.CANVAS_MULTIPLIER, Game.CANVAS_MULTIPLIER);

	g.setColor(Color.WHITE);
	g.setFont(new Font("Arial", 0, 20));
	g.drawString("Asteroideja: " + game.getGameLogic().getAsteroids().size(), 0, 20);

	g.dispose();
	bs.show();
    }

    public Canvas getCanvas() {
	return canvas;
    }

}
