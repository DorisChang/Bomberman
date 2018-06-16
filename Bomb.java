//Powerups Available
//Bombs - increase # of bombs by 1
//Flames - increase explosion range by 1 square
import java.awt.Rectangle;
import java.util.*;
//import java.awt.*;

public class Bomb{
	private int bx, by, spriteCounter;
	private int bombRange;
	private Rectangle bRect;
	private int detonateTime, explosionTime;
	private boolean done,added;
	private int[][] blockedDirections = new int[4][5]; //[direction][square from bomb]
	//if 0, square is open, 1, square is too far away, if 2, there was originally a soft block there

	private Rectangle[]explodeRects = new Rectangle[4];
	//private Rectangle[]explodeRectsAhead = new Rectangle[4];

	public Bomb(int x, int y, int numInc){
		bx = x;
		by = y;
		spriteCounter = 0;
		bRect = new Rectangle(bx,by,45,45);

		detonateTime = 70;
		explosionTime = 14;

		bombRange = 1;

		for(int i = 0; i < numInc; i++){ //if the range inc powerup has been used
			incRange();
		}

		fillGrid();

		done = false;
		added = false;
	}

	public int getBX(){
		return bx;
	}

	public int getBY(){
		return by;
	}
	
	public int getSpriteCounter(){
		return spriteCounter;
	}
	
	public boolean getStatus(){
		return done;
	}

	public void fillGrid(){
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 5; j++){
				if(j < bombRange){
					blockedDirections[i][j] = 0;
				}

				else{
					blockedDirections[i][j] = 1;
				}
			}
		}
	}

	public void detonate(){ //countdown to explode
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
	
	public int getExplosionStage(){
		return (int)(explosionTime/2);
	}

	public int getBExplosionStage(){
		return (int)((12-explosionTime+2)/3);
	}

	public void tickDT(){
		detonateTime -= 1;
		spriteCounter ++;
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

	public void incRange(){
		bombRange += 1;
	}

	public int getRange(){
		return bombRange;
	}

	public Rectangle getBombRect(){
		return bRect;
	}

	public int blockType(int direction){ //get the type of block at a certain location from the bomb
		for(int i = 1; i < 6; i++){
			if(blockedDirections[direction][i-1] == 2){
				return i;
			}
		}

		return 0;
	}

	public boolean isOccupied(int direction, int n){ //if there is something at the square
		if(blockedDirections[direction][n]  != 0){
			return true;
		}

		return false;
	}

	public void blockPresent(int direction, int n){ //block before it had something there, everything after it cannot be reached
		for(int i = n; i < 5; i++){
			blockedDirections[direction][i] = 1;
		}
	}

	public int countEmpty(int d){ //# of empty squares from bomb
		int count = 0;
		for(int i = 0; i < 5; i++){
			if(blockedDirections[d][i] == 0){
				count += 1;
			}
		}

		return count;
	}

	public void softBlock(int direction, int n){ //the square has a soft block
		blockedDirections[direction][n-1] = 2;
	}
	public void explode(){ //expand the bomb in all directions
		explodeRects[0]= new Rectangle(45*(bx-countEmpty(0))+5,45*by+65+10,45*countEmpty(0)-5,25);
		explodeRects[1]= new Rectangle(45*bx+45,45*by+65+10,45*countEmpty(1)-5,25);
		explodeRects[2]= new Rectangle(45*bx+10,45*(by-countEmpty(2))+65+5,25,45*countEmpty(2)-5);
		explodeRects[3]= new Rectangle(45*bx+10,45*by+110,25,45*countEmpty(3)-5);
	}

	public Rectangle getExpRect(int direction){
		return explodeRects[direction];
	}
}