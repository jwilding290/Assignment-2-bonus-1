import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameManager extends JFrame implements KeyListener {
	private int canvasWidth;
	private int canvasHeight;
	private int borderLeft;
	private int borderTop;
	
	private BufferedImage canvas;
	
	private Stage stage;
	
	private Enemy[] enemies;
	
	private Player player;
	
	private Goal[] goals;
	
	private Graphics gameGraphics;
	private Graphics canvasGraphics;
	
	private int numGoals;
	private int numEnemies;
	private boolean continueGame;
	
	private static String numberEnemies;
	private static int n;
	private static String numberGoals;
	private static int n2;
	private static boolean check = false;
	private static boolean cancel = false;
	private static boolean check2 = false;
	
	private double[] distances;
	private int closestgoal;
	private static boolean[] goalsdead;
	private static boolean lastchaughtenemy = false;
	
	private int playerscore = 0;
	private int enemyscore = 0;
	private int numgoalsdead = 0;
	private int scorediff;
	
	
	public static void main(String[] args) {
	
		while(!check) {
			
			numberEnemies = JOptionPane.showInputDialog("Enter the number of enemies");
			
				if(numberEnemies != null){
					
					n = Integer.parseInt(numberEnemies); 
		
					
					if(n>0){
						
						check = true;
						
					}
					else {
						
						JOptionPane.showMessageDialog(null,"ERROR: The the number of Enemies must be in range 1,2,3...n/n need at least 1 enemy");
						
					}
					
				}
				
				else {
					
					check = true;
					cancel = true;
					
				}
		}
		
while(!check2 && check && !cancel) {
			
			numberGoals = JOptionPane.showInputDialog("Enter the number of goals");
			
				if(numberGoals != null){
					
					n2 = Integer.parseInt(numberGoals); 
		
					
					if(n2>0){
						
						check2 = true;
						
					}
					else {
						
						JOptionPane.showMessageDialog(null,"ERROR: The the number of Goals must be in range 1,2,3...n/nas need at least 1 goal");
						
					}
					
				}
				
				else {
					
					check = true;
					cancel = true;
					
				}
		}
	
		if(!cancel) {
			
			GameManager managerObj = new GameManager(1920, 1280, n2, n);
		
		}
	
	}
	
	public GameManager(int preferredWidth, int preferredHeight, int maxGoals, int maxEnemies) {
		
		
		this.borderLeft = getInsets().left;
		this.borderTop = getInsets().top;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (screenSize.width < preferredWidth)
			this.canvasWidth = screenSize.width - getInsets().left - getInsets().right;
		else
			this.canvasWidth = preferredWidth - getInsets().left - getInsets().right;
		if (screenSize.height < preferredHeight)
			this.canvasHeight = screenSize.height - getInsets().top - getInsets().bottom;
		else
			this.canvasHeight = preferredHeight - getInsets().top - getInsets().bottom;
		
		setSize(this.canvasWidth, this.canvasHeight);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		addKeyListener(this);
		
		this.canvas = new BufferedImage(this.canvasWidth, this.canvasHeight, BufferedImage.TYPE_INT_RGB);
		
		Random rng = new Random(4);
		
		this.stage = new Stage();
		
		this.player = new Player(this.canvasWidth - (Math.abs(rng.nextInt()) % (this.canvasWidth / 2)),
				(Math.abs(rng.nextInt()) % this.canvasHeight));
		
		this.numGoals = maxGoals;
		this.goals = new Goal[this.numGoals];
		goalsdead = new boolean[this.numGoals];
		for (int i = 0; i < this.numGoals; i++) {
			this.goals[i] = new Goal(this, this.canvasWidth / 2, Math.abs(rng.nextInt()) % this.canvasHeight);
		}
		
		this.numEnemies = maxEnemies;
		this.enemies = new Enemy[this.numEnemies];
		for (int i = 0; i < this.numEnemies; i++) {
			this.enemies[i] = new Enemy(this, Math.abs(rng.nextInt()) % (this.canvasWidth / 4),
					Math.abs(rng.nextInt()) % this.canvasHeight);
		}
		
		
		this.gameGraphics = getGraphics();
		this.canvasGraphics = this.canvas.getGraphics();
		
		this.continueGame = true;
		while (this.continueGame) {
			updateCanvas();
		}
		this.stage.setGameOverBackground();
		scorediff = Math.abs(playerscore-enemyscore);
		
		if(playerscore > enemyscore) {
			JOptionPane.showMessageDialog(null, "Player won:\nPlayer ate " + playerscore +" fish\nEnemy ate " + enemyscore + " fish\nWon by " + scorediff + " fish");
		}
		else if(playerscore < enemyscore) {
			JOptionPane.showMessageDialog(null, "Enemy won:\nPlayer ate " + playerscore + " fish\nEnemy ate " + enemyscore + " fish\nWon by " + scorediff + " fish");
		}
		else {
			if(!lastchaughtenemy) {
				JOptionPane.showMessageDialog(null, "Draw:\nPlayer ate " + playerscore + " fish\nEnemy ate " + enemyscore + " fish\n(Enemy ate " + enemyscore + " fish first wins on countback)");
			}
			else {
				JOptionPane.showMessageDialog(null, "Draw:\nPlayer ate " + playerscore + " fish\nEnemy ate " + enemyscore + " fish\n(Player ate " + playerscore + " fish first wins on countback)");
			}
			
		}
		updateCanvas();
		
		
		
		
	}
	

	
	public void updateCanvas() {
		long start = System.nanoTime();
		
		distances = new double[this.numGoals];
		
		this.player.performAction();
		
		for (int i = 0; i < this.numEnemies; i++) {
			
			for(int j = 0; j < this.numGoals; j++) {
				if(!goalsdead[j]) {
					
				
					distances[j] = Math.sqrt(Math.pow(goals[j].getX()-enemies[i].getX(), 2) + Math.pow(goals[j].getY()-enemies[i].getY(), 2));
				
				}
				else {
					
					distances[j] = 1000000;
				}
			}
			
			closestgoal = 0;
			
			for(int j = 1; j < distances.length; j++) {
					
					if(distances[j] < distances[closestgoal]) {
						
						closestgoal = j;
		
				}
				
			}
			
			this.enemies[i].performAction();

		}
		
	for (int i = 0; i < this.numGoals; i++) {
			
			{
				
				this.goals[i].performAction(goalsdead[i]);
				
			}
			

		}
	
	for(int i = 0; i<this.numGoals; i++) {
		
		if ((Math.abs(this.goals[i].getX() - this.player.getX()) < (this.goals[i].getCurrentImage().getWidth() / 2))
				&& (Math.abs(this.goals[i].getY() - this.player.getY()) < (this.goals[i].getCurrentImage().getWidth() / 2)) && !goalsdead[i]) {
			this.goals[i].die();	
			goalsdead[i] = true;
			playerscore++;
			numgoalsdead++;
			if(numgoalsdead == this.numGoals) {
				if(playerscore > enemyscore) {
					
					for (int k = 0; k < this.numEnemies; k++) {
						this.enemies[k].die();
					}
					
				}
				else if(playerscore < enemyscore) {
					
					this.player.die();
				}
				else {
					this.player.die();
	
				}
				
				this.stage.setGameOverBackground();
				this.continueGame=false;
			}
		}
	}
	
	
	
	for(int j = 0; j < this.numEnemies; j++) {
		
		for(int i = 0; i < this.numGoals; i++) {
			
			if ((Math.abs(this.goals[i].getX() - this.enemies[j].getX()) < (this.goals[i].getCurrentImage().getWidth() / 2))
					&& (Math.abs(this.goals[i].getY() - this.enemies[j].getY()) <
					(this.goals[i].getCurrentImage().getWidth()/ 2)) && !goalsdead[i]) {
						this.goals[i].die();	
						goalsdead[i] = true;
						enemyscore++;
						numgoalsdead++;
						if(numgoalsdead == this.numGoals) {
							if(playerscore > enemyscore) {
								
								for (int k = 0; k < this.numEnemies; k++) {
									this.enemies[k].die();
								}
								
							}
							else if(playerscore < enemyscore) {
								
								this.player.die();
							}
							else {
								for (int k = 0; k < this.numEnemies; k++) {
									this.enemies[k].die();
									lastchaughtenemy = true;
								}
							}
							this.stage.setGameOverBackground();
							this.continueGame=false;
						}
		}
	}
	
		
	
	try {
		this.canvasGraphics.drawImage(stage.getCurrentImage(), 0, 0, null);
		
		this.canvasGraphics.drawImage(player.getCurrentImage(),
		this.player.getX() - (this.player.getCurrentImage().getWidth() / 2),
		this.player.getY() - (this.player.getCurrentImage().getHeight() / 2), null);
		
		for (int i = 0; i < this.numEnemies; i++) {
			this.canvasGraphics.drawImage(this.enemies[i].getCurrentImage(),
			this.enemies[i].getX() - (this.enemies[i].getCurrentImage().getWidth() / 2),
			this.enemies[i].getY() - (this.enemies[i].getCurrentImage().getHeight() / 2), null);
		}
		
		for (int i = 0; i < this.numGoals; i++) {
			this.canvasGraphics.drawImage(this.goals[i].getCurrentImage(),
			this.goals[i].getX() - (this.goals[i].getCurrentImage().getWidth() / 2),
			this.goals[i].getY() - (this.goals[i].getCurrentImage().getHeight() / 2), null);
		}
	} 
		catch (Exception e) {
		System.err.println(e.getMessage());
		
		
	}
	
	this.gameGraphics.drawImage(this.canvas, this.borderLeft, this.borderTop, this);
	long end = System.nanoTime();
	this.gameGraphics.drawString("FPS: " + String.format("%2d", (int) (1000000000.0 / (end - start))),
			this.borderLeft + 50, this.borderTop + 50);
		}
	}
	
	public Goal getGoal() {
		return this.goals[closestgoal];
	}
	
	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_LEFT)
			this.player.setKey('L', true);
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
			this.player.setKey('R', true);
		if (ke.getKeyCode() == KeyEvent.VK_UP)
			this.player.setKey('U', true);
		if (ke.getKeyCode() == KeyEvent.VK_DOWN)
			this.player.setKey('D', true);
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE)
			this.continueGame = false;
		
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_LEFT)
			this.player.setKey('L', false);
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT)
			this.player.setKey('R', false);
		if (ke.getKeyCode() == KeyEvent.VK_UP)
			this.player.setKey('U', false);
		if (ke.getKeyCode() == KeyEvent.VK_DOWN)
			this.player.setKey('D', false);
		
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		
	}
}
