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

public class finalProject extends JFrame implements ActionListener{
	static final Color SOFT_BLOCK_COLOUR = new Color(120,190,120);
	static final Color HARD_BLOCK_COLOUR = new Color(200,200,200);
	static final Color FLOOR_COLOUR = new Color(150,150,150);
	Timer myTimer;
	Timer powerUpTimer;
	GamePanel game;
	
	public finalProject(){
		super("Bomberman");
		
		setSize(720,690);
		myTimer = new Timer(25,this);
		game = new GamePanel();
		add(game);
		
		myTimer.start();
 		setVisible(true);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//new GameMenu(this);
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
}

class GamePanel extends JPanel implements KeyListener{
	private boolean [] keys;
	private Player p;
	private int mx,sx,bx,by,detonateTime,explosionTime;
	private Image back,sBlock;
	private int lives = 3;
	private Rectangle[]explodeRects = new Rectangle[4];

	private boolean dropBomb; //if bomb is on screen
	
	private Node[][] nodesAll;
	private Node[][] nodesPassSoftBlocks; //used for enemies that can pass through softwalls
	
	public static final int RIGHT = 1, LEFT = -1, UP = -2, DOWN = 2;
	
	private Block grid[][] = new Block [13][27];
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	private ArrayList<String> allMonsters = new ArrayList<String>();
	private ArrayList<String> allLevels = new ArrayList<String>();
	
	//private Font font = new Font("Comic Sans",Font.PLAIN,50);
	
	
	public GamePanel(){
		setFocusable(true);
		addKeyListener(this);
		requestFocus();

		back = new ImageIcon("back.png").getImage(); //background of the game when playing
		sBlock = new ImageIcon("soft_block.png").getImage();

		keys = new boolean[KeyEvent.KEY_LAST+1];
			
		
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

		//read file that has # of monsters per level
		
		try{ //reading the monster file 
			Scanner inFile = new Scanner(new BufferedReader(new FileReader("levels.txt")));
			while(inFile.hasNextLine()){
				String nextLine = inFile.nextLine();
				allLevels.add(nextLine);
			}	
		}
		catch(IOException ex){
			System.out.println("levels.txt not found"); //couldn't find the txt file
		}

		startLevel(1); //start at level 1
	}

	public void startLevel(int level){
		p = new Player();

		newGrid(grid);
		addSoftBlocks(50);
		
		mx = 0;

		detonateTime = 70;
		explosionTime = 45;

		dropBomb = false;

		explodeRects[0] = new Rectangle(0,0,0,39);
		explodeRects[1] = new Rectangle(0,0,0,39);
		explodeRects[2] = new Rectangle(0,0,39,0);
		explodeRects[3] = new Rectangle(0,0,39,0);

		monsters.clear();
		
		
		String [] infoList = allLevels.get(level-1).split(",");
		for(int n = 0; n<7; n++){
			for(int i = 0; i<Integer.parseInt(infoList[n]); i++){
				//addMonster(n);
				System.out.println(n);
				}
			}
    	
    	//System.out.println(allLevels.get(0));
			
		
		addMonster(4);
	}

	public void gameOver(){

	}

	@Override
	public void paintComponent(Graphics g){
		g.drawImage(back,mx,0,this);
		
		g.setColor(new Color(23,223,186));
		g.fillRect(p.getX(),p.getY(),31,31);

		g.setColor(new Color(0,0,255));
		
		Rectangle cRect = p.getRect(0);
		g.drawRect((int)(cRect.getX()),(int)(cRect.getY()),(int)(cRect.getWidth()),(int)(cRect.getHeight()));
		
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
				g.setColor(new Color(244, 152, 66)); //ORANGE
				g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
				g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				Rectangle bRect = m.getRect();
				g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
				}
			else if(m.getType().equals("onil")){
				g.setColor(new Color(130, 225, 242)); //BLUE
				g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
				g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				Rectangle bRect = m.getRect();
				g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
				}
			else if(m.getType().equals("dahl")){
				g.setColor(new Color(241, 131, 129)); //RED/PINK
				g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
				g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				Rectangle bRect = m.getRect();
				g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
				}
			else if(m.getType().equals("minvo")){
				g.setColor(new Color(240, 239, 129)); //YELLOW
				g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
				g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				Rectangle bRect = m.getRect();
				g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
				}
			else if(m.getType().equals("doria")){
				g.setColor(new Color(202, 128, 237)); //PURPLE
				g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
				//g.setColor(new Color(0,0,0));
				g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				Rectangle bRect = m.getRect();
				g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
				}
			else if(m.getType().equals("ovape")){
				g.setColor(new Color(109, 67, 67)); //BROWN
				g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
				g.setColor(new Color(0,0,0));
				g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				Rectangle bRect = m.getRect();
				g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
				}
			else if(m.getType().equals("pontan")){
				g.setColor(new Color(37, 7, 155)); //NAVY
				g.fillRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
				g.setColor(new Color(0,0,0));
				g.drawRect((int)(m.getX())+mx,(int)(m.getY()),31,31);
				Rectangle bRect = m.getRect();
				g.drawRect((int)(bRect.getX()+mx),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
				}
			
		}

