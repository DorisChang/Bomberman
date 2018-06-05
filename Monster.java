//Monster.java
//monster objects for all the enemies in the game

//https://strategywiki.org/wiki/Super_Bomberman/Enemies
//http://bomberman.wikia.com/wiki/Ballom

import java.awt.Rectangle;

import java.util.*;
public class Monster {
	int x, y, speed, hits, points, currentDirection;  
	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4;
	String type;
	ArrayList<Integer> path;
	private Rectangle borderRect;
	
    public Monster(String info,ArrayList<Integer> p) { //info <type, speed, hits, points, x, y>
    	String [] infoList = info.split(",");
    	type = infoList[0];
    	speed = Integer.parseInt(infoList[1]);
    	hits = Integer.parseInt(infoList[2]);
    	points = Integer.parseInt(infoList[3]);
    	x = Integer.parseInt(infoList[4]);
    	y = Integer.parseInt(infoList[5]);
    	
    	//currentDirection = Integer.parseInt(infoList[6]);
    	currentDirection = p.get(0);
    	borderRect = new Rectangle(x - 3, x - 3, 37, 37);
    	
    	path = p;
    }
    
    public int getX(){
    	return x;
    	}
    	
    public int getY(){
    	return y;
    	}
    
    public int getSpeed(){
    	return speed;
    	}
    	
    public String getType(){
    	return type;
    	}
    	
    public int setHits(int n){ //decrease the number of lives of a monster
    	hits -= n;
    	return hits;
    	}
    
    public int xDirection(int pX){ //return -1 for left, 1 for right
    	if(pX<x){
    		return -1;
    		}
    	else{
    		return 1;
    		}
    	}
    	
    public int yDirection(int pY){ //return -1 for up, 1 for down
    	if(pY<y){
    		return -1;
    		}
    	else{
    		return 1;
    		}
    	}
    
    public void setPath(ArrayList<Integer> np){
    	path = np;
    	}
    	
    public ArrayList<Integer> getPath(){
    	return path;
    	}
    	
    public void setCurrentDirection(int d){
    	currentDirection = d;
    	}
    	
    public void moveStraight(int n){
 		//System.out.println(currentDirection);
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
    	
    public Rectangle getRect(int mx){
		borderRect = new Rectangle(x - 3 - mx, y - 3, 37, 37);
		return borderRect;
	}
	
	public Rectangle getRightRect(int mx){
		Rectangle tempRect = new Rectangle(x-3-mx, y-3,40,37);
		return tempRect;
		}
	public Rectangle getLeftRect(int mx){
		Rectangle tempRect = new Rectangle(x-6-mx, y-3,37,37);
		return tempRect;
		}
	public Rectangle getUpRect(int mx){
		Rectangle tempRect = new Rectangle(x-3-mx, y-6,37,37);
		return tempRect;
		}
	public Rectangle getDownRect(int mx){
		Rectangle tempRect = new Rectangle(x-3-mx, y-3,37,40);
		return tempRect;
		}
    
    public void moveUp(int n){
    	y-=n;
    	}
    	
    public void moveDown(int n){
    	y+=n;
    	}
    	
    public void moveLeft(int n){
    	x-=n;
    	}
    	
    public void moveRight(int n){
    	x+=n;
    	}
    	
}