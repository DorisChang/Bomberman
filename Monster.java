//Monster.java
//monster objects for all the enemies in the game

//https://strategywiki.org/wiki/Super_Bomberman/Enemies
//http://bomberman.wikia.com/wiki/Bomberman_(NES)

import java.awt.Rectangle;
import java.util.*;

public class Monster {
	double x, y, speed; 
	int mx, hits, points, currentDirection,spriteCounter; 
	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4;
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
    	
    	/*path.add(LEFT);
    	path.add(RIGHT);
		path.add(DOWN);
		path.add(LEFT);
		path.add(RIGHT);
		path.add(DOWN);
		path.add(LEFT);
		path.add(RIGHT);
		path.add(DOWN);
		path.add(LEFT);*/
    }
    
    public double getX(){
    	return x;
    }
    	
    public double getY(){
    	return y;
    }

    public int getSpriteCounter(){
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
    	
    public Rectangle getActualRect(){
    	actualRect.setLocation((int)(x), (int)(y));
    	return actualRect;
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
    	
    public void moveStraight(double n){
 		//System.out.println(currentDirection);
    	if(currentDirection==LEFT){
    		moveLeft(n);
            spriteCounter++;
    	}
    	if(currentDirection==RIGHT){
    		moveRight(n);
            spriteCounter++;
    	}
    	if(currentDirection==UP){
    		moveUp(n);
            spriteCounter++;
    	}
    	if(currentDirection==DOWN){
    		moveDown(n);
            spriteCounter++;
    	}
    }
    
    public Rectangle getRect(){
		borderRect = new Rectangle((int)(x-7), (int)(y - 7), 45, 45);
		return borderRect;
	}
	
	public Rectangle getRightRect(){
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
    	
}