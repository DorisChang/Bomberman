//Powerups Available
//Bombs - increase # of bombs by 1
//Flames - increase explosion range by 1 square
import java.awt.Rectangle;
import java.util.*;

public class Bomb{
	private int bx, by;
	private int bombRange;
	private Rectangle bRect;
	private int detonateTime, explosionTime;
	private boolean done,added;

	private Rectangle[]explodeRects = new Rectangle[4];

	public Bomb(int x, int y){
		bx = x;
		by = y;

		bRect = new Rectangle(bx,by,39,39);
		for(int i = 0; i < 4; i++){
			explodeRects[i] = new Rectangle(0,0,0,0);
		}
		//resetPos();

		detonateTime = 70;
		explosionTime = 45;

		done = false;
		added = false;
	}

	public int getBX(){
		return bx;
	}

	public int getBY(){
		return by;
	}

	public boolean getStatus(){
		return done;
	}

	public boolean added(){
		return added;
	}

	public void getAdded(){
		added = true;
	}

	public void detonate(){
		if(getDT() > 0){
			tickDT();
		}

		else if(getDT() == 0 && getET() > 0){
			tickET();
		}

		else if(getDT() == 0 && getET() == 0){
			done = true;
		}
	}

	public int getDT(){
		return detonateTime;
	}

	public void tickDT(){
		detonateTime -= 1;
	}

	public int getET(){
		return explosionTime;
	}

	public void tickET(){
		explosionTime -= 1;
	}

	public void explosionTriggered(){
		detonateTime = 0;
	}

	/*public void resetTime(){
		detonate = false;
		detonateTime = 70;
		explosionTime = 45;
	}*/

	public int getRange(){
		return bombRange;
	}

	public Rectangle getBombRect(){
		return bRect;
	}

	public Rectangle getExpRect(int direction){
		return explodeRects[direction];
	}

	public void explode(int direction){
		if(direction == 0){
			explodeRects[0].setBounds(45*bx+explosionTime-42,45*by+69,45-explosionTime,39);
		}

		else if(direction == 1){
			explodeRects[1].setBounds(45*bx+42,45*by+69,45-explosionTime,39);
		}

		else if(direction == 2){
			explodeRects[2].setBounds(45*bx+3,45*by+explosionTime+29,39,45-explosionTime);
		}

		else if(direction == 3){
			explodeRects[3].setBounds(45*bx+3,45*by+108,39,45-explosionTime);
		}
	}
}