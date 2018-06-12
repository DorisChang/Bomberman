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

public class Powerups{
	private String name;
	private int lvl;

	public Powerups(String powerUp){
		name = powerUp;
		lvl = 1;
	}

	public String getName(){
		return name;
	}

	public int getLevel(){
		return lvl;
	}

	public void incLevel(){
		lvl += 1;
	}
}