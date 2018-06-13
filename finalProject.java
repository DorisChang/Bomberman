//gyishudkqadhajdisakdssk


//finalProject.java
//Minya Bai & Doris Chang
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
		myTimer = new Timer(25,this);
		game = new GamePanel();
		add(game);
		
		titleScreen = Applet.newAudioClip(getClass().getResource("sounds/01_Title Screen.wav"));
		//myTimer.start();
 		//setVisible(true);
 		titleScreen.loop();
 	

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new GameMenu(this);
		
	}

	public void actionPerformed(ActionEvent e){
		if(game != null){
			game.start();
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
 		//game.start();
 		setVisible(true);
 		}

}


class GameMenu extends JFrame implements KeyListener{ //JFrame for the game/main menu, uses SPACE to continue 
 	private finalProject menu; 
 	private boolean [] keys;
 	
 	public GameMenu(finalProject m){
 		super("game menu");
 		
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
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		}
    
    public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
			
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
 		super("instructions");
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
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		}
 	
 	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
			
		if(keys[KeyEvent.VK_SPACE]){ //when SPACE is pressed
			bg.start(); //starts the game
			setVisible(false);	
			}
		}
		
	public void keyReleased(KeyEvent e){
		}
	public void keyTyped(KeyEvent e){ 
		}
	}



class GamePanel extends JPanel implements KeyListener{
	private boolean [] keys;
	private Player p;
	private int mx,sx,sNum;
	private Image back,sBlock;
	private int lives = 3;
	private int level = 1;
	private int dropMax; //current level on
	private int points = 0;
	private int displayPoints = 0;
	private LinkedList<Bomb> bombs = new LinkedList<Bomb>();
	private Rectangle gate;

	private boolean dropBomb; //if bomb is on screen
	private int bombsLeft;
	private boolean playTheme = false; //used for checking if the music is already playing
	private boolean stageStartMusic = false; //used for checking if the music is already playing
	private int newLevelScreen = 100; //used to time how long the new level screen stays on screen for
	private int screenFreeze = 0; //used to time how long the keep screen frozen for
	private boolean lifeLostMusic = false;
	private boolean gameOver = false;
	private boolean gameOverMusicOn = false;
	
	private Node[][] nodesAll;
	private Node[][] nodesPassSoftBlocks; //used for enemies that can pass through softwalls
	
	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4;
	
	private Block grid[][] = new Block [13][27];
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	private ArrayList<String> allMonsters = new ArrayList<String>();
	private ArrayList<String> allLevels = new ArrayList<String>();

	//POWERUPS
	private boolean detonator;// = true;
		
	private Image heart = new ImageIcon("sprites/heart.png").getImage();
	private Image[][] bombermanSprites = new Image[4][3];
	private Image[] ballomSprites = new Image[6];
	private Image[] onilSprites = new Image[6];
	private Image[] dahlSprites = new Image[6];
	private Image[] minvoSprites = new Image[6];
	private Image[] doriaSprites = new Image[6];
	private Image[] ovapeSprites = new Image[6];
	private Image[] pontanSprites = new Image[6];
	
	private Image gateImage = new ImageIcon("gate.png").getImage();
	
	public AudioClip titleScreen, stageStart, stageTheme, stageComplete, lifeLost, gameOverMusic, ending;

