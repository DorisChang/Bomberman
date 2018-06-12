//Powerups Available
//Bombs - increase # of bombs by 1
//Flames - increase explosion range by 1 square

public class Bomb{
	private int bx, by;
	private int dropMax, bombRange;
	private Rectangle bRect;

	private Rectangle[]explodeRects = new Rectangle[4];

	public Bomb(int x, int y){
		bx = x;
		by = y;

		bRect = new Rectangle(bx,by,39,39);
	}

	public int getMaxBombs(){
		return dropMax;
	}

	public int getRange(){
		return bombRange;
	}

	public void explode(int direction){
		if(direction == 0){
			explodeRects[0].setBounds(45*bx+explosionTime+mx-42,45*by+69,45-explosionTime,39);
		}

		else if(direction == 1){
			explodeRects[1].setBounds(45*bx+42+mx,45*by+69,45-explosionTime,39);
		}
	}
}