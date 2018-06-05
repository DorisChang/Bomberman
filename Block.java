import java.util.*;
import java.io.*;
import java.awt.*;

public class Block {
    public static final int HARD = 1, SOFT = 2;
    private int type;
    private Rectangle r;

    //private HashTable<BlockRect> hardBlocks = new HashTable<BlockRect>();
    //private HashTable<BlockRect> softBlocks = new HashTable<BlockRect>();

    public Block(int x, int y, int t) { //t can either be 1 (hard block) or 2 (soft block)
    	//BlockRect wall = new BlockRect(x,y,45);

        r = new Rectangle(45*x,45*y+65,45,45);

        type = t;

        /*if(t == HARD){
            hardBlocks.add(wall);
        }

        else if(t == SOFT){
            softBlocks.add(wall);
        }*/
    }

    public int getType(){
        return type;
    }

    /*public int getType(int x, int y){
        int key = (int)(x*1000+y);

        if(hardBlocks.get(key) != null){
            return HARD;
        }

        if(softBlocks.get(key) != null){
            return SOFT;
        }

        return 0;
    }*/
    	
    public Rectangle getRect(){
        return r;
    } 
    
}