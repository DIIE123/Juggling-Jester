 	import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import javax.sound.sampled.*;

public class Main extends JPanel implements MouseListener, KeyListener, Runnable {
	public static int gameState = 1;
	public static int lives = 3;
	public static int score = 0;
	public static int multiplier = 1;
	
	public static int count1 = 0;
	public static int count2 = 950;
	public static int count3 = 0;
	public static int[] powerCounts = {0, 0, 0, 0};
	
	public static int bombUpdate = 0;
	
	public static int windowX = 650;
	public static int windowY = 650;
	
	public static int mouseX;
	public static int mouseY;
	
	Player player = new Player(windowX / 2 - 40, 500, 80, 120, 12, 1);
	public static boolean moveRight = false;
	public static boolean moveLeft = false;
	public static int lastMoved = 0;
	
	public static ArrayList<Ball> balls = new ArrayList<>();
	public static ArrayList<Bomb> bombs = new ArrayList<>();
	public static int bombTime = 75;
	public static ArrayList<PowerUp> powerups = new ArrayList<>();
	
	public static boolean music = true;
	public static Menu startMenu = new Menu();
	public static ArrayList<Button> startButtonList = startMenu.buttonList;
	
	public static boolean paused = false;
	public static Menu pauseMenu = new Menu();
	public static ArrayList<Button> pauseButtonList = pauseMenu.buttonList;
	
	Button rulesButton = new Button (435, 105, 125, 70);
	Button endButton = new Button (260, 405, 125, 70);
	
	public static BufferedImage startMenuImage;
	public static BufferedImage pauseMenuImage;
	public static BufferedImage background;
	public static BufferedImage rules;
	public static BufferedImage end;
	
	public static BufferedImage onButton;
	public static BufferedImage offButton;
	
	public static BufferedImage ball;
	public static BufferedImage bomb;
	public static BufferedImage life;
	
	public static BufferedImage tomatoSplat;
	public static BufferedImage stunImage;
	public static BufferedImage invincibleImage;
	
	public static BufferedImage playerImage;
	public static BufferedImage[] playerRight = new BufferedImage[4];
	public static BufferedImage[] playerLeft = new BufferedImage[4];
	public static double playerState = 0;
	
	public static Clip menuMusic;
	public static Clip gameMusic;
	public static Clip hit;
	public static Clip bounce;
	public static Clip apple;
	public static Clip watermelon;
	public static Clip tomato;
	public static Clip candy;
	public static Clip pretzel;
	
	
	
	//Power Up Booleans
	public static boolean speedUp = false;
	public static boolean invincible = false;
	public static boolean tomatoed = false;
	public static boolean stun = false;

