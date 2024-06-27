import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy {
	private BufferedImage imageRunning;
	private BufferedImage imageOver;
	private BufferedImage imageCurrent;
	
	private double xi;
	private double yj;
	private double distance;
	private GameManager gameManager;
	private Goal goal;
	private int speed;
	
	
	private int x;
	private int y;
	

	public Enemy(GameManager gameManager, int xcoor, int ycoor) {
		
		this.gameManager = gameManager;
		this.x = xcoor;
		this.y = ycoor;
		
		try {
		this.imageRunning = ImageIO
				.read(new File("A2-image-files/enemy-alive.png"));
		this.imageOver = ImageIO
				.read(new File("A2-image-files/enemy-dead.png"));
		} catch (IOException e) {
		e.printStackTrace();
		}
		this.imageCurrent = this.imageRunning;
	}


	public BufferedImage getCurrentImage() {
		return this.imageCurrent;
	}
	
	public void die() {
		this.imageCurrent = this.imageOver;
	}

	public int getX() {
		
		return x;
	}

	public int getY() {
		
		return y;
	}


	public void performAction() {
		goal = goallocation();
		speed = 3;
		
		xi= goal.getX()-getX();
		yj = goal.getY()-getY();
		
		distance = Math.sqrt(Math.pow(xi, 2)+Math.pow(yj, 2));
		
		xi /= distance;
		yj /= distance;
		
		x += xi*speed;
		y += yj*speed;
	
	}
	
	private Goal goallocation() {
		
		goal = this.gameManager.getGoal();
		return goal;
	}
}

