import java.util.*;
import java.io.*;
import java.awt.*;

public class Block {
    public static final int HARD = 1, SOFT = 2;
    private int type;
    private Rectangle r;

    public Block(int x, int y, int t) { //t can either be 1 (hard block) or 2 (soft block), or 3 (bomb), or 4 (gate)
        r = new Rectangle(45*x,45*y+65,45,45);

        type = t;
    }

    public int getType(){
        return type;
    }
    	
    public Rectangle getRect(){
        return r;
    } 
    
}