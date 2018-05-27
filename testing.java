import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.Timer;

import java.io.*;

public class testing {
	
    public static void main(String[]args){
		HashTable<Block> softBlocks = new HashTable<Block>();
		softBlocks.add(new Block("330,70,60,60"));
		softBlocks.add(new Block("350,70,60,60"));
		softBlocks.add(new Block("330,70,60,60"));
		System.out.println(softBlocks.toArray());
		softBlocks.clear();
		System.out.println(softBlocks.toArray());
		HashTable<String> words = new HashTable<String>();
		words.add("hello");
		words.add("who?");
		words.add("what?");
		System.out.println(words.toArray());
		words.clear();
		System.out.println(words.toArray());
	}
    
    
}