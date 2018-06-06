//finalProject.java
//Minya Bai & Doris Chang
import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
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
	private int mx;
	private int sx; //screen movement
	
	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4;
	
	private Block grid[][] = new Block [13][27];
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	private ArrayList<String> allMonsters = new ArrayList<String>();
	
	
	public GamePanel(){
		setFocusable(true);
		addKeyListener(this);
		requestFocus();
		newGrid(grid);
		addSoftBlocks(50);
		
		mx = 0;

		tBkg = new ImageIcon("temp_bkg.png").getImage();

		p = new Player();

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
		for(int i =0 ; i<20; i++){
			addMonster(0);
			}
		
		//System.out.println(monsters.size());
		//for(Monster m: monsters){
			//System.out.printf("X: %d  Y: %d\n",m.getX(),m.getY());
		//}
	}

	@Override
	public void paintComponent(Graphics g){
		//g.drawImage(tBkg,mx,0,this);
		g.setColor(finalProject.FLOOR_COLOUR);
		g.fillRect(0,0,1600,832);
		
		g.setColor(new Color(23,223,186));
		g.fillRect(p.getX(),p.getY(),31,31);
		
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

		g.setColor(new Color(0,0,255));
		
		Rectangle bRect = p.getRect(0);
		g.drawRect((int)(bRect.getX()),(int)(bRect.getY()),(int)(bRect.getWidth()),(int)(bRect.getHeight()));
		
	}

	public void start(){
		moveMan();
		for(Monster m:monsters){
			moveMonster(m,m.getPath());
			}
		hitMonster();
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
		for(int d = 1; d<5; d++){
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
			//System.out.println(v);
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
			
				
			//System.out.println(m.getCurrentDirection());
			if(hitBlock(m,m.getCurrentDirection())){
				//System.out.println("hit block, change direction");
				m.setCurrentDirection(validRandomDirection(m));
			}
			m.moveStraight(3);
		}
		/*else{
			if(path.size()>0){
				System.out.printf("X: %d Y: %d ",(m.getX()-7)%45,(m.getY()-72)%45);
				System.out.println(path.get(0));
				int nextInstruction = path.get(0); //gets the next direction the monster should head to
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
				
				m.moveStraight(3); //move in the specified direction
				
				if((m.getY()-72)%45==0 && (m.getX()-7)%45==0){ //if a direction was followed, remove it 
					path.remove(0);
				}
			}

		}*/
	}
	
	public Boolean hitMonster(){
		Rectangle pRect = p.getActualRect(mx);
		//System.out.println(pRect.toString());
		for(Monster m: monsters){
			Rectangle mRect = m.getActualRect();
			//System.out.println(mRect.toString());
			//System.out.println();
			if(pRect.intersects(mRect)){
				//System.out.println("true "+Math.random());
				return true;
			}
		}
		//System.out.println("false");
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
				//blocks.add(new Block(""+randX*45+","+(randY*45+110)+",45,45"));
				grid[randY][randX]= new Block(randX,randY,2); //soft block
			}
			//System.out.printf("randX: %d, randY: %d\n",randX,randY);
		}
		//System.out.println(blocks.toArray().size());
	}
}