package game;

import java.awt.Color;
import java.awt.Graphics;

import main.Game;
import main.GameElement;
import main.StartGame;


public class Brick implements GameElement{

	private int x;
	private int y;
	private int neededHits;
	private static int neededHitsMax = 3;
	private Color color;
	private final int width = 60;
	private final int height = 18;
	
	/**
	 * Konstruktor des Balls
	 * 
	 * @param x
	 * 			x-Koordinate von der obenen linken Ecke des Steins
	 * @param y
	 * 			y-Koordinate von der obenen linken Ecke des Steins
	 * 
	 * @param neededHits
	 * 			Anzahl benoetigter Treffer, bis der Stein verschwindet
	 */
	public Brick(int x, int y,int neededHits){
		this.x = x;
		this.y = y;
		this.neededHits = neededHits;
		setColor();
	}

	/**
	 * Konsturktor des Balls, der die Stärke des Balls zufällig generiert
	 * 
	 * @param x
	 * 			x-Koordinate von der obenen linken Ecke des Steins
	 * @param y
	 * 			y-Koordinate von der obenen linken Ecke des Steins
	 * 
	 */
	public Brick(int x, int y){
		this(x, y, (int) ((Math.random()*3)+1));
	}
	
	/**
	 * 
	 * Setz die Farbe des Steins entsprechend seiner Staerke
	 * 
	 */
	private void setColor() {
		switch(this.neededHits){
			case 1: this.color = Color.GREEN; break;
			case 2: this.color = Color.ORANGE; break;
			case 3: this.color = Color.RED; break;
			case -1: this.color = Color.MAGENTA; break;
			case -2: this.color = Color.BLUE; break;
			case -3: this.color = Color.MAGENTA; break;
		}
	}
	
	/**
	 * 
	 * Paint-Methode
	 * 
	 * @param g
	 * 			Graphics
	 */
	public void paintElement(Graphics g) {
		g.setColor(this.color);
		g.fillRect(this.x, this.y, this.width, this.height);
	}
	
	/**
	 * 
	 * Wird aufgerufen, wenn der Stein getroffen wird
	 */
	public void hitted(){
		if(Math.abs(neededHits)>=1 && Math.abs(neededHits) <= 3){
			StartGame.game.setPkt(5,Game.ADD);
			if(Math.abs(neededHits) == 1) {
				delete();
			} else {
				if(neededHits < 0) {
					neededHits++;
				} else {
					neededHits--;
				}
			}
			setColor();
		}
	}

	/**
	 * Gibt die Koordinaten auf denen die vier Seiten des Stein liegen zurueck
	 * 
	 * @return array
	 * 			[0]: x, [1]: y, [2]: x + Breite, [3]: y + Höhe
	 */
	public int[] getPosition(){
		int[] array = {this.x, this.y, this.x + this.width, this.y + this.height};
		return array;
	}
	
//	/**
//	 * Gibt die vier Eckpunkte des Stein im Uhrzeigersinn aus, beginnend mit der
//	 * oberen linken Ecke
//	 */
//	public int[][] getPosition(){
//		int[][] array = {
//						{this.x, this.y},
//						{this.x + this.width, this.y},
//						{this.x + this.width, this.y + this.height},
//						{this.x, this.y+this.height}
//		};
//		return array;
//	}

	/**
	 * 
	 * Gibt die Koordinaten des Mittelpunktes des Steins aus
	 * 
	 * @return array
	 * 			Mittelpunkt des Steins
	 * 
	 */
	public int[] getPos(){
		int[] array = {this.x + this.width/2, this.y + this.height/2};
		return array;
	}
	
	/**
	 * 
	 * Gibt die Dimension des Steins aus
	 * 
	 * @return dims
	 * 			[0]: width, [1]: height
	 * 
	 */
	public int[] getDims(){
		int[] dims = {this.width, this.height};
		return dims;
	}

	/**
	 * 
	 * Deletes the current Brick
	 * 
	 */
	public void delete() {
		if(specialBrickCheck()) {
			specialBrickAction();
		}
		StartGame.game.bricks.remove(this);
		StartGame.game.elements.remove(this);
		StartGame.game.checkLevelCompleted();
	}

	/**
	 * 
	 * Gibt die maximal Staerke eines Steins zurueck
	 * 
	 * @return neededHitsMax
	 * 			Maximal Staerke
	 * 
	 */
	public static int getNeededHitsMax() {
		return neededHitsMax;
	}
	
	/**
	 * 
	 * Ueberprueft, ob es sich bei dem Stein um einen Spezialstein handel
	 * 
	 * @return boolean
	 * 
	 */
	public boolean specialBrickCheck() {
		if(this.neededHits == -1) return true;
		else return false;
	}
	
	/**
	 * 
	 * Ruft zufaellig eine Methode zur Manipulation des Balls oder des
	 * Schlaegers auf
	 * 
	 */
	public void specialBrickAction() {
		int random = (int) ((Math.random()*neededHitsMax*2)+1);
		switch(random) {
			case 1: StartGame.game.racket.setWidth(0.75); break;
			case 2: StartGame.game.b1.setSpeed(0.75); break;
			case 3: StartGame.game.b1.setRadius(0.85); break;
			case 4: StartGame.game.racket.setWidth(1.25); break;
			case 5: StartGame.game.b1.setSpeed(1.25); break;
			case 6: StartGame.game.b1.setRadius(1.25); break;
			default: break;
		}
	}
}