		if(dropBomb == true && detonateTime != 0){
			g.setColor(new Color(0,0,0));
			g.fillRect(45*bx+mx+3,45*by+69,39,39);
		}

		if(dropBomb == true && explosionTime > 0 && detonateTime == 0){
			g.setColor(new Color(255,100,0));

			g.fillRect(45*bx+mx+3,45*by+69,39,39);

			g.fillRect((int)explodeRects[0].getX(),(int)explodeRects[0].getY(),(int)explodeRects[0].getWidth(),(int)explodeRects[0].getHeight());
			g.fillRect((int)explodeRects[1].getX(),(int)explodeRects[1].getY(),(int)explodeRects[1].getWidth(),(int)explodeRects[1].getHeight());
			g.fillRect((int)explodeRects[2].getX(),(int)explodeRects[2].getY(),(int)explodeRects[2].getWidth(),(int)explodeRects[2].getHeight());
			g.fillRect((int)explodeRects[3].getX(),(int)explodeRects[3].getY(),(int)explodeRects[3].getWidth(),(int)explodeRects[3].getHeight());
		}
		
		g.setColor(finalProject.SOFT_BLOCK_COLOUR);
		g.setFont(new Font("Calibri",Font.PLAIN,25));
		String livesMessage = "Lives Remaining: " + lives;
		g.drawString(livesMessage,10,30);
	}

