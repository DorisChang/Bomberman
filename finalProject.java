import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.util.Hashtable;
import java.io.*;


/*import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.io.*;*/

public class finalProject extends JFrame implements ActionListener{
	Timer myTimer;
	Timer powerUpTimer;
	GamePanel game;
	
	
	public finalProject(){
		super("Bomberman");
		//setSize(480,460); //440 y screen size
		setSize(960,920);
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

	private HashTable<Block> hardBlocks = new HashTable<Block>();
	private HashTable<Block> softBlocks = new HashTable<Block>();
	
	//two hashtables one for soft blocks and one for hard blocks

	public GamePanel(){
		setFocusable(true);
		addKeyListener(this);
		requestFocus();
		addSoftBlocks(softBlocks);
		mx = 0;
		my = 0;

		tBkg = new ImageIcon("temp_bkg.png").getImage();

		p = new Player();

		keys = new boolean[KeyEvent.KEY_LAST+1];
	}

	@Override
	public void paintComponent(Graphics g){
		//g.drawImage(tBkg,mx,0,this);
		g.setColor(new Color(100,100,100));
		g.fillRect(0,0,1600,832);

		g.setColor(new Color(23,223,186));
		g.fillRect(p.getX(),p.getY(),40,40);

		//borders
		for(int i = 0; i < 1860; i +=60){
			g.setColor(new Color(0,255,0));
			g.fillRect(i+mx,100,60,60); //y = 50
			g.fillRect(i+mx,820,60,60);

			g.setColor(new Color(255,0,0));
			g.drawRect(i+mx,100,60,60);
			g.drawRect(i+mx,820,60,60);
		}

		for(int i = 100; i < 932; i +=60){
			g.setColor(new Color(0,255,0));
			g.fillRect(mx,i,60,60);
			g.fillRect(1800+mx,i,60,60);

			g.setColor(new Color(255,0,0));
			g.drawRect(mx,i,60,60);
			g.drawRect(1800+mx,i,60,60);
		}

		//change so there are only 5 hard blocks downwards and 15 blocks across
		//block are 60 by 60
		for(int i = 120; i < 1860; i+=120){
			for(int j = 220; j < 800; j+=120){
				String info = ""+i+","+j+",60,60";
				hardBlocks.add(new Block(info));

				g.setColor(new Color(0,255,0));
				g.fillRect(i+mx,j,60,60);

				g.setColor(new Color(255,0,0));
				g.drawRect(i+mx,j,60,60);
			}
		}
		
		
		ArrayList<Block> sBlocks = softBlocks.toArray();
		for(Block b : sBlocks ){
			g.setColor(new Color(255,0,0));
			g.fillRect(b.getX()+mx,b.getY(),60,60);
			//System.out.printf("bX: %d, bY: %d \n",b.getX(),b.getY());
			g.setColor(new Color(0,255,0));
			g.drawRect(b.getX()+mx,b.getY(),60,60);
			}
		
		
	}

	public void start(){
		moveMan();
	}

	//ALL THREE MUST BE PRESENT
	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e){}

	public boolean hitBlock(int direction){
		if(hardBlocks.collidesBlock((p.getX()+sx)*1000+p.getY(),direction)!= true && softBlocks.collidesBlock((p.getX()+sx)*1000+p.getY(),direction)!= true){
			return false;
			}
		return true;
	}

	public void moveMan(){
		if(keys[KeyEvent.VK_RIGHT]){
			
			if(hitBlock(RIGHT) == false){
				
				if(p.getX() > 225 && mx > -450){
					mx -= 6;
					sx += 6;
				}

				else{
					p.moveRight();
				}
			}
		}
		else if(keys[KeyEvent.VK_LEFT]){
			if(hitBlock(LEFT) == false){
				if(p.getX() < 235 && mx < 0){
					mx += 6;
					sx -= 6;
				}

				else{
					p.moveLeft();
				}
			}

			else{
				p.moveRight();
			}
		}
		else if(keys[KeyEvent.VK_UP]){
			//System.out.println("Up");
			if(hitBlock(UP) == false){
				p.moveUp();
			}
		}
		else if(keys[KeyEvent.VK_DOWN]){
			//System.out.println("Down");
			if(hitBlock(DOWN) == false){
				p.moveDown();
			}
		}
	}
	
	public void addSoftBlocks(HashTable <Block> blocks){
		Random rand = new Random();
		for(int n = 0; n<=40; n++){
			int randX = rand.nextInt(20)+1; //randomly generated X 
			int randY = rand.nextInt(8)+1; //randomly generated Y
			if (randX%2 == 0){
				randY+=(randY%2==1?1:0);
				}
			//randX+=(randX%2==0?1:0);
			
			blocks.add(new Block(""+randX*60+","+(randY*60+160)+",60,60"));
			System.out.printf("randX: %d, randY: %d\n",randX,randY);
		
			}
				/*String info = ""+i+","+j+",60,60";
				hardBlocks.add(new Block(info));*/
		}
	
	public void addMonster(){
		
		}
}