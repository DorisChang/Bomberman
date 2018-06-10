import java.util.*;
import java.io.*;

public class HashTable<T>{
	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4;
	private ArrayList<LinkedList<T>> table;
	private int items = 0;
	private int maxLoad = 70;
	private void fill(int n){
		table = new ArrayList<LinkedList<T>>();
		for(int i = 0; i < n; i++){
			table.add(null);
		}
	}
	
	/*public int size(){
		return table.size();
		}*/
	
    public HashTable(){
    	fill(10);
 	}
 	
 	public void add(T val){
 		int hash = Math.abs(val.hashCode());
 		int spot = hash % table.size();
 		LinkedList<T> list = table.get(spot);
 		if(list == null){
 			list = new LinkedList<T>();
 			table.set(spot,list);
 		}
 		if(!list.contains(val)){
 			list.add(val);
 			items++;
 			if(items*100/table.size() > maxLoad){ //above maxLoad full, resize
 				resize();
 			}
 		}
 	}
 	
 	public void remove(T val){
 		int hash = Math.abs(val.hashCode());
 		int spot = hash % table.size();
 		LinkedList<T> list = table.get(spot);
 		list.remove(val); //removes the value where it is found in the list
 		}
 	
 	public T get(int key){ //returns an object given its hashcode
 		int spot = key % table.size();
 		if(table.get(spot)!=null){
 			LinkedList<T> list = table.get(spot);
 			for(int i = 0; i<list.size(); i++){
 				if(list.get(i).hashCode()==key){ //if the object is found
 					return list.get(i); 
 					}
 				}
 			}
 			
 		//System.out.println("not found");
 		return null; //if not found
 		}
 	
 	public double getLoad(){ //return how full the table is (number from 0-1)
 		double i = items;
 		return i/table.size();
 		}
 	
 	public void setMaxLoad(double max){
 		if(0.1<=max && max<=0.8){ //if given value is acceptable
 			if(max<this.getLoad()){ //if the new max is less than current load, then resize
 			 	ArrayList<LinkedList<T>> tmp = table;
			 	fill((int)(items/max));
			 	items = 0;
			 	for(LinkedList<T> L : tmp){
			 		if(L != null){
			 			for(T v : L){
			 				add(v);
			 			}
			 		}
			 	}
 			}
 			else{ //if the new max is more than the current load
 				max = Math.round(max);
 				maxLoad = (int)(max*100.0); //set the new max load
 				}
 			}
 		}
 		
 	public void setLoad(double percent){
 		if(0.1<=percent && percent<=0.8 && percent<(double)(maxLoad/100.00)){ //checkes that the percent is acceptable and require action (ie. it is greater than max load)
 			ArrayList<LinkedList<T>> tmp = table;
			fill((int)(items/percent)); //fill it so that it satisfies the percent given
			items = 0;
			for(LinkedList<T> L : tmp){
				if(L != null){
		 			for(T v : L){
	 				add(v);
		 			}
 				}
			}
 		}
 	}
 	
 	public ArrayList toArray(){
 		ArrayList<T> al = new ArrayList<T>(); 
 		for(LinkedList<T> list:table){
 			if(list!=null){ //if it is not null
 				for(T i:list){
 					al.add(i); //add to the array
 					}
 				}
 			}
 		return al;
 		}
 	
 	public String toString(){
 		String ans = "";
 		for(LinkedList<T> L : table){
 			if(L != null){
 				for(T v : L){
 					ans += v + ",";
 				}
 			}
 		}
 		if(ans.length() > 0){
 			ans = ans.substring(0,ans.length()-1);
 		}
 		return "[[" + ans + "]]";
 	}
 	
 	public void resize(){
 		ArrayList<LinkedList<T>> tmp = table;
 		items = 0;
 		fill(table.size()*10);
 		for(LinkedList<T> L : tmp){
 			if(L != null){
 				for(T v : L){
 					add(v);
 				}
 			}
 		}
 	}
 		
 	/*public Boolean collidesBlock(int key, int direction){ //checks the path of the player in that direction
 		if(direction == RIGHT){ //if walking to the right, check blocks to the right side
	 		for(int x = 0; x<=38; x++){
	 			for(int y = -48; y <38; y++){ //check for blocks, above and below because coordinates are relative to the upper left corner of both the player and blocks
	 				if(key+x*1000+y>0 && this.get(key+x*1000+y)!=null){ //if a block exists at this coordinate then it collideds
	 					return true;
	 					}
	 				}
	 			}
 			}
	 	else if(direction == LEFT){
	 		for(int x = -52; x<1; x++){
	 			for(int y = -48; y <38; y++){
	 				if(key+x*1000+y>0 && this.get(key+x*1000+y)!=null){
	 					return true;
	 					}
	 				}
	 			}
 			}
 		else if(direction == UP){
	 		for(int x = -48; x<=36; x++){
	 			for(int y = -52; y <1; y++){
	 				if(key+x*1000+y>0 && this.get(key+x*1000+y)!=null){
	 					return true;
	 					}
	 				}
	 			}
 			}
 		else if(direction == DOWN){
	 		for(int x = -48; x<=34; x++){
	 			for(int y = 0; y <=40; y++){
	 				if(key+x*1000+y>0 && this.get(key+x*1000+y)!=null){
	 					return true;
	 					}
	 				}
	 			}
 			}
 		return false;
 		}*/
 		
 	public void clear(){ //turns everything inside the hashtable into nulls, used for clearing the softblocks after each level
		table = new ArrayList<LinkedList<T>>();
		for(int i = 0; i < (int)(items/this.getLoad()); i++){ 
			table.add(null);
		}
	}
	
	public ArrayList<Integer> validDirections(Monster m){
		ArrayList<Integer> d = new ArrayList<Integer>();
		for(int i = 1; i<5; i++){
			if(this.collidesBlock(m.getX()*1000+m.getY(),i)==false){
				d.add(i);
				}
			}
		String info = "";
		for(int n: d){
			info+=n+", ";
			}
			
		System.out.println(info);
		
		return d;
		}
}