	// PaintComponent method
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Main menu
		if (gameState == 1) {
			g.drawImage(startMenuImage, 0, 0, this);
			if (music) {
				g.drawImage(onButton, 460, 325, 35, 70, this);
			}
			else
			{
				g.drawImage(offButton, 460, 325, 35, 70, this);
			}
		}
		// Gameplay
		else if (gameState == 2) {
			g.drawImage(background, 0, 0, this);
			
			// Player
			if (moveLeft) {
				playerImage = playerLeft[(int) playerState];
			}
			else if (moveRight) {
				playerImage = playerRight[(int) playerState];
			}
			g.drawImage(playerImage, player.xPos, player.yPos, this);
			
			// Balls
			for(int i = 0; i < balls.size(); i++) {
				Ball b1 = balls.get(i);
				g.drawImage(ball, b1.getX(), b1.getY(), b1.getR(), b1.getR(), this);
			}
			
			// Bombs
			for(int i = 0; i < bombs.size(); i++) {
				Bomb b2 = bombs.get(i);
				g.drawImage(bomb, b2.getX(), b2.getY(), b2.getR(), b2.getR(), this);
			}
			
			// Powerups
			for(int i = 0; i < powerups.size(); i++) {
				PowerUp p = powerups.get(i);
				g.drawImage(p.pic, p.rect.x, p.rect.y, p.rect.width, p.rect.height, this);
			}
			
			if (stun) {
				g.drawImage(stunImage, player.xPos + 20, player.yPos - 50, this);
			}
			
			if (invincible) {
				g.drawImage(invincibleImage, player.xPos + 20, player.yPos - 40, this);
			}
			
			if (tomatoed) {
				g.drawImage(tomatoSplat, 0, 0, this);
			}
			
						
			// Lives
			for(int i = 0; i < lives; i++) {
				g.drawImage(life, 10 + 30 * i, 10, this);
			}
			
			// Score
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier New", Font.PLAIN, 30));
			g.drawString(String.format("Score: %06d", score), 400, 30);
			
			// Pause
			if (paused) {
				g.drawImage(pauseMenuImage, 150, 100, this);
				if (music) {
					g.drawImage(onButton, 420, 290, 35, 70, this);
				}
				else
				{
					g.drawImage(offButton, 420, 290, 35, 70, this);
				}
			}
		}
		// Rules
		else if (gameState == 3) {
			g.drawImage(rules, 0, 0, 650, 650, this);
		}
		// End
		else if (gameState == 4) {
			g.drawImage(end, 0, 0, 650, 650, this);
			g.setColor(Color.RED);
			g.setFont(new Font("Courier New", Font.BOLD, 50));
			g.drawString(String.format("%06d", score), 235, 370);
		}
		
	}
	
	// Runnable Method
	public void run() {
		while(true) {
			
			try {
				Thread.sleep(16);
				repaint();
				if (gameState == 2 && !paused) {
					// Bomb stuff
					count1++;
					count2++;
					count3++;
					
					if(count1 == bombTime) {
						count1 = 0;
						Bomb b = new Bomb(randomX(), -50 , randomRadius());
						bombs.add(b);
						if (bombTime > 45) {
							bombTime--;
						}
	
					}
					if(count2 == 1000 && balls.size() < 3) {
						count2 = 0;
						Ball b = new Ball(randomX(), -50, randomRadius());
						balls.add(b);
					}
					if(count3 == 300) {
						count3 = 0;
						PowerUp b = new PowerUp((int)(Math.random() * 5) + 1, randomX());
						powerups.add(b);
					}
					
					if(speedUp) {
						if(powerCounts[0] == 300) {
							speedUp = false;
							player.maxSpeed = 12;
							powerCounts[0] = 0;
						}
						else {
							powerCounts[0]++;
							player.maxSpeed = 18;
						}
						
					}
					
					if(invincible) {
						if(powerCounts[1] == 300) {
							invincible = false;
							powerCounts[1] = 0;
						}
						else {
							powerCounts[1]++;
						}
					}
					
					if(tomatoed) {
						if(powerCounts[2] == 180) {
							tomatoed = false;
							powerCounts[2] = 0;
						}
						else {
							powerCounts[2]++;
						}
					}
					
					if(stun) {
						if(powerCounts[3] == 60) {
							stun = false;
							player.maxSpeed = 12;
							powerCounts[3] = 0;
						}
						else {
							powerCounts[3]++;
							player.maxSpeed = 0;
							player.acceleration = 0;
						}
					}
					
					update();
					check();
					
					// Player stuff
					playerState += 0.2;
					if (playerState >= 4) {
						playerState = 0;
					}
					
					if (moveRight && !moveLeft) {
						if (player.speed < player.maxSpeed)
							player.acceleration = 1;
						else player.acceleration = 0;
					}
					else if (moveLeft && !moveRight) {
						if (player.speed > -1 * player.maxSpeed)
							player.acceleration = -1;
						else player.acceleration = 0;
					}
					else {
						if (player.speed > 0) player.acceleration = -1;
						else if (player.speed < 0) player.acceleration = 1;
						else player.acceleration = 0;
					}
					player.move();
					
					// Lives
					if (lives <= 0) {
						gameState = 4;
						gameMusic.stop();
					}
				}
			}
			catch(IndexOutOfBoundsException e) {
				System.out.println("Index");
				break;
			}
			catch(Exception e) {
				System.out.println("Exception"); 
				break;
			}
			
		}
		
	}
	
	// Reset variables
	public void reset() {
		paused = false;
		player.xPos = windowX / 2 - 40;
		lives = 3;
		score = 0;
		count1 = 0;
		count2 = 950;
		count3 = 0;
		powerCounts[0] = 0;
		powerCounts[1] = 0;
		powerCounts[2] = 0;
		powerCounts[3] = 0;
		player.speed = 0;
		player.acceleration = 0;
		lastMoved = 0;
		moveRight = false;
		moveLeft = false;
		multiplier = 1;
		bombTime = 75;
		
		speedUp = false;
		invincible = false;
		tomatoed = false;
		stun = false;
		
		balls.clear();
		bombs.clear();
		
		menuMusic.stop();
		menuMusic.setFramePosition(0);
		gameMusic.setFramePosition(0);
	}
	
	// Method to update balls
	public void update() {
		bombUpdate++;
		if (bombUpdate >= 2) {
			bombUpdate = 0;
		}
		
		for(int i = 0; i < balls.size(); i++) {
			Ball b1 = balls.get(i);
			b1.setX(b1.getX() + b1.getXVel());
			if (bombUpdate == 0) {
				b1.decreaseY();
			}
			b1.setY(b1.getY() + b1.getYVel());
			if(b1.getY() > 650) {
				balls.remove(i);
				Ball b = new Ball(randomX(), -50, randomRadius());
				balls.add(b);
				
				multiplier = 1;
				score -= 50;
				if (score < 0) score = 0;
				
				i--;
			}
			
		}
		
		
		for(int j = 0; j < bombs.size(); j++) {
			Bomb b2 = bombs.get(j);
		 	b2.setX(b2.getX() + b2.getXVel());
		 	if (bombUpdate == 0) {
		 		b2.decreaseY();
			}
		 	
			b2.setY(b2.getY() + b2.getYVel());
			if(b2.getY() > 650) {
				bombs.remove(j);
				j--;
			}
		}
		
		for (int k = 0; k < powerups.size(); k++) {
			PowerUp p = powerups.get(k);
			p.rect.x += p.xVel;
			if (bombUpdate == 0) {
				p.yVel++;
			}
			p.rect.y += p.yVel;
			if(p.rect.y > 650) {
				powerups.remove(k);
				k--;
			}
		}
	}
	
	// Check collision
	public void check() {
		for(int i = 0; i < balls.size(); i++) {
			//Right check
			Ball b1 = balls.get(i);
			
			if(b1.getY() + b1.getR() >= player.yPos && (b1.getCenter() >= player.xPos && b1.getCenter() <= (player.xPos+player.playerWidth))) {
				if (!b1.getHitball()) {
					score += 10 * multiplier;
					multiplier++;
					
					if (music) {
						bounce.setFramePosition(0);
						bounce.start();
					}
				}
				b1.setHitball(true);
				
				b1.setYVel(-20);
				if(b1.getX() > 325) 
					b1.setXVel(-1*Math.abs(b1.getXVel()));
				else
					b1.setXVel(Math.abs(b1.getXVel()));
			}
			else {
				b1.setHitball(false);
			}
		}
		for(int i = 0; i < bombs.size(); i++) {
			Bomb b1 = bombs.get(i);
			if(b1.getY() + b1.getR() >= player.yPos && (b1.getCenter() >= player.xPos && b1.getCenter() <= (player.xPos+player.playerWidth))) {
				bombs.remove(i);
				if(!invincible) {
					lives--;
					if (music) {
						hit.setFramePosition(0);
						hit.start();
					}
				}
				i--;
			}
			
		}
		for(int i = 0; i < powerups.size(); i++) {
			PowerUp p = powerups.get(i);
			if((p.rect.y + p.rect.height >= player.yPos) && (p.rect.x + (p.rect.width/2) >= player.xPos && (p.rect.x + p.rect.width/2) <= (player.xPos+player.playerWidth))) {
				if(p.type == 1) {
					if(lives <= 2) {
						lives++;
					}
					if (music) {
						apple.setFramePosition(0);
						apple.start();
					}
				}
				else if(p.type == 2) {
					if (music) {
						candy.setFramePosition(0);
						candy.start();
					}
					speedUp = true;
				}
				else if(p.type == 3) {
					if (music) {
						pretzel.setFramePosition(0);
						pretzel.start();
					}
					invincible = true;
				}
				else if(p.type == 4){
					if (music) {
						tomato.setFramePosition(0);
						tomato.start();
					}
					tomatoed = true;
				}
				else {
					if (music) {
						watermelon.setFramePosition(0);
						watermelon.start();
					}
					stun = true;
				}
				powerups.remove(i);
				i--;
			}
		}
	}
	
	// Method to check button click
	public int checkButtonClick(Menu m) {
		for (int i = 0; i < m.buttonList.size(); i++) {
			if(m.buttonList.get(i).checkInBounds(mouseX, mouseY)) {
				return i+1;
			}
		}
		return 0;
	}
	
	// Method for radius stuff
	public static int randomRadius()
	{	
		return (int)(Math.random() * 41) + 40;
	}
	public static int randomX()
	{	
		return (int)(Math.random() * 401) + 100;
	}
	
	// JPanel Constructor
	public Main()
	{
		setPreferredSize(new Dimension(windowX, windowY));
		this.setFocusable(true);
		addMouseListener(this);
		addKeyListener(this);
		Thread thread = new Thread(this);
		thread.start();
	}
	
	// Key Pressed Event
	public void keyPressed(KeyEvent e) {
		if (gameState == 2) {
			if (e.getKeyChar() == 27) {
				paused = !paused;
			}
			if (e.getKeyChar() == 'a' && e.getKeyChar() == 'd') {
				moveRight = false;
				moveLeft = false;
			}
			else if (e.getKeyChar() == 'a') {
				moveLeft = true;
			}
			else if (e.getKeyChar() == 'd') {
				moveRight = true;
			}
		}
		if (gameState == 3 || gameState == 4) {
			if (e.getKeyChar() == 27) {
				gameState = 1;
				if (gameState == 4) {
					if (music)
						menuMusic.start();
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (gameState == 2) {
			if (e.getKeyChar() == 'a') {
				moveLeft = false;
			}
			if (e.getKeyChar() == 'd') {
				moveRight = false;
			}
		}
	}
	
	// Mouse Pressed Event
	public void mousePressed(MouseEvent e) {
		// Obtaining Coordinates
		mouseX = e.getX();
		mouseY = e.getY();

		// Main menu
		if (gameState == 1) {
			int buttonIndex = checkButtonClick(startMenu);
			
			// Play
			if (buttonIndex == 1) {
				reset();
				gameState = 2;
				if (music)
					gameMusic.start();
			}
			// How to play
			else if (buttonIndex == 2) {
				gameState = 3;
			}
			// Music
			else if (buttonIndex == 3) {
				music = !music;
				if (music) {
					menuMusic.start();
				}
				else {
					menuMusic.stop();
				}
			}
			// Exit
			else if (buttonIndex == 4) {
				System.exit(0);
			}
		}
		// Gameplay
		else if (gameState == 2) {
			if (paused) {
				int buttonIndex = checkButtonClick(pauseMenu);
				
				// Resume
				if (buttonIndex == 1) {
					paused = false;
				}
				// Options
				else if (buttonIndex == 2) {
					music = !music;
					if (music) {
						gameMusic.start();
					}
					else {
						gameMusic.stop();
					}
				}
				// Exit
				else if (buttonIndex == 3) {
					gameState = 1;
					gameMusic.stop();
					if (music)
						menuMusic.start();
				}
			}
		}
		// Rules
		else if (gameState == 3) {
			if (rulesButton.checkInBounds(mouseX, mouseY)) {
				gameState = 1;
			}
		}
		// End
		else if (gameState == 4) {
			if (endButton.checkInBounds(mouseX, mouseY)) {
				gameState = 1;
				if (music)
					menuMusic.start();
			}
		}
	}
	
	// Main Method
	public static void main (String[] args) {
		// Images
		try {
			// Images
			startMenuImage = ImageIO.read(new File("MenuScreen.png"));
			pauseMenuImage = ImageIO.read(new File("Pause.png"));
			rules = ImageIO.read(new File("Rules.png"));
			background = ImageIO.read(new File("background.png"));
			end = ImageIO.read(new File("GameOver.png"));
			
			onButton = ImageIO.read(new File("onButton.png"));
			offButton = ImageIO.read(new File("offButton.png"));
			
			ball = ImageIO.read(new File("ball.png"));
			bomb = ImageIO.read(new File("bomb.png"));
			life = ImageIO.read(new File("life.png"));
			
			tomatoSplat = ImageIO.read(new File("TomatoSplat.png"));
			stunImage = ImageIO.read(new File("Stun.png"));
			invincibleImage = ImageIO.read(new File("Pretzel.png"));
			
			playerImage = ImageIO.read(new File("PlayerRight-1.png"));
			for (int i = 0; i < 4; i++) {
				playerRight[i] = ImageIO.read(new File("PlayerRight-" + (i+1) + ".png"));
				playerLeft[i] = ImageIO.read(new File("PlayerLeft-" + (i+1) + ".png"));
			}
			
			// Sound
			AudioInputStream sound = AudioSystem.getAudioInputStream(new File ("titlescreen.wav"));
			menuMusic = AudioSystem.getClip();
			menuMusic.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("gamemusic.wav"));
			gameMusic = AudioSystem.getClip();
			gameMusic.open(sound);
			
			sound = AudioSystem.getAudioInputStream(new File ("Boing.wav"));
			bounce = AudioSystem.getClip();
			bounce.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("Damage.wav"));
			hit = AudioSystem.getClip();
			hit.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("Connect.wav"));
			apple = AudioSystem.getClip();
			apple.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("Kick Drum.wav"));
			watermelon = AudioSystem.getClip();
			watermelon.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("Splat.wav"));
			tomato = AudioSystem.getClip();
			tomato.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("Speed.wav"));
			candy = AudioSystem.getClip();
			candy.open(sound);
			sound = AudioSystem.getAudioInputStream(new File ("Invincible.wav"));
			pretzel = AudioSystem.getClip();
			pretzel.open(sound);
		}
		catch (Exception e) {
			System.out.println("Files not found");
		}
		
		// Panel Controls
		JFrame frame = new JFrame("Juggling Jester");
		Main panel = new Main(); 
		frame.add(panel);
		ImageIcon img = new ImageIcon("ball.png");
		frame.setIconImage(img.getImage());
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Menus
		startButtonList.add(new Button(200, 325, 125, 70));
		startButtonList.add(new Button(200, 395, 125, 70));
		startButtonList.add(new Button(325, 325, 170, 70));
		startButtonList.add(new Button(325, 395, 125, 70));
		
		pauseButtonList.add(new Button(175, 125, 300, 100));
		pauseButtonList.add(new Button(175, 275, 300, 100));
		pauseButtonList.add(new Button(175, 425, 300, 100));
		
		menuMusic.start();
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
