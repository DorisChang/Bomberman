//Player.java
//player bomberman's movements

public class Player{
	int cx,cy; //player's coords

	public Player(){
		cx = 52;
		cy = 117;
	}

	public int getX(){
		return cx;
	}

	public int getY(){
		return cy;
	}

	public void moveUp(){
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
	}

}