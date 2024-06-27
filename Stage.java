import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Stage {
	private BufferedImage imageRunning;
	private BufferedImage imageOver;
	private BufferedImage imageCurrent;

	public Stage() {
		try {
		this.imageRunning = ImageIO
				.read(new File("Assignment 2/A2-image-files/stage-normal.png"));
		this.imageOver = ImageIO
				.read(new File("Assignment 2/A2-image-files/stage-gameover.png"));
		} catch (IOException e) {
		e.printStackTrace();
		}
		this.imageCurrent = this.imageRunning;
	}

	public BufferedImage getCurrentImage() {
		return this.imageCurrent;
	}
	
	public void setGameOverBackground() {
		this.imageCurrent = this.imageOver;
	}
}
