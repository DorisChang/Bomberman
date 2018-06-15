//finalProject.java
//Minya Bai & Doris Chang
//Bomberman
import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.io.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.applet.Applet;
import java.applet.AudioClip;

public class finalProject extends JFrame implements ActionListener{
	static final Color SOFT_BLOCK_COLOUR = new Color(120,190,120);
	static final Color HARD_BLOCK_COLOUR = new Color(200,200,200);
	static final Color FLOOR_COLOUR = new Color(150,150,150);
	static final int BALLOM_RANDOM = 0;
	static final int ONIL_NEARBY_FOLLOW = 1;
	static final int DAHL_DUMB = 2;
	static final int MINRO_NEARBY_FOLLOW_FAST = 3;
	static final int DONA_FOLLOW_SMART_SLOW_GHOST = 4;
	static final int OVAPE_GHOST_DUMB = 5;
	static final int PONTAN_FOLLOW_SMART_FAST_GHOST = 6;
	
	public AudioClip titleScreen;
	Timer myTimer;
	Timer powerUpTimer;
	GamePanel game;
	
	public finalProject(){
		super("Bomberman");
		
		setSize(720,690);
		myTimer = new Timer(40,this);
		game = new GamePanel();
		add(game);
		
		titleScreen = Applet.newAudioClip(getClass().getResource("sounds/01_Title Screen.wav")); //import the title screen music
 		titleScreen.loop();

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new GameMenu(this);
		
	}

	public void actionPerformed(ActionEvent e){
		if(game != null){
			game.update();
			game.repaint();
		}
	}

	public static void main(String[]args){
		new finalProject();
	}
	
	public void instructions(){ //used for the instructions screen 
		new InstructMenu(this);
		}
	
	public void start(){ //used to start the game
		titleScreen.stop();
 		myTimer.start();
 		game.update();
 		setVisible(true);
 		}
}


class GameMenu extends JFrame implements KeyListener{ //JFrame for the game/main menu, uses SPACE to continue 
 	private finalProject menu; 
 	private boolean [] keys;
 	
 	public GameMenu(finalProject m){
 		super("Game Menu");
 		
		setSize(720,690);
 		menu = m;
 		
 		ImageIcon back = new ImageIcon("titleScreen.png"); 
 		JLabel backLabel = new JLabel(back);
 		JLayeredPane mPage=new JLayeredPane(); 	
 		mPage.setLayout(null);
 		
 		backLabel.setSize(720,690);
 		backLabel.setLocation(0,0);
 		mPage.add(backLabel,1);				
 			
 		add(mPage);
 		addKeyListener(this);
 		setVisible(true);
 		keys = new boolean[KeyEvent.KEY_LAST+1];
 		setResizable(false);
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		}
    
    public void keyPressed(KeyEvent e){
    	try{
			keys[e.getKeyCode()] = true;
    	}
    	catch(ArrayIndexOutOfBoundsException weirdButton){
    		System.out.println("Weird key pressed");
    	}
			
		if(keys[KeyEvent.VK_SPACE]){ //when SPACE is pressed
			menu.instructions(); //show the instructions 
			setVisible(false);	
			}

		}
	public void keyReleased(KeyEvent e){
		}
	public void keyTyped(KeyEvent e){ 
		}
 	}
 	
class InstructMenu extends JFrame implements KeyListener{ //the instructions menu, uses SPACE to continue
	private finalProject bg;
 	private boolean [] keys;
 	
 	public InstructMenu(finalProject m){
 		super("Instructions");
 		setSize(720,690);
 		bg = m;
 		
 		ImageIcon back = new ImageIcon("Instructions.png");
 		JLabel backLabel = new JLabel(back);
 		JLayeredPane mPage=new JLayeredPane(); 
 		mPage.setLayout(null);
 		
 		backLabel.setSize(720,690);
 		backLabel.setLocation(0,0);
 		mPage.add(backLabel,1);					
 			
 		add(mPage);
 		addKeyListener(this);
 		setVisible(true);
 		keys = new boolean[KeyEvent.KEY_LAST+1];
 		setResizable(false);
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		}
 	
 	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
			
		if(keys[KeyEvent.VK_SPACE]){ //when SPACE is pressed
			bg.start(); //starts the game
			setVisible(false);	
			}
		}
		
	public void keyReleased(KeyEvent e){}
	
	public void keyTyped(KeyEvent e){}
}

class GamePanel extends JPanel implements KeyListener{
	private boolean [] keys;
	private Player p;
	private int mx,sNum;
	private Image back,sBlock;
	private int lives = 3; 
	private int level = 7; 
	private int dropMax; //current level on
	private int points = 0; //points collected in total
	private int displayPoints = 0; //used for gradually displaying the points going up
	private int monstersKilled = 0; //monsters killed in total
	private int blocksDestroyed = 0; //blocks destroyed in total
	private LinkedList<Bomb> bombs = new LinkedList<Bomb>();
	private ArrayList<Bomb> emptyBombs = new ArrayList<Bomb>();
	private Rectangle gate;

	private boolean dropBomb; //if bomb is on screen
	private int bombsLeft;
	private boolean playTheme = false; //used for checking if the music is already playing
	private boolean stageStartMusicOn = false; //used for checking if the music is already playing
	private int newLevelScreen = 80; //used to time how long the new level screen stays on for
	private boolean lifeLostMusicOn = false; //used for checking if the music is already playing
	private boolean gameOver = false; //used to check if the player lost yet
	private boolean gameOverMusicOn = false; //used for checking if the music is already playing
	private boolean wonGame = false; //used to check if the player has won
	private boolean wonGameMusicOn = false; //used for checking if the music is already playing
	
	//grids used for deciding the shortest path
	private Node[][] nodesAll; //contains the hard blocks and soft blocks
	private Node[][] nodesPassSoftBlocks; //contains the hard blocks, used for enemies that can pass through soft blocks

	private ArrayList<Powerup> powerUps = new ArrayList<Powerup>();

	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4;
	private Block grid[][] = new Block [13][27]; //grid used for the whole game, contains the soft blocks, hard blocks, and bombs
	private ArrayList<Monster> monsters = new ArrayList<Monster>(); //contains all the monsters on the current level
	private ArrayList<String> allMonsters = new ArrayList<String>(); //contains all the possible monsters 
	private ArrayList<String> allLevels = new ArrayList<String>(); //contains the specifics of the level (eg. the number of each type of enemy)

	//POWERUPS
	private boolean detonator;// = true;
	
	//IMPORT FONT
	public Font joystixSmall, joystixMedium, joystixLarge;
	
	//IMAGES/SPRITES	
	private Image heart = new ImageIcon("sprites/heart.png").getImage(); 
	private Image gateImage = new ImageIcon("gate.png").getImage();
	private Image[][] bombermanSprites = new Image[4][3];
	private Image[] bombermanDeathSprites = new Image[7];
	
	private Image[] ballomSprites = new Image[6];
	private Image[] onilSprites = new Image[6];
	private Image[] dahlSprites = new Image[6];
	private Image[] minvoSprites = new Image[6];
	private Image[] doriaSprites = new Image[6];
	private Image[] ovapeSprites = new Image[6];
	private Image[] pontanSprites = new Image[6];
	private Image ballomHit = new ImageIcon("sprites/ballomHit.png").getImage();
	private Image onilHit = new ImageIcon("sprites/onilHit.png").getImage();
	private Image dahlHit = new ImageIcon("sprites/dahlHit.png").getImage();
	private Image minvoHit = new ImageIcon("sprites/doriaHit.png").getImage();
	private Image doriaHit = new ImageIcon("sprites/doriaHit.png").getImage();
	private Image ovapeHit = new ImageIcon("sprites/ovapeHit.png").getImage();
	private Image pontanHit = new ImageIcon("sprites/pontanHit.png").getImage();
	private Image[][] monsterDeathSprites = new Image[3][4];
	
