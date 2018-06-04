import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.util.Arrays;

import java.io.*;

public class testing {
	
    public static void main(String[]args){
		HashTable<Block> softBlocks = new HashTable<Block>();
		softBlocks.add(new Block("330,70,60,60"));
		softBlocks.add(new Block("350,70,60,60"));
		softBlocks.add(new Block("330,70,60,60"));
		//System.out.println(softBlocks.toArray());
		softBlocks.clear();
		//System.out.println(softBlocks.toArray());
		HashTable<String> words = new HashTable<String>();
		words.add("hello");
		words.add("who?");
		words.add("what?");
		//System.out.println(words.toArray());
		words.clear();
		//System.out.println(words.toArray());
		
		
		int grid[][] = new int[13][27];
		grid[1][4]=1;
		System.out.println(Arrays.deepToString(grid));
		for(int row=0; row<13; row++){
			Arrays.fill(grid[row],0);
			}
		for(int x = 0; x<=26; x++){
			for(int y = 0; y<13; y+=12){
				grid[y][x]=1;
				}
			}
		for(int x = 0; x<27; x+=26){
			for(int y = 0; y<13; y+=1){
				grid[y][x]=1;
				}
			} 
		for(int x = 2; x<25; x+=2){
			for(int y = 2; y<11; y+=2){
				grid[y][x]=1;
				}
			}
		//for(int x = )
		//System.out.println(Arrays.deepToString(grid));
		//System.out.println(grid[2][4]);
	}
    
    
}