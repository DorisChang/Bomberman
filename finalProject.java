import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.util.Hashtable;
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
		myTimer = new Timer(60,this);
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
	private int mx,my;
	private int sx; //screen movement
	
	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4;
	
	private int grid[][] = new int[13][27];
	private HashTable<Block> hardBlocks = new HashTable<Block>();
	private HashTable<Block> softBlocks = new HashTable<Block>();
	// private HashTable<Integer, Block> hardBlocks = new HashTable<Integer, Block>();
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	
	private ArrayList<String> allMonsters = new ArrayList<String>();
	
	//two hashtables one for soft blocks and one for hard blocks

	public GamePanel(){
		setFocusable(true);
		addKeyListener(this);
		requestFocus();
		newGrid(grid);
		addSoftBlocks(50);
		//System.out.println(Arrays.deepToString(grid));
		
		mx = 0;
		my = 0;

		tBkg = new ImageIcon("temp_bkg.png").getImage();

		p = new Player();

		keys = new boolean[KeyEvent.KEY_LAST+1];
			
		
		try{ //reading the monster file and adding it to the HashTable of emotions
			Scanner inFile = new Scanner(new BufferedReader(new FileReader("monsters.txt")));
			while(inFile.hasNextLine()){
				String nextLine = inFile.nextLine();
				allMonsters.add(nextLine);
				}	
			}
		catch(IOException ex){
			System.out.println("monsters.txt not found"); //couldn't find the txt file
			}
		
		//System.out.println(allMonsters.get(0));
		//for(int i=0;i<5; i++){
			//addMonster(0);
		//	}
		
			
		System.out.println(monsters.size());
		for(Monster m: monsters){
			//System.out.printf("X: %d  Y: %d\n",m.getX(),m.getY());
			}
	}

	@Override
	public void paintComponent(Graphics g){
		//g.drawImage(tBkg,mx,0,this);
		g.setColor(finalProject.FLOOR_COLOUR);
		g.fillRect(0,0,1600,832);
		
		g.setColor(new Color(23,223,186));
		g.fillRect(p.getX(),p.getY(),31,31);

		//BORDERS
		//Horizontal Borders
		//int n =0;
		/*for(int i = 0; i < 1275; i +=45){
			g.setColor(finalProject.HARD_BLOCK_COLOUR);
			g.fillRect(i+mx,65,45,45);
			g.fillRect(i+mx,605,45,45);

			g.setColor(new Color(255,0,0));
			g.drawRect(i+mx,65,45,45);
			g.drawRect(i+mx,605,45,45);
			//n++;
		}
		//System.out.printf("number: %d\n",n);
		
		//Vertical Borders
		for(int i = 110; i < 745; i +=45){
			g.setColor(finalProject.HARD_BLOCK_COLOUR);
			g.fillRect(mx,i,45,45);
			g.fillRect(1170+mx,i,45,45);

			g.setColor(new Color(255,0,0));
			g.drawRect(mx,i,45,45);
			g.drawRect(1170+mx,i,45,45);
		}
		
		//HARDBLOCKS 
		for(int i = 90; i < 1150; i+=90){
			for(int j = 155; j < 605; j+=90){
				String info = ""+i+","+j+",45,45";
				hardBlocks.add(new Block(info));

				g.setColor(finalProject.HARD_BLOCK_COLOUR);
				g.fillRect(i+mx,j,45,45);

				g.setColor(new Color(255,0,0));
				g.drawRect(i+mx,j,45,45);
			}
		}*/
		
		for(int r=0; r<13; r++){ //row
			for(int c=0; c<27; c++){ //column
				if(grid[r][c]==1){
					g.setColor(finalProject.HARD_BLOCK_COLOUR);
					g.fillRect(c*45+mx,r*45+65,45,45);

					g.setColor(new Color(255,0,0));
					g.drawRect(c*45+mx,r*45+65,45,45);
					}
				if(grid[r][c]==2){
					g.setColor(finalProject.SOFT_BLOCK_COLOUR);
					g.fillRect(c*45+mx,r*45+65,45,45);

					g.setColor(new Color(255,0,0));
					g.drawRect(c*45+mx,r*45+65,45,45);
					}
				}
			}
		
		//SOFTBLOCKS
		ArrayList<Block> sBlocks = softBlocks.toArray();
		for(Block b : sBlocks ){
			g.setColor(finalProject.SOFT_BLOCK_COLOUR);
			g.fillRect(b.getX()+mx,b.getY(),45,45);
			//System.out.printf("bX: %d, bY: %d \n",b.getX(),b.getY());
			g.setColor(new Color(0,255,0));
			g.drawRect(b.getX()+mx,b.getY(),45,45);
			}
		
		//DRAWING MONSTERS aka enemies
		for(Monster m : monsters){
			g.setColor(new Color(250,0,0));
			g.fillRect(m.getX()+mx,m.getY(),31,31);
			//System.out.printf("bX: %d, bY: %d \n",m.getX(),m.getY());
			g.setColor(new Color(0,0,0));
			g.drawRect(m.getX()+mx,m.getY(),31,31);
			}
		
	}

	public void start(){
		moveMan();
		//for(Monster m:monsters){
		//	moveMonster(m);
		//	}
	}

	//ALL THREE MUST BE PRESENT
	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e){}

	/*public boolean hitBlock(int direction){
		if(hardBlocks.collidesBlock((p.getX()+sx)*1000+p.getY(),direction)!= true && softBlocks.collidesBlock((p.getX()+sx)*1000+p.getY(),direction)!= true){
			return false;
			}
		return true;
	}*/
	
	public Boolean hitBlock(int direction){
		
		double c = (p.getX()+15.5)/45; //closest column
		double r = (p.getY()+15.5-65)/45; //closest row
		System.out.printf("column: %f, row: %f\n", c,r);
		//if(direction == RIGHT){
		//	 }
		return false;
		}

	public void moveMan(){
		if(keys[KeyEvent.VK_RIGHT]){
			
			if(p.getX()<= 1130+mx && hitBlock(RIGHT) == false){
				
				if(p.getX() > 225 && mx > -495){
					mx -= 6;
					sx += 6;
				}

				else{
					p.moveRight();
				}
			}
		}
		else if(keys[KeyEvent.VK_LEFT]){
			if(p.getX()>= 54 && hitBlock(LEFT) == false){
				if(p.getX() < 235 && mx < 0){
					mx += 6;
					sx -= 6;
				}

				else{
					p.moveLeft();
				}
			}
		}
		else if(p.getY()>=118 && keys[KeyEvent.VK_UP]){
			//System.out.println("Up");
			if(hitBlock(UP) == false){
				p.moveUp();
			}
		}
		else if(keys[KeyEvent.VK_DOWN]){
			//System.out.println("Down");
			if(p.getY()<= 565 && hitBlock(DOWN) == false){
				p.moveDown();
			}
		}
	}
	
	public void newGrid(int grid[][]){ //resets the 2D grid of the map with the borders and hardblocks
		for(int row=0; row<13; row++){ //clear the grid to be just zeros
			Arrays.fill(grid[row],0);
			}
		for(int x = 0; x<=26; x++){ //add the top and bottom borders
			for(int y = 0; y<13; y+=12){
				grid[y][x]=1;
				}
			}
		for(int x = 0; x<27; x+=26){ //add the left and right borders
			for(int y = 0; y<13; y+=1){
				grid[y][x]=1;
				}
			} 
		for(int x = 2; x<25; x+=2){ //add the hardblocks in the middle
			for(int y = 2; y<11; y+=2){
				grid[y][x]=1;
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
			if(randX == 2 && randY == 0 ||  randX == 1 && randY == 1 || randX==1 && randY ==0){
				addSoftBlocks(1);
				}
			else{
				//blocks.add(new Block(""+randX*45+","+(randY*45+110)+",45,45"));
				grid[randY][randX]=2;	
				}
			//System.out.printf("randX: %d, randY: %d\n",randX,randY);
			}
		//System.out.println(blocks.toArray().size());
		}
		
		
	
	/*public void addMonster(int type){
		Random rand = new Random();
		int randX = rand.nextInt(24)+1; //randomly generated X 
		int randY = rand.nextInt(10)+1; //randomly generated Y
		if (randX%2 == 0){
			randY+=(randY%2==1?1:0);
			}
		if(softBlocks.get(randX*45*1000+randY*45+110)==null && hardBlocks.get(randX*1000+randY+110)==null){ //check that the spot is empty (ie no hardblocks or softblocks at that location)
			if(softBlocks.get((randX+1)*45*1000+randY*45+110)!=null && softBlocks.get((randX-1)*45*1000+randY*45+110)!=null && hardBlocks.get(randX*1000+randY+45+110)!=null
				&& hardBlocks.get(randX*1000+randY-45+110)!=null || hardBlocks.get((randX+1)*45*1000+randY*45+110)!=null && hardBlocks.get((randX-1)*45*1000+randY*45+110)!=null
			    && softBlocks.get(randX*1000+randY+45+110)!=null
				&& softBlocks.get(randX*1000+randY-45+110)!=null){ //check that the monster won't be immediately surrounded by blocks
					System.out.println("not valid spot");
					addMonster(type);
					}
			else{ //found a suitable spot
				monsters.add(new Monster(allMonsters.get(type)+","+(randX*45+7)+","+(randY*45+117)));	
				}
			}
			else{ //current spot is occupied by a soft/hard block
				addMonster(type);
				}
		}
		
	public void moveMonster(Monster m){
		softBlocks.validDirections(m);
		//rx = (m.getX()-7)/45; //row #
		//ry = (m.getY()-7)/45; //row #
		//if()
		}*/
}