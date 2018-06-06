//Player.java
//player bomberman's movements

import java.awt.Rectangle;

public class Player{
	private int cx,cy; //player's coords
	//Rectangle uRect, dRect, lRect, rRect;
	private Rectangle borderRect;
	private Rectangle actualRect;

	public Player(){
		cx = 52;
		cy = 117;

		borderRect = new Rectangle(cx - 3, cy - 3, 37, 37);
		actualRect = new Rectangle(cx, cy, 31, 31);

		/*uRect = new Rectangle(cx,cy-6,31,6);
		dRect = new Rectangle(cx,cy+31,31,6);
		lRect = new Rectangle(cx-6,cy,6,31);
		rRect = new Rectangle(cx+31,cy,6,31);*/
	}

	public int getX(){
		return cx;
	}

	public int getY(){
		return cy;
	}
	
	public Rectangle getActualRect(int mx){
		actualRect.setLocation(cx-mx, cy);
		return actualRect;
		}

	/*public Rectangle getRect(int d){
		if(d == 1){
			return rRect;
		}

		else if(d == 2){
			return lRect;
		}

		else if(d == 3){
			return uRect;
		}

		else if(d == 4){
			return dRect;
		}

		

		return null;
	}*/

	public Rectangle getRect(int mx){
		borderRect = new Rectangle(cx - 3 - mx, cy - 3, 37, 37);
		return borderRect;
	}
	
	public Rectangle getRightRect(int mx){
		Rectangle tempRect = new Rectangle(cx-3-mx, cy-3,40,37);
		return tempRect;
		}
	public Rectangle getLeftRect(int mx){
		Rectangle tempRect = new Rectangle(cx-6-mx, cy-3,37,37);
		return tempRect;
		}
	public Rectangle getUpRect(int mx){
		Rectangle tempRect = new Rectangle(cx-3-mx, cy-6,37,37);
		return tempRect;
		}
	public Rectangle getDownRect(int mx){
		Rectangle tempRect = new Rectangle(cx-3-mx, cy-3,37,40);
		return tempRect;
		}

	public void moveUp(int y){
		cy -= y;

		borderRect.setLocation(cx-3,cy-3);

		/*uRect.setLocation(cx,cy-6);
		dRect.setLocation(cx,cy+31);
		lRect.setLocation(cx-6,cy);
		rRect.setLocation(cx+31,cy);*/
	}

	public void moveDown(int y){
		cy += y;

		borderRect.setLocation(cx-3,cy-3);

		/*uRect.setLocation(cx,cy-6);
		dRect.setLocation(cx,cy+31);
		lRect.setLocation(cx-6,cy);
		rRect.setLocation(cx+31,cy);*/
	}

	public void moveRight(int x){
		cx += x;

		borderRect.setLocation(cx-3,cy-3);

		/*uRect.setLocation(cx,cy-6);
		dRect.setLocation(cx,cy+31);
		lRect.setLocation(cx-6,cy);
		rRect.setLocation(cx+31,cy);*/
	}

	public void moveLeft(int x){
		cx -= x;

		borderRect.setLocation(cx-3,cy-3);

		/*uRect.setLocation(cx,cy-6);
		dRect.setLocation(cx,cy+31);
		lRect.setLocation(cx-6,cy);
		rRect.setLocation(cx+31,cy);*/
	}

	/*public void moveUp(){
		if(cy - 6 > 24){
			cy -= 6;
		}

		//System.out.println(cy);
	}

	public void moveDown(){
		if(cy + 6 < 698){
			cy += 6;
		}

		//System.out.println(cy);
	}

	public void moveRight(){
		if(cx + 6 < 798){
			cx += 6;
		}

		//System.out.println(cx);
	}

	public void moveLeft(){
		if(cx - 6 > 24){
			cx -= 6;
		}

		//System.out.println(cx);
	}*/

}