	private Image[] bombSprites = new Image[3];
	private Image[][] explosionSprites = new Image[3][8];
	private Image[][] explosionEdgeSprites = new Image[4][8];
	
	public AudioClip titleScreen, stageStart, stageTheme, lifeLost, gameOverMusic, ending, bombExplode;

	public GamePanel(){
		setFocusable(true);
		addKeyListener(this);
		requestFocus();

		detonator = false;
		dropMax = 1;

		back = new ImageIcon("back.png").getImage(); //background of the game when playing
		sBlock = new ImageIcon("soft_block.png").getImage();

		keys = new boolean[KeyEvent.KEY_LAST+1];
		
		//IMPORT FONT
		try{
			joystixSmall = Font.createFont(Font.TRUETYPE_FONT,new File("joystix monospace.ttf")).deriveFont(Font.PLAIN,24);
		}
		catch(Exception ex){
			System.out.println("didn't work");
			ex.printStackTrace();
		}
		joystixMedium = joystixSmall.deriveFont(35F);
		joystixLarge = joystixSmall.deriveFont(40F);
		
    	stageStart = Applet.newAudioClip(getClass().getResource("sounds/02_Stage Start.wav"));
    	stageTheme = Applet.newAudioClip(getClass().getResource("sounds/03_Stage Theme.wav"));
    	lifeLost = Applet.newAudioClip(getClass().getResource("sounds/08_Life Lost.wav"));
    	gameOverMusic = Applet.newAudioClip(getClass().getResource("sounds/09_Game Over.wav"));
    	ending = Applet.newAudioClip(getClass().getResource("sounds/10_Ending.wav"));
    	bombExplode = Applet.newAudioClip(getClass().getResource("sounds/11_Explosion.wav"));

		try{ //reading the monsters file 
			Scanner inFile = new Scanner(new BufferedReader(new FileReader("monsters.txt")));
			while(inFile.hasNextLine()){
				String nextLine = inFile.nextLine();
				allMonsters.add(nextLine);
			}	
		}
		catch(IOException ex){
			System.out.println("monsters.txt not found"); //couldn't find the txt file
		}
		
		try{ //reading the levels file 
			Scanner inFile = new Scanner(new BufferedReader(new FileReader("levels.txt")));
			while(inFile.hasNextLine()){
				String nextLine = inFile.nextLine();
				allLevels.add(nextLine);
			}	
		}
		catch(IOException ex){
			System.out.println("levels.txt not found"); //couldn't find the txt file
		}

		loadSprites();

		startLevel(level);
	}

	public void loadSprites(){
		for(int i=1 ;i<5;i++){
			for(int k=1;k<4;k++){
				bombermanSprites[i-1][k-1] = new ImageIcon("sprites/bomberman"+i+k+".png").getImage();
			}
		}
		for(int i=1; i<7; i++){
			ballomSprites[i-1] = new ImageIcon("sprites/ballom1"+i+".png").getImage();
		}
		for(int i=1; i<7; i++){
			onilSprites[i-1] = new ImageIcon("sprites/onil"+i+".png").getImage();
		}
		for(int i=1; i<7; i++){
			dahlSprites[i-1] = new ImageIcon("sprites/dahl"+i+".png").getImage();
		}
		for(int i=1; i<7; i++){
			minvoSprites[i-1] = new ImageIcon("sprites/minvo"+i+".png").getImage();
		}
		for(int i=1; i<7; i++){
			doriaSprites[i-1] = new ImageIcon("sprites/doria"+i+".png").getImage();
		}
		for(int i=1; i<7; i++){
			ovapeSprites[i-1] = new ImageIcon("sprites/ovape"+i+".png").getImage();
		}
		for(int i=1; i<7; i++){
			pontanSprites[i-1] = new ImageIcon("sprites/pontan"+i+".png").getImage();
		}
		for(int i=1; i<4; i++){
			bombSprites[i-1] = new ImageIcon("sprites/bomb"+i+".png").getImage();
		}
		for(int i=1 ; i<4;i++){
			for(int k=1; k<5;k++){
				explosionSprites[i-1][k-1] = new ImageIcon("sprites/explosion"+(i-1)+k+".png").getImage();
			}
		}
		for(int i=1; i<4; i++){
			for(int k=5; k<9; k++){
				explosionSprites[i-1][k-1] = new ImageIcon("sprites/explosion"+(i-1)+(9-k)+".png").getImage(); 
			}
		}
		for(int i=1 ; i<5;i++){
			for(int k=1; k<5;k++){
				explosionEdgeSprites[i-1][k-1] = new ImageIcon("sprites/explosionEdge"+i+k+".png").getImage();
			}
		}
		for(int i=1; i<5; i++){
			for(int k=5; k<9; k++){
				explosionEdgeSprites[i-1][k-1] = new ImageIcon("sprites/explosionEdge"+i+(9-k)+".png").getImage(); 
			}
		}
		for(int i=1 ; i<4;i++){
			for(int k=1; k<5;k++){
				monsterDeathSprites[i-1][k-1] = new ImageIcon("sprites/monsterDeath"+i+k+".png").getImage();
			}
		}
		for(int i=1; i<8; i++){
			bombermanDeathSprites[i-1] = new ImageIcon("sprites/bombermanDeath"+i+".png").getImage();
		}		
	}

