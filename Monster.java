//Monster.java
//monster objects for all the enemies in the game

//https://strategywiki.org/wiki/Super_Bomberman/Enemies
//http://bomberman.wikia.com/wiki/Ballom

import java.util.*;
public class Monster {
	int x, y, speed, hits, points; 
	String type;
	
    public Monster(String info) { //info <type, speed, hits, points, x, y>
    	String [] infoList = info.split(",");
    	type = infoList[0];
    	speed = Integer.parseInt(infoList[1]);
    	hits = Integer.parseInt(infoList[2]);
    	points = Integer.parseInt(infoList[3]);
    	x = Integer.parseInt(infoList[4]);
    	y = Integer.parseInt(infoList[5]);
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