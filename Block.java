import java.util.*;
import java.io.*;
import java.awt.*;

public class Block {
	private int x,y,w,l;
    public Block(String info) {
    	String [] infoList = info.split(",");
 
    	x = Integer.parseInt(infoList[0]);
    	y = Integer.parseInt(infoList[1]);
    	w = Integer.parseInt(infoList[2]);
    	l = Integer.parseInt(infoList[3]);
    }
    	
    public int getX(){
    	return x;
    	}
    	
    public int getY(){
    	return y;
    	}
    
    @Override 
    public int hashCode(){
    	return (int)(x*1000+y); 
    	}
    
}