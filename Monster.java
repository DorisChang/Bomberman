//Monster.java
//Monster objects for all the enemies in the game.

import java.awt.Rectangle;
import java.util.*;

public class Monster {
	double x, y, speed; 
	int mx, hits, points, currentDirection, spriteCounter, deathTimer; 
	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4;
	public boolean alive;
	String type;
	ArrayList<Integer> path;
	private Rectangle borderRect; //a 45 x 45 square that is used for keeping the monster in the middle of aisles 
	private Rectangle actualRect; //a 31 x 31 square of the actual monster seen on screen
	
    public Monster(String info, ArrayList<Integer> p, Integer mx) { //info <type, speed, hits, points, x, y>
    	String [] infoList = info.split(",");
    	type = infoList[0];
    	speed = Double.parseDouble(infoList[1]);
    	hits = Integer.parseInt(infoList[2]);
    	points = Integer.parseInt(infoList[3]);
    	x = Double.parseDouble(infoList[4]);
    	y = Double.parseDouble(infoList[5]);
    	currentDirection = p.get(0);
    	p.remove(0);
    	path = p;
    	actualRect = new Rectangle((int)(x) , (int)(y) , 31, 31);
    	borderRect = new Rectangle((int)(x - 7), (int)(y - 7), 45, 45);
    	deathTimer = 40;
    	alive = true;
    }
    
    public double getX(){
    	return x;
    }
    	
    public double getY(){
    	return y;
    }

    public int getSpriteCounter(){ //used to determine which sprite should be used
        return spriteCounter;
    }
        
    public void addToSpriteCounter(){
        spriteCounter++;
    }

    public int getPoints(){
        return points;
    }
    
    public double getSpeed(){
    	return speed;
    }
    	
    public String getType(){
    	return type;
    }
    	
    public ArrayList<Integer> getPath(){
    	return path;
    }
    	
    public Rectangle getActualRect(){ //used for detecting collisons
    	actualRect.setLocation((int)(x), (int)(y));
    	return actualRect;
    }

    public void setType(String info){
        String [] infoList = info.split(",");
        type = infoList[0];
        speed = Double.parseDouble(infoList[1]);
        hits = Integer.parseInt(infoList[2]);
        points = Integer.parseInt(infoList[3]);
    }
    	
    public int setHits(int n){ //decrease the number of lives of a monster
    	hits -= n;
    	return hits;
    }
    
    public void setPath(ArrayList<Integer> p){
    	path = p;
    }
    	
    public void pathRemoveFirst(){
    	path.remove(0);
    }
    
    public void addDirection(int d){
    	path.add(d);
    }
    
    public void setCurrentDirection(int d){
    	currentDirection = d;
    }
    
    public int getCurrentDirection(){
    	return currentDirection;
    }
    	
    public void moveStraight(double n){ //keep moving in the current direction
    	if(currentDirection==LEFT){
    		moveLeft(n);
    	}
    	if(currentDirection==RIGHT){
    		moveRight(n);
    	}
    	if(currentDirection==UP){
    		moveUp(n);
    	}
    	if(currentDirection==DOWN){
    		moveDown(n);
    	}
    }
    
    public Rectangle getRect(){
		borderRect = new Rectangle((int)(x-7), (int)(y - 7), 45, 45);
		return borderRect;
	}
	
	public Rectangle getRightRect(){ //returns the rectangle next to it, used for collisions
		Rectangle tempRect = new Rectangle((int)(x-7), (int)(y - 7),46,45);
		return tempRect;
	}
	public Rectangle getLeftRect(){
		Rectangle tempRect = new Rectangle((int)(x-8), (int)(y - 7),45,45);
		return tempRect;
	}
	public Rectangle getUpRect(){
		Rectangle tempRect = new Rectangle((int)(x-7), (int)(y - 8),45,45);
		return tempRect;
	}
	public Rectangle getDownRect(){
		Rectangle tempRect = new Rectangle((int)(x-7), (int)(y - 7),45,46);
		return tempRect;
	}
    
    public void moveUp(double n){
    	y-=n;
    }
    	
    public void moveDown(double n){
    	y+=n;
    }
    	
    public void moveLeft(double n){
    	x-=n;
    }
    	
    public void moveRight(double n){
    	x+=n;
    }
    
    public void setState(boolean b){
    	alive = b;
   	}
    
    public boolean getState(){ //check if the monster has died 
    	return alive;
   	}
    	
    public void deathTimerTick(){ //keep counting down the death timer
    	deathTimer--;
   	}
   	
    public int getDeathStage(){ //returns which phase of death should be shown on the screen
    	return 4-(int)(deathTimer/10);
   	}
}