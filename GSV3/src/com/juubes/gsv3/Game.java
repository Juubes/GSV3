package com.juubes.gsv3;

public class Game {
	public static final int ASTEROID_COUNT = 20;
	public static final boolean BORDERS = true;
	public static final double CANVAS_MULTIPLIER = 500;
	public static final double RADIUS_MULTIPLIER = 1;
	public static final double GRAVITATIONAL_CONSTANT = 5;
	public static final int EXPLODING_PIECES = 3;
	public static final double DEFAULT_MAX_MASS = 5E7;

	public static void main(String[] args) {
		new Game();
	}

	private final GameLogic gameLogic;
	private final Renderer renderer;
	private final Thread gameThread;

	public Game() {
		this.gameLogic = new GameLogic(this);
		this.renderer = new Renderer(this);
		this.gameThread = new Thread(() -> {
			renderer.createCanvas();
			gameLogic.prepareGame();
			while (true) {
				gameLogic.tick();
				renderer.render();

				try {
					Thread.sleep(1000 / 60);
				} catch (Exception e) {
				}
			}
		});

		this.gameThread.start();
	}

	public GameLogic getGameLogic() {
		return gameLogic;
	}

	public Thread getGameThread() {
		return gameThread;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public double getWidth() {
		return getCanvasWidth() * CANVAS_MULTIPLIER;
	}

	public double getCanvasWidth() {
		return renderer.getCanvas().getWidth();
	}

	public double getHeight() {
		return getCanvasHeight() * CANVAS_MULTIPLIER;
	}

	public double getCanvasHeight() {
		return renderer.getCanvas().getHeight();
	}

}
