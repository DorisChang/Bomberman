//Player.java
//player bomberman's movements

public class Player{
	int cx,cy; //player's coords

	public Player(){
		cx = 10;
		cy = 10;
	}

	public int getX(){
		return cx;
	}

	public int getY(){
		return cy;
	}

	public void moveUp(){
		if(cy - 20 > 10){
			cy -= 20;
		}

		//System.out.println(cy);
	}

	public void moveDown(){
		if(cy + 20 < 790){
			cy += 20;
		}

		//System.out.println(cy);
	}

	public void moveRight(){
		if(cx + 20 < 890){
			cx += 20;
		}

		//System.out.println(cx);
	}

	public void moveLeft(){
		if(cx - 20 > 10){
			cx -= 20;
		}

		//System.out.println(cx);
	}

}