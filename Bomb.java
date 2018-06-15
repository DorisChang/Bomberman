//Powerups Available
//Bombs - increase # of bombs by 1
//Flames - increase explosion range by 1 square
import java.awt.Rectangle;
import java.util.*;

public class Bomb{
	private int bx, by, spriteCounter;
	private int bombRange;
	private Rectangle bRect;
	private int detonateTime, explosionTime;
	private boolean done,added;
	private int[][] blockedDirections = new int[4][5];

	private Rectangle[]explodeRects = new Rectangle[4];
	//private Rectangle[]explodeRectsAhead = new Rectangle[4];

	public Bomb(int x, int y){
		bx = x;
		by = y;
		spriteCounter = 0;
		bRect = new Rectangle(bx,by,45,45);
		
		/*for(Rectangle r : explodeRects){
			r = new Rectangle (0,0,45,45);
		}*/
		//resetPos();

		detonateTime = 70;
		explosionTime = 14;

		bombRange = 1;
		//activeBombs = ;

		incRange();
		incRange();

		fillGrid();

		//System.out.println(bombRange);

		done = false;
		added = false;

		/*for(int i = 0; i < 4; i++){
			blockedDirections[i] = 0;
		}*/

		System.out.println("BEFORE: "+Arrays.toString(blockedDirections));
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
		//System.out.println(bombRange);
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 5; j++){
				if(j < bombRange){
					blockedDirections[i][j] = 0;
				}

				else{
					blockedDirections[i][j] = 1;
				}
			}

			//System.out.println(Arrays.toString(blockedDirections[i]));
		}
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

	/*public void noActiveBombs(){
		int totBombs = 0;
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 5; j++){
				totBombs +=  blockedDirections[i][j];
			}
		}

		if(totBombs == 25){
			detonateTime = 0;
			explosionTime = 0;
		}
	}*/

	public int getDT(){
		return detonateTime;
	}
	
	public int getExplosionStage(){
		return (int)(explosionTime/2);
		}

	public void tickDT(){
		detonateTime -= 1;
		spriteCounter ++;
		
		//System.out.println(spriteCounter);
	}

	public int getET(){
		return explosionTime;
	}

	public void tickET(){
		explosionTime -= 1;//*bombRange;
	}

	public void explosionTriggered(){
		detonateTime = 0;
	}

	public void incRange(){
		bombRange += 1;
		//explosionTime *= bombRange;
	}

	public int getRange(){
		return bombRange;
	}

	public Rectangle getBombRect(){
		return bRect;
	}

	public boolean isOccupied(int direction, int n){
		if(blockedDirections[direction][n]  != 0){
			return true;
		}

		return false;
	}

	public void blockPresent(int direction, int n){
		for(int i = n; i < 5; i++){
			blockedDirections[direction][i] = 1;
		}

		System.out.println(direction + " : "+ Arrays.toString(blockedDirections[direction]));
	}

	public int countEmpty(int d){
		int count = 0;
		for(int i = 0; i < 5; i++){
			if(blockedDirections[d][i] == 0){
				count += 1;
			}
		}

		return count;
	}

	/*public Rectangle getExpRect(int direction){
		return explodeRects[direction];
	}*/

	/*public void explosionBlocked(int direction){
		blockedDirections[direction] = 1;
		System.out.println(blockedDirections);
	}*/

	public void explode(){
		explodeRects[0]= new Rectangle(45*(bx-countEmpty(0)),45*by+65+10,45*countEmpty(0),25);
		explodeRects[1]= new Rectangle(45*bx+45,45*by+65,45*countEmpty(1)+10,25);
		explodeRects[2]= new Rectangle(45*bx+10,45*(by-countEmpty(2))+65,25,45*countEmpty(2));
		explodeRects[3]= new Rectangle(45*bx+10,45*by+110,25,45*countEmpty(3));
	}

	public Rectangle getExpRect(int direction){
		return explodeRects[direction];
	}

	/*public boolean nextBlockClear(int direction){
		if(blockedDirections[direction] == 0){ //direction is clear
			//System.out.println("NO BLOCKS");
			return true;
		}

		//System.out.println("BLOCK!");
		return false;
	}

	public void blockAhead(int direction){
		//System.out.println("Direction: "+direction);
		//System.out.println("BEFORE: "+Arrays.toString(blockedDirections));
		blockedDirections[direction] = 1;
		//System.out.println(Arrays.toString(blockedDirections));
	}

	public Rectangle checkAhead(int direction){
		return explodeRectsAhead[direction];
	}*/
}