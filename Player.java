import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player {
	private BufferedImage imageRunning;
	private BufferedImage imageOver;
	private BufferedImage imageCurrent;
	
	private char key;
	private boolean pressed;
	
	private int speed;

	
	private int x;
	private int y;
	

	public Player(int xcoor, int ycoor) {
		
		this.x = xcoor;
		this.y = ycoor;
		
		try {
		this.imageRunning = ImageIO
				.read(new File("A2-image-files/player-alive.png"));
		this.imageOver = ImageIO
				.read(new File("A2-image-files/player-dead.png"));
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
		if(pressed) {
			speed = 7;		
		}
		else {
			speed = 0;
		}
		
		if(key == 'L') {
			x -= speed;
		}
		else if(key == 'R') {
			x += speed;
		}
		else if(key == 'U') {
			y -= speed;
		}
		else if(key == 'D') {
			y += speed;
		}
		
		
	}

	public void setKey(char c, boolean b) {
		key = c;
		pressed = b;
	}
}

