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
	private Image tBkg;
	private int mx,sx,bx,by,detonateTime,explosionTime;
	private int pHitTimer = 0; 
	private int lives = 3;
	private Rectangle[]explodeRects = new Rectangle[4];

	private boolean dropBomb; //if bomb is on screen
	
	public static final int RIGHT = 1, LEFT = -1, UP = -2, DOWN = 2;
	
	private Block grid[][] = new Block [13][27];
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	private ArrayList<String> allMonsters = new ArrayList<String>();
	
	//private Font font = new Font("Comic Sans",Font.PLAIN,50);
	
	
	public GamePanel(){
		setFocusable(true);
		addKeyListener(this);
		requestFocus();
		newGrid(grid);
		addSoftBlocks(50);

		p = new Player();
		
		mx = 0;

		tBkg = new ImageIcon("temp_bkg.png").getImage();

		detonateTime = 70;
		explosionTime = 45;

		dropBomb = false;

		explodeRects[0] = new Rectangle(0,0,0,39);
		explodeRects[1] = new Rectangle(0,0,0,39);
		explodeRects[2] = new Rectangle(0,0,39,0);
		explodeRects[3] = new Rectangle(0,0,39,0);

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
		for(int i =0 ; i<10; i++){
			addMonster(0);
			}
	}

	@Override
	public void paintComponent(Graphics g){
		//g.drawImage(tBkg,mx,0,this);
		
		g.setColor(finalProject.FLOOR_COLOUR);
		g.fillRect(0,0,1600,832);
		
		g.setColor(new Color(23,223,186));
		g.fillRect(p.getX(),p.getY(),31,31);

		g.setColor(new Color(0,0,255));
		
		Rectangle cRect = p.getRect(0);
		g.drawRect((int)(cRect.getX()),(int)(cRect.getY()),(int)(cRect.getWidth()),(int)(cRect.getHeight()));
		
		for(int r=0; r<13; r++){ //row
			for(int c=0; c<27; c++){ //column
				if(grid[r][c] != null){
					if((grid[r][c]).getType()==1){
						g.setColor(finalProject.HARD_BLOCK_COLOUR);
						g.fillRect(c*45+mx,r*45+65,45,45);

						g.setColor(new Color(255,0,0));
						g.drawRect(c*45+mx,r*45+65,45,45);
					}

					if((grid[r][c]).getType()==2){
						g.setColor(finalProject.SOFT_BLOCK_COLOUR);
						g.fillRect(c*45+mx,r*45+65,45,45);

						g.setColor(new Color(255,0,0));
						g.drawRect(c*45+mx,r*45+65,45,45);
					}
				}	
			}
		}
		
		//DRAWING MONSTERS aka enemies
		for(Monster m : monsters){
			g.setColor(new Color(250,0,0));
			g.fillRect(m.getX()+mx,m.getY(),31,31);
			//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
			g.setColor(new Color(0,0,0));
			g.drawRect(m.getX()+mx,m.getY(),31,31);
			Rectangle bRect = m.getRect();
			g.drawRect((int)(bRect.getX())+mx,(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
		}

		if(dropBomb == true && detonateTime != 0){
			g.setColor(new Color(0,0,0));
			g.fillRect(bx+mx,by+60,39,39);
		}

		if(dropBomb == true && explosionTime > 0 && detonateTime == 0){
			g.setColor(new Color(255,100,0));

			g.fillRect(bx+mx,by+60,39,39);

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

	public void start(){
		moveMan();

		for(Monster m:monsters){
			moveMonster(m,m.getPath());
		}

		playerHitMonster();
		
		if(pHitTimer!=0){
			pHitTimer-=1;
			}

		if(dropBomb){
			int bombX = (bx-3)/45;
			int bombY = (by-9)/45;

			if(grid[bombY][bombX-1] == null || grid[bombY][bombX-1].getType() == 2){
				explodeRects[0].setBounds(bx+explosionTime+mx-45,by+60,45-explosionTime,39);

				if(explosionTime > 0 && detonateTime == 0){
					grid[bombY][bombX-1] = null;
				}
			}

			if(grid[bombY][bombX+1] == null || grid[bombY][bombX+1].getType() == 2){
				explodeRects[1].setBounds(bx+39+mx,by+60,45-explosionTime,39);

				if(explosionTime > 0 && detonateTime == 0){
					grid[bombY][bombX+1] = null;
				}
			}

			if(grid[bombY-1][bombX] == null || grid[bombY-1][bombX].getType() == 2){
				explodeRects[2].setBounds(bx+mx,by+explosionTime+20,39,45-explosionTime);

				if(explosionTime > 0 && detonateTime == 0){
					grid[bombY-1][bombX] = null;
				}
			}

			if(grid[bombY+1][bombX] == null || grid[bombY+1][bombX].getType() == 2){
				explodeRects[3].setBounds(bx+mx,by+99,39,45-explosionTime);

				if(explosionTime > 0 && detonateTime == 0){
					grid[bombY+1][bombX] = null;
				}
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

				grid[(by-9)/45][(bx-3)/45] = null;

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
				//Rectangle rR = p.getRect(RIGHT); //get the rect on the 
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

			if(hitBlock(RIGHT) == false){
				if(p.getX() > 225 && mx > -495){
					mx -= 3;
					sx += 3;
				}

				else{
					p.moveRight(3);
				}
			}

			else if(hitBlock(RIGHT) == true){
				p.moveRight(0);
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

				bx = (int)(Math.round(p.getX()+15.5-mx)/45)*45+3; //closest column blocks
				by = (int)(Math.round(p.getY()+15.5-60)/45)*45+9; //closest row

				grid[(by-9)/45][(bx-3)/45] = new Block(bx,by,3); //bomb is 3
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
			return 5;
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
					if(hitBlock(m,UP)==false){
						v.add(UP);
						}
					if(hitBlock(m,DOWN)==false){
						v.add(DOWN);
						}
					}
				else if(m.getCurrentDirection()==2 || m.getCurrentDirection()==-2){ //if monster is going up/down
					if(hitBlock(m,LEFT)==false){
						v.add(LEFT);
						}
					if(hitBlock(m,RIGHT)==false){
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

			m.moveStraight(3);
		}
	}
	
	public Boolean playerHitMonster(){
		Rectangle pRect = p.getActualRect(mx);
		
		for(Monster m: monsters){
			Rectangle mRect = m.getActualRect();
			
			if(pHitTimer==0 && pRect.intersects(mRect)){
				System.out.println("player hit monster");
				pHitTimer = 50;
				lives-=1;
				return true;
			}
		}
		return false;
	}
	
	public Boolean bombHitMonster(Monster m){
		return false;
		}
	
	public void newGrid(Block grid[][]){ //resets the 2D grid of the map with the borders and hardblocks
		for(int row=0; row<13; row++){ //clear the grid to be just nulls
			Arrays.fill(grid[row],null);
		}

		for(int x = 0; x<=26; x++){ //add the top and bottom borders
			for(int y = 0; y<13; y+=12){
				grid[y][x]= new Block(x,y,1); //hard block
			}
		}
		for(int x = 0; x<27; x+=26){ //add the left and right borders
			for(int y = 0; y<13; y+=1){
				grid[y][x]=new Block(x,y,1); //hard block
			}
		} 
		for(int x = 2; x<25; x+=2){ //add the hardblocks in the middle
			for(int y = 2; y<11; y+=2){
				grid[y][x]= new Block(x,y,1); //hard block
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
			}
		}
	}
}