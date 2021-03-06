//Player.java
//player bomberman's movements

import java.awt.Rectangle;

public class Player{
	private int cx,cy; //player's coords
	private int deathTimer;
	private int direction = 1;
	private int spriteCounter;
	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4;
	public boolean alive;

	private Rectangle borderRect; //square that borders the player
	private Rectangle actualRect; //actual place of player

	private boolean wallPass,bombPass,flamePass;

	public Player(){
		cx = 52;
		cy = 117;
		deathTimer = 60;
		alive = true;

		borderRect = new Rectangle(cx, cy - 3, 31, 37);
		actualRect = new Rectangle(cx, cy, 31, 31);

		wallPass = false;
		bombPass = false;
		flamePass = false;
	}

	public int getX(){
		return cx;
	}

	public int getY(){
		return cy;
	}
	
	public Rectangle getActualRect(int mx){
		actualRect.setLocation(cx-mx, cy);
		return actualRect;
	}

	public int getDirection(){
		return direction;
	}
		
	public void setDirection(int d){
		direction = d;
	}
		
	public int getSpriteCounter(){
		return spriteCounter;
	}
		
	public void addToSpriteCounter(){
		spriteCounter++;
	}

	public Rectangle getRect(int mx){
		borderRect = new Rectangle(cx - mx, cy - 3, 31, 37);
		return borderRect;
	}
	
	//check around the player
	public Rectangle getRightRect(int mx){
		Rectangle tempRect = new Rectangle(cx-3-mx, cy-3,40,37);
		return tempRect;
	}
	public Rectangle getLeftRect(int mx){
		Rectangle tempRect = new Rectangle(cx-6-mx, cy-3,37,37);
		return tempRect;
	}
	public Rectangle getUpRect(int mx){
		Rectangle tempRect = new Rectangle(cx-3-mx, cy-6,37,37);
		return tempRect;
	}
	public Rectangle getDownRect(int mx){
		Rectangle tempRect = new Rectangle(cx-3-mx, cy-3,37,40);
		return tempRect;
	}

	public void moveUp(int y){
		cy -= y;
		direction = UP;
		spriteCounter ++;
		borderRect.setLocation(cx-3,cy-3);
	}

	public void moveDown(int y){
		cy += y;
		direction = DOWN;
		spriteCounter ++;
		borderRect.setLocation(cx-3,cy-3);
	}

	public void moveRight(int x){
		cx += x;
		direction = RIGHT;
		spriteCounter ++;
		borderRect.setLocation(cx-3,cy-3);
	}

	public void moveLeft(int x){
		cx -= x;
		direction = LEFT;
		spriteCounter ++;
		borderRect.setLocation(cx-3,cy-3);
	}

	public void canPassWalls(){
		wallPass = true;
	}

	public void canPassBombs(){
		bombPass = true;
	}

	public void canPassFlames(){
		flamePass = true;
	}

	public boolean passWall(){
		return wallPass;
	}

	public boolean passFire(){
		return flamePass;
	}

	public boolean passBomb(){
		return bombPass;
	}
	
	public void setState(boolean b){
    	alive = b;
    }
    
    public boolean getState(){
    	return alive;
    }
    	
    public void deathTimerTick(){
    	deathTimer--;
    }
    	
    public int getDeathStage(){
    	return 6-(int)(deathTimer/10);
    }
    
    public int getDeathTimer(){
    	return deathTimer;
    	}

}