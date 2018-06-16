//PowerUps
//diff powerups

/*
Bombs - increase # of bombs by 1
Flames - increase explosion range by 1 square
Speed - increase speed by 1 (Stage 4 >> faster opponents start to show up)
Wallpass - ability to pass through soft blocks
Detonator - ability to detonate oldest bomb >> click space
Bombpass - ability to pass through bombs
Flamepass - immunity to flames
Mystery - temporary invincibility
*/

import java.awt.Rectangle;

public class Powerup{
	private Rectangle r;
	private int type;

	public Powerup(int x,int y,int t){
		r = new Rectangle(x,y,37,37);
		type = t;
	}

	public Rectangle getRect(){
		return r;
	}

	public int getType(){
		return type;
	}
}