	public void bombPlayer(Rectangle r){
		Rectangle pRect = p.getActualRect(mx);
		Rectangle rect = new Rectangle((int)r.getX()-mx,(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());

		System.out.println("PLAYER >>>> X: "+pRect.getX() + "Y: "+pRect.getY());
		System.out.println("BOMB >>>>>> X: "+rect.getX() + "Y: "+rect.getY());


		if(pRect.intersects(rect)){
			System.out.println("BOO");
			lives-=1;

			startLevel(1);
		}
	}

	public void start(){
		moveMan();

		if(playerHitMonster()){
			/*if(lives == -1){
				gameOver();
			}

			else{*/
				startLevel(1);
			//}
		}
		
		for(Monster m:monsters){
			moveMonster(m,m.getPath());
		}

		if(dropBomb){
			
			if(grid[by][bx-1] == null || grid[by][bx-1].getType() == 2){
				explodeRects[0].setBounds(45*bx+explosionTime+mx-42,45*by+69,45-explosionTime,39);

				bombPlayer(explodeRects[0]);

				if(explosionTime > 0 && detonateTime == 0){
					grid[by][bx-1] = null;
					nodesAll[bx-1][by] = new Node(bx-1, by, true);
				}
			}

			if(grid[by][bx+1] == null || grid[by][bx+1].getType() == 2){
				explodeRects[1].setBounds(45*bx+42+mx,45*by+69,45-explosionTime,39);

				bombPlayer(explodeRects[1]);

				if(explosionTime > 0 && detonateTime == 0){
					grid[by][bx+1] = null;
					nodesAll[bx+1][by] = new Node(bx+1, by, true);
				}
			}

			if(grid[by-1][bx] == null || grid[by-1][bx].getType() == 2){
				explodeRects[2].setBounds(45*bx+mx+3,45*by+explosionTime+29,39,45-explosionTime);

				bombPlayer(explodeRects[2]);

				if(explosionTime > 0 && detonateTime == 0){
					grid[by-1][bx] = null;
					nodesAll[bx][by-1] = new Node(bx, by-1, true);
				}
			}

			if(grid[by+1][bx] == null || grid[by+1][bx].getType() == 2){
				explodeRects[3].setBounds(45*bx+mx+3,45*by+108,39,45-explosionTime);

				bombPlayer(explodeRects[3]);

				if(explosionTime > 0 && detonateTime == 0){
					grid[by+1][bx] = null;
					nodesAll[bx][by+1] = new Node(bx, by+1, true);
				}
			}
		}

		if(bombHitMonster().size() > 0){

			System.out.println("HI");
			for(int i : bombHitMonster()){
				monsters.remove(i);
			}
		}

		tick();
	}

	public void tick(){
		if(dropBomb == true){
			if(detonateTime > 0){
				detonateTime -= 1;
			}

			else if(detonateTime == 0 && explosionTime > 0){
				explosionTime -= 1;
			}

			else if(detonateTime == 0 && explosionTime == 0){
				dropBomb = false;
				detonateTime = 70;
				explosionTime = 45; //POWERUP EXCEPTION: if lvl of bomb increases, explosionTime = 30 * lvl;

				grid[by][bx] = null;

				explodeRects[0].setBounds(0,0,0,39);
				explodeRects[1].setBounds(0,0,0,39);
				explodeRects[2].setBounds(0,0,39,0);
				explodeRects[3].setBounds(0,0,39,0);
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
			Rectangle bRect = p.getUpRect(mx);
			
			if(grid[gY-1][gX] != null){
				Rectangle r = (grid[gY-1][gX]).getRect();
				//Rectangle rU = p.getRect(UP);

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

			System.out.println(hitBlock(RIGHT));

			if(hitBlock(RIGHT)){
				p.moveRight(0);
			}

			else{
				//System.out.println("YO");
				if(p.getX() > 225 && mx > -495){
					mx -= 3;
					sx += 3;
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
			if(dropBomb == false){ //POWERUP EXCEPTION: if(dropBomb == false && multibomb == false) >> for multibomb, cant put two bombs same spot
				dropBomb = true;

				bx = (int)(Math.round(p.getX()+15.5-mx)/45); //closest column blocks
				by = (int)(Math.round(p.getY()+15.5-60)/45); //closest row

				System.out.println("BX: "+bx+ "    BY: "+by);

				grid[by][bx] = new Block(bx,by,3); //bomb is 3
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
			if (randX%2 == 0){ //makes sure that the randomly generated spot is not occupied by a hardblock
				randY+=(randY%2==0?1:0); //adjusts to an available position
			}
			if(grid[randY][randX]==null){ //if this is an available position
				Monster m = new Monster(allMonsters.get(type)+","+(randX*45+7)+","+(randY*45+72),path,mx);
				monsters.add(m); //NEED TO CHANGE THE "RIGHT" TO TAKE INTO CONSIDERATION OF ACTUAL SURROUNDINGS
				m.setCurrentDirection(validRandomDirection(m));
			}
			else{
				addMonster(type);	
			}
	}
	
	public int validRandomDirection(Monster m){ //finds all valid directions for the monster to go and return a random one
		ArrayList<Integer> v = new ArrayList<Integer>(); //contains all valid directions
		for(int d = -2; d<0; d++){
			if(hitBlock(m,d)==false){
				v.add(d);
			}
		}
		
		for(int d = 1; d<3; d++){
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
			
			if(gX<26 && grid[gY][gX+1] != null){ //if there is a block there 
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
			
			if(gX>0 && grid[gY][gX-1] != null){
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
			
			if(gY>0 && grid[gY-1][gX] != null){
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
			
			if(gY<12 && grid[gY+1][gX] != null){
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
	
	public void moveMonster(Monster m, ArrayList<Integer> path){ //takes in the monster and path the monster should take
		if(m.getType().equals("ballom")){
			int gX = (int)(Math.round(m.getX()+15.5)/45); //closest column blocks
			int gY = (int)(Math.round(m.getY()+15.5-65)/45); //closest row
			ArrayList<Integer> v = new ArrayList<Integer>(); 
			
			if(gX%2==1 && gY%2==1){
				if(m.getCurrentDirection()==1 || m.getCurrentDirection()==-1){ //if monster is going left/right
					if(!hitBlock(m,UP)){
						v.add(UP);
						}
					if(!hitBlock(m,DOWN)){
						v.add(DOWN);
						}
					}
				else if(m.getCurrentDirection()==2 || m.getCurrentDirection()==-2){ //if monster is going up/down
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
		
		else if(m.getType().equals("doria")){
			if(((int)(m.getX())-7)%45<2 && ((int)(m.getY())-7-65)%45<2){
				m.setPath(getArray(m,findPath(nodesPassSoftBlocks,(int)(Math.round(m.getX()+15.5)/45),(int)(Math.round(m.getY()+15.5-65)/45),(int)(Math.round(p.getX()-mx+15.5)/45),(int)(Math.round(p.getY()+15.5-65)/45))));
			}
			//path = m.getPath();
			//System.out.println(path.size());
			
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
						if(m.getCurrentDirection()==1 || m.getCurrentDirection()==-1){ //if monster is going left/right
							if(!hitBlock(m,UP)){
								v.add(UP);
								}
							if(!hitBlock(m,DOWN)){
								v.add(DOWN);
								}
							}
						else if(m.getCurrentDirection()==2 || m.getCurrentDirection()==-2){ //if monster is going up/down
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
		
	}
	/*public ArrayList<Integer> fastestPath(Monster m){
		ArrayList<Integer> path = new ArrayList<Integer>();
		ArrayList<Point> openList = new ArrayList<Point>();
		ArrayList<Point> closedList = new ArrayList<Point>();
		
		path.add(UP);
		path.add(DOWN);
		path.add(LEFT);
		path.add(RIGHT);
		
		int mX = (int)(Math.round(m.getX()+15.5)/45); //closest column to the monster
		int mY = (int)(Math.round(m.getY()+15.5-65)/45); //closest row to the monster
		int pX = (int)(Math.round(p.getX()-mx+15.5)/45); //closest column to the player
		int pY = (int)(Math.round(p.getY()+15.5-65)/45); //closest row to the player
		
		openList.add(new Point(mX,mY));
		
		while(openList.size()>0){
			
			}
		
		int h = Math.abs(mY-pY)+Math.abs(mX-pX); //heuristic - estimated movement cost from the current square to the player's square 
		//System.out.println(h);
		
		return path;
		}*/
	
	public Boolean playerHitMonster(){
		Rectangle pRect = p.getActualRect(mx);
		
		for(Monster m: monsters){
			Rectangle mRect = m.getActualRect();
			
			if(pRect.intersects(mRect)){
				System.out.println("player hit monster");
				lives-=1;
				return true;
			}
		}
		return false;
	}
	
	public Boolean bombHitMonster(Monster m){
		return false;
		}
		
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
  		System.out.println(al.size());
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

	
	public ArrayList<Integer> bombHitMonster(){
		ArrayList<Integer>hits = new ArrayList<Integer>();

		for(int i = 0; i < monsters.size(); i ++){
			for(Rectangle r : explodeRects){
				Rectangle rect = new Rectangle((int)r.getX()-mx,(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());

				if(rect.intersects(monsters.get(i).getActualRect())){
					hits.add(i);
				}
			}
			/*if(explodeRects[0].intersects(monsters.get(i).getActualRect())){
				hits.add(i);
			}

			else if(explodeRects[1].intersects(monsters.get(i).getActualRect())){
				hits.add(i);
			}

			else if(explodeRects[2].intersects(monsters.get(i).getActualRect())){
				hits.add(i);
			}

			else if(explodeRects[3].intersects(monsters.get(i).getActualRect())){
				hits.add(i);
			}*/
		}

		return hits;
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
				nodesAll[x][y] = new Node(x, y, true);
				nodesPassSoftBlocks[x][y] = new Node(x, y, true);
				}
			}

		for(int x = 0; x<=26; x++){ //add the top and bottom borders
			for(int y = 0; y<13; y+=12){
				grid[y][x]= new Block(x,y,1); //hard block
				nodesAll[x][y] = new Node(x, y, false);
				nodesPassSoftBlocks[x][y] = new Node(x, y, false);
			}
		}
		for(int x = 0; x<27; x+=26){ //add the left and right borders
			for(int y = 0; y<13; y+=1){
				grid[y][x]=new Block(x,y,1); //hard block
				nodesAll[x][y] = new Node(x, y, false);
				nodesPassSoftBlocks[x][y] = new Node(x, y, false);
			}
		} 
		for(int x = 2; x<25; x+=2){ //add the hardblocks in the middle
			for(int y = 2; y<11; y+=2){
				grid[y][x]= new Block(x,y,1); //hard block
				nodesAll[x][y] = new Node(x, y, false);
				nodesPassSoftBlocks[x][y] = new Node(x, y, false);
			}
		}
	}
	
	public void addSoftBlocks(int num){
		Random rand = new Random();
		for(int n = 0; n<num; n++){ 
			int randX = rand.nextInt(24)+1; //randomly generated X 
			int randY = rand.nextInt(10)+1; //randomly generated Y
			if (randX%2 == 0){ //makes sure that the randomly generated spot is not occupied by a hardblock
				randY+=(randY%2==0?1:0);
			}

			if(randX == 2 && randY == 1 ||  randX == 1 && randY == 1 || randX==1 && randY ==2){
                addSoftBlocks(1);
            }

			else{
				grid[randY][randX]= new Block(randX,randY,2); //soft block
				nodesAll[randX][randY] = new Node(randX, randY, false);
			}
		}
	}
}