	public GamePanel(){
		setFocusable(true);
		addKeyListener(this);
		requestFocus();

		detonator = false;

		back = new ImageIcon("back.png").getImage(); //background of the game when playing
		sBlock = new ImageIcon("soft_block.png").getImage();

		keys = new boolean[KeyEvent.KEY_LAST+1];
		


		//titleScreen = Applet.newAudioClip(getClass().getResource("sounds/01_Title Screen.wav"));
    	stageStart = Applet.newAudioClip(getClass().getResource("sounds/02_Stage Start.wav"));
    	stageTheme = Applet.newAudioClip(getClass().getResource("sounds/03_Stage Theme.wav"));
    	stageComplete = Applet.newAudioClip(getClass().getResource("sounds/05_Stage Complete.wav"));
    	lifeLost = Applet.newAudioClip(getClass().getResource("sounds/08_Life Lost.wav"));
    	gameOverMusic = Applet.newAudioClip(getClass().getResource("sounds/09_Game Over.wav"));
    	ending = Applet.newAudioClip(getClass().getResource("sounds/10_Ending.wav"));
    	//stageTheme.play();

		try{ //reading the monster file 
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

		startLevel(level); //start at level 1
		
	}

	public void loadSprites(){
		//bombermanSprites = new ImageIcon("sprites/bombermanleft1.png").getImage();
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
		}

	public void startLevel(int level){
		
		p = new Player();
		//stageTheme.loop();
		
		newLevelScreen = 180;
		stageStartMusic = false;
		lifeLostMusic = false;

		newGrid(grid);
		addSoftBlocks(50);
		
		mx = 0;

		dropBomb = false;

		//points = 0;
		//displayPoints = 0;

		dropMax = 3;
		bombsLeft = dropMax;

		bombs.clear();
		monsters.clear();
		
		String [] infoList = allLevels.get(level-1).split(",");
		for(int n = 0; n<7; n++){
			for(int i = 0; i<Integer.parseInt(infoList[n]); i++){
				addMonster(n);
				//System.out.println(n);
			}
		}
	}

	public void gameOver(){

	}

	@Override
	public void paintComponent(Graphics g){
		//g.drawImage(back,mx,0,this);
		//g.drawImage(door,(int)exit.getX()+mx,(int)exit.getY(),this);
		
		//g.setColor(new Color(23,223,186));
		
		if(!gameOver){
			if(newLevelScreen>0 && screenFreeze<0){
				g.setColor(new Color(0,0,0));
				g.fillRect(0,0,720,690);
				g.setColor(new Color(250,250,250));
				g.setFont(new Font("Calibri",Font.PLAIN,50));
				String levelMessage = "LEVEL " + level;
				g.drawString(levelMessage,270,300);
				if(stageStartMusic == false){
					playStageStartMusic();
					stageStartMusic = true;
					}
				 }
			else if(screenFreeze<0){
				g.drawImage(back,mx,0,this);
	
				//g.setColor(new Color(0,0,255));
				
				//Rectangle cRect = p.getRect(0);
				//g.drawRect((int)(cRect.getX()),(int)(cRect.getY()),(int)(cRect.getWidth()),(int)(cRect.getHeight()));
				
				System.out.printf("rect X: "+(int)(gate.getX()/45)+" rect Y: "+(int)(gate.getY()/45)+"\n");
				//g.fillRect((int)(gate.getX()+mx),(int)(gate.getY()),(int)(gate.getWidth()),(int)(gate.getHeight()));
				g.drawImage(gateImage,(int)(gate.getX()+mx),(int)(gate.getY()),this);
		
				//g.setColor(new Color(0,0,255));
		
				g.drawImage(bombermanSprites[p.getDirection()-1][p.getSpriteCounter()%3],p.getX()-7,p.getY()-7,this);
		
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
					//System.out.println(m.getType());
					if(m.getType().equals("ballom")){
						g.drawImage(ballomSprites[m.getSpriteCounter()%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						//g.setColor(new Color(244, 152, 66)); //ORANGE
						//g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
						//g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						//Rectangle bRect = m.getRect();
						//g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
						}
					else if(m.getType().equals("onil")){
						g.drawImage(onilSprites[m.getSpriteCounter()%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						/*g.setColor(new Color(130, 225, 242)); //BLUE
						g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
						g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						Rectangle bRect = m.getRect();
						g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));*/
						}
					else if(m.getType().equals("dahl")){
						g.drawImage(dahlSprites[m.getSpriteCounter()%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						/*g.setColor(new Color(241, 131, 129)); //RED/PINK
						g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
						g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						Rectangle bRect = m.getRect();
						g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));*/
						}
					else if(m.getType().equals("minvo")){
						g.drawImage(minvoSprites[m.getSpriteCounter()%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						/*g.setColor(new Color(240, 239, 129)); //YELLOW
						g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
						g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						Rectangle bRect = m.getRect();
						g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));*/
						}
					else if(m.getType().equals("doria")){
						g.drawImage(doriaSprites[m.getSpriteCounter()%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						/*g.setColor(new Color(202, 128, 237)); //PURPLE
						g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
						//g.setColor(new Color(0,0,0));
						g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						Rectangle bRect = m.getRect();
						g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));*/
						}
					else if(m.getType().equals("ovape")){
						g.drawImage(ovapeSprites[m.getSpriteCounter()%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						/*g.setColor(new Color(109, 67, 67)); //BROWN
						g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
						g.setColor(new Color(0,0,0));
						g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						Rectangle bRect = m.getRect();
						g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));*/
						}
					else if(m.getType().equals("pontan")){
						g.drawImage(pontanSprites[m.getSpriteCounter()%6],(int)(m.getX()-7+mx),(int)(m.getY()-7),this);
						/*g.setColor(new Color(37, 7, 155)); //NAVY
						g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
						g.setColor(new Color(0,0,0));
						g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
						Rectangle bRect = m.getRect();
						g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));*/
						}
				}
		
				//System.out.println(dropBomb);
		
				if(dropBomb){
					for(Bomb b : bombs){
						if(b!= null){
							if(b.getDT() != 0){
								//System.out.println("Bomb!");
								g.setColor(new Color(0,0,0));
								g.fillRect((int)b.getBombRect().getX()*45+3+mx,(int)b.getBombRect().getY()*45+67,(int)b.getBombRect().getWidth(),(int)b.getBombRect().getHeight());
							}
		
							if(b.getET() > 0 && b.getDT() == 0){
								//System.out.println("Explode!");
								g.setColor(new Color(255,100,0));
		
								g.fillRect((int)b.getBombRect().getX()*45+3+mx,(int)b.getBombRect().getY()*45+67,(int)b.getBombRect().getWidth(),(int)b.getBombRect().getHeight());
		
								for(int i = 0; i < 4; i++){
									g.fillRect((int)(int)b.getExpRect(i).getX()+mx,(int)b.getExpRect(i).getY()-2,(int)b.getExpRect(i).getWidth(),(int)b.getExpRect(i).getHeight());
								}
							}
						}
					}
				}
				
				for(int n = 0; n<lives-1; n++){
					g.drawImage(heart,10+n*40,15,this);
					} 
						
				g.setColor(new Color(0,0,0));
				g.setFont(new Font("Calibri",Font.PLAIN,25));
				String livesMessage = "Lives Remaining: " + lives;
				//g.drawString(livesMessage,10,25);
				if(displayPoints<points){
					displayPoints+=50;
					}
				String pointsMessage = "POINTS: " + displayPoints;
				g.drawString(pointsMessage,230,30);
			}
		}
		
		else{
			g.setColor(new Color(0,0,0));
			g.fillRect(0,0,720,690);
			g.setColor(new Color(250,250,250));
			g.setFont(new Font("Calibri",Font.PLAIN,50));
			g.drawString("GAME OVER",230,300);
			}
	}

	
	public void playThemeMusic(){
		stageTheme.loop();
		}
		
	public void playStageStartMusic(){
		stageStart.play();
		}
	
	public void start(){
		if(!gameOver){
			
			if(newLevelScreen<=0 && playTheme == false){
				playThemeMusic();
				playTheme = true;
				}
				
			if(screenFreeze>0 && lifeLostMusic==false){
				lifeLost.play();
				lifeLostMusic = true;
				}
				
			newLevelScreen -= 1;
			screenFreeze -=1;
			
			if(screenFreeze<=0 && newLevelScreen<0){
				
				moveMan();
		
				if(playerHitMonster()){
					stageTheme.stop();
					playTheme = false;
					lifeLost.play();
					screenFreeze = 80;
					if(lives>0){
						startLevel(level);
					}
					else{
						gameOver = true;
					}
				}
				
				for(Monster m:monsters){
					moveMonster(m,m.getPath());
				}
		
				ArrayList<Bomb> emptyBombs = new ArrayList<Bomb>();
		
				if(dropBomb){
					for(Bomb b : bombs){
						if(b != null){
							int bx = b.getBX();
							int by = b.getBY();
		
							b.detonate();
		
							//System.out.println("MERPPP");
		
							//checks if hits player
							for(int i = 0; i < 4; i++){
								//System.out.println("YOOOO");
								bombPlayer(b.getExpRect(i));
							}
		
							bombHitMonster(); //checks if hits monster
		
							if(b.getET() > 0 && b.getDT() == 0){
								if(grid[by][bx-1] == null || grid[by][bx-1].getType() == 2){
									b.explode(0);
		
									grid[by][bx-1] = null;
									nodesAll[bx-1][by] = new Node(bx-1, by, true, 10);
								}
		
								if(grid[by][bx+1] == null || grid[by][bx+1].getType() == 2){
									b.explode(1);
									grid[by][bx+1] = null;
									nodesAll[bx+1][by] = new Node(bx+1, by, true,10);
								}
		
								if(grid[by-1][bx] == null || grid[by-1][bx].getType() == 2){
									b.explode(2);
									grid[by-1][bx] = null;
									nodesAll[bx][by-1] = new Node(bx, by-1, true,10);
								}
		
								if(grid[by+1][bx] == null || grid[by+1][bx].getType() == 2){
									b.explode(3);
									grid[by+1][bx] = null;
									nodesAll[bx][by+1] = new Node(bx, by+1, true,10);
								}
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
		
							bombs.remove();
		
							bombsLeft += 1;
						}
					}
				}
		
				if(foundDoor()==true && level<7){
					level+=1;
					stageTheme.stop(); 
					playTheme = false;
					startLevel(level);
				}
		
				if(bombsLeft == 3){
					dropBomb = false;
				}
			}
		}
		
		else{
			if(!gameOverMusicOn){
				gameOverMusic.play();
				gameOverMusicOn = true;		
			}	
		}
		
	}

	public void bombPlayer(Rectangle r){
		System.out.println("HALLLO");

		Rectangle pRect = p.getActualRect(mx);
		Rectangle rect = new Rectangle((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());

		if(pRect.intersects(rect)){
			System.out.println("BOO");
			lives-=1;
			//bombs.clear();

			dropBomb = false;
			stageTheme.stop();
			playTheme = false;
			screenFreeze = 80;
			if(lives>0){
				startLevel(level);
			}
			else{
				gameOver = true;
			}
		}
	}

	//ALL THREE MUST BE PRESENT
	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e){}
	
	public Boolean hitBlock(int direction){
		int gX = (int)(Math.round(p.getX()+15.5-mx)/45); //closest column blocks
		int gY = (int)(Math.round(p.getY()+15.5-65)/45); //closest row
		
		if(direction == RIGHT){
			Rectangle bRect = p.getRightRect(mx);

			if(grid[gY][gX+1] != null){ //if there is a block there 
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

			//System.out.println(hitBlock(RIGHT));

			if(hitBlock(RIGHT)){
				p.moveRight(0);
			}

			else{
				//System.out.println("YO");
				if(p.getX() > 225 && mx > -495){
					mx -= 3;
					sx += 3;
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
					sx -= 3;
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
				if(bombsLeft > 0){ //POWERUP EXCEPTION: if(dropBomb == false && multibomb == false) >> for multibomb, cant put two bombs same spot
					//if(b.added() == false){
						Bomb b = new Bomb(bx,by);

						grid[by][bx] = new Block(bx,by,3); //bomb is 3
						dropBomb = true;

						bombs.add(b);

						if(detonator == false){
							b.detonate();
						}

						bombsLeft -= 1;
						//b.getAdded();
					//}
				}
			}
		}

		/*else if(keys[KeyEvent.VK_X] == false){
			added = false;
		}

		if(keys[KeyEvent.VK_Z]){
			if(detonator){
				if(bombsLeft > 0){
					//System.out.println("HI");
					dropBomb = true;
					//detonate(bombs.getFirst());
				}
			}
		}*/
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

		//if (randX%2 == 0){ //makes sure that the randomly generated spot is not occupied by a hardblock
		//	randY+=(randY%2==0?1:0); //adjusts to an available position
		//}
		Monster m = new Monster(allMonsters.get(type)+","+(randX*45+7)+","+(randY*45+72),path,mx);
		monsters.add(m); //NEED TO CHANGE THE "RIGHT" TO TAKE INTO CONSIDERATION OF ACTUAL SURROUNDINGS
		m.setCurrentDirection(validRandomDirection(m));
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
		
		//System.out.printf("mX: %d mY: %d\n",gX,gY);
		
		if(direction == RIGHT){
			Rectangle bRect = m.getRightRect();
			
			if(m.getType().equals("ovape") && grid[gY][gX+1]!=null && grid[gY][gX+1].getType()==1 || !m.getType().equals("ovape") && grid[gY][gX+1]!=null){ //if there is a block there 
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
				//Rectangle rL = p.getRect(LEFT);

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
			//System.out.println(m.getType().equals("ovape"));
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

	public void targetMovement(Monster m, Node[][] nodes){
		if(((int)(m.getX())-7)%45<2 && ((int)(m.getY())-7-65)%45<2){
			m.setPath(getArray(m,findPath(nodes,(int)(Math.round(m.getX()+15.5)/45),(int)(Math.round(m.getY()+15.5-65)/45),(int)(Math.round(p.getX()-mx+15.5)/45),(int)(Math.round(p.getY()+15.5-65)/45))));
		}
			
		if(m.getPath().size()>0){
			//System.out.println(path.get(0));
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
				//System.out.println("hello");
				m.pathRemoveFirst();
			}
		}
		//System.out.printf("Monster X: %d  Monster Y: %d\n",(int)(Math.round(m.getX()+15.5)/45),(int)(Math.round(m.getY()+15.5-65)/45));
		
		if(m.getPath().size()==0){
			//System.out.println("nothing in path");
			//m.setPath(getArray(m,findPath((int)(Math.round(m.getX()+15.5)/45),(int)(Math.round(m.getY()+15.5-65)/45),(int)(Math.round(p.getX()-mx+15.5)/45),(int)(Math.round(p.getY()+15.5-65)/45))));
			//if(m.getPath().size()==0){
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
		if(m.getType().equals("ballom") || m.getType().equals("dahl")){
			m.addToSpriteCounter();
			randomMovement(m);
		}
		
		else if(m.getType().equals("onil") || m.getType().equals("minvo")){
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
		
		else if(m.getType().equals("doria") || m.getType().equals("pontan")){
			m.addToSpriteCounter();
			targetMovement(m,nodesPassSoftBlocks);
		}
		
		else if(m.getType().equals("ovape")){
			m.addToSpriteCounter();
			randomMovement(m);
		}
	}
	
	public Boolean playerHitMonster(){
		Rectangle pRect = p.getActualRect(mx);
		
		for(Monster m: monsters){
			Rectangle mRect = m.getActualRect();
			
			if(pRect.intersects(mRect)){
				//System.out.println("player hit monster");
				lives-=1;
				return true;
			}
		}
		return false;
	}
	
	/*public Boolean bombHitMonster(Monster m){
		return false;
		}*/
		
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
	
	public ArrayList<Integer> getArray(Monster m,List<Node> linkedList){
		List<Node> t = new ArrayList<Node>();
   		t.addAll(linkedList);
   		ArrayList<Integer> al = new ArrayList<Integer>();
   		
   		//System.out.printf("Monster X: %d  Monster Y: %d\n",(int)(Math.round(m.getX()+15.5)/45),(int)(Math.round(m.getY()+15.5-65)/45));
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
  		//System.out.println(al.size());
   		return al;
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
			//System.out.println(nodes[1][3]);
			//System.out.println(current.getX());
			//System.out.println(current.getY());
		
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

	
	public void bombHitMonster(){
		ArrayList<Monster>hits = new ArrayList<Monster>();

		for(Bomb b : bombs){
			for(int i = 0; i < monsters.size(); i ++){
				for(int j = 0; j < 4; j++){
					Rectangle monRect = monsters.get(i).getActualRect();
					//Rectangle bombRect = grid[b.getY()][b.getX()];

					//System.out.println("RECT >> X: "+monsters.get(i).getX()+"    Y: "+monsters.get(i).getY());

					if(monRect.intersects(b.getBombRect())){
						//System.out.println("HI");
						hits.add(monsters.get(i));
					}

					else if(monRect.intersects(b.getExpRect(j))){
						//System.out.println("BOO");
						hits.add(monsters.get(i));
					}
				}
			}
		}

		
		if(hits.size() > 0){
			for(Monster m : hits){
				points += m.getPoints();
				monsters.remove(m);
			}
		}

		//return hits;
	}
	
	public boolean foundDoor(){
		if(gate.intersects(p.getActualRect(mx)) && keys[KeyEvent.VK_ENTER]){
			System.out.println("found the door, standing on it");
			return true;
		}
		return false;
	}

	//------------------------------------------------------------------------------------------------------------
	
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
	
	public void addSoftBlocks(int num){
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
		//gate = allSoftBlocks.get(randGate);
	}
}