	public void startLevel(int level){
		p = new Player();

		powerUps.clear();
		bombs.clear();
		monsters.clear();
		
		newLevelScreen = 80;
		stageStartMusicOn = false;
		lifeLostMusicOn = false;

		newGrid(grid);
		addSoftBlocks(50);
		
		mx = 0; 

		dropBomb = true;
		bombsLeft = dropMax;
		
		//get the specifics of this level and add the monsters accordingly
		String [] infoList = allLevels.get(level-1).split(",");
		for(int n = 0; n<7; n++){
			for(int i = 0; i<Integer.parseInt(infoList[n]); i++){
				addMonster(n);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g){
		if(!gameOver && !wonGame){
			if(newLevelScreen>0){
				g.setColor(new Color(0,0,0));
				g.fillRect(0,0,720,690);
				g.setColor(new Color(250,250,250));
				g.setFont(joystixMedium);
				String levelMessage = "LEVEL " + level;
				g.drawString(levelMessage,260,300);
				
				if(stageStartMusicOn == false){
					stageStart.play();
					stageStartMusicOn = true;
					}
				 }
			else{
				g.drawImage(back,mx,0,this);
				g.setColor(new Color(0,0,255));
				

				System.out.printf("Gate rect X: "+(int)(gate.getX()/45)+" Gate rect Y: "+(int)(gate.getY()/45)+"\n");
				g.drawImage(gateImage,(int)(gate.getX()+mx),(int)(gate.getY()),this);

				for(int j = 0; j < powerUps.size(); j ++){
					Rectangle r = powerUps.get(j).getRect();
					g.drawRect((int)r.getX()+mx,(int)r.getY(),37,37);
				}

				if(dropBomb){
					for(Bomb b : bombs){
						if(b!= null){
							if(b.getDT() >= 0){
								g.drawImage(bombSprites[(b.getSpriteCounter()/5)%3],(int)(b.getBX()*45+mx+4),(int)(b.getBY()*45+69),this);
							}

							if(b.getET() >= 0 && b.getDT() == 0){
								int r = b.getRange();
								//System.out.println("Explode!");
								g.drawImage(explosionSprites[0][b.getExplosionStage()],(int)(b.getBX()*45+mx),(int)(b.getBY()*45+65),this);
								//g.setColor(new Color(255,100,0));
								//g.fillRect((int)b.getBombRect().getX()*45+mx,(int)b.getBombRect().getY()*45+65,(int)b.getBombRect().getWidth(),(int)b.getBombRect().getHeight());

								for(int i = 1; i < r+1; i++){
									if(b.isOccupied(0,i-1) == false){ //LEFT
										//g.fillRect(b.getBX()*45-45*i+mx,b.getBY()*45+65+10,45,25);
										if(i!=r){
											g.drawImage(explosionSprites[1][b.getExplosionStage()],(int)((b.getBX()-i)*45+mx),(int)(b.getBY()*45+65),this);
										}
										else{
											g.drawImage(explosionEdgeSprites[1][b.getExplosionStage()],(int)((b.getBX()-i)*45+mx),(int)(b.getBY()*45+65),this);
											}
										
									}

									if(b.isOccupied(1,i-1) == false){ //RIGHT
										//g.fillRect(b.getBX()*45+45*i+mx,b.getBY()*45+65+10,45,25);
										//g.drawImage(explosionSprites[1][b.getExplosionStage()],(int)((b.getBX()+i)*45+mx),(int)(b.getBY()*45+65),this);
										if(i!=r){
											g.drawImage(explosionSprites[1][b.getExplosionStage()],(int)((b.getBX()+i)*45+mx),(int)(b.getBY()*45+65),this);
										}
										else{
											g.drawImage(explosionEdgeSprites[0][b.getExplosionStage()],(int)((b.getBX()+i)*45+mx),(int)(b.getBY()*45+65),this);
											}
									}

									if(b.isOccupied(2,i-1) == false){ //UP
										//g.fillRect(b.getBX()*45+mx+10,(int)b.getBY()*45+65-45*i,25,45);
										//g.drawImage(explosionSprites[2][b.getExplosionStage()],(int)(b.getBX()*45+mx),(int)((b.getBY()-i)*45+65),this);	
										if(i!=r){
											g.drawImage(explosionSprites[2][b.getExplosionStage()],(int)(b.getBX()*45+mx),(int)((b.getBY()-i)*45+65),this);
										}
										else{
											g.drawImage(explosionEdgeSprites[2][b.getExplosionStage()],(int)(b.getBX()*45+mx),(int)((b.getBY()-i)*45+65),this);
											}
									}
									
									if(b.isOccupied(3,i-1) == false){ //DOWN
										//g.fillRect(b.getBX()*45+mx+10,(int)b.getBY()*45+65+45*i,25,45);
										//g.drawImage(explosionSprites[2][b.getExplosionStage()],(int)(b.getBX()*45+mx),(int)((b.getBY()+i)*45+65),this);
										if(i!=r){
											g.drawImage(explosionSprites[2][b.getExplosionStage()],(int)(b.getBX()*45+mx),(int)((b.getBY()+i)*45+65),this);
										}
										else{
											g.drawImage(explosionEdgeSprites[3][b.getExplosionStage()],(int)(b.getBX()*45+mx),(int)((b.getBY()+i)*45+65),this);
										}	
									}
								}
							}
						}
					}
				}
				
				if(p.getState()){
					g.drawImage(bombermanSprites[p.getDirection()-1][(p.getSpriteCounter()/2)%3],p.getX()-7,p.getY()-7,this);
					}
				else{
					g.drawImage(bombermanDeathSprites[p.getDeathStage()],p.getX()-7,p.getY()-7,this);
					}
		
				for(int r=0; r<13; r++){ //row
					for(int c=0; c<27; c++){ //column
						if(grid[r][c] != null){
							if((grid[r][c]).getType()==2){
								g.drawImage(sBlock,c*45+mx,r*45+65,this);
							}
						}	
					}
				}
				
				//DRAWING MONSTERS aka enemies
				for(Monster m : monsters){
					if(m.getState()){ //if the monster is alive
						if(m.getType().equals("ballom")){
							g.drawImage(ballomSprites[(m.getSpriteCounter()/2)%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						}
						else if(m.getType().equals("onil")){
							g.drawImage(onilSprites[(m.getSpriteCounter()/2)%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						}
						else if(m.getType().equals("dahl")){
							g.drawImage(dahlSprites[(m.getSpriteCounter()/2)%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						}
						else if(m.getType().equals("minvo")){
							g.drawImage(minvoSprites[(m.getSpriteCounter()/2)%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						}
						else if(m.getType().equals("doria")){
							g.drawImage(doriaSprites[(m.getSpriteCounter()/2)%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						}
						else if(m.getType().equals("ovape")){
							g.drawImage(ovapeSprites[(m.getSpriteCounter()/2)%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						}
						else if(m.getType().equals("pontan")){
							g.drawImage(pontanSprites[(m.getSpriteCounter()/2)%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						}
					}
					else if(!m.getState()){ //if the monster has been bombed
						m.deathTimerTick(); //keep counting down, used for displaying the monster's death
						if(m.getDeathStage()<5){
							if(m.getType().equals("ballom")){
								if(m.getDeathStage()==0){ //if in the first phase of death, show the alarmed sprite
									g.drawImage(ballomHit,(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
								else{ //if in later phases, show the shrinking sprites
									g.drawImage(monsterDeathSprites[0][m.getDeathStage()-1],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
							}
							else if(m.getType().equals("onil")){
								if(m.getDeathStage()==0){
									g.drawImage(onilHit,(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
								else{
									g.drawImage(monsterDeathSprites[1][m.getDeathStage()-1],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
							}
							else if(m.getType().equals("dahl")){
								if(m.getDeathStage()==0){
									g.drawImage(dahlHit,(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
								else{
									g.drawImage(monsterDeathSprites[2][m.getDeathStage()-1],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
							}
							else if(m.getType().equals("minvo")){
								if(m.getDeathStage()==0){
									g.drawImage(minvoHit,(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
								else{
									g.drawImage(monsterDeathSprites[0][m.getDeathStage()-1],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
							}
							else if(m.getType().equals("doria")){
								if(m.getDeathStage()==0){
									g.drawImage(doriaHit,(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
								else{
									g.drawImage(monsterDeathSprites[1][m.getDeathStage()-1],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
							}
							else if(m.getType().equals("ovape")){
								if(m.getDeathStage()==0){
									g.drawImage(ovapeHit,(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
								else{
									g.drawImage(monsterDeathSprites[2][m.getDeathStage()-1],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
							}
							else if(m.getType().equals("pontan")){
								if(m.getDeathStage()==0){
									g.drawImage(pontanHit,(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
								else{
									g.drawImage(monsterDeathSprites[2][m.getDeathStage()-1],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
								}
							}	
						}
					}
				}
				
				//DISPLAY LIVES LEFT AND POINTS EARNED
				for(int n = 0; n<lives-1; n++){
					g.drawImage(heart,10+n*40,15,this);
					} 
						
				g.setColor(new Color(0,0,0));
				
				if(displayPoints<points){ //used to increase the points over a period of time
					displayPoints+=100;
					}
				g.setFont(joystixSmall);
				String pointsMessage = "POINTS: " + displayPoints;
				g.drawString(pointsMessage,230,40);
			}
		}
		
		else if(gameOver){ //if the player lost all 3 lives, show game over screen
			g.setColor(new Color(0,0,0));
			g.fillRect(0,0,720,690);
			g.setColor(new Color(250,250,250));
			g.setFont(joystixLarge);
			g.drawString("GAME OVER",210,300);
			g.setFont(joystixSmall);
			g.drawString("PRESS [SPACE] TO RESTART",120,600);
			}
			
		else if(wonGame){ //if the player passed all levels, show 'You Finished!' screen with their stats
			g.setColor(new Color(0,0,0));
			g.fillRect(0,0,720,690);
			g.setColor(new Color(250,250,250));
			g.setFont(joystixLarge);
			g.setFont(joystixLarge);
			g.drawString("YOU FINISHED!",140,100);
			String pointsText = String.format("%1$-16s %2$16d","TOTAL POINTS:",points); //format the string to be aligned
			g.setFont(joystixSmall);
			g.drawString(pointsText,20,300);
			String livesText = String.format("%1$-16s %2$16d","LIVES LEFT:",lives);
			g.drawString(livesText,20,350);
			String monstersText = String.format("%1$-16s %2$16d","MONSTERS KILLED:",monstersKilled);
			g.drawString(monstersText,20,400);
			String blocksText = String.format("%1$-16s %2$16d","WALLS DESTROYED:",blocksDestroyed);
			g.drawString(blocksText,20,450);
			g.setFont(joystixSmall);
			g.drawString("PRESS [SPACE] TO RESTART",120,600);
			}
	}
	
	public void update(){ //used to continuously refresh the game
		if(!gameOver && !wonGame && p.getState()){
			if(newLevelScreen<=0 && playTheme == false && p.getState()){
				stageTheme.loop();
				playTheme = true;
			}
				
			newLevelScreen -= 1; //count down the level screen timer
			
			if(newLevelScreen<0){
				moveMan();
		
				if(playerHitMonster()){
					stageTheme.stop();
					playTheme = false;
					lifeLost.play();
					p.setState(false); //player has died
				}

				for(Bomb b : bombs){
					if(b.getET() >= 0 && b.getDT() == 0){
						for(int i = 0; i < 4; i++){
							if(b.getExpRect(i) != null){
								bombPlayer(b.getExpRect(i));
								bombHitMonster(b.getExpRect(i));
							}
						}
					}
				}
				
				for(Monster m:monsters){
					if(m.getState()){
						moveMonster(m,m.getPath());
					}
				}

				ArrayList<Powerup> pGrabbed = new ArrayList<Powerup>();

				for(Powerup q : powerUps){
					//System.out.printf("PowerUp rect X: "+(int)(q.getRect().getX()/45)+" PowerUp rect Y: "+(int)(q.getRect().getY()/45)+"\n");
					if(p.getActualRect(mx).intersects(q.getRect())){
						if(q.getType() == 0){
							dropMax += 1;
							pGrabbed.add(q);
						}
					}
				}

				for(Powerup q : pGrabbed){
					if(q != null){
						powerUps.remove(q);
					}
				}

				//System.out.println("DROP MAX: "+dropMax);
		
				if(dropBomb){ //if a bomb has been dropped
					for(Bomb b : bombs){ //go through each bomb
						if(b != null){ //if there is a bomb
							int bx = b.getBX(); //get the x in the grid
							int by = b.getBY(); //gets y in grid
							
							if(detonator == false){ //if you cannot control when the bomb goes off
								b.detonate(); //start timer
								//b.noActiveBombs(); //all directions are blocked, bomb is useless take off screen
							}

							if(detonator && b.getDT() == 0 && b.getET() >= 0){ //if you can control when bomb goes off
								b.detonate(); //start timer (skips right to explosion time)
								//b.noActiveBombs();
							}

							int r = b.getRange(); //gets the max range the bomb at reach

							if(b.getET() >= 0 && b.getDT() == 0){ //if it is exploding
								for(int i = 1; i < r+1 ; i++){ //checks each layer from the bomb
									if(bx - i >= 0){ //still in screen
										if(grid[by][bx-i] != null){ //if there is another block there
											b.blockPresent(0,i-1); //everything after that block is no-no

											if(grid[by][bx-i].getType() == 2){ //if it is a soft block
												if(i > 1 && b.isOccupied(0,i-2) == false){ //check if the previous block is open
													grid[by][bx-i] = null; //that block is now empty
													blocksDestroyed++;
													nodesAll[bx-i][by] = new Node(bx-i, by, true,10); //let a smart enemy know there's nothing there now
												}

												else if(i == 1){ //if it is the spot right next to bomb (there is no previous block to check)
													grid[by][bx-i] = null; //that block is now empty
													blocksDestroyed++;
													nodesAll[bx-i][by] = new Node(bx-i, by, true,10); //let a smart enemy know there is nothing there now
												}
											}

											else if(grid[by][bx-i].getType() == 3){ //if there is another bomb there
												findBomb(bx-i,by).explosionTriggered(); //trigger the bomb there
											}
										}
									}

									if(bx + i <= 26){
										if(grid[by][bx+i] != null){
											b.blockPresent(1,i-1);

											if(grid[by][bx+i].getType() == 2){
												if(i > 1 && b.isOccupied(1,i-2) == false){
													grid[by][bx+i] = null;
													blocksDestroyed++;
													nodesAll[bx+i][by] = new Node(bx+i, by, true,10);
												}

												else if(i == 1){
													grid[by][bx+i] = null;
													blocksDestroyed++;
													nodesAll[bx+i][by] = new Node(bx+i, by, true,10);
												}
											}

											else if(grid[by][bx+i].getType() == 3){
												findBomb(bx+i,by).explosionTriggered();
											}
										}
									}

									if(by - i >= 0){
										if(grid[by-i][bx] != null){
											b.blockPresent(2,i-1);

											if(grid[by-i][bx].getType() == 2){
												if(i > 1 && b.isOccupied(2,i-2) == false){
													grid[by-i][bx] = null;
													blocksDestroyed++;
													nodesAll[bx][by-i] = new Node(bx, by-i, true,10);
												}

												else if(i == 1){
													grid[by-i][bx] = null;
													blocksDestroyed++;
													nodesAll[bx][by-i] = new Node(bx, by-i, true,10);
												}
											}

											else if(grid[by-i][bx].getType() == 3){
												findBomb(bx,by-i).explosionTriggered();
											}
										}
									}

									if(by + i <= 12){
										if(grid[by+i][bx] != null){
											b.blockPresent(3,i-1);

											if(grid[by+i][bx].getType() == 2){
												if(i > 1 && b.isOccupied(3,i-2) == false){
													grid[by+i][bx] = null;
													blocksDestroyed++;
													nodesAll[bx][by+i] = new Node(bx, by+i, true,10);
												}

												else if(i == 1){
													grid[by+i][bx] = null;
													blocksDestroyed++;
													nodesAll[bx][by+i] = new Node(bx, by+i, true,10);
												}
											}

											else if(grid[by+i][bx].getType() == 3){
												findBomb(bx,by+i).explosionTriggered();
											}
										}
									}
								}

								b.explode();
							}

							if(b.getStatus()){
								emptyBombs.add(b);
							}
						}
					}



					for(Bomb b : emptyBombs){
						if(b != null){
							int bx = b.getBX();
							int by = b.getBY();

							grid[by][bx] = null;
							blocksDestroyed++;

							bombs.remove(b);

							bombsLeft += 1;
						}
					}

					emptyBombs.clear();
				}
		
				if(foundDoor()==true && level<7){
					level+=1;
					stageTheme.stop(); 
					playTheme = false;
					startLevel(level);
				}
				else if(foundDoor()==true && level ==7){
					wonGame = true;
					stageTheme.stop();
					playTheme = false;
					ending.play();
					}
		
				if(bombsLeft == 3){
					dropBomb = false;
				}
			}
		}
		
		else if(!p.getState()){
					p.deathTimerTick();
				}
				
				if(!p.getState() && lifeLostMusicOn == false){
					lifeLost.play();
					lifeLostMusicOn = true;
					}
					
				if(p.getDeathTimer()<=0){
					if(lives>0){
						startLevel(level);
					}
					else{
						gameOver = true;
					}
				}
		
		else if(gameOver){ //game over
			if(!gameOverMusicOn){
				gameOverMusic.play();
				gameOverMusicOn = true;		
			}	
		}
		
		if(gameOver && keys[KeyEvent.VK_SPACE] || wonGame && keys[KeyEvent.VK_SPACE]){ //restart game from level 1
			level = 1;
			lives = 3;
			points = 0;
			displayPoints = 0;
			monstersKilled = 0; 
			blocksDestroyed = 0; 
			gameOver = false;
			gameOverMusic.stop();
			lifeLost.stop();
			ending.stop();
			wonGame = false;
			lifeLostMusicOn = false;
			gameOverMusicOn = false;	
			startLevel(level);
			}
	}

	public Bomb findBomb(int x, int y){
		for(Bomb b : bombs){
			if(b.getBX() == x && b.getBY() == y){
				return b;
			}
		}

		return null;
	}

	public void bombPlayer(Rectangle r){

		Rectangle pRect = p.getActualRect(mx);
		Rectangle rect = new Rectangle((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());

		if(p.getState() && pRect.intersects(rect)){
			//System.out.println("BOO");
			lives-=1;
			//bombs.clear();

			dropBomb = false;
			stageTheme.stop();
			playTheme = false;
			//screenFreeze = 80;
			//if(lives>0){
				p.setState(false);
				//startLevel(level);
			//}
			/*else{
				p.setState(false);
				//gameOver = true;
			}*/
		}
	}

	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e){}
	
	public Boolean hitBlock(int direction){ //ensures that the player stays on the valid path (don't walk across blocks)
		int gX = (int)(Math.round(p.getX()+15.5-mx)/45); //closest column blocks
		int gY = (int)(Math.round(p.getY()+15.5-65)/45); //closest row
		
		if(direction == RIGHT){
			Rectangle bRect = p.getRightRect(mx);

			if(grid[gY][gX+1] != null){ //if there is a block to the right
				Rectangle r = (grid[gY][gX+1]).getRect(); //get rect of that block
	
				if(bRect.intersects(r)){ //if they rectangles intersect, then player will hit the wall and therefore is not a valid move
					return true;
				}
			}
			
			//check the other two blocks on the right above and below the previous block checked (ensures that the player is in the middle of the aisles, without any part on the blocks)
			Rectangle r2 = new Rectangle(45*(gX+1),45*(gY+1)+65,45,45); 
			if(bRect.intersects(r2)){
				return true;
			}
			
			Rectangle r3 = new Rectangle(45*(gX+1),45*(gY-1)+65,45,45);
			if(bRect.intersects(r3)){
				return true;
			}
		}

		else if(direction == LEFT){
			Rectangle bRect = p.getLeftRect(mx);
			
			if(grid[gY][gX-1] != null){
				Rectangle r = (grid[gY][gX-1]).getRect();

				if(bRect.intersects(r)){
					return true;
				}
			}
			Rectangle r2 = new Rectangle(45*(gX-1),45*(gY+1)+65,45,45); 
			if(bRect.intersects(r2)){
				return true;
			}
			
			Rectangle r3 = new Rectangle(45*(gX-1),45*(gY-1)+65,45,45);
			if(bRect.intersects(r3)){
				return true;
			}
		}

		else if(direction == UP){
			Rectangle bRect = p.getUpRect(mx);
			
			if(grid[gY-1][gX] != null){
				Rectangle r = (grid[gY-1][gX]).getRect();

				if(bRect.intersects(r)){
					return true;
				}
			}
			Rectangle r2 = new Rectangle(45*(gX+1),45*(gY-1)+65,45,45); 
			if(bRect.intersects(r2)){
				return true;
			}
			
			Rectangle r3 = new Rectangle(45*(gX-1),45*(gY-1)+65,45,45);
			if(bRect.intersects(r3)){
				return true;
			}
		}

		else if(direction == DOWN){
			Rectangle bRect = p.getDownRect(mx);
			
			if(grid[gY+1][gX] != null){
				Rectangle r = (grid[gY+1][gX]).getRect();

				if(bRect.intersects(r)){
					return true;
				}
			}
			Rectangle r2 = new Rectangle(45*(gX+1),45*(gY+1)+65,45,45); 
			if(bRect.intersects(r2)){
				return true;
			}
			
			Rectangle r3 = new Rectangle(45*(gX-1),45*(gY+1)+65,45,45);
			if(bRect.intersects(r3)){
				return true;
			}
		}

		return false;
	}

	public void moveMan(){
		if(keys[KeyEvent.VK_RIGHT]){
			if(hitBlock(RIGHT)){
				p.moveRight(0);
			}
			else{
				//System.out.println("YO");
				if(p.getX() > 225 && mx > -495){
					mx -= 3;
					p.setDirection(RIGHT);
					p.addToSpriteCounter();
				}

				else{
					p.moveRight(3);
				}
			}
		}
		else if(keys[KeyEvent.VK_LEFT]){

			if(hitBlock(LEFT) == false){
				if(p.getX() < 235 && mx < 0){
					mx += 3;
					p.setDirection(LEFT);
					p.addToSpriteCounter();
				}

				else{
					p.moveLeft(3);
				}
			}

			else if(hitBlock(LEFT) == true){
				p.moveLeft(0);
			}
		}
		else if(p.getY()>=118 && keys[KeyEvent.VK_UP]){
			if(hitBlock(UP) == false){
				p.moveUp(3);
			}

			else if(hitBlock(UP) == true){
				p.moveUp(0);
			}
		}
		else if(keys[KeyEvent.VK_DOWN]){
			if(hitBlock(DOWN) == false){
				p.moveDown(3);
			}

			else if(hitBlock(DOWN) == true){
				p.moveDown(0);
			}
		}

		if(keys[KeyEvent.VK_X]){
			int bx = (int)(Math.round(p.getX()+15.5-mx)/45); //closest column blocks
			int by = (int)(Math.round(p.getY()+15.5-60)/45); //closest row

			if(grid[by][bx] == null){
				System.out.println("BOMBS LEFT: "+bombsLeft);
				if(bombsLeft > 0){ //POWERUP EXCEPTION: if(dropBomb == false && multibomb == false) >> for multibomb, cant put two bombs same spot
					Bomb b = new Bomb(bx,by);

					grid[by][bx] = new Block(bx,by,3); //bomb is 3
					dropBomb = true;

					bombs.add(b);

					if(detonator == false){
						b.detonate();
					}

					bombsLeft -= 1;
					
				}
			}
		}

		if(keys[KeyEvent.VK_Z]){
			if(detonator){
				System.out.println("DETONATE!");
				if(bombsLeft < 3){
					System.out.println("BOMBS!");
					for(Bomb b:bombs){
						System.out.println("EXPLODE");
						b.explosionTriggered();
					}
				}
			}
		}
	}
	
	//MONSTERS 
	public void addMonster(int type){ //add a monster of the specified type
		Random rand = new Random();
		int randX = rand.nextInt(22)+3; //randomly generated X 
		int randY = rand.nextInt(8)+3; //randomly generated Y
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(0);
		while(grid[randY][randX]!=null){
			randX = rand.nextInt(22)+3; //randomly generated X 
			randY = rand.nextInt(8)+3; //randomly generated Y
		}

		Monster m = new Monster(allMonsters.get(type)+","+(randX*45+7)+","+(randY*45+72),path,mx);
		monsters.add(m); 
		if(!m.getType().equals("dahl")){
			m.setCurrentDirection(validRandomDirection(m));
		}
		else{ //dahl has its special movements (prefers to move horizontally)
			m.setCurrentDirection(validDahlDirection(m));
		}
	}
	
	public int validDahlDirection(Monster m){ //finds all valid directions for dahl to go, giving preferance to horizontal directions
		ArrayList<Integer> v = new ArrayList<Integer>(); //contains all valid directions
		for(int d = 1; d<3; d++){
			if(hitBlock(m,d)==false){
				v.add(d);
			}
		}
		
		if(v.size()>0){
			Random rand = new Random();
			int randIndex = rand.nextInt(v.size());
			return v.get(randIndex);
		}
			
		else{
			for(int d = 3; d<5; d++){
				if(hitBlock(m,d)==false){
					v.add(d);
				}
			}
			if(v.size()>0){
				Random rand = new Random();
				int randIndex = rand.nextInt(v.size());
				return v.get(randIndex);
			}	
		}
		return 0;
	}
	
	public int validRandomDirection(Monster m){ //finds all valid directions for the monster to go and return a random one
		ArrayList<Integer> v = new ArrayList<Integer>(); //contains all valid directions
		for(int d = 1; d<5; d++){
			if(hitBlock(m,d)==false){
				v.add(d);
			}
		}
		
		if(v.size()==0){
			return 0;
		}
			
		else{
			Random rand = new Random();
			int randIndex = rand.nextInt(v.size());
			return v.get(randIndex);
		}
	}
	
	public Boolean hitBlock(Monster m,int direction){ //returns whether the monster will hit a block
		int gX = (int)(Math.round(m.getX()+15.5)/45); //closest column blocks
		int gY = (int)(Math.round(m.getY()+15.5-65)/45); //closest row
		
		if(direction == RIGHT){
			Rectangle bRect = m.getRightRect();
			
			if(m.getType().equals("ovape") && grid[gY][gX+1]!=null && grid[gY][gX+1].getType()==1 || !m.getType().equals("ovape") && grid[gY][gX+1]!=null){ //if there is a block there, ovape is able to pass through soft blocks 
				Rectangle r = (grid[gY][gX+1]).getRect(); //get rect of that block
				if(bRect.intersects(r)){
					return true;
				}
			}
			
			Rectangle r2 = new Rectangle(45*(gX+1),45*(gY+1)+65,45,45); 
			if(bRect.intersects(r2)){
				return true;
			}
			
			Rectangle r3 = new Rectangle(45*(gX+1),45*(gY-1)+65,45,45);
			if(bRect.intersects(r3)){
				return true;
			}
		}

		else if(direction == LEFT){
			Rectangle bRect = m.getLeftRect();
			
			if(m.getType().equals("ovape") && grid[gY][gX-1]!=null && grid[gY][gX-1].getType()==1 || !m.getType().equals("ovape") && grid[gY][gX-1]!=null){
				Rectangle r = (grid[gY][gX-1]).getRect();

				if(bRect.intersects(r)){
					return true;
				}
			}
			Rectangle r2 = new Rectangle(45*(gX-1),45*(gY+1)+65,45,45); 
			if(bRect.intersects(r2)){
				return true;
			}
			
			Rectangle r3 = new Rectangle(45*(gX-1),45*(gY-1)+65,45,45);
			if(bRect.intersects(r3)){
				return true;
			}
		}

		else if(direction == UP){
			Rectangle bRect = m.getUpRect();
			if(m.getType().equals("ovape") && grid[gY-1][gX]!=null && grid[gY-1][gX].getType()==1 || !m.getType().equals("ovape") && grid[gY-1][gX]!=null){
				Rectangle r = (grid[gY-1][gX]).getRect();
				if(bRect.intersects(r)){
					return true;
				}
			}
			Rectangle r2 = new Rectangle(45*(gX+1),45*(gY-1)+65,45,45); 
			if(bRect.intersects(r2)){
				return true;
			}
			
			Rectangle r3 = new Rectangle(45*(gX-1),45*(gY-1)+65,45,45);
			if(bRect.intersects(r3)){
				return true;
			}
		}

		else if(direction == DOWN){
			Rectangle bRect = m.getDownRect();
			
			if(m.getType().equals("ovape") && grid[gY+1][gX]!=null && grid[gY+1][gX].getType()==1 || !m.getType().equals("ovape") && grid[gY+1][gX]!=null){
				Rectangle r = (grid[gY+1][gX]).getRect();

				if(bRect.intersects(r)){
					return true;
				}
			}
			Rectangle r2 = new Rectangle(45*(gX+1),45*(gY+1)+65,45,45); 
			if(bRect.intersects(r2)){
				return true;
			}
			
			Rectangle r3 = new Rectangle(45*(gX-1),45*(gY+1)+65,45,45);
			if(bRect.intersects(r3)){
				return true;
			}
		}
		return false;
	}

	public void randomMovement(Monster m){
		int gX = (int)(Math.round(m.getX()+15.5)/45); //closest column blocks
		int gY = (int)(Math.round(m.getY()+15.5-65)/45); //closest row
		ArrayList<Integer> v = new ArrayList<Integer>(); 
		
		if(gX%2==1 && gY%2==1){
			if(m.getCurrentDirection()==LEFT || m.getCurrentDirection()==RIGHT){ //if monster is going left/right
				if(!hitBlock(m,UP)){
					v.add(UP);
				}
				if(!hitBlock(m,DOWN)){
					v.add(DOWN);
				}
			}
			else if(m.getCurrentDirection()==UP || m.getCurrentDirection()==DOWN){ //if monster is going up/down
				if(!hitBlock(m,LEFT)){
					v.add(LEFT);
				}
				if(!hitBlock(m,RIGHT)){
					v.add(RIGHT);
				}
			}
		}
			
		if(v.size()>0){
			Random rand = new Random();
			int randIndex = rand.nextInt(v.size());
			m.setCurrentDirection(v.get(randIndex));
		}
			
		else if(hitBlock(m,m.getCurrentDirection())){
			m.setCurrentDirection(validRandomDirection(m));
		}
		
		else if(m.getCurrentDirection()==0 && validRandomDirection(m)!=0){
			m.setCurrentDirection(validRandomDirection(m));
		}

		m.moveStraight(m.getSpeed());
	}
	
	public void dahlMovement(Monster m){
		int gX = (int)(Math.round(m.getX()+15.5)/45); //closest column blocks
		int gY = (int)(Math.round(m.getY()+15.5-65)/45); //closest row
		ArrayList<Integer> v = new ArrayList<Integer>(); 
		
		if(gX%2==1 && gY%2==1){
			if(m.getCurrentDirection()==UP || m.getCurrentDirection()==DOWN){ //if monster is going up/down
				if(!hitBlock(m,LEFT)){
					v.add(LEFT);
				}
				if(!hitBlock(m,RIGHT)){
					v.add(RIGHT);
				}
			}
			
		}
			
		if(v.size()>0){
			Random rand = new Random();
			int randIndex = rand.nextInt(v.size());
			m.setCurrentDirection(v.get(randIndex));
		}
			
		else if(hitBlock(m,m.getCurrentDirection())){
			m.setCurrentDirection(validDahlDirection(m));
		}
		
		else if(m.getCurrentDirection()==0 && validDahlDirection(m)!=0){
			m.setCurrentDirection(validDahlDirection(m));
		}

		m.moveStraight(m.getSpeed());
	}

	public void targetMovement(Monster m, Node[][] nodes){ //used for monsters that follow the shortest path to the player
		if(((int)(m.getX())-7)%45<2 && ((int)(m.getY())-7-65)%45<2){ //find the shortest path and set it to be the current path
			m.setPath(getArray(m,findPath(nodes,(int)(Math.round(m.getX()+15.5)/45),(int)(Math.round(m.getY()+15.5-65)/45),(int)(Math.round(p.getX()-mx+15.5)/45),(int)(Math.round(p.getY()+15.5-65)/45))));
		}
			
		if(m.getPath().size()>0){ //if there is a path the monster should follow
			int nextInstruction = m.getPath().get(0); //gets the next direction the monster should head to
			if(nextInstruction==RIGHT){
				m.setCurrentDirection(RIGHT);
			}
			else if(nextInstruction==LEFT){
				m.setCurrentDirection(LEFT);
			}
			else if(nextInstruction==UP){
				m.setCurrentDirection(UP);
			}
			else if(nextInstruction==DOWN){
				m.setCurrentDirection(DOWN);
			}
			
			m.moveStraight(m.getSpeed()); //move in the specified direction
			
			//System.out.printf("Monster X: %d  Monster Y: %d\n",(int)(Math.round(m.getX()+15.5)/45),(int)(Math.round(m.getY()+15.5-65)/45));
			if((int)((m.getY()-72)%45)==0 && (int)((m.getX()-7))%45==0){ //if a direction was followed, remove it 
				m.pathRemoveFirst();
			}
		}
		
		if(m.getPath().size()==0){ //if there is no path the monster can follow that will lead to the player, move randomly
			int gX = (int)(Math.round(m.getX()+15.5)/45); //closest column blocks
			int gY = (int)(Math.round(m.getY()+15.5-65)/45); //closest row
			ArrayList<Integer> v = new ArrayList<Integer>(); 
			
			if(gX%2==1 && gY%2==1){
				if(m.getCurrentDirection()==LEFT || m.getCurrentDirection()==RIGHT){ //if monster is going left/right
					if(!hitBlock(m,UP)){
						v.add(UP);
					}
					if(!hitBlock(m,DOWN)){
						v.add(DOWN);
					}
				}
			else if(m.getCurrentDirection()==UP || m.getCurrentDirection()==DOWN){ //if monster is going up/down
				if(!hitBlock(m,LEFT)){
					v.add(LEFT);
				}
				if(!hitBlock(m,RIGHT)){
					v.add(RIGHT);
				}
			}
		}
					
		if(v.size()>0){
			Random rand = new Random();
			int randIndex = rand.nextInt(v.size());
			m.setCurrentDirection(v.get(randIndex));
		}
			
		else if(hitBlock(m,m.getCurrentDirection())){
			m.setCurrentDirection(validRandomDirection(m));
		}
		
		else if(m.getCurrentDirection()==0 && validRandomDirection(m)!=0){
			m.setCurrentDirection(validRandomDirection(m));
			}

		m.moveStraight(m.getSpeed());
		}
	}
	
	public void moveMonster(Monster m, ArrayList<Integer> path){ //takes in the monster and path the monster should take
		if(m.getType().equals("ballom")){ //moves randomly, can not pass through bombs
			m.addToSpriteCounter();
			randomMovement(m);
		}
		 
		else if(m.getType().equals("dahl")){ //prefers to move horizontally, can not pass through bombs
			m.addToSpriteCounter();
			dahlMovement(m);
			}
		
		else if(m.getType().equals("onil") || m.getType().equals("minvo")){ //these monsters move randomly until they are close to the player
			m.addToSpriteCounter();
			int estimateDist = (int)(Math.abs((m.getX()+15.5)/45-(p.getX()-mx+15.5)/45)) + (int)(Math.abs((m.getY()+15.5-65)/45-(p.getY()+15.5-65)/45));
			ArrayList<Integer> v = new ArrayList<Integer>(); 
			if(estimateDist>4){
				randomMovement(m);
				m.setPath(new ArrayList<Integer>());
			}
			else{
				targetMovement(m,nodesAll);
			}
		}
		
		else if(m.getType().equals("doria") || m.getType().equals("pontan")){ //these monsters can pass through soft blocks and chase after the player
			m.addToSpriteCounter();
			targetMovement(m,nodesPassSoftBlocks);
		}
		
		else if(m.getType().equals("ovape")){ //these monsters move randomly and is able to pass through soft blocks
			m.addToSpriteCounter();
			randomMovement(m);
		}
	}
	
	public Boolean playerHitMonster(){ //returns true if the player is hit by a monster
		Rectangle pRect = p.getActualRect(mx);
		
		for(Monster m: monsters){
			Rectangle mRect = m.getActualRect();
			
			if(m.getState() && p.getState() && pRect.intersects(mRect)){
				//System.out.println("player hit monster");
				lives-=1;
				return true;
			}
		}
		return false;
	}
	
	
	public ArrayList<Integer> getArray(Monster m,List<Node> linkedList){ //takes in the shortest path and converts it into an array of directions
		List<Node> t = new ArrayList<Node>();
   		t.addAll(linkedList);
   		ArrayList<Integer> al = new ArrayList<Integer>();
   		
   		if(t.size()>0){
   			if(t.get(0).getX()<(int)(Math.round(m.getX()+15.5)/45)){
				al.add(LEFT);
			}
			if(t.get(0).getX()>(int)(Math.round(m.getX()+15.5)/45)){
				al.add(RIGHT);
			}
			if(t.get(0).getY()>(int)(Math.round(m.getY()+15.5-65)/45)){
				al.add(DOWN);
			}
			if(t.get(0).getY()<(int)(Math.round(m.getY()+15.5-65)/45)){
				al.add(UP);
			}
   		}
   		
   		for(int i =0; i<t.size()-1; i++){
   			if(t.get(i+1).getX()<t.get(i).getX()){
   				al.add(LEFT);
   				}
   			if(t.get(i+1).getX()>t.get(i).getX()){
   				al.add(RIGHT);
   				}
   			if(t.get(i+1).getY()>t.get(i).getY()){
   				al.add(DOWN);
   				}
   			if(t.get(i+1).getY()<t.get(i).getY()){
   				al.add(UP);
   				}
   			}
   		return al;
		}
		
		
	//-----------------------------------------------------------------------------------------------------------------------
	//FINDING THE SHORTEST PATH TO THE PLAYER 
	//COPIED FROM THE INTERNET (https://github.com/xito313/java-a-star/blob/master/src/game/astar/Node.java)
	//WITH MINOR CHANGES
	//USES A* ALGORITHM TO CALCULATE THE PATH
	public Node getNode(Node[][] nodes, int x, int y)
	{
		if (x >= 0 && x < 27 && y >= 0 && y < 13)
		{
			return nodes[x][y];
		}
		else
		{
			return null;
		}
	}
	
	public final List<Node> findPath(Node[][] nodes, int startX, int startY, int goalX, int goalY)
	{
		// If our start position is the same as our goal position ...
		if (startX == goalX && startY == goalY)
		{
			// Return an empty path, because we don't need to move at all.
			return new LinkedList<Node>();
		}

		// The set of nodes already visited.
		List<Node> openList = new LinkedList<Node>();
		// The set of currently discovered nodes still to be visited.
		List<Node> closedList = new LinkedList<Node>();

		// Add starting node to open list.
		openList.add(nodes[startX][startY]);
		//System.out.printf("monster X: %d monster Y: %d\n",startX,startY);
		//System.out.printf("player X: %d player Y: %d\n",startX,startY);

		// This loop will be broken as soon as the current node position is
		// equal to the goal position.
		while (true)
		{
			// Gets node with the lowest F score from open list.
			//System.out.println("openList size: "+openList.size());
			Node current = lowestFInList(openList);
			
			// Remove current node from open list.
			openList.remove(current);
			// Add current node to closed list.
			closedList.add(current);

			// If the current node position is equal to the goal position ...
			if ((current.getX() == goalX) && (current.getY() == goalY))
			{
				// Return a LinkedList containing all of the visited nodes.
				return calcPath(nodes[startX][startY], current);
			}

			List<Node> adjacentNodes = getAdjacent(nodes, current, closedList);
			for (Node adjacent : adjacentNodes)
			{
				// If node is not in the open list ...
				if (!openList.contains(adjacent))
				{
					// Set current node as parent for this node.
					adjacent.setParent(current);
					// Set H costs of this node (estimated costs to goal).
					adjacent.setH(nodes[goalX][goalY]);
					// Set G costs of this node (costs from start to this node).
					adjacent.setG(current);
					// Add node to openList.
					openList.add(adjacent);
				}
				// Else if the node is in the open list and the G score from
				// current node is cheaper than previous costs ...
				else if (adjacent.getG() > adjacent.calculateG(current))
				{
					// Set current node as parent for this node.
					adjacent.setParent(current);
					// Set G costs of this node (costs from start to this node).
					adjacent.setG(current);
				}
			}

			// If no path exists ...
			if (openList.isEmpty())
			{
				// Return an empty list.
				return new LinkedList<Node>();
			}
			// But if it does, continue the loop.
		}
	}

	/**
	 * @param start
	 *            The first node on the path.
	 * @param goal
	 *            The last node on the path.
	 * @return a list containing all of the visited nodes, from the goal to the
	 *         start.
	 */
	private List<Node> calcPath(Node start, Node goal)
	{
		LinkedList<Node> path = new LinkedList<Node>();

		Node node = goal;
		boolean done = false;
		while (!done)
		{
			path.addFirst(node);
			node = node.getParent();
			if (node.equals(start))
			{
				done = true;
			}
		}
		return path;
	}

	/**
	 * @param list
	 *            The list to be checked.
	 * @return The node with the lowest F score in the list.
	 */
	private Node lowestFInList(List<Node> list)
	{
		//System.out.println(list.size());
		Node cheapest = list.get(0);
		
		if(list.size()==1){
			return cheapest;
			}
		for (int i = 0; i < list.size(); i++)
		{
			//System.out.println(i);
			//System.out.println(list.get(i).getF());
			//System.out.println(cheapest.getF());
			if (list.get(i).getF() < cheapest.getF())
			{
				cheapest = list.get(i);
			}
		}
		return cheapest;
	}

	/**
	 * @param node
	 *            The node to be checked for adjacent nodes.
	 * @param closedList
	 *            A list containing all of the nodes already visited.
	 * @return A LinkedList with nodes adjacent to the given node if those
	 *         exist, are walkable and are not already in the closed list.
	 */
	private List<Node> getAdjacent(Node[][] nodes, Node node, List<Node> closedList)
	{
		List<Node> adjacentNodes = new LinkedList<Node>();
		int x = node.getX();
		int y = node.getY();

		Node adjacent;

		// Check left node
		if (x > 0)
		{
			adjacent = getNode(nodes, x - 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check right node
		if (x < 27)
		{
			adjacent = getNode(nodes, x + 1, y);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check top node
		if (y > 0)
		{
			adjacent = this.getNode(nodes, x, y - 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}

		// Check bottom node
		if (y < 13)
		{
			adjacent = this.getNode(nodes, x, y + 1);
			if (adjacent != null && adjacent.isWalkable() && !closedList.contains(adjacent))
			{
				adjacentNodes.add(adjacent);
			}
		}
		return adjacentNodes;
	}
	//-------------------------------------------------------------------------------------------------------------------------------

	
	public void bombHitMonster(Rectangle r){
		ArrayList<Monster>hits = new ArrayList<Monster>();

		//Rectangle rect = new Rectangle((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());

		for(Monster m: monsters){
			if(m.getState()){
				Rectangle monRect = m.getActualRect();
				//Rectangle bombRect = grid[b.getY()][b.getX()];
	
				//System.out.println("RECT >> X: "+monsters.get(i).getX()+"    Y: "+monsters.get(i).getY());
	
				//if(monRect.intersects(r)){
					//System.out.println("HI");
				//	hits.add(monsters.get(i));
				//}
	
				if(monRect.intersects(r)){
					//System.out.println("BOO");
					hits.add(m);
				}	
			}
		}

		
		if(hits.size() > 0){
			for(Monster m : hits){
				if(m.getState()){
					points += m.getPoints();
					m.setState(false);
					monstersKilled++;
				}
			}
		}

		//return hits;
	}
	
	public boolean foundDoor(){ //returns true if the player is at the door and presses enter to go to the next level
		if(gate.intersects(p.getActualRect(mx)) && keys[KeyEvent.VK_ENTER]){
			return true;
		}
		return false;
	}
	
	public void newGrid(Block grid[][]){ //resets the 2D grid of the map with the borders and hardblocks
		nodesAll = new Node[27][13];
		nodesPassSoftBlocks = new Node[27][13];
		for(int row=0; row<13; row++){ //clear the grid to be just nulls
			Arrays.fill(grid[row],null);
		}
		
		for(int x = 0; x<27; x++){
			for(int y = 0; y<13; y++){
				nodesAll[x][y] = new Node(x, y, true,10);
				nodesPassSoftBlocks[x][y] = new Node(x, y, true,10);
				}
			}

		for(int x = 0; x<=26; x++){ //add the top and bottom borders
			for(int y = 0; y<13; y+=12){
				grid[y][x]= new Block(x,y,1); //hard block
				nodesAll[x][y] = new Node(x, y, false,10);
				nodesPassSoftBlocks[x][y] = new Node(x, y, false,10);
			}
		}
		for(int x = 0; x<27; x+=26){ //add the left and right borders
			for(int y = 0; y<13; y+=1){
				grid[y][x]=new Block(x,y,1); //hard block
				nodesAll[x][y] = new Node(x, y, false,10);
				nodesPassSoftBlocks[x][y] = new Node(x, y, false,10);
			}
		} 
		for(int x = 2; x<25; x+=2){ //add the hardblocks in the middle
			for(int y = 2; y<11; y+=2){
				grid[y][x]= new Block(x,y,1); //hard block
				nodesAll[x][y] = new Node(x, y, false,10);
				nodesPassSoftBlocks[x][y] = new Node(x, y, false,10);
			}
		}
	}
	
	public void addSoftBlocks(int num){ //randomly generates the indicated amount of soft blocks and adds the gate underneath one of the the soft blocks 
		Random rand = new Random();
		ArrayList<Rectangle> allSoftBlocks = new ArrayList<Rectangle>();
		
		for(int n = 0; n<num; n++){ 
			int randX = rand.nextInt(24)+1; //randomly generated X 
			int randY = rand.nextInt(10)+1; //randomly generated Y
			
			while(randX == 2 && randY == 1 ||  randX == 1 && randY == 1 || randX==1 && randY ==2){
            	randX = rand.nextInt(24)+1; //randomly generated X 
				randY = rand.nextInt(10)+1; //randomly generated Y
            }
			
			if (randX%2 == 0){ //makes sure that the randomly generated spot is not occupied by a hardblock
				randY+=(randY%2==0?1:0);
			}

			else{
				grid[randY][randX]= new Block(randX,randY,2); //soft block
				allSoftBlocks.add(grid[randY][randX].getRect());
				nodesAll[randX][randY] = new Node(randX, randY, false,10);
			}
		}
		
		int randGate = rand.nextInt(allSoftBlocks.size());
		gate = allSoftBlocks.get(randGate);

		for(int i = 0; i < 2; i++){
			randPowerUp(randGate,allSoftBlocks);
		}
	}

	public void randPowerUp(int g,ArrayList<Rectangle> s){
		Random rand = new Random();
		int randPU = rand.nextInt(s.size());
		if(randPU == g){
			randPowerUp(g,s);
		}

		else{
			//randPower = rand.nextInt(7);
			Rectangle rect = s.get(randPU);
			powerUps.add(new Powerup((int)rect.getX(),(int)rect.getY(),0));
		}
	}
}









