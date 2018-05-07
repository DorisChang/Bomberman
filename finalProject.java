import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

public class finalProject extends JFrame implements ActionListener{
	Timer myTimer;
	Timer powerUpTimer;
	GamePanel game;

	public finalProject(){
		super("Tron");
		setSize(855,800);
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
	//two hashtables one for soft blocks and one for hard blocks

	public GamePanel(){
		setFocusable(true);
		addKeyListener(this);
		requestFocus();

		mx = 0;
		my = 0;

		tBkg = new ImageIcon("temp_bkg.png").getImage();

		p = new Player();

		keys = new boolean[KeyEvent.KEY_LAST+1];
	}

	@Override
	public void paintComponent(Graphics g){
		g.drawImage(tBkg,mx,my,this);
		//g.setColor(new Color(100,100,100));
		//g.fillRect(0,0,900,800);

		g.setColor(new Color(23,223,186));
		g.fillRect(p.getX(),p.getY(),30,30);
	}

	public void start(){
		moveMan();

		//System.out.println("HI");
	}

	//ALL THREE MUST BE PRESENT
	public void keyPressed(KeyEvent e){
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e){
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e){}

	public void moveMan(){
		//System.out.println("MOVE");
		if(keys[KeyEvent.VK_D]){
			//System.out.println("Right");
			p.moveRight();

			if(p.getX() > 450 && mx > -500){
				mx -= 20;
			}
		}
		else if(keys[KeyEvent.VK_A]){
			//System.out.println("Left");
			p.moveLeft();

			if(p.getX() < 450 && mx < 0){
				mx += 20;
			}
		}
		else if(keys[KeyEvent.VK_W]){
			//System.out.println("Up");
			p.moveUp();

			if(p.getY() < 400 && my < 0){
				my += 20;
			}
		}
		else if(keys[KeyEvent.VK_S]){
			//System.out.println("Down");
			p.moveDown();

			if(p.getY() > 400 && my > -200){
				my -= 20;
			}
		}
	}
}